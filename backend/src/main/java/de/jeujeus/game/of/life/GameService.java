package de.jeujeus.game.of.life;

import de.jeujeus.game.of.life.game.Generation;
import de.jeujeus.game.of.life.game.TrimmedField;
import de.jeujeus.game.of.life.game.model.Cell;
import de.jeujeus.game.of.life.websocket.message.dto.GameState;

import java.util.List;

public class GameService {

    private GameService() {
    }

    public static GameState calculateAliveNextGenerationCells(final GameState state) {
        final List<Cell> currentGeneration = state.field();

        final List<Cell> nextGeneration = Generation.calculateNextGeneration(currentGeneration);
        final List<Cell> aliveCellsNextGeneration = TrimmedField.trimDeadCellsFromField(nextGeneration);

        return new GameState(aliveCellsNextGeneration);
    }
}