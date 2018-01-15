package com.lays.indexer;

import java.util.ListIterator;

public interface SkipListIterator<E> extends ListIterator<E> {
    E getNextSkip();
    boolean hasNextSkip();
    E nextSkip();
    boolean hasPreviousSkip();
    E getPreviousSkip();
}
