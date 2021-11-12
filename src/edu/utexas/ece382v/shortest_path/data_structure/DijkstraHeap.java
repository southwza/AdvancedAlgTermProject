package edu.utexas.ece382v.shortest_path.data_structure;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Basic implementation of a min heap that is not thread safe, and includes an 'insertOrAdjust'
 * method to maintain the heap invariant when an element is re-inserted after its comparator value
 * has changed. This is implemented in the Java style where instead of passing in a separate object
 * and priority value, only the objects need to be passed in and they must be Comparable.
 * 
 * Implemented with a binary heap s.t. the first element is at index 1 and child elements for any
 * element at position i are: (2 * i) and ((2 * i) + 1)
 * 
 * Supported operations:
 *  -enqueue/offer O(logN)
 *  -dequeue/poll O(logN)
 *  -peek O(1)
 *  -size O(1)
 *  -insertOrAdjust O(logN)
 *  
 *  The contract for the usage of 'insertOrAdjust' requires these things:
 *  -Duplicate elements(elements whose .equals() method evaluates to true) may not be inserted
 *  -The equals/hashcode of objects passed may not be allowed to change as the comparator changes.
 *  -the comparable value of an element may only get smaller. (The 'adjustment' will only move an
 *  element up in the queue)
 *  
 *  
 * @author Zach Southwell, Lydia Guarino
 *
 * References:
 * - java.util.PriorityQueue - Bloch & Lea
 * - https://en.wikipedia.org/wiki/Priority_queue
 * 
 * @param <E>
 */
@SuppressWarnings("unchecked")
public class DijkstraHeap<E extends Comparable<E>> extends AbstractQueue<E> {

    private int size = 0;
    private Object[] heapArray;
    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    private Map<E, Integer> objectToHeapIndex = new HashMap<>();

    public DijkstraHeap () {
        heapArray = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    @Override
    public boolean offer(E e) {
        if (heapArray.length - 1 == size) {
            growArray();
        }
        heapArray[++size] = e;
        bubbleUp(size);

        return true;
    }

    @Override
    public E poll() {
        E result;
        if (size == 0) {
            throw new IndexOutOfBoundsException();
        }
        result = (E) heapArray[1];
        heapArray[1] = heapArray[size];
        heapArray[size--] = null;
        objectToHeapIndex.remove(result);

        bubbleDown();
        return result;
    }

    @Override
    public E peek() {
        throw new UnsupportedOperationException("Not implemented for this class");
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not implemented for this class");
    }

    @Override
    public int size() {
        return size;
    }

    //Bubble up the element at the provided index
    private void bubbleUp(int index) {
        E element = (E) heapArray[index];
        while (index / 2 > 0) {
            E parent = (E) heapArray[index / 2];
            if (element.compareTo(parent) > 0) {
                break;
            }
            heapArray[index] = parent;
            objectToHeapIndex.put(parent, index);
            index = index / 2;
        }
        heapArray[index] = element;
        objectToHeapIndex.put(element, index);
    }

    //Bubble down the element at position 1
    private void bubbleDown() {
        int position = 1;
        E element = (E) heapArray[position];
        objectToHeapIndex.put(element, position);

        while (true) {
            int leftPosition = position * 2;
            int rightPosition = position * 2 + 1;
            E leftChild = leftPosition > size ? null : (E) heapArray[leftPosition];
            E rightChild = rightPosition > size ? null : (E) heapArray[rightPosition];
            if (leftChild == null) {
                    break;
            } else if (rightChild == null) {
                if (element.compareTo(leftChild) <= 0) {
                    break;
                }
                heapArray[position] = leftChild;
                heapArray[leftPosition] = element;

                objectToHeapIndex.put(leftChild, position);
                objectToHeapIndex.put(element, leftPosition);

                position = leftPosition;
                break;
            } else {
                if (leftChild.compareTo(rightChild) <= 0 && leftChild.compareTo(element) < 0) {
                    heapArray[position] = leftChild;
                    heapArray[leftPosition] = element;

                    objectToHeapIndex.put(leftChild, position);
                    objectToHeapIndex.put(element, leftPosition);

                    position = leftPosition;
                    continue;
                }
                if (rightChild.compareTo(leftChild) <= 0 && rightChild.compareTo(element) < 0) {
                    heapArray[position] = rightChild;
                    heapArray[rightPosition] = element;

                    objectToHeapIndex.put(rightChild, position);
                    objectToHeapIndex.put(element, rightPosition);

                    position = rightPosition;
                    continue;
                }
                break;
            }
        }
    }

    public boolean insertOrAdjust(E e) {
         if (objectToHeapIndex.containsKey(e)) {
             bubbleUp(objectToHeapIndex.get(e));
             return true;
         }
         return offer(e);
    }

    private void growArray() {
        int newSize = heapArray.length * 2;
        heapArray = Arrays.copyOf(heapArray, newSize);
    }

    @Override
    public String toString() {
        String result = "";
        int depth = 1;
        int size = this.size;
        while (size > 1) {
            size = size >>> 1;
            depth++;
        }
        for (int d = 0; d < depth; d++) {
            String spacer = " ".repeat((depth - d));
            for (int i = (1 << d); i < (1 << (d+1)); i++) {
                if (i > this.size) {
                    break;
                }
                result += spacer + String.format("%2d", heapArray[i]);
            }
            result += System.lineSeparator();
        }
        return result;
    }
}
