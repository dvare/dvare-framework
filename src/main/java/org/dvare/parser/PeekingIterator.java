package org.dvare.parser;

import java.util.List;
import java.util.ListIterator;

public class PeekingIterator<T> implements ListIterator<T> {

    private final ListIterator<T> iterator;
    private T peek;

    public PeekingIterator(ListIterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        peek = iterator.next();
        return peek;
    }

    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    @Override
    public T previous() {
        return iterator.previous();
    }

    @Override
    public int nextIndex() {
        return iterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return iterator.previousIndex();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void set(T t) {
        iterator.set(t);
    }

    @Override
    public void add(T t) {
        iterator.add(t);
    }

    public void addAll(List<T> ts) {
        for (T t : ts) {
            iterator.add(t);
        }
    }

    public T peek() {
        if (peek == null && hasNext()) {
            next();
        }
        return peek;
    }
}
