package erika.core.redux.immutable;


import android.support.annotation.CheckResult;

public class ImmutableStack<T> extends ImmutableArrayList<T> {
    public ImmutableStack() {
    }

    public ImmutableStack(T instance) {
        super(instance);
    }

    ImmutableStack(Object[] items) {
        super(items);
    }

    public T peek() {
        @SuppressWarnings({"unchecked"})
        T t = (T) items[items.length - 1];
        return t;
    }

    @CheckResult
    public ImmutableStack<T> push(T component) {
        return super.append(component);
    }

    @CheckResult
    public ImmutableStack<T> pop() {
        Object[] buffer = new Object[items.length - 1];
        System.arraycopy(items, 0, buffer, 0, buffer.length);
        return new ImmutableStack<>(buffer);
    }
}
