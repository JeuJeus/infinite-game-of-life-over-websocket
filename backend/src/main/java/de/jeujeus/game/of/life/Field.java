package de.jeujeus.game.of.life;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.List;

public class Field {

    private Field() {
    }

    static Table<Integer, Integer, Cell> generateField(final int fieldWidth, final int fieldHeight) {
        Table<Integer,Integer,Cell> field = HashBasedTable.create();
        for (int i = 0; i < fieldWidth; i++) {
            for (int j = 0; j < fieldHeight; j++) {
                field.put(i, j, new Cell(i, j));
            }
        }
        return field;
    }

    static List<List<Cell>> trimDeadCellsFromField(final List<List<Cell>> untrimmedField) {
        return untrimmedField;
    }
}
