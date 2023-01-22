package de.jeujeus.game.of.life;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;

import java.util.Map;

import static java.util.stream.IntStream.*;

public class Field {

    private Field() {
    }

    static Table<Integer, Integer, Cell> generateField(final int fieldWidth, final int fieldHeight) {
        Table<Integer, Integer, Cell> field = HashBasedTable.create();

        range(0, fieldWidth)
                .forEachOrdered(c -> range(0, fieldHeight)
                        .forEachOrdered(r -> field.put(c, r, new Cell(c, r))));

        return field;
    }

    static Table<Integer, Integer, Cell> trimDeadOuterCellsFromField(final Table<Integer, Integer, Cell> untrimmedField) {
        //TODO refactor this monstrum
        if (untrimmedField.isEmpty()) return untrimmedField;

        Map<Integer, Map<Integer, Cell>> untrimmedFieldColumns = untrimmedField.columnMap();
        Map<Integer, Map<Integer, Cell>> untrimmedFieldRows = untrimmedField.rowMap();

        boolean outerLeftColumnDead = isOuterLeftColumnDead(untrimmedFieldColumns);
        boolean outerRightColumnDead = isOuterRightColumnDead(untrimmedFieldColumns);
        boolean outerTopRowDead = isOuterTopRowDead(untrimmedFieldRows);
        boolean outerBottomRowDead = isOuterBottomRowDead(untrimmedFieldRows);

        if (isNoOuterRowsColumnsDead(outerLeftColumnDead, outerRightColumnDead, outerTopRowDead, outerBottomRowDead)) {
            return untrimmedField;
        }

        if (outerLeftColumnDead) {
            untrimmedField.column(getFirstIndexIn(untrimmedFieldColumns))
                    .clear();
            return trimDeadOuterCellsFromField(untrimmedField);
        }
        if (outerRightColumnDead) {
            untrimmedField.column(getLastIndexIn(untrimmedFieldColumns))
                    .clear();
            return trimDeadOuterCellsFromField(untrimmedField);
        }
        if (outerTopRowDead) {
            untrimmedField.row(getFirstIndexIn(untrimmedFieldRows))
                    .clear();
            return trimDeadOuterCellsFromField(untrimmedField);
        }
        if (outerBottomRowDead) {
            untrimmedField.row(getLastIndexIn(untrimmedFieldRows))
                    .clear();
            return trimDeadOuterCellsFromField(untrimmedField);
        }


        return untrimmedField;
    }

    static Integer getFirstIndexIn(final Map<Integer, Map<Integer, Cell>> fields) {
        return Iterables.getFirst(fields.keySet()
                .stream()
                .sorted()
                .toList(), 0);
    }

    static Integer getLastIndexIn(final Map<Integer, Map<Integer, Cell>> fields) {
        return Iterables.getLast(fields.keySet()
                .stream()
                .sorted()
                .toList(), fields.size());
    }

    private static boolean isOuterLeftColumnDead(final Map<Integer, Map<Integer, Cell>> untrimmedFieldColumns) {
        Integer indexOfLeftColumn = getFirstIndexIn(untrimmedFieldColumns);
        Map<Integer, Cell> outerLeftColumn = untrimmedFieldColumns.get(indexOfLeftColumn);
        return containsOnlyDeadCells(outerLeftColumn);
    }

    private static boolean isOuterRightColumnDead(final Map<Integer, Map<Integer, Cell>> untrimmedFieldColumns) {
        Integer indexOfRightColumn = getLastIndexIn(untrimmedFieldColumns);
        Map<Integer, Cell> outerRightColumn = untrimmedFieldColumns.get(indexOfRightColumn);
        return containsOnlyDeadCells(outerRightColumn);
    }

    private static boolean isOuterTopRowDead(final Map<Integer, Map<Integer, Cell>> untrimmedFieldRows) {
        Integer indexOfTopRow = getFirstIndexIn(untrimmedFieldRows);
        Map<Integer, Cell> outerTopRow = untrimmedFieldRows.get(indexOfTopRow);
        return containsOnlyDeadCells(outerTopRow);
    }

    private static boolean isOuterBottomRowDead(final Map<Integer, Map<Integer, Cell>> untrimmedFieldRows) {
        Integer indexOfBottomRow = getLastIndexIn(untrimmedFieldRows);
        Map<Integer, Cell> outerBottomRow = untrimmedFieldRows.get(indexOfBottomRow);
        return containsOnlyDeadCells(outerBottomRow);
    }

    private static boolean isNoOuterRowsColumnsDead(final boolean outerLeftColumnDead,
                                                    final boolean outerRightColumnDead,
                                                    final boolean outerTopRowDead,
                                                    final boolean outerBottomRowDead) {
        return !outerLeftColumnDead && !outerRightColumnDead && !outerTopRowDead && !outerBottomRowDead;
    }

    static boolean containsOnlyDeadCells(final Map<Integer, Cell> cells) {
        return cells.values()
                .stream()
                .noneMatch(Cell::isAlive);
    }
}
