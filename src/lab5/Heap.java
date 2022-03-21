package lab5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * TODO class declaration
 * We want to accept any type which is comparable to itself
 *
 * @param <T>
 */
public class Heap<T extends Comparable<T>> {
    private ArrayList<T> container;

    public Heap() {
        container = new ArrayList<>();
    }

    /*
    public int compareTo(T t) {
    	if (t instanceof String) {
    		return this.compareTo(t);
    	}
    	else {
    		if (this > t) {
    			return 1
    		}
    		else if (this == t)

    	}
    }
    */

    /**
     * @return If size is 0, throw {@link IllegalStateException}.
     * Otherwise, return the first element of {@link this#container}
     */
    public T peek() throws IllegalStateException{
        //TODO
        //return null; // replace this line with implementation
    	if (container.size() == 0) {
    		throw new IllegalStateException("Size of container is 0");
    	}
		else {
			return container.get(0);
		}
    }

    /**
     *
     * @return If size is 0, throw {@link IllegalStateException}. Otherwise, temporarily save the first element.
     * Afterwards, set the first position to the last element, and remove the last element.
     * Call {@link this#heapifyDown()}, then return the original first element
     */
    public T poll() throws IllegalStateException{
		//TODO
        //return null; // replace this line with implementation

		if (container.size() == 0) {
    		throw new IllegalStateException("Size of container is 0");
		}
    	else {
    		T temp = container.get(0);
    		container.set(0, container.get(container.size()-1));
    		container.remove(container.size()-1);
    		this.heapifyDown();
    		return temp;
    	}
    }

    private void heapifyDown() {
        int pos = 0;
        while (hasLeft(pos)) {
            int smallerChildIndex = getLeftIndex(pos);
            if (hasRight(pos) && container.get(getRightIndex(pos)).compareTo(container.get(getLeftIndex(pos))) < 0) {
                smallerChildIndex = getRightIndex(pos);
            }
            if (container.get(pos).compareTo(container.get(smallerChildIndex)) < 0) {
                break;
            } else {
                swap(pos, smallerChildIndex);
            }
            pos = smallerChildIndex;
        }
    }

    /**
     * Add the object into {@link this#container}, then call {@link this#heapifyUp()}
     *
     * @param obj the object to add
     */
    public void add(T obj) {
        //TODO
    	container.add(obj);
    	this.heapifyUp();
    }

    public void addAll(Collection<T> list) {
        list.forEach(this::add);
    }

    /**
     * While the last element has a parent and is smaller than its parent, swap the two elements. Then, check again
     * with the new parent until there's either no parent or we're larger than our parent.
     */
    private void heapifyUp() {
        // TODO
    	int index = container.size()-1;
    	while (this.hasParent(index) && container.get(index).compareTo(container.get(this.getParentIndex(index))) == -1) {
    		Collections.swap(container, index, this.getParentIndex(index));
    		index = this.getParentIndex(index);
    	}
    }

    public int size() {
        return container.size();
    }

    private int getParentIndex(int i) {
        return (i - 1) / 2;
    }

    private int getLeftIndex(int i) {
        return 2 * i + 1;
    }

    private int getRightIndex(int i) {
        return 2 * i + 2;
    }

    private boolean hasParent(int i) {
        return getParentIndex(i) >= 0;
    }

    private boolean hasLeft(int i) {
        return getLeftIndex(i) < container.size();
    }

    private boolean hasRight(int i) {
        return getRightIndex(i) < container.size();
    }

    private void swap(int i1, int i2) {
        Collections.swap(container, i1, i2);
    }
}
