/*
 * Blaise Johnson
 * CS 3308
 * Isaac Griffith
 * 1/24/19
 */

package edu.isu.cs.cs3308.structures.impl;

import edu.isu.cs.cs3308.structures.List;

/**
 * Represents a node-based list which can be traversed both forward and backward.
 * @param <E> The data type of the contents of each Node.
 * @author Blaise Johnson
 */
public class DoublyLinkedList<E> implements List<E> {

    private SentinelNode head;
    private SentinelNode tail;
    private int size;

    DoublyLinkedList() {
        SentinelNode headSentinel = new SentinelNode();
        SentinelNode tailSentinel = new SentinelNode();

        head = headSentinel;
        tail = tailSentinel;
        size = 0;

        head.next = tail;
        tail.prev = head;
    }

    /**
     * @return first element in the list or null if the list is empty.
     */
    @Override
    public E first() {
        return head.next().data;
    }

    /**
     * @return last element in the list or null if the list is empty.
     */
    @Override
    public E last() {
        return tail.prev().data;
    }

    /**
     * Adds the provided element to the end of the list, only if the element is
     * not null.
     *
     * @param element Element to be added to the end of the list.
     */
    @Override
    public void addLast(E element) {
        if (element != null) {
            Node newNode = new Node(element);
            insertNode(tail, newNode);
        }
    }

    /**
     * Adds the provided element to the front of the list, only if the element
     * is not null.
     *
     * @param element Element to be added to the front of the list.
     */
    @Override
    public void addFirst(E element) {
        if (element != null) {
            Node newNode = new Node(element);
            insertNode(head.next(), newNode);
        }
    }

    /**
     * Removes the element at the front of the list.
     *
     * @return Element at the front of the list, or null if the list is empty.
     */
    @Override
    public E removeFirst() {
        return remove(0);
    }

    /**
     * Removes the element at the end of the list.
     *
     * @return Element at the end of the list, or null if the list is empty.
     */
    @Override
    public E removeLast() {
        return remove(size() - 1);
    }

    /**
     * Inserts the given element into the list at the provided index. The
     * element will not be inserted if either the element provided is null or if
     * the index provided is less than 0. If the index is greater than or equal
     * to the current size of the list, the element will be added to the end of
     * the list.
     *
     * @param element Element to be added (as long as it is not null).
     * @param index   Index in the list where the element is to be inserted.
     */
    @Override
    public void insert(E element, int index) {
        if (element != null && index >= 0) {
            // If the insertion index is greater than the size of the list...
            if (index >= size()) {
                addLast(element);
            }
            else if (index == 0) {
                addFirst(element);
            }
            // If the insertion index is in the latter half of the list...
            else if (index >= size/2) {
                Node newNode = new Node(element);
                Node nodeAtIndex = walkBackwards(tail.prev(), size - index);

                insertNode(nodeAtIndex, newNode);
            }
            // If the insertion index is in the former half of the list...
            else {
                Node newNode = new Node(element);
                Node nodeAtIndex = walkForwards(head.next(), index);

                insertNode(nodeAtIndex, newNode);
            }
        }
    }

    /**
     * Removes the element at the given index and returns the value.
     *
     * @param index Index of the element to remove
     * @return The value of the element at the given index, or null if the index
     * is greater than or equal to the size of the list or less than 0.
     */
    @Override
    public E remove(int index) {
        if (index >= size || index < 0) return null;

        // If the removal index is in the latter half of the list..
        if (index >= size/2) {
            Node nodeAtIndex = walkBackwards(tail.prev(), size - index);

            return removeNode(nodeAtIndex);
        }
        // If the removal index is in the former half of the list...
        else {
            Node nodeAtIndex = walkForwards(head.next(), index);

            return removeNode(nodeAtIndex);
        }
    }

    /**
     * Retrieves the value at the specified index. Will return null if the index
     * provided is less than 0 or greater than or equal to the current size of
     * the list.
     *
     * @param index Index of the value to be retrieved.
     * @return Element at the given index, or null if the index is less than 0
     * or greater than or equal to the list size.
     */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) return null;

        // If the index is in the latter half of the list...
        if (index >= size/2) {
            Node nodeAtIndex = walkBackwards(tail.prev(), size - index);

            return nodeAtIndex.data;
        }
        // If the index is in the former half of the list...
        else {
            Node nodeAtIndex = walkForwards(head.next(), index);

            return nodeAtIndex.data;
        }
    }

    /**
     * @return The current size of the list. Note that 0 is returned for an
     * empty list.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * @return true if there are no items currently stored in the list, false
     * otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Prints the contents of the list each item on its own line
     */
    @Override
    public void printList() {
        Node currentNode = head.next;

        while (!(currentNode instanceof DoublyLinkedList.SentinelNode)) {
            System.out.println(currentNode.data);
            currentNode = currentNode.next();
        }
    }

    /**
     * Locates a given element in the list and returns the index of that element.
     *
     * @param element The element being searched for.
     */
    @Override
    public int indexOf(E element) {
        if (element == null || size == 0) return -1;

        Node currentNode = head.next();
        boolean elementFound = false;
        int elementIndex = 0;

        // While the element hasn't been found and currentNode is not a SentinelNode...
        while (!elementFound && !(currentNode instanceof DoublyLinkedList.SentinelNode)) {
            if (currentNode.data.equals(element)) {
                elementFound = true;
            }
            else {
                currentNode = currentNode.next();
                elementIndex++;
            }
        }

        return elementFound? elementIndex : -1;
    }

    /**
     * Dependency function for inserting a node into a doubly-linked list.
     * @param currentNode The node into which whose index the new node will be inserted.
     * @param newNode The node to be inserted.
     */
    private void insertNode(Node currentNode, Node newNode) {
        Node previousNode = currentNode.prev();

        // Connect new node to adjacent nodes.
        newNode.prev = previousNode;
        newNode.next = currentNode;

        // Connect adjacent nodes to new node.
        previousNode.next = newNode;
        currentNode.prev = newNode;

        size++;
    }

    /**
     * Dependency function for walking forwards through a list.
     * @param currentNode Starting node from which to walk.
     * @param index How far to walk.
     * @return The resulting node after the walk.
     */
    private Node walkForwards(Node currentNode, int index) {
        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next();
        }

        return currentNode;
    }

    /**
     * Dependency function for walking backwards through a list.
     * @param currentNode Starting node from which to walk.
     * @param indexFromEnd How far to walk.
     * @return The resulting node after the walk.
     */
    private Node walkBackwards(Node currentNode, int indexFromEnd) {
        for (int i = 0; i < indexFromEnd - 1; i++) {
            currentNode = currentNode.prev();
        }

        return currentNode;
    }

    /**
     * Dependency function for removing nodes from a list.
     * @param nodeToRemove The node to be removed.
     * @return The data of the removed node.
     */
    private E removeNode(Node nodeToRemove) {
        Node previousNode = nodeToRemove.prev();
        Node nextNode = nodeToRemove.next();

        // Connect adjacent nodes.
        previousNode.next = nextNode;
        nextNode.prev = previousNode;

        // Detach current node.
        nodeToRemove.prev = null;
        nodeToRemove.next = null;

        size--;

        return nodeToRemove.data;
    }

    /**
     * Represents the data container used in a node-based list.
     */
    private class Node {
        E data;
        Node next;
        Node prev;

        Node(E data) {
            this.data = data;
            next = null;
            prev = null;
        }

        Node next(){ return next; }

        Node prev() { return prev; }
    }

    /**
     * Represents the end caps of a node-based list.
     */
    private class SentinelNode extends Node {
        SentinelNode() {
            super(null);
        }
    }
}
