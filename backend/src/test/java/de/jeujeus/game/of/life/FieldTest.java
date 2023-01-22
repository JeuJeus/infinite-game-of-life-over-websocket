package de.jeujeus.game.of.life;

import com.google.common.collect.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldTest {

    @Test
    void should_generate_field_with_dead_cells_only() {
        int fieldWidth = 2;
        int fieldHeight = 2;

        Table<Integer, Integer, Cell> field = Field.generateField(fieldWidth, fieldHeight);

        long aliveCellsInField = field.cellSet().stream()
                .map(Table.Cell::getValue)
                .filter(Cell::isAlive)
                .count();

        assertEquals(aliveCellsInField,0);
    }

    @ParameterizedTest(name = "{index} - field {0}x{1} is correct")
    @CsvSource(value = {"2,2", "12,12", "6,4"})
    void should_generate_field_with_correct_size(final int fieldWidth, final int fieldHeight) {

        Table<Integer, Integer, Cell> field = Field.generateField(fieldWidth, fieldHeight);

        Set<Table.Cell<Integer, Integer, Cell>> cells = field.cellSet();
        Map<Integer, Map<Integer, Cell>> cellRows = field.rowMap();
        Map<Integer, Map<Integer, Cell>> cellColumns = field.columnMap();

        assertEquals(cells.size(),fieldHeight*fieldWidth);
        assertEquals(cellRows.size(),fieldWidth);
        assertEquals(cellColumns.size(),fieldHeight);

    }
//
//    @Test
//    void should_trim_dead_columns_from_field_with_only_dead_is_emtpy() {
//        final int fieldWidth = 4;
//        final int fieldHeight = 4;
//
//        List<List<Cell>> field = Field.generateField(fieldWidth, fieldHeight);
//
//        List<List<Cell>> fieldWithoutDeadColumns = Field.getFieldWithoutDeadColumns(field);
//
//        assertEquals(fieldWithoutDeadColumns.size(),0);
//    }
//
//    @Test
//    void should_trim_dead_columns_from_field_with_only_one_alive_cell() {
//        final int fieldWidth = 4;
//        final int fieldHeight = 4;
//
//        List<List<Cell>> field = Field.generateField(fieldWidth, fieldHeight);
//
//        field.get(1)
//                .get(1)
//                .setAlive(true);
//
//        List<List<Cell>> fieldWithoutDeadColumns = Field.getFieldWithoutDeadColumns(field);
//
//        assertEquals(fieldWithoutDeadColumns.size(),1);
//    }
//
//    @Test
//    void should_trim_dead_rows_from_field_with_only_dead_is_emtpy() {
//        final int fieldWidth = 4;
//        final int fieldHeight = 4;
//
//        List<List<Cell>> field = Field.generateField(fieldWidth, fieldHeight);
//
//        List<List<Cell>> fieldWithoutDeadRows = Field.getFieldWithoutDeadRows(field);
//
//        assertEquals(fieldWithoutDeadRows.size(),0);
//    }

}