package gnu.trove.list.linked;

import gnu.trove.queue.TCharQueue;

import java.util.NoSuchElementException;

/**
 * A resizable, double linked list of char primitives with queue capabilities.
 */
public class TCharLinkedQueueList extends TCharLinkedList implements TCharQueue {
    public TCharLinkedQueueList() {
        super();
    }

    public TCharLinkedQueueList(char no_entry_value) {
        super(no_entry_value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char element() {
        final TCharLink f = head;
        if (f == null)
            throw new NoSuchElementException();
        return f.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean offer(char e) {
        return add(e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char peek() {
        final TCharLink f = head;
        return (f == null) ? no_entry_value : f.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char poll() {
        final TCharLink f = head;
        return (f == null) ? no_entry_value : unlinkFirst(f);
    }

    private char unlinkFirst(TCharLink f) {
        // assert f == first && f != null;
        final char element = f.value;
        final TCharLink next = f.next;
        f.next = null; // help GC
        head = next;
        if (next == null)
            tail = null;
        else
            next.previous = null;
        size--;

        return element;
    }
}
