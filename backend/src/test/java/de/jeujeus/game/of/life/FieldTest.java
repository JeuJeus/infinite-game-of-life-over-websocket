package de.jeujeus.game.of.life;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldTest {

    @Test
    void should_generate_field_with_dead_cells_only() {
        int fieldWidth = 2;
        int fieldHeight = 2;

        List<List<Cell>> field = Field.generateField(fieldWidth, fieldHeight);

        List<Cell> allCells = field.stream()
                .flatMap(List::stream)
                .toList();

        long aliveCellsInField = allCells.stream()
                .filter(Cell::isAlive)
                .count();

        assertEquals(aliveCellsInField,0);
    }

    @ParameterizedTest(name = "{index} - field {0}x{1} is correct")
    @CsvSource(value = {"2,2", "12,12", "6,4"})
    void should_generate_field_with_correct_size(final int fieldWidth, final int fieldHeight) {

        List<List<Cell>> field = Field.generateField(fieldWidth, fieldHeight);

        List<Cell> allCells = field.stream()
                .flatMap(List::stream)
                .toList();

        assertEquals(field.size(),fieldWidth);
        assertEquals(field.get(0).size(),fieldHeight);
        assertEquals(field.get(1).size(),fieldHeight);

        assertEquals(allCells.size(),fieldHeight*fieldWidth);
    }
}