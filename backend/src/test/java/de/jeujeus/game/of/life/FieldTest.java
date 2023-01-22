package de.jeujeus.game.of.life;

import com.google.common.collect.Table;
import de.jeujeus.game.of.life.model.Cell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(aliveCellsInField, 0);
    }

    @Test
    void should_generate_field_with_negative_lower_bound() {
        int fieldWidth = 2;
        int fieldHeight = 2;

        Table<Integer, Integer, Cell> field = Field.generateField(-fieldWidth,fieldWidth, -fieldHeight,fieldHeight);

        Set<Table.Cell<Integer, Integer, Cell>> cells = field.cellSet();
        Map<Integer, Map<Integer, Cell>> cellRows = field.rowMap();
        Map<Integer, Map<Integer, Cell>> cellColumns = field.columnMap();

        assertEquals((2*fieldHeight) * (2*fieldWidth), cells.size());
        assertEquals((2*fieldHeight), cellRows.size());
        assertEquals((2*fieldWidth), cellColumns.size());
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

    @Test
    void should_determine_all_cells_are_dead() {
        Cell deadCell = new Cell(0, 0);
        Map<Integer, Cell> deadCells = Map.of(1, deadCell, 2, deadCell, 3, deadCell, 4, deadCell, 5, deadCell);

        assertEquals(true, Field.containsOnlyDeadCells(deadCells));
    }

    @Test
    void should_determine_not_all_cells_are_dead() {
        Cell deadCell = new Cell(0, 0);
        Cell aliveCell = new Cell(true, 0, 0);
        Map<Integer, Cell> deadCells = Map.of(1, deadCell, 2, deadCell, 3, deadCell, 4, aliveCell, 5, deadCell);

        assertEquals(false, Field.containsOnlyDeadCells(deadCells));
    }

    @Test
    void should_find_lowest_index_of_map() {
        Cell cell = new Cell(0, 0);
        int lowestIndex = 3;
        Map<Integer, Map<Integer, Cell>> targetMap = Map.of(27, Map.of(0, cell), lowestIndex, Map.of(1, cell), 5, Map.of(2, cell));

        assertEquals(lowestIndex, Field.getFirstIndexIn(targetMap));
    }

    @Test
    void should_find_highest_index_of_map() {
        Cell cell = new Cell(0, 0);
        int highestIndex = 27;
        Map<Integer, Map<Integer, Cell>> targetMap = Map.of(highestIndex, Map.of(0, cell), 3, Map.of(1, cell), 5, Map.of(2, cell));

        assertEquals(highestIndex, Field.getLastIndexIn(targetMap));
    }

    @Test
    void should_trim_outer_columns_and_rows_with_only_dead_cells_from_field() {
        final int fieldWidth = 4;
        final int fieldHeight = 4;

        Table<Integer, Integer, Cell> field = Field.generateField(fieldWidth, fieldHeight);

        Table<Integer, Integer, Cell> trimmedField = Field.trimDeadOuterCellsFromField(field);

        assertEquals(0, trimmedField.cellSet()
                .size());
    }

    @Test
    void should_trim_dead_columns_from_field_with_only_one_alive_cell() {
        final int fieldWidth = 4;
        final int fieldHeight = 4;

        Table<Integer, Integer, Cell> field = Field.generateField(fieldWidth, fieldHeight);
        field.get(2, 2)
                .setAlive(true);

        Table<Integer, Integer, Cell> trimmedField = Field.trimDeadOuterCellsFromField(field);

        assertEquals(1, trimmedField.size());
        Cell remainingCell = trimmedField.cellSet()
                .stream()
                .findFirst()
                .orElseThrow()
                .getValue();
        assertEquals(remainingCell.getXCoordinate(),2);
        assertEquals(remainingCell.getYCoordinate(),2);
        assertEquals(remainingCell.isAlive(),true);
    }

    @Test
    void should_trim_nothing_from_field_with_every_cell_alive() {
        final int fieldWidth = 2;
        final int fieldHeight = 2;

        Table<Integer, Integer, Cell> field = Field.generateField(fieldWidth, fieldHeight);
        field.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .forEach(c -> c.setAlive(true));

        Table<Integer, Integer, Cell> trimmedField = Field.trimDeadOuterCellsFromField(field);

        assertEquals(field,trimmedField);
    }
}