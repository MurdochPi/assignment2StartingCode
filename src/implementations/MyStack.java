package implementations;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;

import utilities.Iterator;
import utilities.StackADT;

public class MyStack<E> implements StackADT<E> {
	private static final long serialVersionUID = 7046702979773370239L;
	private MyArrayList<E> list;

    // Constructor
    public MyStack() {
        list = new MyArrayList<>();
    }

    @Override
    public void push(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null elements to the stack.");
        }
        list.add(element);  // Add element to the top of the stack (list's end)
    }

    @Override
    public E pop() throws EmptyStackException {
        if (list.isEmpty()) {
            throw new EmptyStackException();  // If the stack is empty, throw an exception
        }
        return list.remove(list.size() - 1);  // Remove and return the top element
    }

    @Override
    public E peek() throws EmptyStackException {
        if (list.isEmpty()) {
            throw new EmptyStackException();  // If the stack is empty, throw an exception
        }
        return list.get(list.size() - 1);  // Return the top element without removing it
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();  // Check if the stack is empty by checking the list
    }

    @Override
    public int size() {
        return list.size();  // Return the current size of the stack
    }

    @Override
    public void clear() {
        list.clear();  // Clear all elements in the stack
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[list.size()];
        
        // Copy elements from the top of the stack to the array
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(list.size() - 1 - i);  // Get elements starting from the top of the stack
        }
        
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E[] toArray(E[] holder) throws NullPointerException {
        // Step 1: Check if the provided array (holder) is null.
        if (holder == null) {
            throw new NullPointerException("The provided array cannot be null.");
        }

        // Step 2: If the provided array is too small, create a new array with the correct size.
        if (holder.length < list.size()) {
            // Create a new array of the correct size using Array.newInstance
            holder = (E[]) java.lang.reflect.Array.newInstance(holder.getClass().getComponentType(), list.size());
        }

        // Step 3: Copy elements into the provided holder array (we assume it's large enough).
        for (int i = 0; i < list.size(); i++) {
            holder[i] = list.get(list.size() - 1 - i);  // Copy elements from top to bottom
        }

        // Step 4: If the holder array is larger than necessary, null out the extra space.
        if (holder.length > list.size()) {
            holder[list.size()] = null;  // Set any unused elements to null
        }

        return holder;
    }


    @Override
    public boolean contains(E toFind) throws NullPointerException {
        if (toFind == null) {
            throw new NullPointerException("Cannot search for null elements.");
        }
        return list.contains(toFind);
    }

    @Override
    public int search(E toFind) {
        if (toFind == null) {
            throw new NullPointerException("Cannot search for null elements.");
        }
        
        // Traverse from the top (end of the list) towards the bottom
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).equals(toFind)) {
                return list.size() - i;
            }
        }
        
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        // Create a new Iterator for the stack, starting from the top element
        return new Iterator<E>() {
            // Start from the top of the stack
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


    @Override
    public boolean equals(StackADT<E> that) {
        if (that == null) {
            return false;
        }
        if (this.size() != that.size()) {
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

    @Override
    public boolean stackOverflow() {
        return false;
    }
}