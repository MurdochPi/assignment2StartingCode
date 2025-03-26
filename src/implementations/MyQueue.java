package implementations;

public class MyQueue<E> implements QueueADT<E> {
    private MyDLL<E> list;

    public MyQueue() {
        list = new MyDLL<>();
    }

    @Override
    public void enqueue(E element) {
        // Add element to the queue (tail of the list)
    }

    @Override
    public E dequeue() throws EmptyQueueException {
        // Remove and return the front element from the queue
        return null;
    }

    @Override
    public E peek() throws EmptyQueueException {
        // Return the front element without removing it
        return null;
    }

    @Override
    public boolean isEmpty() {
        // Check if queue is empty
        return false;
    }

    @Override
    public int size() {
        // Return the size of the queue
        return 0;
    }
}

