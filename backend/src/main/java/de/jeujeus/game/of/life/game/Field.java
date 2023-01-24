package de.jeujeus.game.of.life.game;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;

import static java.util.stream.IntStream.range;

public class Field {

    private Field() {
    }

    static Table<Integer, Integer, Cell> generateNextField(final int currentFieldStartWidth,
                                                           final int currentFieldWidth,
                                                           final int currentFielStartHeight,
                                                           final int currentFieldHeight) {
        return generateField(currentFieldStartWidth - 1,
                currentFieldWidth + 1,
                currentFielStartHeight - 1,
                currentFieldHeight + 1);
    }

    static Table<Integer, Integer, Cell> generateField(final int fieldStartWidth,
                                                       final int fieldWidth,
                                                       final int fielStartHeight,
                                                       final int fieldHeight) {

        final Table<Integer, Integer, Cell> field = HashBasedTable.create();

        range(fieldStartWidth, fieldWidth)
                .forEachOrdered(c -> range(fielStartHeight, fieldHeight)
                        .forEachOrdered(r -> field.put(c, r, new Cell(c, r))));

        return field;
    }

    static Table<Integer, Integer, Cell> generateField(final int fieldWidth, final int fieldHeight) {
        return generateField(0, fieldWidth, 0, fieldHeight);
    }
}
