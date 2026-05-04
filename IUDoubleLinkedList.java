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
        return new DoubleLinkedListListIterator();
    }


    @Override
    public ListIterator<E> listIterator() {
        return new DoubleLinkedListListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        return new DoubleLinkedListListIterator(startingIndex);
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

    /*Cursor for list iterator */
    private class DoubleLinkedListCursor {
        private int iterNextIndex;

        private DoubleLinkedListCursor(int nextIndex) {
            if (nextIndex < 0 || nextIndex > elemCount) throw new IndexOutOfBoundsException();
            this.iterNextIndex = nextIndex;
        }

        public int getNextIndex() {
            return iterNextIndex;
        }

        public int getPreviousIndex() {
            return iterNextIndex-1;
        }

        public void rightShift() {
            if(iterNextIndex >= elemCount) return;
            iterNextIndex++;
        }

        public void leftShift() {
            if(getPreviousIndex() <= -1) return;
            iterNextIndex--;
        }        
    }

    /*list iterator state enum*/
    private enum ListIteratorState { PREVIOUS, NEXT, NEITHER }

    /*list iterator */
	private class DoubleLinkedListListIterator implements ListIterator<E> {
        private DoubleLinkedListCursor cursor;
        private ListIteratorState state;
        private int listIterModCount;

        public DoubleLinkedListListIterator() {
            this(0);
        }

        public DoubleLinkedListListIterator(int nextIndex) {
            cursor = new DoubleLinkedListCursor(nextIndex);
            state = ListIteratorState.NEITHER;
            listIterModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            if(listIterModCount != modCount) throw new ConcurrentModificationException();
            return(cursor.getNextIndex() < elemCount);
        }

        @Override
        public E next() {
            if(!hasNext()) throw new NoSuchElementException();
            E element = get(cursor.getNextIndex());
            cursor.rightShift();
            state = ListIteratorState.NEXT;
            return element;
        }

        @Override
        public boolean hasPrevious() {
            if(listIterModCount != modCount) throw new ConcurrentModificationException();
            return cursor.getPreviousIndex() > -1;
        }

        @Override
        public E previous() {
            if(!hasPrevious()) throw new NoSuchElementException();
            E element = get(cursor.getPreviousIndex());
            cursor.leftShift();
            state = ListIteratorState.PREVIOUS;
            return element;
        }

        @Override
        public int nextIndex() {
            if (listIterModCount != modCount) throw new ConcurrentModificationException();
            return cursor.getNextIndex();
        }

        @Override
        public int previousIndex() {
            if (listIterModCount != modCount) throw new ConcurrentModificationException();
            return cursor.getPreviousIndex();
        }

        @Override
        public void remove() {
            if (listIterModCount != modCount) throw new ConcurrentModificationException();
            BidirectionalNode<E> offendingElement;
            if (!isEmpty() && elemCount != 1) {
                
                switch (state) {
                    case NEXT:
                        offendingElement = getNodeAtIndex(cursor.getPreviousIndex());
                        //grab the nodes surrounding the bad one
                        BidirectionalNode<E> next = offendingElement.getNext();
                        BidirectionalNode<E> previous = offendingElement.getPrevious();
                        //Tie the gap shut
                        previous.setNext(next);
                        next.setPrevious(previous);
                        offendingElement = null;
                        elemCount--;
                        cursor.leftShift();
                        break;
                    case PREVIOUS:
                        offendingElement = getNodeAtIndex(cursor.getNextIndex());
                        //grab the nodes surrounding the bad one
                        BidirectionalNode<E> prevNext = offendingElement.getNext();
                        BidirectionalNode<E> prevPrevious = offendingElement.getPrevious();
                        //Tie the gap shut
                        prevPrevious.setNext(prevNext);
                        prevNext.setPrevious(prevPrevious);
                        offendingElement = null;
                        elemCount--;
                        break;
                    case NEITHER:
                        throw new IllegalStateException();
                }
            } else if(elemCount == 1) {
                removeFirst();
                listIterModCount++;
            }else{
                return;
            }
        }

        @Override
        public void set(E element) {
            if (listIterModCount != modCount) throw new ConcurrentModificationException();
            if (size() == 0){
                addToRear(element);
                cursor.rightShift();
                listIterModCount++;
            }

            BidirectionalNode<E> newElement = new BidirectionalNode<>(element);
            BidirectionalNode<E> next = getNodeAtIndex(cursor.getNextIndex());
            BidirectionalNode<E> previous = getNodeAtIndex(cursor.getPreviousIndex());            

            switch (state) {
                case NEXT:
                    newElement.setNext(next);
                    newElement.setPrevious(previous.getPrevious());
                    break;
                case PREVIOUS:
                    newElement.setPrevious(previous);
                    newElement.setNext(next.getNext());
                    break;
                case NEITHER:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void add(E element) {
            if (listIterModCount != modCount) throw new ConcurrentModificationException();
            BidirectionalNode<E> newElement = new BidirectionalNode<>(element);
            if(elemCount == 0) {
                addToRear(element);
                listIterModCount++;
            } else {
                BidirectionalNode<E> next, previous;
                //get the nodes around the cursor
                previous = getNodeAtIndex(cursor.getPreviousIndex());
                next = getNodeAtIndex(cursor.getNextIndex());
                //tie the new node to the next and previous ones
                newElement.setPrevious(previous);
                newElement.setNext(next);
                //sever the tie between next and previous
                if(elemCount > 1){
                    if(previous != null) previous.setNext(newElement);
                    if(next != null) next.setPrevious(newElement);
                }
            }
        }  
    }
}
