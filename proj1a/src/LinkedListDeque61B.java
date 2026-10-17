import net.sf.saxon.functions.ConstantFunction;

import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    private class Node {
        private T item;
        Node next;
        Node prev;

        Node(T item, Node next, Node prev){
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque61B() {
        sentinel = new Node(null, null, null);
        size = 0;
        sentinel.next = null;
        sentinel.prev = null;
    }

    @Override
    public void addFirst(T x) {
        size++;
        if (sentinel.next == null){
            Node p = new Node(x, null, null);
            p.next = p;
            p.prev = p;
            sentinel.next = p;
            return;
        }
        Node p = new Node(x, sentinel.next, sentinel.next.prev);
        p.next.prev = p;
        p.prev.next = p;
        sentinel.next = p;


    }

    @Override
    public void addLast(T x) {
        size++;
        if (sentinel.next == null){
            Node p = new Node(x, null, null);
            p.next = p;
            p.prev = p;
            sentinel.next = p;
            return;
        }
        Node p = new Node(x, sentinel.next, sentinel.next.prev);
        p.next.prev = p;
        p.prev.next = p;

    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        if (sentinel.next == null){
            return returnList;
        }
        Node p = sentinel.next;
        do{
            returnList.add(p.item);
            p = p.next;
        } while(p != sentinel.next);
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return sentinel.next == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            size--;
            Node p = sentinel.next;
            sentinel.next = null;
            return p.item;
        }
        size--;
        Node p = sentinel.next;
        p.next.prev = p.prev;
        p.prev.next = p.next;
        sentinel.next = p.next;
        return p.item;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            size--;
            Node p = sentinel.next;
            sentinel.next = null;
            return p.item;
        }
        size--;
        Node p = sentinel.next.prev;
        p.next.prev = p.prev;
        p.prev.next = p.next;
        return p.item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node p = sentinel.next;
        while (index != 0) {
            p = p.next;
            index--;
        }
        return p.item;
    }

    @Override
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null; // 非法下标的处理以作业要求为准
        }
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(Node node, int index) {
        if (index == 0) {
            return node.item;
        }
        return getRecursiveHelper(node.next, index - 1);
    }
}
