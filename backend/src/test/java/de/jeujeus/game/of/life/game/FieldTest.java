package de.jeujeus.game.of.life.game;

import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {

    @Test
    void should_generate_field_with_dead_cells_only() {
        int fieldWidth = 2;
        int fieldHeight = 2;

        Table<Integer, Integer, Cell> field = Field.generateField(fieldWidth, fieldHeight);

        long aliveCellsInField = field.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .filter(Cell::isAlive)
                .count();

        assertEquals(0, aliveCellsInField);
    }

    @Test
    void should_generate_field_with_negative_lower_bound() {
        int fieldWidth = 2;
        int fieldHeight = 2;

        Table<Integer, Integer, Cell> field = Field.generateField(-fieldWidth, fieldWidth, -fieldHeight, fieldHeight);

        Set<Table.Cell<Integer, Integer, Cell>> cells = field.cellSet();
        Map<Integer, Map<Integer, Cell>> cellRows = field.rowMap();
        Map<Integer, Map<Integer, Cell>> cellColumns = field.columnMap();

        assertEquals((2 * fieldHeight) * (2 * fieldWidth), cells.size());
        assertEquals((2 * fieldHeight), cellRows.size());
        assertEquals((2 * fieldWidth), cellColumns.size());
    }

    @ParameterizedTest(name = "{index} - field {0}x{1} is correct")
    @CsvSource(value = {"2,2", "12,12", "6,4"})
    void should_generate_field_with_correct_size(final int fieldWidth, final int fieldHeight) {

        Table<Integer, Integer, Cell> field = Field.generateField(fieldWidth, fieldHeight);

        Set<Table.Cell<Integer, Integer, Cell>> cells = field.cellSet();
        Map<Integer, Map<Integer, Cell>> cellRows = field.rowMap();
        Map<Integer, Map<Integer, Cell>> cellColumns = field.columnMap();

        assertEquals(fieldHeight * fieldWidth, cells.size());
        assertEquals(fieldWidth, cellRows.size());
        assertEquals(fieldHeight, cellColumns.size());

    }

}