package implementations;

import utilities.QueueADT;
import exceptions.EmptyQueueException;

/**
 * A generic queue implementation based on a doubly linked list.
 * 
 * <p>This class provides a First-In-First-Out (FIFO) data structure where elements are added
 * to the rear and removed from the front. It delegates its storage and basic operations to
 * an internal {@link MyDLL} instance.
 *
 * <p>Null elements are not allowed. Attempting to enqueue or search for null will throw a
 * {@link NullPointerException}.
 *
 * @param <E> the type of elements held in this queue
 * 
 * @version 1.0
 * @since 2025
 * 
 * @see MyDLL
 * @see QueueADT
 * @see EmptyQueueException
 * 
 * @author Lochlan Piercey
 * @author Murdoch Piercey
 * @author Terril Moyo
 */
public class MyQueue<E> implements QueueADT<E> {
	private static final long serialVersionUID = -3344759102270235119L;

    /** Internal doubly linked list to store queue elements */
	private MyDLL<E> list;

    /**
     * Constructs an empty queue.
     */
    public MyQueue() {
        list = new MyDLL<>();
    }

    /**
     * Adds an element to the rear of the queue.
     *
     * @param element the element to be added
     * @throws NullPointerException if the element is {@code null}
     */
    @Override
    public void enqueue(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null element to the queue.");
        }
        list.add(element);
    }

    /**
     * Removes and returns the front element from the queue.
     *
     * @return the element removed from the front
     * @throws EmptyQueueException if the queue is empty
     */
    @Override
    public E dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty, cannot dequeue.");
        }
        return list.remove(0);
    }

    /**
     * Retrieves, but does not remove, the front element of the queue.
     *
     * @return the front element
     * @throws EmptyQueueException if the queue is empty
     */
    @Override
    public E peek() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty, cannot peek.");
        }
        return list.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return list.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public utilities.Iterator<E> iterator() {
        return list.iterator();
    }

    /**
     * Removes all elements from the queue.
     */
    @Override
    public void dequeueAll() {
        list.clear();
    }

    /**
     * Checks if a specific element exists in the queue.
     *
     * @param toFind the element to search for
     * @return {@code true} if the element is found, {@code false} otherwise
     */
    @Override
    public boolean contains(E toFind) {
        return list.contains(toFind);
    }

    /**
     * Searches for the position of the specified element in the queue.
     *
     * @param toFind the element to find
     * @return the 1-based position of the element if found, or -1 if not found
     * @throws NullPointerException if {@code toFind} is null
     */
    @Override
    public int search(E toFind) {
        if (toFind == null) {
            throw new NullPointerException("Cannot search for null elements.");
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(toFind)) {
                return i + 1; // Return 1-based index
            }
        }

        return -1;
    }

    /**
     * Compares this queue to another queue for equality based on their elements.
     *
     * @param that another queue to compare with
     * @return {@code true} if both queues contain equal elements in the same order
     */
    @Override
    public boolean equals(QueueADT<E> that) {
        if (that == null || that.size() != this.size()) return false;

        utilities.Iterator<E> thisIt = this.iterator();
        utilities.Iterator<E> thatIt = that.iterator();

        while (thisIt.hasNext() && thatIt.hasNext()) {
            if (!thisIt.next().equals(thatIt.next())) return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E[] toArray(E[] holder) {
        return list.toArray(holder);
    }

    /**
     * Indicates whether the queue is full.
     *
     * @return always {@code false}, as this implementation has no fixed size
     */
    @Override
    public boolean isFull() {
        return false;
    }
}
