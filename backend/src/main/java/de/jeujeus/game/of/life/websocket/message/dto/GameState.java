package de.jeujeus.game.of.life.websocket.message.dto;

import de.jeujeus.game.of.life.game.model.Cell;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameState {
    private List<Cell> field;

    public GameState(final List<Cell> field) {
        this.field = field;
    }
}
