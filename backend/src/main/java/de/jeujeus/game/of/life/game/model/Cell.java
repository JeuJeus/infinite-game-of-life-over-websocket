package de.jeujeus.game.of.life.game.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

import static com.google.common.collect.Tables.toTable;
import static java.util.stream.IntStream.rangeClosed;

@Getter
public class Cell {

    @Setter
    private boolean isAlive;
    private final int xCoordinate;
    private final int yCoordinate;


    public Cell(final int xCoordinate, final int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.isAlive = false;
    }

    public Cell(final boolean isAlive, final int xCoordinate, final int yCoordinate) {
        this.isAlive = isAlive;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public static boolean isAliveNextGeneration(Table<Integer, Integer, Cell> currentGeneration, Cell cell) {
        final int aliveNeighbours = getCountOfAliveNeighbours(currentGeneration, cell);
        //Any live cell with two or three live neighbours survives.
        //Any dead cell with three live neighbours becomes a live cell.
        //All other live cells die in the next generation. Similarly, all other dead cells stay dead.
        return (cell.isAlive() && aliveNeighbours == 2) || aliveNeighbours == 3;
    }

    public static int getCountOfAliveNeighbours(final Table<Integer, Integer, Cell> currentGeneration, final Cell cell) {
        final List<Cell> neighbours = getNeighbours(currentGeneration, cell);
        return Math.toIntExact(neighbours.parallelStream()
                .filter(Cell::isAlive)
                .count());
    }

    public static HashBasedTable<Integer, Integer, Cell> getCellAndItsNeighbours(final Table<Integer, Integer, Cell> currentGeneration, final Cell c) {
        final List<Cell> neighbours = Cell.getCellAndItsNeighbours(currentGeneration, c.getXCoordinate(), c.getYCoordinate());
        return neighbours.parallelStream()
                .collect(toTable(
                        Cell::getXCoordinate,
                        Cell::getYCoordinate,
                        cell -> cell,
                        HashBasedTable::create));
    }

    private static List<Cell> getCellAndItsNeighbours(final Table<Integer, Integer, Cell> currentGeneration, final int xCoordinate, final int yCoordinate) {
        return rangeClosed(xCoordinate - 1, xCoordinate + 1).boxed()
                .flatMap(c -> rangeClosed(yCoordinate - 1, yCoordinate + 1).boxed()
                        .map(r -> currentGeneration.get(c, r))
                        .filter(Objects::nonNull)
                )
                .toList();
    }

    public static List<Cell> getNeighbours(final Table<Integer, Integer, Cell> currentGeneration, final Cell cell) {
        final int xCoordinate = cell.getXCoordinate();
        final int yCoordinate = cell.getYCoordinate();
        return getCellAndItsNeighbours(currentGeneration, xCoordinate, yCoordinate).parallelStream()
                .filter(c -> isNotOriginCell(xCoordinate, yCoordinate, c))
                .toList();
    }

    private static boolean isNotOriginCell(final int xCoordinate, final int yCoordinate, final Cell n) {
        return !(n.getXCoordinate() == xCoordinate && n.getYCoordinate() == yCoordinate);
    }
}
