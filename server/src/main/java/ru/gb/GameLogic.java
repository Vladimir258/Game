package ru.gb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Array;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import ru.gb.ws.WebSocketHandler;
import java.io.IOException;

@Component
public class GameLogic extends ApplicationAdapter {
    private final WebSocketHandler socketHandler;
    private final Array<String> events = new Array<>();

    public GameLogic(WebSocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    @Override
    public void create() {
        socketHandler.setConnectListener(session -> {
            events.add(session.getId() + " join");
        });
        socketHandler.setDisconnectListener(session -> {
            events.add(session.getId() + " disconnect");
        });
        socketHandler.setMessageListener(((session, message) -> {
            events.add(session.getId() + " say " + message);
        }));
    }

    @Override
    public void render() {
        for (WebSocketSession session: socketHandler.getSessions()) {
            try {
                for (String event : events) {
                    session.sendMessage(new TextMessage(event));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        events.clear();
    }

    @Override
    public void dispose() {

    }
}
