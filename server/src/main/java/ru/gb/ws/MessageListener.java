package ru.gb.ws;

import org.springframework.web.socket.WebSocketSession;

public interface MessageListener {
    void handler(WebSocketSession session, String message);
}
