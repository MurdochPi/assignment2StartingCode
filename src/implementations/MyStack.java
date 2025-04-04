package implementations;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;

import utilities.Iterator;
import utilities.StackADT;

/**
 * A generic stack implementation based on an internal dynamic array list ({@link MyArrayList}).
 * 
 * <p>This class follows the Last-In-First-Out (LIFO) principle. Elements are pushed and popped
 * from the top of the stack, which corresponds to the end of the internal list.
 *
 * <p>Null elements are not allowed. Attempting to push or search for {@code null} will throw
 * a {@link NullPointerException}.
 *
 * @param <E> the type of elements held in this stack
 * 
 * @version 1.0
 * @since 2025
 * 
 * @see StackADT
 * @see MyArrayList
 * @see EmptyStackException
 * @see Iterator
 * 
 * @author Lochlan Piercey
 * @author Murdoch Piercey
 * @author Terril Moyo
 */
public class MyStack<E> implements StackADT<E> {

    private static final long serialVersionUID = 7046702979773370239L;

    /** Internal array-based list to hold stack elements */
    private MyArrayList<E> list;

    /**
     * Constructs an empty stack.
     */
    public MyStack() {
        list = new MyArrayList<>();
    }

    /**
     * Pushes an element onto the top of the stack.
     *
     * @param element the element to push
     * @throws NullPointerException if the element is {@code null}
     */
    @Override
    public void push(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null elements to the stack.");
        }
        list.add(element);
    }

    /**
     * Removes and returns the top element from the stack.
     *
     * @return the element removed from the top
     * @throws EmptyStackException if the stack is empty
     */
    @Override
    public E pop() throws EmptyStackException {
        if (list.isEmpty()) {
            throw new EmptyStackException();
        }
        return list.remove(list.size() - 1);
    }

    /**
     * Returns the top element without removing it.
     *
     * @return the top element
     * @throws EmptyStackException if the stack is empty
     */
    @Override
    public E peek() throws EmptyStackException {
        if (list.isEmpty()) {
            throw new EmptyStackException();
        }
        return list.get(list.size() - 1);
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
    public void clear() {
        list.clear();
    }

    /**
     * Returns an array containing the elements in the stack, from top to bottom.
     *
     * @return an array representing the stack
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(list.size() - 1 - i);
        }
        return array;
    }

    /**
     * Returns an array containing the elements in the stack, from top to bottom.
     *
     * @param holder the array into which the elements will be stored
     * @return the array containing stack elements
     * @throws NullPointerException if {@code holder} is {@code null}
     */
    @Override
    @SuppressWarnings("unchecked")
    public E[] toArray(E[] holder) throws NullPointerException {
        if (holder == null) {
            throw new NullPointerException("The provided array cannot be null.");
        }

        if (holder.length < list.size()) {
            holder = (E[]) java.lang.reflect.Array.newInstance(holder.getClass().getComponentType(), list.size());
        }

        for (int i = 0; i < list.size(); i++) {
            holder[i] = list.get(list.size() - 1 - i);
        }

        if (holder.length > list.size()) {
            holder[list.size()] = null;
        }

        return holder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(E toFind) throws NullPointerException {
        if (toFind == null) {
            throw new NullPointerException("Cannot search for null elements.");
        }
        return list.contains(toFind);
    }

    /**
     * Searches for the 1-based position of an element in the stack, starting from the top.
     *
     * @param toFind the element to search for
     * @return the 1-based position if found; -1 otherwise
     * @throws NullPointerException if {@code toFind} is {@code null}
     */
    @Override
    public int search(E toFind) {
        if (toFind == null) {
            throw new NullPointerException("Cannot search for null elements.");
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).equals(toFind)) {
                return list.size() - i;
            }
        }

        return -1;
    }

    /**
     * Returns an iterator that traverses the stack from top to bottom.
     *
     * @return an iterator for the stack
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = list.size() - 1;

            @Override
            public boolean hasNext() {
                return currentIndex >= 0;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return list.get(currentIndex--);
            }
        };
    }

    /**
     * Compares this stack to another for equality, based on order and values of elements.
     *
     * @param that another stack
     * @return {@code true} if both stacks are equal; {@code false} otherwise
     */
    @Override
    public boolean equals(StackADT<E> that) {
        if (that == null || this.size() != that.size()) {
            return false;
        }

        Iterator<E> thisIterator = this.iterator();
        Iterator<E> thatIterator = that.iterator();

        while (thisIterator.hasNext()) {
            if (!thisIterator.next().equals(thatIterator.next())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Indicates whether the stack is full.
     *
     * @return always {@code false} — this stack is dynamically resizable
     */
    @Override
    public boolean stackOverflow() {
        return false;
    }
}
