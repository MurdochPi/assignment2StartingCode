package implementations;

import java.util.NoSuchElementException;
import utilities.Iterator;
import utilities.ListADT;

/**
 * A custom implementation of the {@link ListADT} interface using a resizable array.
 * 
 * <p>This implementation mimics the behavior of {@link java.util.ArrayList}, supporting
 * dynamic resizing, indexed access, and standard list operations.
 * 
 * <p>Null elements are not allowed. Operations that attempt to add or manipulate null elements
 * will result in a {@link NullPointerException}.
 * 
 * @param <E> the type of elements in this list
 * 
 * @version 1.0 
 * 
 * @author Lochlan Piercey
 * @author Murdoch Piercey
 * @author Terril Moyo
 */
public class MyArrayList<E> implements ListADT<E> {

    private static final long serialVersionUID = 982267963980463371L;
    private static final int INITIAL_CAPACITY = 10;

    private Object[] data;
    private int size;

    /**
     * Constructs an empty list with an initial capacity.
     */
    public MyArrayList() {
        data = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        data = new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(int index, E toAdd) throws NullPointerException, IndexOutOfBoundsException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot add null element.");
        }
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        ensureCapacity();

        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }

        data[index] = toAdd;
        size++;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(E toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot add null element.");
        }

        ensureCapacity();
        data[size++] = toAdd;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("The provided list is null.");
        }

        Iterator<? extends E> iterator = toAdd.iterator();
        boolean modified = false;

        while (iterator.hasNext()) {
            E element = iterator.next();
            add(element);
            modified = true;
        }

        return modified;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        return (E) data[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        E removedElement = (E) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        data[size - 1] = null;
        size--;
        return removedElement;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return java.util.Arrays.copyOf(data, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<E> iterator() {
        return new MyArrayListIterator();
    }

    /**
     * Ensures that the internal array has enough capacity to hold additional elements.
     * If not, the array size is doubled.
     */
    private void ensureCapacity() {
        if (size >= data.length) {
            data = java.util.Arrays.copyOf(data, data.length * 2);
        }
    }

    /**
     * An implementation of the {@link Iterator} interface for {@code MyArrayList}.
     */
    private class MyArrayListIterator implements Iterator<E> {
        private int currentIndex = 0;

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        /**
         * {@inheritDoc}
         */
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
