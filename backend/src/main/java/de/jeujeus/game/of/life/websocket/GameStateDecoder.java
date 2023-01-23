package de.jeujeus.game.of.life.websocket;

import com.google.gson.Gson;
import de.jeujeus.game.of.life.websocket.message.dto.GameState;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class GameStateDecoder implements Decoder.Text<GameState> {

    private static final Gson gson = new Gson();

    @Override
    public GameState decode(String s) {
        return gson.fromJson(s, GameState.class);
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}