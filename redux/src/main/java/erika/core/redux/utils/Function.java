package erika.core.redux.utils;

public interface Function<T, R> {
    R apply(T t);
}
