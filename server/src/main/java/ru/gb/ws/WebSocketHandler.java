package ru.gb.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import com.badlogic.gdx.utils.Array;

@Component
public class WebSocketHandler extends AbstractWebSocketHandler {
    private Array<WebSocketSession> sessions = new Array<>();
    private ConnectListener connectListener;
    private DisconnectListener disconnectListener;
    private MessageListener messageListener;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        connectListener.handler(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        messageListener.handler(session, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.removeValue(session, true);
        disconnectListener.handler(session);
    }

    public Array<WebSocketSession> getSessions() {
        return sessions;
    }

    public void setConnectListener(ConnectListener connectListener) {
        this.connectListener = connectListener;
    }

    public void setDisconnectListener(DisconnectListener disconnectListener) {
        this.disconnectListener = disconnectListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }
}
