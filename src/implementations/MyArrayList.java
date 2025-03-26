/**
 * 
 */
package implementations;

import java.util.Iterator;

public class MyArrayList<E> {
    private E[] elements;
    private int size;
    
    public MyArrayList() {
        // Initialize with a default size
        elements = (E[]) new Object[10];  // Casting to E[]
        size = 0;
    }

    public void add(E element) {
        // Add element to the list
    }

    public void add(int index, E element) throws IndexOutOfBoundsException {
        // Add element at specified index
    }

    public E remove(int index) throws IndexOutOfBoundsException {
		return null;
        // Remove element at specified index
    }

    public E get(int index) throws IndexOutOfBoundsException {
        // Get element at specified index
        return null;
    }

    public int size() {
        // Return the size of the list
        return size;
    }

    public Iterator<E> iterator() {
        // Return an iterator
        return null;
    }

    // Utility method for resizing the array
    private void resize() {
        // Resize logic
    }
}
