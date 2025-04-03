package implementations;

import utilities.StackADT;

public class MyStack<E> implements StackADT<E> {
    private MyArrayList<E> list;

    public MyStack() {
        list = new MyArrayList<>();
    }

    @Override
    public void push(E element) {
        // Add element to the stack (top of the list)
    }

    @Override
    public E pop() throws EmptyStackException {
        // Remove and return top element from the stack
        return null;
    }

    @Override
    public E peek() throws EmptyStackException {
        // Return top element without removing it
        return null;
    }

    @Override
    public boolean isEmpty() {
        // Check if stack is empty
        return false;
    }

    @Override
    public int size() {
        // Return the size of the stack
        return 0;
    }
}

