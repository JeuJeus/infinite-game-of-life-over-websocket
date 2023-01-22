package de.jeujeus.game.of.life;

import java.util.ArrayList;
import java.util.List;

public class Field {

    static List<List<Cell>> generateField(final int fieldWidth, final int fieldHeight) {
        ArrayList<List<Cell>> field = new ArrayList<>();
        for (int i = 0; i < fieldWidth; i++) {
            ArrayList<Cell> column = new ArrayList<>();
            for (int j = 0; j < fieldHeight; j++) {
                Cell cell = new Cell(i, j);
                column.add(cell);
            }
            field.add(column);
        }
        return field;
    }

}
