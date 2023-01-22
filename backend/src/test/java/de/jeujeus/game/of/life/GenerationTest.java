package de.jeujeus.game.of.life;

import com.google.common.collect.Table;
import de.jeujeus.game.of.life.model.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerationTest {

    Table<Integer, Integer, Cell> currentGeneration;
    Cell cellInMiddle;

    @BeforeEach
    void setupGeneration() {
        currentGeneration = Field.generateField(3, 3);
        cellInMiddle = new Cell(true, 1, 1);
    }

    @Test
    void should_return_list_of_8_neighbours_when_all_present() {
        List<Cell> neighbours = Generation.getNeighbours(currentGeneration, cellInMiddle);
        assertEquals(8, neighbours.size());

        List<Cell> startCellInNeighbours = neighbours.parallelStream()
                .filter(c -> c.getYCoordinate() == cellInMiddle.getXCoordinate() && c.getYCoordinate() == cellInMiddle.getYCoordinate())
                .toList();

        assertEquals(0, startCellInNeighbours.size());
    }

    @Test
    void count_alive_neighbours_when_none_alive() {
        int countOfAliveNeighbours = Generation.getCountOfAliveNeighbours(currentGeneration, cellInMiddle);
        assertEquals(0, countOfAliveNeighbours);
    }

    @Test
    void count_number_of_alive_neighbours_when_everyone_alive() {
        currentGeneration.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .forEach(c -> c.setAlive(true));

        int countOfAliveNeighbours = Generation.getCountOfAliveNeighbours(currentGeneration, cellInMiddle);
        assertEquals(4, countOfAliveNeighbours);
    }

    @Nested
    @DisplayName("Conways Rules for Game of Live - condensed")
    class ConwayGameOfLifeRules {
        @Test
        @DisplayName("Any live cell with two or three live neighbours survives")
        void cell_lives_with_correct_number_of_neighbours() {
            currentGeneration.get(1, 1)
                    .setAlive(true);

            //two neighbours
            currentGeneration.get(1, 2)
                    .setAlive(true);
            currentGeneration.get(2, 1)
                    .setAlive(true);

            boolean aliveNextGenerationWithTwoNeighbours = Generation.isAliveNextGeneration(currentGeneration, cellInMiddle);

            assertEquals(true, aliveNextGenerationWithTwoNeighbours);

            //three neighbours
            currentGeneration.get(1, 0)
                    .setAlive(true);

            boolean aliveNextGenerationWithThreeNeighbours = Generation.isAliveNextGeneration(currentGeneration, cellInMiddle);

            assertEquals(true, aliveNextGenerationWithThreeNeighbours);

        }

        @Test
        @DisplayName("Any dead cell with three live neighbours becomes a live cell")
        void dead_cell_with_correct_numer_of_neighbours_becomes_alive() {
            currentGeneration.get(1, 1)
                    .setAlive(false);

            //two neighbours
            currentGeneration.get(1, 2)
                    .setAlive(true);
            currentGeneration.get(2, 1)
                    .setAlive(true);

            boolean aliveNextGenerationWithTwoNeighbours = Generation.isAliveNextGeneration(currentGeneration, cellInMiddle);

            assertEquals(true, aliveNextGenerationWithTwoNeighbours);

            //three neighbours
            currentGeneration.get(1, 0)
                    .setAlive(true);

            boolean aliveNextGenerationWithThreeNeighbours = Generation.isAliveNextGeneration(currentGeneration, cellInMiddle);

            assertEquals(true, aliveNextGenerationWithThreeNeighbours);
        }

        @Test
        @DisplayName("All other live cells die in the next generation. Similarly, all other dead cells stay dead")
        void all_other_cells_die_and_dead_stay_dead() {
            //one neighbour
            currentGeneration.get(1, 2)
                    .setAlive(true);

            boolean notAliveWithOneNeighbour = Generation.isAliveNextGeneration(currentGeneration, cellInMiddle);

            assertEquals(false, notAliveWithOneNeighbour);

            //four neighbours
            currentGeneration.get(1, 2)
                    .setAlive(true);
            currentGeneration.get(2, 1)
                    .setAlive(true);
            currentGeneration.get(1, 0)
                    .setAlive(true);
            currentGeneration.get(0, 1)
                    .setAlive(true);

            boolean notAliveWithFourNeighbours = Generation.isAliveNextGeneration(currentGeneration, cellInMiddle);

            assertEquals(false, notAliveWithFourNeighbours);
        }
    }

}