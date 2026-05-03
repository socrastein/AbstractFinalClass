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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAfter'");
    }

    @Override
    public void add(int index, E element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public E removeFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFirst'");
    }

    @Override
    public E removeLast() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLast'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return count;
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
    
}
