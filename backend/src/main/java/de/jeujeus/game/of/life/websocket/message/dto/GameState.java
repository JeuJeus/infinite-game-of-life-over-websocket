package de.jeujeus.game.of.life.websocket.message.dto;

import de.jeujeus.game.of.life.game.model.Cell;

import java.util.List;

public record GameState(List<Cell> field) {
}
