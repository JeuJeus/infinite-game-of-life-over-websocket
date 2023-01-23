package de.jeujeus.game.of.life.game;

import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TrimmedFieldTest {

    @Test
    void should_remove_dead_cells_from_field() {
        Cell alive = new Cell(true, 1, 1);
        Cell dead = new Cell(false, 0, 0);
        List<Cell> cells = List.of(alive, dead);

        List<Cell> cellsAlive = TrimmedField.trimDeadCellsFromField(cells);

        assertEquals(1,cellsAlive.size());
        assertEquals(alive,cellsAlive.get(0));
    }

    @Test
    void should_trim_outer_columns_and_rows_with_only_dead_cells_from_field() {
        final int fieldWidth = 4;
        final int fieldHeight = 4;

        Table<Integer, Integer, Cell> field = Field.generateField(fieldWidth, fieldHeight);

        Table<Integer, Integer, Cell> trimmedField = TrimmedField.trimDeadOuterCellsFromField(field);

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

        Table<Integer, Integer, Cell> trimmedField = TrimmedField.trimDeadOuterCellsFromField(field);

        assertEquals(1, trimmedField.size());
        Cell remainingCell = trimmedField.cellSet()
                .stream()
                .findFirst()
                .orElseThrow()
                .getValue();
        assertEquals(2, remainingCell.getXCoordinate());
        assertEquals(2, remainingCell.getYCoordinate());
        assertTrue(remainingCell.isAlive());
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

        Table<Integer, Integer, Cell> trimmedField = TrimmedField.trimDeadOuterCellsFromField(field);

        assertEquals(field,trimmedField);
    }


    @Test
    void should_determine_all_cells_are_dead() {
        Cell deadCell = new Cell(0, 0);
        Map<Integer, Cell> deadCells = Map.of(1, deadCell, 2, deadCell, 3, deadCell, 4, deadCell, 5, deadCell);

        assertTrue(TrimmedField.containsOnlyDeadCells(deadCells));
    }

    @Test
    void should_determine_not_all_cells_are_dead() {
        Cell deadCell = new Cell(0, 0);
        Cell aliveCell = new Cell(true, 0, 0);
        Map<Integer, Cell> deadCells = Map.of(1, deadCell, 2, deadCell, 3, deadCell, 4, aliveCell, 5, deadCell);

        assertFalse(TrimmedField.containsOnlyDeadCells(deadCells));
    }

    @Test
    void should_find_lowest_index_of_map() {
        Cell cell = new Cell(0, 0);
        int lowestIndex = 3;
        Map<Integer, Map<Integer, Cell>> targetMap = Map.of(27, Map.of(0, cell), lowestIndex, Map.of(1, cell), 5, Map.of(2, cell));

        assertEquals(lowestIndex, TrimmedField.getFirstIndexIn(targetMap));
    }

    @Test
    void should_find_highest_index_of_map() {
        Cell cell = new Cell(0, 0);
        int highestIndex = 27;
        Map<Integer, Map<Integer, Cell>> targetMap = Map.of(highestIndex, Map.of(0, cell), 3, Map.of(1, cell), 5, Map.of(2, cell));

        assertEquals(highestIndex, TrimmedField.getLastIndexIn(targetMap));
    }

}
