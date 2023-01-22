package de.jeujeus.game.of.life.websocket;

import de.jeujeus.game.of.life.websocket.message.dto.GameState;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/gameOfLife",
        decoders = GameStateDecoder.class,
        encoders = GameStateEncoder.class)
public class ConnectionEndpoint {

    private Session session;
    private static final Set<ConnectionEndpoint> connectionEndpoints = new CopyOnWriteArraySet<>();
    private static final HashSet<String> connections = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connectionEndpoints.add(this);
        connections.add(session.getId());
    }

    private void sendMessage(final GameState state) {
        try {
            this.session.getBasicRemote().
                    sendObject(state);
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(Session session, GameState state) {
        sendMessage(state);
    }

    @OnClose
    public void onClose(Session session) {
        connectionEndpoints.remove(this);
    }

}
