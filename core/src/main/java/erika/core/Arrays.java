package erika.core;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Native array helper
 */
public class Arrays {

    public static <T> boolean contains(T[] source, T value) {
        if (value == null || source == null) {
            return false;
        }
        for (T e : source) {
            if (value.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contains(@NonNull Iterable<T> source, T value) {
        if (value == null) {
            return false;
        }
        for (T e : source) {
            if (value.equals(e)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T first(@NonNull Iterable<T> source, Predicate<T> predicate) {
        for (T e : source) {
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }

    public static <T> T first(@NonNull T[] source, Predicate<T> predicate) {
        for (T e : source) {
            if (predicate.test(e)) {
                return e;
            }
        }
        return null;
    }

    public static <T> int count(@NonNull Iterable<T> source, Predicate<T> predicate) {
        int value = 0;
        for (T t : source) {
            if (predicate.test(t)) {
                value += 1;
            }
        }
        return value;
    }

    public static <A, B> List<B> map(Iterable<A> source, Function<A, B> function) {
        if (source == null) {
            return null;
        }
        int cap = source instanceof Collection ? ((Collection) source).size() : 0;
        List<B> result = new ArrayList<>(cap);
        for (A e : source) {
            result.add(function.apply(e));
        }
        return result;
    }

    public static <A, B> B[] map(A[] source, B[] destination, Function<A, B> function) {
        for (int i = 0; i < source.length; i++) {
            destination[i] = function.apply(source[i]);
        }
        return destination;
    }

    public static <A, B> List<B> map(A[] source, Function<A, B> function) {
        if (source == null) {
            return null;
        }
        int cap = source.length;
        List<B> result = new ArrayList<>(cap);
        for (A e : source) {
            result.add(function.apply(e));
        }
        return result;
    }

    public static <T> String[] mapAsString(T[] source) {
        if (source == null) {
            return null;
        }
        String[] result = new String[source.length];
        for (int i = 0; i < source.length; i++) {
            result[i] = source[i].toString();
        }
        return result;
    }

    public static <T> int indexOf(Iterable<T> source, T value) {
        if (source == null || value == null) {
            return -1;
        }
        int i = 0;
        for (T t : source) {
            if (value.equals(t)) {
                return i;
            }
            i += 1;
        }
        return -1;
    }

    public static <T> int indexOf(Iterable<T> source, Predicate<T> where) {
        if (source == null) {
            return -1;
        }
        int i = 0;
        for (T t : source) {
            if (where.test(t)) {
                return i;
            }
            i += 1;
        }
        return -1;
    }

    public static <T> int indexOf(T[] source, T value) {
        if (source == null || value == null) {
            return -1;
        }
        for (int i = 0; i < source.length; i++) {
            if (value.equals(source[i])) {
                return i;
            }
        }
        return -1;
    }


    public static <T> T[] concat(T[] destination, T[] a, T[] b) {
        if (a != null) {
            System.arraycopy(a, 0, destination, 0, a.length);
        }
        if (b != null) {
            int offset = a == null ? 0 : a.length;
            System.arraycopy(b, 0, destination, offset, b.length);
        }
        return destination;
    }

    public static <T> int sum(T[] source, ArrayHelper.IntCreator<T> creator) {
        int value = 0;
        for (T e : source) {
            value += creator.value(e);
        }
        return value;
    }

    public static <T> double sum(T[] source, ArrayHelper.DoubleCreator<T> creator) {
        double value = 0;
        for (T e : source) {
            value += creator.value(e);
        }
        return value;
    }

    public static <T> int sum(Iterable<T> source, ArrayHelper.IntCreator<T> creator) {
        int value = 0;
        for (T t : source) {
            value += creator.value(t);
        }
        return value;
    }

    @SafeVarargs
    public static <T> List<T> asModifiableList(T... values) {
        if (values == null) {
            return new ArrayList<>();
        }
        ArrayList<T> ts = new ArrayList<>(values.length);
        Collections.addAll(ts, values);
        return ts;
    }

    public static String join(Iterable<String> source, String separator) {
        if (source == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String s : source) {
            if (builder.length() != 0) {
                builder.append(separator);
            }
            builder.append(s);
        }
        return builder.toString();
    }
}
