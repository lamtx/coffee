package erika.core;

public interface Property<T, V> {

    V get(T object);

    void set(T object, V value);

}
