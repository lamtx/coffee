package erika.core.redux.immutable;


import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import java.util.EmptyStackException;

public class ImmutableStack<T> extends ImmutableArrayList<T> {
    public ImmutableStack() {
    }

    public ImmutableStack(T instance) {
        super(instance);
    }

    ImmutableStack(Object[] items) {
        super(items);
    }

    @NonNull
    public T peek() {
        if (items.length == 0) {
            throw new EmptyStackException();
        }
        @SuppressWarnings({"unchecked"})
        T t = (T) items[items.length - 1];
        return t;
    }

    @CheckResult
    public ImmutableStack<T> push(T component) {
        Object[] buffer = new Object[items.length + 1];
        System.arraycopy(items, 0, buffer, 0, items.length);
        buffer[buffer.length - 1] = component;
        return new ImmutableStack<>(buffer);
    }

    @CheckResult
    public ImmutableStack<T> pop() {
        Object[] buffer = new Object[items.length - 1];
        System.arraycopy(items, 0, buffer, 0, buffer.length);
        return new ImmutableStack<>(buffer);
    }
}
