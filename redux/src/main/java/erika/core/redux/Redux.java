package erika.core.redux;

import android.support.annotation.CheckResult;
import android.util.SparseArray;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import erika.core.redux.utils.Predicate;

public class Redux {
    private static final Method cloneMethod;

    static {
        try {
            cloneMethod = Object.class.getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    private Redux() {
    }

    @CheckResult
    public static <T extends Cloneable> T copy(T old) {
        T clone;
        try {
            clone = (T) cloneMethod.invoke(old);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
        return clone;
    }

    @CheckResult
    public static <T extends Cloneable> T copy(T old, ObjectModifier<T> modifier) {
        T clone;
        try {
            clone = (T) cloneMethod.invoke(old);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
        modifier.modify(clone);
        return clone;
    }

    @CheckResult
    public static <T> List<T> map(List<T> source, ObjectCopier<T> function) {
        if (source == null) {
            return null;
        }
        List<T> result = new ArrayList<>(source.size());
        for (T e : source) {
            result.add(function.copy(e));
        }
        return result;
    }

    @CheckResult
    public static <T> List<T> remove(List<T> source, Predicate<T> where) {
        if (source == null) {
            return null;
        }
        List<T> result = new ArrayList<>(source.size());
        for (T t : source) {
            if (!where.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    @CheckResult
    public static <T> List<T> add(List<T> source, T element) {
        List<T> result;
        if (source == null) {
            result = new ArrayList<>(1);
        } else {
            result = new ArrayList<>(source.size() + 1);
            result.addAll(source);
        }
        result.add(element);
        return result;
    }

    @CheckResult
    public static <T> SparseArray<T> put(SparseArray<T> source, int key, T value) {
        SparseArray<T> destination = new SparseArray<T>();
        for (int i = 0; i < source.size(); i++) {
            int e = source.keyAt(i);
            destination.put(e, source.get(e));
        }
        destination.put(key, value);
        return destination;
    }
}
