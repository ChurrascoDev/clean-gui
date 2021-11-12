package com.github.imthenico.cleangui.util;

public class Counter implements Runnable {

    private final int totalUpdates;
    private int elapsedUpdates;
    private final Runnable endAction;

    public Counter(int totalUpdates, Runnable endAction) {
        this.totalUpdates = totalUpdates;
        this.endAction = endAction;
    }

    public Counter(int totalUpdates) {
        this(totalUpdates, null);
    }

    public int getTotalUpdates() {
        return totalUpdates;
    }

    public int getElapsedUpdates() {
        return elapsedUpdates;
    }

    public Counter restart() {
        this.elapsedUpdates = 0;
        return this;
    }

    public boolean finished() {
        return elapsedUpdates >= totalUpdates;
    }

    @Override
    public void run() {
        if (totalUpdates <= 0)
            return;

        elapsedUpdates++;

        if (elapsedUpdates >= totalUpdates) {
            if (endAction != null)
                endAction.run();
        }
    }
}