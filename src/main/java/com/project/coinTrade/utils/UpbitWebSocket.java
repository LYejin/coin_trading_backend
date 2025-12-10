package com.project.coinTrade.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import okio.ByteString;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UpbitWebSocket {

    private final String WS_URL = "wss://api.upbit.com/websocket/v1";

    private static final String TOPIC_TICKER = "/topic/upbit/ticker";
    private static final String TOPIC_TRADE  = "/topic/upbit/trade";

    private OkHttpClient client;
    private WebSocket webSocket;

    /**
     * 멀티쓰레드 환경에서 동시성 보장
     */
    private final AtomicBoolean connected = new AtomicBoolean(false);

    /**
     * "한 번만 받아서 리턴" 용도로 사용할 Future
     */
    private CompletableFuture<String> singleResponseFuture;

    /**
     * 프론트로 브로드캐스트용 STOMP 템플릿
     */
    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 생성자 주입
    public UpbitWebSocket(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void init() {
        client = new OkHttpClient();
        // 필요하면 여기서 connect() 호출 가능
    }

    /**
     * WebSocket 연결
     * synchronized
     * ㄴ 동시에 접근 하지 못하도록 막는것
     */
    private synchronized void connect() {
        if (connected.get() && webSocket != null) {
            return;
        }

        if (client == null) {
            client = new OkHttpClient();
        }

        Request request = new Request.Builder()
                .url(WS_URL)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                connected.set(true);
                System.out.println("[WebSocket] Connected!");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("[Message TXT] " + text);

                // 1) 한번만 받는 모드인 경우
                if (singleResponseFuture != null && !singleResponseFuture.isDone()) {
                    singleResponseFuture.complete(text);
                    close();
                    return;
                }

                // 2) 스트리밍 모드인 경우 → 타입 분기 후 브로드캐스트
                routeAndBroadcast(text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                String text = bytes.string(StandardCharsets.UTF_8);
                System.out.println("[Message BIN] " + text);

                // 1) 한번만 받는 모드
                if (singleResponseFuture != null && !singleResponseFuture.isDone()) {
                    singleResponseFuture.complete(text);
                    close();
                    return;
                }

                // 2) 스트리밍 모드
                routeAndBroadcast(text);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                connected.set(false);
                System.out.println("[WebSocket] Closed: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                connected.set(false);
                System.out.println("[WebSocket] Failure: " + t.getMessage());
                t.printStackTrace();

                if (singleResponseFuture != null && !singleResponseFuture.isDone()) {
                    singleResponseFuture.completeExceptionally(t);
                }
            }
        });
    }

    /**
     * JSON에서 type 뽑아서 토픽 라우팅
     */
    private void routeAndBroadcast(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            String type = node.path("type").asText("");

            broadcastToFrontend(message, type);
        } catch (Exception e) {
            // type 없는 경우(에러 등)는 일단 trade 토픽이나 로그만 찍고 무시해도 됨
            System.out.println("[WebSocket] Failed to parse message: " + e.getMessage());
        }
    }

    /**
     * 프론트로 STOMP 브로드캐스트
     */
    private void broadcastToFrontend(String message, String type) {

        String topic = switch (type) {
            case "ticker" -> TOPIC_TICKER;
            case "trade"  -> TOPIC_TRADE;
            default       -> null;
        };

        if (topic == null || topic.isBlank()) {
            // 알 수 없는 타입이면 그냥 버리거나, 필요하면 디버그용 토픽 하나 더 만들어서 보내도 됨
            System.out.println("[WebSocket] Unknown type: " + type + " / message: " + message);
            return;
        }

        messagingTemplate.convertAndSend(topic, message);
    }

    /**
     * 기존처럼 스트리밍 구독만 하고 싶을 때 사용하는 메소드
     */
    public void sendSubscribeMessage(String subscribeMessage) {
        if (webSocket == null || !connected.get()) {
            System.out.println("[WebSocket] Not connected. Trying reconnect...");
            connect();
        }

        webSocket.send(subscribeMessage);
        System.out.println("[WebSocket] Sent Subscribe Message: " + subscribeMessage);
    }

    /**
     * 딱 한 번만 메시지 받고 리턴하고 싶은 경우
     */
    public String sendAndReceiveOnce(String subscribeMessage) throws Exception {
        // 새 Future 준비
        singleResponseFuture = new CompletableFuture<>();

        if (webSocket == null || !connected.get()) {
            System.out.println("[WebSocket] Not connected. Trying reconnect...");
            connect();
        }

        webSocket.send(subscribeMessage);
        System.out.println("[WebSocket] Sent (once) Subscribe Message: " + subscribeMessage);

        try {
            // 최대 5초 정도만 기다렸다가 응답 오면 리턴
            return singleResponseFuture.get(5, TimeUnit.SECONDS);
        } finally {
            // 정상/예외 상관없이 소켓 닫기
            close();
        }
    }

    /**
     * 애플리케이션 종료 시 안전하게 소켓 닫기
     */
    public void close() {
        connected.set(false);
        if (webSocket != null) {
            webSocket.close(1000, "Client closed connection");
            webSocket = null;
        }
        System.out.println("[WebSocket] Manually closed.");
    }
}
