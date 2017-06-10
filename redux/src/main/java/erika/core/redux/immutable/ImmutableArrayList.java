package erika.core.redux.immutable;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;

import erika.core.redux.utils.Function;

public class ImmutableArrayList<E> implements Collection<E> {
    private static final Object[] EMPTY = {};
    final Object[] items;

    public ImmutableArrayList() {
        this(EMPTY);
    }

    public ImmutableArrayList(E instance) {
        this(new Object[]{instance});
    }

    public ImmutableArrayList(int populated) {
        this(new Object[populated]);
    }

    public ImmutableArrayList(int populated, Function<Integer, E> creator) {
        this(populateData(populated, creator));
    }

    private static <E> Object[] populateData(int populated, Function<Integer, E> creator) {
        Object[] items = new Object[populated];
        for (int i = 0; i < items.length; i++) {
            items[i] = creator.apply(i);
        }
        int s = populated == 0 ? 1 : 2;
        return items;
    }

    ImmutableArrayList(Object[] items) {
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
    public Iterator<E> iterator() {
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
    public boolean add(E e) {
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
    public boolean addAll(@NonNull Collection<? extends E> c) {
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

    @CheckResult
    public ImmutableArrayList<E> set(int index, E value) {
        Object[] items = toArray();
        items[index] = value;
        return new ImmutableArrayList<>(items);
    }

    public E get(int index) {
        @SuppressWarnings({"unchecked"})
        E value = (E) items[index];
        return value;
    }

    @CheckResult
    public <T> ImmutableArrayList<T> append(E component) {
        Object[] buffer = new Object[items.length + 1];
        System.arraycopy(items, 0, buffer, 0, items.length);
        buffer[buffer.length - 1] = component;
        return new ImmutableArrayList<>(buffer);
    }

    @CheckResult
    public <NewType> ImmutableArrayList<E> map(Function<E, NewType> expression) {
        Object[] buffer = new Object[items.length];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = expression.apply((E)items[i]);
        }
        return new ImmutableArrayList<>(buffer);
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
