package de.jeujeus.game.of.life.game;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Tables.toTable;
import static de.jeujeus.game.of.life.game.model.Cell.getCellAndItsNeighbours;

public class Generation {

    private Generation() {
    }

    public static List<Cell> calculateNextGeneration(List<Cell> currentGenerationCells) {
        final Table<Integer, Integer, Cell> currentGeneration = Generation.createGenerationFromList(currentGenerationCells);
        final Table<Integer, Integer, Cell> nextGeneration = Generation.calculateNextGeneration(currentGeneration);

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
        final Integer xLowestIndex = TrimmedField.getFirstIndexIn(currentGeneration.columnMap());
        final Integer xHighestIndex = indexCorrection(TrimmedField.getLastIndexIn(currentGeneration.columnMap()));
        final Integer yLowestIndex = TrimmedField.getFirstIndexIn(currentGeneration.rowMap());
        final Integer yHighestIndex = indexCorrection(TrimmedField.getLastIndexIn(currentGeneration.rowMap()));

        final Table<Integer, Integer, Cell> nextField = Field.generateNextField(xLowestIndex, xHighestIndex, yLowestIndex, yHighestIndex);

        final ImmutableTable<Integer, Integer, Cell> onlyAliveCellsWithNeighbours = reduceGenerationToAliveCellsWithNeighbours(currentGeneration);

        nextField.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .forEach(c -> {
                    Cell cellFromOldGeneration = Optional.ofNullable(onlyAliveCellsWithNeighbours.get(c.getXCoordinate(), c.getYCoordinate()))
                            .orElse(new Cell(c.getXCoordinate(), c.getYCoordinate()));
                    c.setAlive(Cell.isAliveNextGeneration(onlyAliveCellsWithNeighbours, cellFromOldGeneration));
                });

        return TrimmedField.trimDeadOuterCellsFromField(nextField);
    }

    private static ImmutableTable<Integer, Integer, Cell> reduceGenerationToAliveCellsWithNeighbours(final Table<Integer, Integer, Cell> currentGeneration) {
        return currentGeneration.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .filter(Cell::isAlive)
                .map(livingCell -> getCellAndItsNeighbours(currentGeneration, livingCell))
                .flatMap(livingCellWithNeighbours -> livingCellWithNeighbours.cellSet()
                        .stream())
                .collect(ImmutableTable.toImmutableTable(
                        Table.Cell::getRowKey,
                        Table.Cell::getColumnKey,
                        Table.Cell::getValue,
                        (c1, c2) -> c1
                ));
    }

    private static int indexCorrection(final int index) {
        //accommodate for Difference between Index and Grid-Size
        return index + 1;
    }

}
