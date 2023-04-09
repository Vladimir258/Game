package ru.gb.ws;

import org.springframework.web.socket.WebSocketSession;

public interface ConnectListener {
    void handler(WebSocketSession session);
}
