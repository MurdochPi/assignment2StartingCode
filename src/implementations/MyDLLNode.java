package implementations;

/**
 * Represents a node in a doubly linked list structure.
 * 
 * <p>This class is used internally by the {@link MyDLL} implementation to store
 * elements and maintain references to both the next and previous nodes in the list.
 *
 * @param <E> the type of data stored in the node
 * 
 * @author Lochlan Piercey
 * @author Murdoch Piercey
 * @author Terril Moyo
 * @version 1.0
 * @since 2025
 */
public class MyDLLNode<E> {

    /** The data stored in this node */
    private E data;

    /** Reference to the next node in the list */
    private MyDLLNode<E> next;

    /** Reference to the previous node in the list */
    private MyDLLNode<E> prev;

    /**
     * Constructs a new node containing the specified data.
     *
     * @param data the data to store in the node
     */
    public MyDLLNode(E data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }

    /**
     * Returns the data stored in this node.
     *
     * @return the data contained in this node
     */
    public E getData() {
        return data;
    }

    /**
     * Sets the data for this node.
     *
     * @param data the new data to store
     */
    public void setData(E data) {
        this.data = data;
    }

    /**
     * Returns the next node in the list.
     *
     * @return the next node, or {@code null} if this is the last node
     */
    public MyDLLNode<E> getNext() {
        return next;
    }

    /**
     * Sets the next node in the list.
     *
     * @param next the node to link as next
     */
    public void setNext(MyDLLNode<E> next) {
        this.next = next;
    }

    /**
     * Returns the previous node in the list.
     *
     * @return the previous node, or {@code null} if this is the first node
     */
    public MyDLLNode<E> getPrev() {
        return prev;
    }

    /**
     * Sets the previous node in the list.
     *
     * @param prev the node to link as previous
     */
    public void setPrev(MyDLLNode<E> prev) {
        this.prev = prev;
    }
}
