package erika.core.redux.immutable;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class ImmutableEnumSet<E extends Enum<E>> implements Set<E> {

    public abstract ImmutableEnumSet<E> put(E value);

    public abstract ImmutableEnumSet<E> remove(E value);

    public abstract ImmutableEnumSet<E> set(E value, boolean contained);

    public static <E extends Enum<E>> ImmutableEnumSet<E> noneOf(Class<E> elementType) {
        E[] universe = elementType.getEnumConstants();
        if (universe == null)
            throw new ClassCastException(elementType + " not an enum");

        if (universe.length <= 64) {
            return new RegularImmutableEnumSet<>(0L, universe);
        } else {
            throw new UnsupportedOperationException("Unsupported enum with size larger than 64");
        }
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
    public boolean addAll(@NonNull Collection<? extends E> c) {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @Override
    @Deprecated
    public boolean retainAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @Override
    @Deprecated
    public boolean removeAll(@NonNull Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException("Unsupported mutable operator");
    }

    private static class RegularImmutableEnumSet<E extends Enum<E>> extends ImmutableEnumSet<E> {
        private final long value;
        private final E[] universe;
        private RegularImmutableEnumSet(long value, E[] universe) {
            this.value = value;
            this.universe = universe;
        }

        @Override
        public RegularImmutableEnumSet<E> put(E e) {
            long newValue = value | (1L << e.ordinal());
            if (newValue != value) {
                return new RegularImmutableEnumSet<>(newValue, universe);
            }
            return this;
        }

        @Override
        public RegularImmutableEnumSet<E> remove(E e) {
            long newValue = value & ~(1L << e.ordinal());
            if (newValue != value) {
                return new RegularImmutableEnumSet<>(newValue, universe);
            }
            return this;
        }

        @Override
        public ImmutableEnumSet<E> set(E value, boolean contained) {
            if (contained) {
                return put(value);
            }
            return remove(value);
        }

        @Override
        public int size() {
            return Long.bitCount(value);
        }

        @Override
        public boolean isEmpty() {
            return value == 0L;
        }

        @Override
        public boolean contains(Object e) {
            if (!(e instanceof Enum)) {
                throw new IllegalArgumentException("Argument is not an enum");
            }
            return (value & (1L << ((Enum) e).ordinal())) != 0;
        }

        @NonNull
        @Override
        public Iterator<E> iterator() {
            return new EnumSetIterator<>(value, universe);
        }

        @NonNull
        @Override
        public Object[] toArray() {
            Object[] result = new Object[size()];
            int i = 0;
            for (E e : this) {
                result[i++] = e;
            }
            return result;
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            int i = 0;
            for (E e : this) {
                a[i++] = (T)e;
            }
            return a;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        private static class EnumSetIterator<E> implements Iterator<E> {
            final long elements;
            private final E[] universe;
            long unseen;
            long lastReturned = 0;

            EnumSetIterator(long elements, E[] universe) {
                unseen = elements;
                this.elements = elements;
                this.universe = universe;
            }

            public boolean hasNext() {
                return unseen != 0;
            }

            public E next() {
                if (unseen == 0)
                    throw new NoSuchElementException();
                lastReturned = unseen & -unseen;
                unseen -= lastReturned;
                return (E) universe[Long.numberOfTrailingZeros(lastReturned)];
            }
        }
    }
}
