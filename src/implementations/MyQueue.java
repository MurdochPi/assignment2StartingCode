package implementations;

import utilities.QueueADT;
import exceptions.EmptyQueueException;
import java.util.Iterator;

public class MyQueue<E> implements QueueADT<E> {
    private MyDLL<E> list;

    public MyQueue() {
        list = new MyDLL<>();
    }

    @Override
    public void enqueue(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null element to the queue.");
        }
        list.add(element); // Add to the end of the list
    }

    @Override
    public E dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty, cannot dequeue.");
        }
        return list.remove(0); // Remove from the front
    }

    @Override
    public E peek() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty, cannot peek.");
        }
        return list.get(0); // Look at the front element
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator(); // Use the DLL's iterator
    }

    @Override
    public void dequeueAll() {
        list.clear(); // Clear the queue
    }

    @Override
    public boolean contains(E toFind) {
        return list.contains(toFind);
    }

    @Override
    public int search(E toFind) {
        return list.indexOf(toFind) + 1; // 1-based index for queue
    }

    @Override
    public boolean equals(QueueADT<E> that) {
        if (that == null || that.size() != this.size()) return false;
        Iterator<E> thisIt = this.iterator();
        Iterator<E> thatIt = that.iterator();
        while (thisIt.hasNext() && thatIt.hasNext()) {
            if (!thisIt.next().equals(thatIt.next())) return false;
        }
        return true;
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public E[] toArray(E[] holder) {
        return list.toArray(holder);
    }

    @Override
    public boolean isFull() {
        return false; // Linked list queues are never "full"
    }
}
