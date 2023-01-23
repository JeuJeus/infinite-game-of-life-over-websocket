package de.jeujeus.game.of.life.game;

import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenerationTest {

    Table<Integer, Integer, Cell> currentGeneration;
    Cell cellInMiddle;

    @BeforeEach
    void setupGeneration() {
        currentGeneration = Field.generateField(3, 3);
        cellInMiddle = new Cell(true, 1, 1);
    }

    @Nested
    @DisplayName("Generation Creation Tests")
    class GenerationCreation {
        @Test
        void should_calculate_correct_next_generation_when_stationary() {
            Table<Integer, Integer, Cell> currentGeneration = Field.generateField(4, 4);
            //square block with
            currentGeneration.get(1, 1)
                    .setAlive(true);
            currentGeneration.get(1, 2)
                    .setAlive(true);
            currentGeneration.get(2, 1)
                    .setAlive(true);
            currentGeneration.get(2, 2)
                    .setAlive(true);

            /*
             * OOOO
             * OXXO
             * OXXO
             * OOOO
             *
             * OOOO
             * OXXO
             * OXXO
             * OOOO
             * */

            Table<Integer, Integer, Cell> nextGeneration = Generation.calculateNextGeneration(currentGeneration);

            assertEquals(4, nextGeneration.size());
            assertTrue(nextGeneration.get(1, 1)
                    .isAlive());
            assertTrue(nextGeneration.get(1, 2)
                    .isAlive());
            assertTrue(nextGeneration.get(2, 1)
                    .isAlive());
            assertTrue(nextGeneration.get(2, 2)
                    .isAlive());
        }

        @Test
        void should_calculate_correct_next_generation_when_transforming() {
            Table<Integer, Integer, Cell> currentGeneration = Field.generateField(4, 4);
            //square block with one missing
            currentGeneration.get(1, 1)
                    .setAlive(true);
            currentGeneration.get(1, 2)
                    .setAlive(true);
            currentGeneration.get(2, 1)
                    .setAlive(true);

            /*
             * OOOO
             * OXOO
             * OXXO
             * OOOO
             *
             * OOOO
             * OXXO
             * OXXO
             * OOOO
             * */

            Table<Integer, Integer, Cell> nextGeneration = Generation.calculateNextGeneration(currentGeneration);

            assertEquals(4, nextGeneration.size());
            assertTrue(nextGeneration.get(1, 1)
                    .isAlive());
            assertTrue(nextGeneration.get(1, 2)
                    .isAlive());
            assertTrue(nextGeneration.get(2, 1)
                    .isAlive());
            assertTrue(nextGeneration.get(2, 2)
                    .isAlive());
        }

        @Test
        void should_calculate_blinker() {
            Table<Integer, Integer, Cell> currentGeneration = Field.generateField(1, 3);
            //veritcal dash
            currentGeneration.get(0, 0)
                    .setAlive(true);
            currentGeneration.get(0, 1)
                    .setAlive(true);
            currentGeneration.get(0, 2)
                    .setAlive(true);

            /*
             * X
             * X
             * X
             *
             * XXX
             * */

            Table<Integer, Integer, Cell> nextGeneration = Generation.calculateNextGeneration(currentGeneration);

            assertEquals(3, nextGeneration.size());
            assertTrue(nextGeneration.get(-1, 1)
                    .isAlive());
            assertTrue(nextGeneration.get(0, 1)
                    .isAlive());
            assertTrue(nextGeneration.get(1, 1)
                    .isAlive());
        }
    }

    @Test
    void parsing_generation_should_equate_to_same_object() {
        Table<Integer, Integer, Cell> generationTable = Field.generateField(1, 3);
        //veritcal dash
        generationTable.get(0, 0)
                .setAlive(true);
        generationTable.get(0, 1)
                .setAlive(true);
        generationTable.get(0, 2)
                .setAlive(true);

        Cell cell0 = new Cell(true, 0, 0);
        Cell cell1 = new Cell(true, 0, 1);
        Cell cell2 = new Cell(true, 0, 2);
        List<Cell> generationList = List.of(cell0, cell1, cell2);

        Table<Integer, Integer, Cell> generationFromList = Generation.createGenerationFromList(generationList);

        assertEquals(generationTable.cellSet()
                .size(), generationFromList.cellSet()
                .size());
        generationFromList.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .forEach(
                        gl -> assertEquals(Objects.requireNonNull(generationTable.get(gl.getXCoordinate(), gl.getYCoordinate()))
                                .isAlive(), gl.isAlive())
                );
    }
}