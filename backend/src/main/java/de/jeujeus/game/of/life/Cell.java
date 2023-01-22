package de.jeujeus.game.of.life;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {

    private boolean isAlive;
    private int xCoordinate;

    private int yCoordinate;


    public Cell(final int xCoordinate, final int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isAlive = false;
    }

    public Cell(final boolean isAlive, final int xCoordinate, final int yCoordinate) {
        this.isAlive = isAlive;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }
}
