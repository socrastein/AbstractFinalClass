import java.util.*;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {

    private BidirectionalNode<E> front, rear;
    private int elemCount, modCount;

    public IUDoubleLinkedList() {
        front = rear = null;
        elemCount = modCount = 0;
    }

    @Override
    public void addToFront(E element) {
        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);

        if (isEmpty()) { // collection empty: newNode is rear
            rear = newNode;
        } else { // collection not empty: newNode and front point at each other
            newNode.setNext(front);
            front.setPrevious(newNode);
        }
        front = newNode; // both cases: newNode is new front

        elemCount++;
        modCount++;
    }

    @Override
    public void addToRear(E element) {
        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);

        if (isEmpty()) { // collection empty: newNode is front
            front = newNode;
        } else { // collection not empty: newNode and rear point at each other
            newNode.setPrevious(rear);
            rear.setNext(newNode);
        }
        rear = newNode; // both cases: newNode is new rear

        elemCount++;
        modCount++;
    }

    @Override
    public void add(E element) {
        this.addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) {
        BidirectionalNode<E> currentNode = front;

        while (currentNode != null) {
            if (currentNode.getElement().equals(target)) { // Make new node if target found
                if (currentNode == rear) {
                    addToRear(element);
                    return;
                }

                BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
                BidirectionalNode<E> next = currentNode.getNext();

                addNodeBetweenNodes(currentNode, newNode, next);
            }
            currentNode = currentNode.getNext(); // Check the next node
        }
        throw new NoSuchElementException("Target not found"); // Throw exception if target not found
    }

    @Override
    public void add(int index, E element) {
        validateIndex(index, size() + 1);

        if (index == 0) { // If adding to the front
            addToFront(element);
            return;
        }
        if (index == size()) { // If adding to the rear
            addToRear(element);
            return;
        }

        BidirectionalNode<E> currentNode = getNodeAtIndex(index);

        // Place node between current's previous and current
        // so it has same index that current did
        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
        BidirectionalNode<E> previous = currentNode.getPrevious();
        addNodeBetweenNodes(previous, newNode, currentNode);
    }

    @Override
    public E removeFirst() {
        throwIfEmpty();
        E returnValue = front.getElement();
        front = front.getNext();
        if (size() != 1) { // If this isn't the only thing in the collection
            front.setPrevious(null);
        } else { // If it IS the only thing in the collection
            rear = null;
        }
        elemCount--;
        modCount++;
        return returnValue;

    }

    @Override
    public E removeLast() {
        throwIfEmpty();
        E returnValue = rear.getElement();
        rear = rear.getPrevious();
        if (size() != 1) { // If this isn't the only thing in the collection
            rear.setNext(null);
        } else { // If it IS the only thing in the collection
            front = null;
        }
        elemCount--;
        modCount++;
        return returnValue;
    }

    @Override
    public E remove(E element) {
        throwIfEmpty();
        BidirectionalNode<E> currentNode = front;
        while (currentNode != null) {
            if (currentNode.getElement().equals(element)) {
                if (currentNode == front)
                    return removeFirst();
                if (currentNode == rear)
                    return removeLast();

                // Found a match somewhere in the middle
                return removeNodeBetweenNodes(currentNode);
            }
            currentNode = currentNode.getNext();
        }
        throw new NoSuchElementException();
    }

    @Override
    public E remove(int index) {
        validateIndex(index, size());
        if (index == 0)
            return removeFirst();
        if (index == size() - 1)
            return removeLast();

        BidirectionalNode<E> currentNode = getNodeAtIndex(index);
        return removeNodeBetweenNodes(currentNode);
    }

    @Override
    public void set(int index, E element) {
        validateIndex(index, size());
        BidirectionalNode<E> target = getNodeAtIndex(index);
        target.setElement(element);
        modCount++;
    }

    @Override
    public E get(int index) {
        validateIndex(index, size());
        BidirectionalNode<E> target = getNodeAtIndex(index);
        return target.getElement();
    }

    @Override
    public int indexOf(E element) {
        int currentIndex = 0;
        BidirectionalNode<E> currentNode = front;
        while (currentNode != null) {
            if (currentNode.getElement().equals(element)) {
                return currentIndex;
            }
            currentNode = currentNode.getNext();
            currentIndex++;
        }
        return -1;
    }

    @Override
    public E first() {
        throwIfEmpty();
        return front.getElement();
    }

    @Override
    public E last() {
        throwIfEmpty();
        return rear.getElement();
    }

    @Override
    public boolean contains(E target) {
        if (isEmpty())
            return false; // Let's not loop if we don't have to
        BidirectionalNode<E> currentNode = front;
        while (currentNode != null) {
            if (currentNode.getElement().equals(target)) { // Return true if element matches target
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false; // Return false if the element wasn't found
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return elemCount;
    }

    @Override
    public Iterator<E> iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    /**
     * Throws NoSuchElementException if collection is empty
     */
    private void throwIfEmpty() {
        if (isEmpty())
            throw new NoSuchElementException("list is empty");
    }

    /**
     * Throws IndexOutOfBoundsException if index is out of bounds
     */
    private void validateIndex(int index, int max) {
        if (index < 0 || index >= max)
            throw new IndexOutOfBoundsException();
    }

    /**
     * Returns a reference to the node at the specified index
     * 
     * @return the node found at index
     */
    private BidirectionalNode<E> getNodeAtIndex(int index) {
        int currentIndex = 0;
        BidirectionalNode<E> currentNode = front;
        while (currentIndex != index) {
            currentNode = currentNode.getNext();
            currentIndex++;
        }
        return currentNode;
    }

    /**
     * Removes node from between two other nodes, setting its previous to point to
     * its next and vice versa
     * 
     * @return the E value the node was holding
     */
    private E removeNodeBetweenNodes(BidirectionalNode<E> node) {
        E returnValue = node.getElement();
        // Set previous node to point to one after current
        node.getPrevious().setNext(node.getNext());
        // Set node after current to point to one before current
        node.getNext().setPrevious(node.getPrevious());

        elemCount--;
        modCount++;
        return returnValue;
    }

    private void addNodeBetweenNodes(BidirectionalNode<E> previous, BidirectionalNode<E> newNode,
            BidirectionalNode<E> next) {
        previous.setNext(newNode);
        newNode.setPrevious(previous);
        next.setPrevious(newNode);
        newNode.setNext(next);

        elemCount++;
        modCount++;
    }
}
