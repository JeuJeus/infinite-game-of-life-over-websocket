package de.jeujeus.game.of.life.game;

import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;

import java.util.*;

public class Generation {

    private Generation() {
    }

    public static List<Cell> calculateNextGeneration(List<Cell> currentGenerationCells) {
        Table<Integer, Integer, Cell> currentGeneration = Generation.createGenerationFromList(currentGenerationCells);
        Table<Integer, Integer, Cell> nextGeneration = Generation.calculateNextGeneration(currentGeneration);

        return nextGeneration.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .toList();
    }

    static Table<Integer, Integer, Cell> createGenerationFromList(List<Cell> generation) {
        //TODO refactor this beast
        int xLowestIndex = generation.stream()
                .min(Comparator.comparing(Cell::getXCoordinate))
                .orElseThrow(NoSuchElementException::new)
                .getXCoordinate();
        int xHighestIndex = indexCorrection(generation.stream()
                .max(Comparator.comparing(Cell::getXCoordinate))
                .orElseThrow(NoSuchElementException::new)
                .getXCoordinate());
        int yLowestIndex = generation.stream()
                .min(Comparator.comparing(Cell::getYCoordinate))
                .orElseThrow(NoSuchElementException::new)
                .getYCoordinate();
        int yHighestIndex = indexCorrection(generation.stream()
                .max(Comparator.comparing(Cell::getYCoordinate))
                .orElseThrow(NoSuchElementException::new)
                .getYCoordinate());

        Table<Integer, Integer, Cell> builtField = Field.generateField(xLowestIndex, xHighestIndex, yLowestIndex, yHighestIndex);
        generation.parallelStream()
                .forEach(c -> Objects.requireNonNull(builtField.get(c.getXCoordinate(), c.getYCoordinate()))
                        .setAlive(c.isAlive()));

        return builtField;
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
