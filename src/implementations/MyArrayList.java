package implementations;

import java.util.NoSuchElementException;

import utilities.Iterator;
import utilities.ListADT;

@SuppressWarnings("serial")
public class MyArrayList<E> implements ListADT<E> {
    
    private static final int INITIAL_CAPACITY = 10;
    private Object[] data;
    private int size;
    
    // Constructor
    public MyArrayList() {
        data = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        data = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public boolean add(int index, E toAdd) throws NullPointerException, IndexOutOfBoundsException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot add null element.");
        }
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        
        // Resize array if necessary
        ensureCapacity();
        
        // Shift elements to the right
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        
        data[index] = toAdd;
        size++;
        return true;
    }

    @Override
    public boolean add(E toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot add null element.");
        }
        
        ensureCapacity();
        
        data[size] = toAdd;
        size++;
        return true;
    }

    @Override
    public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("The provided list is null.");
        }

        Iterator<? extends E> iterator = toAdd.iterator();  // Get the iterator for the provided list
        boolean modified = false;

        while (iterator.hasNext()) {
            E element = iterator.next();  // Get the next element
            add(element);  // Add it to the current list
            modified = true;
        }

        return modified;  // Return true if elements were added
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        return (E) data[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        E removedElement = (E) data[index];

        // Shift elements to the left
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        data[size - 1] = null;
        size--;
        return removedElement;
    }

    @Override
    public E remove(E toRemove) throws NullPointerException {
        if (toRemove == null) {
            throw new NullPointerException("Cannot remove null element.");
        }
        
        for (int i = 0; i < size; i++) {
            if (data[i].equals(toRemove)) {
                return remove(i);
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E set(int index, E toChange) throws NullPointerException, IndexOutOfBoundsException {
        if (toChange == null) {
            throw new NullPointerException("Cannot set null element.");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        E oldElement = (E) data[index];
        data[index] = toChange;
        return oldElement;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E toFind) throws NullPointerException {
        if (toFind == null) {
            throw new NullPointerException("Cannot search for null element.");
        }

        for (int i = 0; i < size; i++) {
            if (data[i].equals(toFind)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E[] toArray(E[] toHold) throws NullPointerException {
        if (toHold == null) {
            throw new NullPointerException("Cannot store elements in null array.");
        }

        if (toHold.length < size) {
            return (E[]) java.util.Arrays.copyOf(data, size, toHold.getClass());
        }

        System.arraycopy(data, 0, toHold, 0, size);
        return toHold;
    }

    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(data, size);
    }

    @Override
    public Iterator<E> iterator() {
        return new MyArrayListIterator();
    }

    // Helper method to ensure capacity
    private void ensureCapacity() {
        if (size >= data.length) {
            data = java.util.Arrays.copyOf(data, data.length * 2);
        }
    }

    // Inner iterator class
    private class MyArrayListIterator implements Iterator<E> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements.");
            }
            return (E) data[currentIndex++];
        }
    }
}
