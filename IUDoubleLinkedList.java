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
            if (currentNode.getElement().equals(target)) {  // Make new node if target found
                BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
                BidirectionalNode<E> nextNode = currentNode.getNext();

                if (currentNode != rear) {  // If it isn't the last node in the collection
                    newNode.setNext(nextNode);
                    nextNode.setPrevious(newNode);
                }
                // We can always do this though
                currentNode.setNext(newNode);
                newNode.setPrevious(currentNode); 
                
                elemCount++;
                modCount++;
            }
            currentNode = currentNode.getNext(); // Check the next node
        }
        throw new NoSuchElementException("Target not found"); // Throw exception if target not found
    }

    @Override
    public void add(int index, E element) {
        validateIndex(index, size() + 1);

        if (index == 0) { // If adding to the front
            this.addToFront(element);
        } else if (index == size()) { // If adding to the rear
            this.addToRear(element);
        } else { // Rest of the time
            int currentIndex = 1;
            BidirectionalNode<E> currentNode = front.getNext();
            while (currentIndex != index) { // Traverse the list until at the correct index
                currentNode = currentNode.getNext();
                currentIndex++;
            }
            // Stitch it up
            BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
            BidirectionalNode<E> nextNode = currentNode.getNext();
            currentNode.setNext(newNode);
            newNode.setPrevious(currentNode);
            newNode.setNext(nextNode);
            nextNode.setPrevious(newNode);
        }

        elemCount++;
        modCount++;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public E remove(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void set(int index, E element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }

    @Override
    public E get(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public int indexOf(E element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'indexOf'");
    }

    @Override
    public E first() {
        if (isEmpty()) throw new NoSuchElementException("list is empty");
        return front.getElement();
    }

    @Override
    public E last() {
        if (isEmpty()) throw new NoSuchElementException("list is empty");
        return rear.getElement();
    }

    @Override
    public boolean contains(E target) {
        if (isEmpty()) return false; // Let's not loop if we don't have to 
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

    // Throws an exception if collection is empty
    private void throwIfEmpty() {
		if (isEmpty()) throw new NoSuchElementException("list is empty");
	}

    // Throws an exception if index is out of bounds
    private void validateIndex(int index, int max) {
		if (index < 0 || index >= max) throw new IndexOutOfBoundsException();
	}
    
}
