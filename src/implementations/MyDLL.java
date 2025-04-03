package implementations;

import utilities.ListADT;

import java.util.NoSuchElementException;

import utilities.Iterator;

public class MyDLL<E> implements ListADT<E> {
    private MyDLLNode<E> head;
    private MyDLLNode<E> tail;
    private int size;

    // Constructor
    public MyDLL() {
        head = tail = null;
        size = 0;
    }

    // Inner Node class for Doubly Linked List
    private static class MyDLLNode<E> {
        E data;
        MyDLLNode<E> next;
        MyDLLNode<E> prev;

        MyDLLNode(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    @Override
    public boolean add(int index, E toAdd) throws NullPointerException, IndexOutOfBoundsException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot add null element.");
        }
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        MyDLLNode<E> newNode = new MyDLLNode<>(toAdd);

        if (index == 0) {
            // Insert at the beginning
            if (head == null) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
        } else if (index == size) {
            // Insert at the end
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
        } else {
            // Insert in the middle
            MyDLLNode<E> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }

            newNode.next = current;
            newNode.prev = current.prev;
            current.prev.next = newNode;
            current.prev = newNode;
        }

        size++;
        return true;
    }
    
    @Override
    public boolean add(E toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot add null element.");
        }

        MyDLLNode<E> newNode = new MyDLLNode<>(toAdd);

        if (head == null) {
            // If the list is empty, set both head and tail to the new node.
            head = tail = newNode;
        } else {
            // Otherwise, add the new node at the end.
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        size++;
        return true;
    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        MyDLLNode<E> current = head;

        // Find the node to remove
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        // Remove the node
        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            // Removing the head
            head = current.next;
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            // Removing the tail
            tail = current.prev;
        }

        size--;
        return current.data;
    }

    @Override
    public E get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        MyDLLNode<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private MyDLLNode<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {
        if (toAdd == null) {
            throw new NullPointerException("Cannot add null list.");
        }

        Iterator<? extends E> it = toAdd.iterator();
        while (it.hasNext()) {
            add(size, it.next());  // Add each element at the end of the list
        }

        return true;
    }

    @Override
    public E remove(E toRemove) throws NullPointerException {
        if (toRemove == null) {
            throw new NullPointerException("Cannot remove null element.");
        }

        MyDLLNode<E> current = head;
        while (current != null) {
            if (current.data.equals(toRemove)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;  // Removing the head
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    tail = current.prev;  // Removing the tail
                }

                size--;
                return current.data;
            }
            current = current.next;
        }

        return null;  // Element not found
    }

    @Override
    public E set(int index, E toChange) throws NullPointerException, IndexOutOfBoundsException {
        if (toChange == null) {
            throw new NullPointerException("Cannot set null element.");
        }
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }

        MyDLLNode<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        E oldData = current.data;
        current.data = toChange;
        return oldData;
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

        MyDLLNode<E> current = head;
        while (current != null) {
            if (current.data.equals(toFind)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray(E[] toHold) throws NullPointerException {
        if (toHold == null) {
            throw new NullPointerException("The provided array is null.");
        }

        // If the provided array is large enough, we will fill it with elements from the list.
        if (toHold.length < size) {
            // Create a new array of the same type and required size if the provided array is too small.
            toHold = (E[]) java.lang.reflect.Array.newInstance(toHold.getClass().getComponentType(), size);
        }

        // Now we can safely copy the elements into the provided array
        MyDLLNode<E> currentNode = head;
        int index = 0;
        while (currentNode != null) {
            toHold[index++] = currentNode.data;
            currentNode = currentNode.next;
        }

        // If there are any unused spots in the array, we set them to null
        if (toHold.length > size) {
            toHold[size] = null;
        }

        return toHold;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        MyDLLNode<E> current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }
}
