import java.util.*;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author
 * 
 * @param <E> type to store
 */
public class IUSingleLinkedList<E> implements IndexedUnsortedList<E> {
	private LinearNode<E> front, rear;
	private int count;
	private int modCount;

	/** Creates an empty list */
	public IUSingleLinkedList() {
		front = rear = null;
		count = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(E element) {
		// TODO
		LinearNode<E> newNode = new LinearNode<>(element);
		newNode.setNext(front);
		front = newNode;

		if (count == 0)
			rear = newNode;

		count++;
		modCount++;
	}

	@Override
	public void addToRear(E element) {
		// TODO
		LinearNode<E> newNode = new LinearNode<>(element);

		if (count > 0)
			rear.setNext(newNode);
		else
			front = newNode;

		rear = newNode;

		count++;
		modCount++;
	}

	@Override
	public void add(E element) {
		// TODO
		this.addToRear(element);
	}

	@Override
	public void addAfter(E element, E target) {
		// TODO
		LinearNode<E> currentNode = front;

		while (currentNode != null) {
			if (currentNode.getElement() == target) {
				LinearNode<E> newNode = new LinearNode<>(element);

				// Link new node in between target and target's next node
				newNode.setNext(currentNode.getNext());
				currentNode.setNext(newNode);

				// If target was the rear node, set the new node being added after it to rear
				if (currentNode == rear)
					rear = newNode;

				count++;
				modCount++;
				return;
			}
			currentNode = currentNode.getNext();
		}

		throw new NoSuchElementException();
	}

	@Override
	public void add(int index, E element) {
		// TODO
		validateIndex(index, count + 1);

		if (index == 0) {
			this.addToFront(element);
			return;
		}

		int currentIndex = 1;
		LinearNode<E> previousNode = front;
		LinearNode<E> currentNode = front.getNext();
		LinearNode<E> newNode = new LinearNode<>(element);

		while (currentIndex != index) {
			previousNode = currentNode;
			currentNode = currentNode.getNext();
			currentIndex++;
		}

		// If element is being set to the last index, designate it the new rear
		previousNode.setNext(newNode);
		if (currentNode == null) {
			rear = newNode;
		} else {
			newNode.setNext(currentNode);
		}

		count++;
		modCount++;
	}

	@Override
	public E removeFirst() {
		// TODO
		throwIfEmpty();
		E returnValue = front.getElement();
		front = front.getNext();
		count--;
		modCount++;
		return returnValue;
	}

	@Override
	public E removeLast() {
		// TODO
		throwIfEmpty();
		E returnValue = rear.getElement();
		LinearNode<E> current = front;
		if (front == rear) {
			front = rear = null;
		} else {
			while (current.getNext() != rear) {
				current = current.getNext();
			}
		}

		rear = current;
		rear.setNext(null);

		count--;
		modCount++;
		return returnValue;
	}

	@Override
	public E remove(E element) {
		throwIfEmpty();
		LinearNode<E> current = front, previous = null;
		while (current != null && !current.getElement().equals(element)) {
			previous = current;
			current = current.getNext();
		}
		// Matching element not found
		if (current == null) {
			throw new NoSuchElementException();
		}
		return removeElement(previous, current);
	}

	@Override
	public E remove(int index) {
		// TODO
		validateIndex(index, count);

		if (index == 0)
			return removeFirst();
		if (index == count - 1)
			return removeLast();

		LinearNode<E> previous = nodeAtIndex(index - 1);
		LinearNode<E> current = nodeAtIndex(index);

		return removeElement(previous, current);
	}

	@Override
	public void set(int index, E element) {
		// TODO
		validateIndex(index, count);
		nodeAtIndex(index).setElement(element);
		modCount++;
	}

	@Override
	public E get(int index) {
		// TODO
		validateIndex(index, count);
		return nodeAtIndex(index).getElement();
	}

	@Override
	public int indexOf(E element) {
		// TODO
		LinearNode<E> current = front;
		int currentIndex = 0;
		while (current != null) {
			if (current.getElement().equals(element)) {
				return currentIndex;
			}
			current = current.getNext();
			currentIndex++;
		}
		return -1;
	}

	@Override
	public E first() {
		// TODO
		throwIfEmpty();
		return front.getElement();
	}

	@Override
	public E last() {
		// TODO
		throwIfEmpty();
		return rear.getElement();
	}

	@Override
	public boolean contains(E target) {
		// TODO
		if (isEmpty())
			return false;
		LinearNode<E> currentNode = front;
		while (currentNode != null) {
			if (currentNode.getElement().equals(target)) {
				return true;
			}
			currentNode = currentNode.getNext();
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO
		return count == 0;
	}

	@Override
	public int size() {
		// TODO
		return count;
	}

	@Override
	public String toString() {
		// TODO
		StringBuilder result = new StringBuilder("[");
		LinearNode<E> currentNode = front;
		while (currentNode != null) {
			result.append(currentNode.getElement());
			if (currentNode.getNext() != null) {
				result.append(", ");
			}
			currentNode = currentNode.getNext();
		}
		result.append("]");
		return result.toString();
	}

	private void throwIfEmpty() {
		if (isEmpty())
			throw new NoSuchElementException();
	}

	private void validateIndex(int index, int max) {
		if (index < 0 || index >= max) {
			throw new IndexOutOfBoundsException();
		}
	}

	private LinearNode<E> nodeAtIndex(int index) {
		LinearNode<E> current = front;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		return current;
	}

	private E removeElement(LinearNode<E> previous, LinearNode<E> current) {
		// Grab element
		E result = current.getElement();
		// If not the first element in the list
		if (previous != null) {
			previous.setNext(current.getNext());
		} else { // If the first element in the list
			front = current.getNext();
		}
		// If the last element in the list
		if (current.getNext() == null) {
			rear = previous;
		}
		count--;
		modCount++;

		return result;
	}

	@Override
	public Iterator<E> iterator() {
		return new SLLIterator();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<E> {
		private LinearNode<E> previous;
		private LinearNode<E> current;
		private LinearNode<E> next;
		private int iterModCount;
		private boolean canRemove = false;

		/** Creates a new iterator for the list */
		public SLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
		}

		public boolean hasNext() {
			if (iterModCount != modCount)
				throw new ConcurrentModificationException();

			return (next != null);
		}

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			E element;

			// Shift 'bookmarks' forward
			previous = current;
			current = next;
			next = next.getNext();

			// Store the element to return and flag the iterator as ready for removal
			element = current.getElement();
			canRemove = true;

			return element;
		}

		public void remove() {
			if (iterModCount != modCount)
				throw new ConcurrentModificationException();
			if (!canRemove)
				throw new IllegalStateException();

			// Using this method will adjust reference variables
			removeElement(previous, current);

			// This should always happen
			current = next;

			// If it isn't the last item next is set to the next item in succession
			// If it is the last item, we don't touch it since next == null
			if (next != null) {
				next = next.getNext();
			}

			// Keeps modCounts sync'd and prevents consecutive remove() calls
			iterModCount++;
			canRemove = false;
		}
	}

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
}
