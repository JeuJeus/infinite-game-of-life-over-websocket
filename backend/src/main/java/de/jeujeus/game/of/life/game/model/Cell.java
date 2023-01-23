package de.jeujeus.game.of.life.game.model;

import com.google.common.collect.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.IntStream.rangeClosed;

@Getter
@Setter
public class Cell {

    private boolean isAlive;
    private int xCoordinate;

    private int yCoordinate;


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
        int aliveNeighbours = getCountOfAliveNeighbours(currentGeneration, cell);
//        Any live cell with two or three live neighbours survives.
//        Any dead cell with three live neighbours becomes a live cell.
//        All other live cells die in the next generation. Similarly, all other dead cells stay dead.
        return (cell.isAlive() && aliveNeighbours == 2) || aliveNeighbours == 3;
    }

    public static int getCountOfAliveNeighbours(final Table<Integer, Integer, Cell> currentGeneration, final Cell cell) {
        List<Cell> neighbours = getNeighbours(currentGeneration, cell);
        return Math.toIntExact(neighbours.stream()
                .filter(Cell::isAlive)
                .count());
    }

    public static List<Cell> getNeighbours(final Table<Integer, Integer, Cell> currentGeneration, final Cell cell) {
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
