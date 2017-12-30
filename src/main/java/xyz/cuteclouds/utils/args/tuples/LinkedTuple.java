package xyz.cuteclouds.utils.args.tuples;

import java.util.LinkedList;

public class LinkedTuple extends LinkedList<Arg> implements Tuple {
    public LinkedTuple(Arg firstValue) {
        add(firstValue);
    }

    public LinkedTuple() {
        super();
    }
}
