package de.jeujeus.game.of.life.game;

import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.IntStream.rangeClosed;

public class Generation {

    private Generation() {
    }

    static Table<Integer, Integer, Cell> calculateNextGeneration(Table<Integer, Integer, Cell> currentGeneration) {
        Integer xLowestIndex = Field.getFirstIndexIn(currentGeneration.columnMap());
        Integer xHighestIndex = indexCorrection(Field.getLastIndexIn(currentGeneration.columnMap()));
        Integer yLowestIndex = Field.getFirstIndexIn(currentGeneration.rowMap());
        Integer yHighestIndex = indexCorrection(Field.getLastIndexIn(currentGeneration.rowMap()));

        Table<Integer, Integer, Cell> nextField = Field.generateNextField(xLowestIndex, xHighestIndex, yLowestIndex, yHighestIndex);

        nextField.cellSet()
                .stream()
                .map(Table.Cell::getValue)
                .forEach(c -> {
                    Cell cellFromOldGeneration = Optional.ofNullable(currentGeneration.get(c.getXCoordinate(), c.getYCoordinate()))
                            .orElse(new Cell(c.getXCoordinate(), c.getYCoordinate()));
                    c.setAlive(isAliveNextGeneration(currentGeneration, cellFromOldGeneration));
                });

        return Field.trimDeadOuterCellsFromField(nextField);
    }

    private static int indexCorrection(final int index) {
        //accommodate for Difference between Index and Grid-Size
        return index + 1;
    }

    static boolean isAliveNextGeneration(Table<Integer, Integer, Cell> currentGeneration, Cell cell) {
        int aliveNeighbours = getCountOfAliveNeighbours(currentGeneration, cell);
//        Any live cell with two or three live neighbours survives.
//        Any dead cell with three live neighbours becomes a live cell.
//        All other live cells die in the next generation. Similarly, all other dead cells stay dead.
        return (cell.isAlive() && aliveNeighbours == 2) || aliveNeighbours == 3;
    }

    static int getCountOfAliveNeighbours(final Table<Integer, Integer, Cell> currentGeneration, final Cell cell) {
        List<Cell> neighbours = getNeighbours(currentGeneration, cell);
        return Math.toIntExact(neighbours.stream()
                .filter(Cell::isAlive)
                .count());
    }

    static List<Cell> getNeighbours(final Table<Integer, Integer, Cell> currentGeneration, final Cell cell) {
        int xCoordinate = cell.getXCoordinate();
        int yCoordinate = cell.getYCoordinate();

        ArrayList<Cell> neighbours = new ArrayList<>();
        rangeClosed(xCoordinate - 1, xCoordinate + 1)
                .forEachOrdered(c -> rangeClosed(yCoordinate - 1, yCoordinate + 1)
                        .forEachOrdered(r -> neighbours.add(currentGeneration.get(c, r))));

        return neighbours.parallelStream()
                .filter(Objects::nonNull)
                .filter(n -> isNotOriginCell(xCoordinate, yCoordinate, n))
                .toList();
    }

    private static boolean isNotOriginCell(final int xCoordinate, final int yCoordinate, final Cell n) {
        return !(n.getXCoordinate() == xCoordinate && n.getYCoordinate() == yCoordinate);
    }

}
