package com.github.imthenico.cleangui.fill;

import com.github.imthenico.cleangui.util.Validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class FillOperation {

    private final List<Integer> results;
    private int skipping;
    private final Set<Integer> ignoredSlots;
    private final int from;
    private final int to;
    private final Direction direction;
    private final Predicate<Integer> condition;

    public FillOperation(int from, int to, Direction direction, Predicate<Integer> condition) {
        this.results = new ArrayList<>();
        this.ignoredSlots = new HashSet<>();
        this.from = from;
        this.to = to;
        this.direction = direction;
        this.condition = condition;
    }

    public FillOperation skipping(int toSkip) {
        this.skipping = toSkip;
        return this;
    }

    public FillOperation ignore(int from, int to) {
        for (int i = from; i <= to; i++) {
            ignoredSlots.add(i);
        }

        return this;
    }

    public FillOperation ignore(int slot) {
        ignoredSlots.add(slot);

        return this;
    }

    public static FillOperation fill(int from, int to) {
        return new FillOperation(from, to, Direction.RIGHT, null);
    }

    public static FillOperation fill(int from, int amountOfSlots, Direction direction) {
        return new FillOperation(from, from + (numberForCalcByDirection(direction) * (amountOfSlots - 1)), direction, null);
    }

    public static FillOperation fillWhile(int from, Direction direction, Predicate<Integer> condition) {
        return new FillOperation(from, -1, direction, condition);
    }

    public static FillOperation[] fillBorders(int inventorySize) {
        int rows = inventorySize / 9;

        return fill(0, 9, Direction.RIGHT, 8, rows, Direction.DOWN, (rows * 9) - 1, 9, Direction.LEFT, (rows - 1) * 9, rows, Direction.UP);
    }

    public static FillOperation[] fill(Object... boundsInThree) {
        Validate.isTrue(boundsInThree.length % 3 == 0, "bounds must be divisible by 3");
        FillOperation[] operations = new FillOperation[boundsInThree.length / 3];

        int a = 0;
        for (int i = 0; i < boundsInThree.length; i++) {
            int from = (int) boundsInThree[i];
            i++;
            int to = (int) boundsInThree[i];
            i++;
            Direction direction = (Direction) boundsInThree[i];

            operations[a] = fill(from, to, direction);
            a++;
        }

        return operations;
    }

    public List<Integer> complete() {
        if (!results.isEmpty())
            return results;

        int direction = numberForCalcByDirection(this.direction);
        if (to < 0) {
            if (direction < 0) {
                reverseFill(to, from, direction);
            } else {
                commonFill(from, to, direction);
            }
        } else {
            if (from > to) {
                reverseFill(to, from, direction);
            } else {
                commonFill(from, to, direction);
            }
        }

        return results;
    }

    private void reverseFill(int from, int to, int numberForCalc) {
        int slotsToSkip = 0;

        for (int slot = to; (condition != null ? condition.test(slot) : slot >= from);) {
            if (ignoredSlots.contains(slot))
                continue;

            if (slotsToSkip == 0) {
                this.results.add(slot);

                slotsToSkip = skipping;
            } else {
                slotsToSkip--;
            }

            slot += numberForCalc;
        }
    }

    private void commonFill(int from, int to, int numberForCalc) {
        int slotsToSkip = 0;

        for (int slot = from; (condition != null ? condition.test(slot) : slot <= to);) {
            if (ignoredSlots.contains(slot))
                continue;

            if (slotsToSkip == 0) {
                this.results.add(slot);

                slotsToSkip = skipping;
            } else {
                slotsToSkip--;
            }

            slot += numberForCalc;
        }
    }

    private static int numberForCalcByDirection(Direction direction) {
        switch (direction) {
            case UP:
                return -9;
            case DOWN:
                return 9;
            case RIGHT:
                return 1;
            case LEFT:
                return -1;
            default:
                throw new IllegalArgumentException();
        }
    }
}