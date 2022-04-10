package service;

import org.springframework.stereotype.Service;

import java.net.http.WebSocket;

/**
 * Singleton class provides an instance of it used to create WebSocket for each Client,
 * and to send notification over WebSocket.
 * */

@Service
public class WebSocketOperations {

    private static WebSocketOperations webSocketOperations = new WebSocketOperations();

    private WebSocketOperations() {

    }

    public static WebSocketOperations getInstance() {
        return webSocketOperations;
    }

    public WebSocket createWebSocket() {

        // create properly
        WebSocket webSocket = null;

        return webSocket;
    }

    public boolean sendNotificationOverWebSocket(int targetNum, String message) {

        boolean bIsSuccessful = false;

        // logic will be implemented here

        return bIsSuccessful;
    }
}
