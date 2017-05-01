package erika.core.redux;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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

    public static <T extends Cloneable> T copy(T old) {
        T clone;
        try {
            clone = (T) cloneMethod.invoke(old);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError(e);
        }
        return clone;
    }

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
}
