package erika.core;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class ArrayHelper {
    public interface IntCreator<T> {
        int value(T t);
    }

    public interface DoubleCreator<T> {
        double value(T t);
    }

    public static <T> T[] toArray(Collection<T> source, Activator<T> activator) {
        T[] value = activator.newArray(source.size());
        int i = 0;
        for (T t : source) {
            value[i++] = t;
        }
        return value;
    }

    public static <T> int[] toIntArrays(T[] source, IntCreator<T> inflater) {
        int[] value = new int[source.length];
        int i = 0;
        for (T t : source) {
            value[i++] = inflater.value(t);
        }
        return value;
    }

    public static <T> int[] toIntArrays(Collection<T> source, IntCreator<T> inflater) {
        int[] value = new int[source.size()];
        int i = 0;
        for (T t : source) {
            value[i++] = inflater.value(t);
        }
        return value;
    }

    public static <T, E> E[] toArray(Collection<T> source, Activator<E> activator, Function<T, E> function) {
        E[] value = activator.newArray(source.size());
        int i = 0;
        for (T t : source) {
            value[i++] = function.apply(t);
        }
        return value;
    }

    public static String[] toStringArray(Collection<?> source) {
        String[] result = new String[source.size()];
        int i = 0;
        for (Object t : source) {
            result[i++] = t == null ? null : t.toString();
        }
        return result;
    }

    public static String[] toStringArray(Object[] source) {
        String[] result = new String[source.length];
        int i = 0;
        for (Object t : source) {
            result[i++] = t == null ? null : t.toString();
        }
        return result;
    }

    public static <T, E> E[] toArray(Collection<T> source, E[] destination, Function<T, E> function) {
        int i = 0;
        for (T t : source) {
            destination[i++] = function.apply(t);
        }
        return destination;
    }

    @SuppressWarnings("unchecked")
    public static <Parent, Child extends Parent> Child[] downCast(Parent[] parent, Child[] child) {
        for (int i = 0; i < parent.length; i++) {
            child[i] = (Child) parent;
        }
        return child;
    }

    public static <Parent, Child extends Parent> Iterable<Parent> upCast(final Iterable<Child> child) {
        return new Iterable<Parent>() {

            @NonNull
            @Override
            public Iterator<Parent> iterator() {
                return new Iterator<Parent>() {
                    private final Iterator<Child> base = child.iterator();

                    @Override
                    public boolean hasNext() {
                        return base.hasNext();
                    }

                    @Override
                    public Parent next() {
                        return base.next();
                    }

                    @Override
                    public void remove() {
                        base.remove();
                    }
                };
            }
        };
    }

    public static <T> ArrayList<T> toList(final T[] source) {
        if (source == null) {
            return null;
        }
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, source);
        return list;
    }

    public static <T> Iterable<T> toIterable(final T[] source) {
        if (source == null) {
            return null;
        }

        return new Iterable<T>() {

            @NonNull
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    int i;
                    final int len = source.length;

                    @Override
                    public boolean hasNext() {
                        return i < len;
                    }

                    @Override
                    public T next() {
                        return source[i++];
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
