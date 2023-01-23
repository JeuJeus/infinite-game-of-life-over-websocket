package de.jeujeus.game.of.life.game;

import com.google.common.collect.Iterables;
import com.google.common.collect.Table;
import de.jeujeus.game.of.life.game.model.Cell;

import java.util.List;
import java.util.Map;

public class TrimmedField {
    private TrimmedField() {
    }

    public static List<Cell> trimDeadCellsFromField(final List<Cell> untrimmedField) {
        return untrimmedField.parallelStream()
                .filter(Cell::isAlive)
                .toList();
    }

    static Table<Integer, Integer, Cell> trimDeadOuterCellsFromField(final Table<Integer, Integer, Cell> fieldBeingTrimmed) {
        //TODO refactor this monstrum
        if (fieldBeingTrimmed.isEmpty()) return fieldBeingTrimmed;

        Map<Integer, Map<Integer, Cell>> untrimmedFieldColumns = fieldBeingTrimmed.columnMap();
        Map<Integer, Map<Integer, Cell>> untrimmedFieldRows = fieldBeingTrimmed.rowMap();

        boolean outerLeftColumnDead = isOuterLeftColumnDead(untrimmedFieldColumns);
        boolean outerRightColumnDead = isOuterRightColumnDead(untrimmedFieldColumns);
        boolean outerTopRowDead = isOuterTopRowDead(untrimmedFieldRows);
        boolean outerBottomRowDead = isOuterBottomRowDead(untrimmedFieldRows);

        if (isNoOuterRowsColumnsDead(outerLeftColumnDead, outerRightColumnDead, outerTopRowDead, outerBottomRowDead)) {
            return fieldBeingTrimmed;
        }

        if (outerLeftColumnDead) {
            fieldBeingTrimmed.column(getFirstIndexIn(untrimmedFieldColumns))
                    .clear();
            return trimDeadOuterCellsFromField(fieldBeingTrimmed);
        }
        if (outerRightColumnDead) {
            fieldBeingTrimmed.column(getLastIndexIn(untrimmedFieldColumns))
                    .clear();
            return trimDeadOuterCellsFromField(fieldBeingTrimmed);
        }
        if (outerTopRowDead) {
            fieldBeingTrimmed.row(getFirstIndexIn(untrimmedFieldRows))
                    .clear();
            return trimDeadOuterCellsFromField(fieldBeingTrimmed);
        }
        if (outerBottomRowDead) {
            fieldBeingTrimmed.row(getLastIndexIn(untrimmedFieldRows))
                    .clear();
            return trimDeadOuterCellsFromField(fieldBeingTrimmed);
        }


        return fieldBeingTrimmed;
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

    static boolean containsOnlyDeadCells(final Map<Integer, Cell> cells) {
        return cells.values()
                .stream()
                .noneMatch(Cell::isAlive);
    }
}
