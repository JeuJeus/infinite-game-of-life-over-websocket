package de.jeujeus.game.of.life.websocket;

import de.jeujeus.game.of.life.GameService;
import de.jeujeus.game.of.life.websocket.message.dto.GameState;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/gameOfLife",
        decoders = GameStateDecoder.class,
        encoders = GameStateEncoder.class)
public class ConnectionEndpoint {

    private Session session;
    private static final Set<ConnectionEndpoint> connectionEndpoints = new CopyOnWriteArraySet<>();
    private static final HashMap<String,Session> connections = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        connectionEndpoints.add(this);
        connections.put(session.getId(),session);
    }

    private void sendMessage(final GameState state) {
        RemoteEndpoint.Async asyncRemote = this.session.getAsyncRemote();
        asyncRemote.sendObject(state);
    }

    @OnMessage
    public void onMessage(Session session, GameState state) {
        GameState gameState = GameService.calculateAliveNextGenerationCells(state);
        sendMessage(gameState);
    }

    @OnClose
    public void onClose(Session session) {
        connectionEndpoints.remove(this);
    }

}
