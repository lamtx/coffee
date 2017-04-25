package erika.core.threading;

public interface TaskBody<T> {
    T apply() throws Exception;
}
