package com.github.imthenico.cleangui.util;

import java.util.Iterator;

public class UnmodifiableIterator<T> implements Iterator<T> {

    private final Iterator<T> original;

    public UnmodifiableIterator(Iterator<T> original) {
        this.original = original;
    }

    @Override
    public boolean hasNext() {
        return original.hasNext();
    }

    @Override
    public T next() {
        return original.next();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}