package de.jeujeus.game.of.life.game;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Tables.toTable;

public class Generation {

    private Generation() {
    }

    public static List<Cell> calculateNextGeneration(List<Cell> currentGenerationCells) {
        Table<Integer, Integer, Cell> currentGeneration = Generation.createGenerationFromList(currentGenerationCells);
        Table<Integer, Integer, Cell> nextGeneration = Generation.calculateNextGeneration(currentGeneration);

        //todo remove all dead cells to improve performance
        return nextGeneration.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .toList();
    }

    static Table<Integer, Integer, Cell> createGenerationFromList(List<Cell> generation) {
        return generation.parallelStream()
                .collect(toTable(
                        Cell::getXCoordinate,
                        Cell::getYCoordinate,
                        cell -> cell,
                        HashBasedTable::create));
    }

    public static Table<Integer, Integer, Cell> calculateNextGeneration(Table<Integer, Integer, Cell> currentGeneration) {
        Integer xLowestIndex = TrimmedField.getFirstIndexIn(currentGeneration.columnMap());
        Integer xHighestIndex = indexCorrection(TrimmedField.getLastIndexIn(currentGeneration.columnMap()));
        Integer yLowestIndex = TrimmedField.getFirstIndexIn(currentGeneration.rowMap());
        Integer yHighestIndex = indexCorrection(TrimmedField.getLastIndexIn(currentGeneration.rowMap()));

        Table<Integer, Integer, Cell> nextField = Field.generateNextField(xLowestIndex, xHighestIndex, yLowestIndex, yHighestIndex);

        nextField.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .forEach(c -> {
                    Cell cellFromOldGeneration = Optional.ofNullable(currentGeneration.get(c.getXCoordinate(), c.getYCoordinate()))
                            .orElse(new Cell(c.getXCoordinate(), c.getYCoordinate()));
                    c.setAlive(Cell.isAliveNextGeneration(currentGeneration, cellFromOldGeneration));
                });

        return TrimmedField.trimDeadOuterCellsFromField(nextField);
    }

    private static int indexCorrection(final int index) {
        //accommodate for Difference between Index and Grid-Size
        return index + 1;
    }

}
