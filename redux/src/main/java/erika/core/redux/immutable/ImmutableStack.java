package erika.core.redux.immutable;


import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;

public class ImmutableStack<T> implements Collection<T> {
    private static final Object[] EMPTY = {};
    private final Object[] items;

    public ImmutableStack() {
        this(EMPTY);
    }

    public ImmutableStack(T instance) {
        this(new Object[]{instance});
    }

    private ImmutableStack(Object[] items) {
        this.items = items;
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public boolean isEmpty() {
        return items.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Object item : items) {
            if (o.equals(item)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new ImmutableStackIterator<>(items);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        Object[] destination = new Object[items.length];
        System.arraycopy(items, 0, destination, 0, items.length);
        return destination;
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(@NonNull T1[] a) {
        System.arraycopy(items, 0, (Object[]) a, 0, items.length);
        return a;
    }

    @Override
    @Deprecated
    public boolean add(T t) {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @Override
    @Deprecated
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @Override
    @Deprecated
    public boolean containsAll(@NonNull Collection<?> c) {
        return false;
    }

    @Override
    @Deprecated
    public boolean addAll(@NonNull Collection<? extends T> c) {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @Override
    @Deprecated
    public boolean removeAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @Override
    @Deprecated
    public boolean retainAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @SuppressWarnings({"unchecked"})
    public T peek() {
        return (T) items[items.length - 1];
    }

    public ImmutableStack<T> push(T component) {
        Object[] buffer = new Object[items.length + 1];
        System.arraycopy(items, 0, buffer, 0, items.length);
        buffer[buffer.length - 1] = component;
        return new ImmutableStack<>(buffer);
    }

    public ImmutableStack<T> pop() {
        Object[] buffer = new Object[items.length - 1];
        System.arraycopy(items, 0, buffer, 0, buffer.length);
        return new ImmutableStack<>(buffer);
    }

    private static class ImmutableStackIterator<T> implements Iterator<T> {
        private final Object[] items;
        private int index = 0;

        ImmutableStackIterator(Object[] items) {
            this.items = items;
        }

        @Override
        public boolean hasNext() {
            return index < items.length;
        }

        @Override
        @SuppressWarnings({"unchecked"})
        public T next() {
            return (T) items[index++];
        }
    }
}
