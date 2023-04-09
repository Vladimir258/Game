package ru.gb.ws;

import org.springframework.web.socket.WebSocketSession;

public interface DisconnectListener {
    void handler(WebSocketSession session);
}
