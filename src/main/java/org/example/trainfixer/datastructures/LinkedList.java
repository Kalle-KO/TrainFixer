package org.example.trainfixer.datastructures;

public class LinkedList<T> {
    private Node<T> head;
    private int size = 0;

    public void addLast(T value) {
        Node<T> n = new Node<>(value);
        if (head == null) head = n;
        else {
            Node<T> cur = head;
            while (cur.getNext() != null) cur = cur.getNext();
            cur.setNext(n);
        }
        size++;
    }

    public int size() { return size; }
    public Node<T> head() { return head; }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        Node<T> cur = head;
        for (int i = 0; i < index; i++) cur = cur.getNext();
        return cur.getValue();
    }

    public void replaceWith(LinkedList<T> other) {
        this.head = other.head;
        this.size = other.size;
    }
}
