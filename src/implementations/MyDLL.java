package implementations;

import utilities.ListADT;

public class MyDLL<E> implements ListADT<E> {
    private MyDLLNode<E> head;
    private MyDLLNode<E> tail;
    private int size;

    public MyDLL() {
        head = tail = null;
        size = 0;
    }

    @Override
    public void add(E element) {
        // Add element to the end of the linked list
    }

    @Override
    public void add(int index, E element) throws IndexOutOfBoundsException {
        // Add element at specified index
    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        // Remove element at specified index
        return null;
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        // Get element at specified index
        return null;
    }

    @Override
    public int size() {
        // Return size of the list
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        // Return iterator
        return null;
    }
}

