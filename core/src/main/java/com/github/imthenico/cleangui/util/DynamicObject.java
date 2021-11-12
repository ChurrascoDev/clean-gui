package com.github.imthenico.cleangui.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A class that stores an array of objects and returns
 * a different object for every specified number of updates.
 *
 * @param <T> - The object type to manipulate
 */
public final class DynamicObject<T> implements Iterable<DynamicObject<T>.Entry> {

    private Counter counter;
    private final List<Entry> entries;
    private int currentIndex;

    public DynamicObject(int totalUpdates, T... objects) {
        Validate.isTrue(objects.length > 0);

        this.entries = new ArrayList<>(objects.length);

        for (T entry : objects) {
            this.entries.add(new Entry(entry, totalUpdates));
        }

        if (entries.size() > 1) {
            this.counter = new Counter(entries.get(currentIndex).updateTime);
        }
    }

    public DynamicObject(Map<T, Integer> objects) {
        Validate.isTrue(objects.size() > 0);

        this.entries = new ArrayList<>(objects.size());

        objects.forEach((k, v) -> this.entries.add(new Entry(k, v)));

        if (entries.size() > 1) {
            this.counter = new Counter(entries.get(currentIndex).updateTime);
        }
    }

    public T get(int index) {
        return entries.get(index).object;
    }

    public T getCurrent() {
        return entries.get(currentIndex).object;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean updateInternalIndex() {
        if (counter == null)
            return false;

        if (counter.finished()) {
            if (currentIndex == (entries.size() - 1)) {
                currentIndex = 0;
            } else {
                currentIndex++;
            }

            counter = new Counter(entries.get(currentIndex).updateTime);
            return true;
        } else {
            counter.run();

            return false;
        }
    }

    public void setIndex(int index) {
        if (index < 0 || index >= entries.size()) {
            throw new ArrayIndexOutOfBoundsException("index < 0 || index >= length");
        }

        this.currentIndex = index;
    }

    public Counter getCounter() {
        return counter;
    }

    @Override
    public Iterator<Entry> iterator() {
        return new UnmodifiableIterator<>(entries.iterator());
    }

    public class Entry {
        private final T object;
        private final int updateTime;

        private Entry(T object, int updateTime) {
            this.object = object;
            this.updateTime = updateTime;
        }

        public T getObject() {
            return object;
        }

        public int getUpdateTime() {
            return updateTime;
        }
    }
}