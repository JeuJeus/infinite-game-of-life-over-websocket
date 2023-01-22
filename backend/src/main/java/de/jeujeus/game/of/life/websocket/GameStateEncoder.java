package de.jeujeus.game.of.life.websocket;

import com.google.gson.Gson;
import de.jeujeus.game.of.life.websocket.message.dto.GameState;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class GameStateEncoder implements Encoder.Text<GameState> {

    private static final Gson gson = new Gson();

    @Override
    public String encode(GameState state) {
        return gson.toJson(state);
    }


    @Override
    public void init(final EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }

}