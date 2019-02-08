package edu.isu.cs.cs3308.structures.impl;

import edu.isu.cs.cs3308.structures.Queue;

/**
 * Represents a queue built on a doubly-linked list.
 * @author Blaise Johnson
 * @param <E> The data type to be stored in the LinkedQueue.
 */
public class LinkedQueue<E> implements Queue<E> {

    /**
     * Represents the underlying structure of the LinkedQueue.
     */
    private DoublyLinkedList<E> queue = new DoublyLinkedList<>();

    /**
     * @return The number of elements in the queue
     */
    @Override
    public int size() {
        return queue.size();
    }

    /**
     * @return tests whether the queue is empty.
     */
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Inserts an element at the end of the queue.
     *
     * @param element Element to be inserted.
     */
    @Override
    public void offer(E element) {
        queue.addLast(element);
    }

    /**
     * @return The value first element of the queue (with out removing it), or
     * null if empty.
     */
    @Override
    public E peek() {
        return queue.get(0);
    }

    /**
     * @return The value of the first element of the queue (and removes it), or
     * null if empty.
     */
    @Override
    public E poll() {
        return queue.removeFirst();
    }

    /**
     * Prints the contents of the queue starting at top, one item per line. Note
     * this method should not empty the contents of the queue.
     */
    @Override
    public void printQueue() {
        queue.printList();
    }

    /**
     * Transfers the contents of this queue into the provided queue. The contents
     * of this queue are to found in reverse order at the top of the provided
     * queue. This queue should be empty once the transfer is completed. Note
     * that if the provided queue is null, nothing is to happen.
     *
     * @param into The new queue onto which the reversed order of contents from
     *             this queue are to be transferred to the top of, unless the provided queue
     *             is null.
     */
    @Override
    public void transfer(Queue<E> into) {
        if (into != null) {
            while (!isEmpty()) {
                into.offer(queue.removeLast());
            }
        }
    }

    /**
     * Reverses the contents of this queue.
     */
    @Override
    public void reverse() {
        LinkedQueue<E> step1 = new LinkedQueue<>();
        LinkedQueue<E> step2 = new LinkedQueue<>();

        this.transfer(step1);
        step1.transfer(step2);
        step2.transfer(this);
    }

    /**
     * Merges the contents of the provided queue onto the bottom of this queue.
     * The order of both queues must be preserved in the order of this queue
     * after the method call. Furthermore, the provided queue must still contain
     * its original contents in their original order after the method is
     * complete. If the provided queue is null, no changes should occur.
     *
     * @param from Queue whose contents are to be merged onto the bottom of
     *             this queue.
     */
    @Override
    public void merge(Queue<E> from) {
        if (from != null) {
            LinkedQueue<E> temp = new LinkedQueue<>();

            while(!from.isEmpty()) {
                E element = from.poll();
                this.offer(element);
                temp.offer(element);
            }

            temp.transfer(from);
            from.reverse();
        }
    }
}
