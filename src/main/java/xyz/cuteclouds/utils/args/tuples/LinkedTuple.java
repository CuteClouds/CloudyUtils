package xyz.cuteclouds.utils.args.tuples;

import java.util.LinkedList;

/**
 * Default implementation of a {@link Tuple}, backed by a {@link LinkedList}
 */
public class LinkedTuple extends LinkedList<Arg> implements Tuple {

    /**
     * Constructs a list containing the specified object as the first element.
     *
     * @param firstValue the first value of the list
     */
    public LinkedTuple(Arg firstValue) {
        add(firstValue);
    }

    /**
     * Constructs an empty list.
     */
    public LinkedTuple() {
        super();
    }
}
