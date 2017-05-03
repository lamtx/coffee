package erika.core.redux.utils;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;

public class LinkedList<E> implements Collection<E> {
    private LinkedListNode<E> head = null;
    private LinkedListNode<E> rear = null;
    private int size = 0;
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public boolean contains(Object o) {
        for (LinkedListNode<E> node = head; node != null; node = node.next) {
            if (o.equals(node.value)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator<>(head);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        Object[] a = new Object[size];
        int i = 0;
        for (LinkedListNode<E> tmp = head; tmp != null; tmp = tmp.next) {
            a[i++] = tmp.value;
        }
        return a;
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] a) {
        int i = 0;
        for (LinkedListNode tmp = head; tmp != null; tmp = tmp.next) {
            a[i++] = (T) tmp.value;
        }
        return a;
    }

    @Override
    public boolean add(E e) {
        if (rear == null) {
            rear = new LinkedListNode<>(null,null, e);
            head = rear;
        } else {
            rear.next = new LinkedListNode<>(rear, null, e);
            rear = rear.next;
        }
        size += 1;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (LinkedListNode<E> node = head; node != null; node = node.next) {
            if (o.equals(node.value)) {
                remove(node);
                return true;
            }
        }
        return false;
    }

    public boolean removeWhere(Predicate<E> where) {
        for (LinkedListNode<E> node = head; node != null; node = node.next) {
            if (where.test(node.value)) {
                remove(node);
                return true;
            }
        }
        return false;
    }

    public void remove(LinkedListNode<E> node) {
        if (node.prev == null || node.next == null) {
            if (node.prev == null) {
                head = node.next;
            }
            if (node.next == null) {
                rear = node.prev;
            }
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        size -= 1;
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> c) {
        boolean added = false;
        for (E e : c) {
            add(e);
            added = true;
        }
        return added;
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("removeAll");
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("retainAll");
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    public LinkedListNode<E> getHead() {
        return head;
    }

    private static class LinkedListIterator<E> implements Iterator<E> {
        private LinkedListNode<E> current;

        LinkedListIterator(LinkedListNode<E> head) {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E value = current.value;
            current = current.next;
            return value;
        }
    }
}
