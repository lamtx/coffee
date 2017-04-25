package erika.core.communication;

public interface Parser<T> {
    T parse(Reader reader) throws MissingFieldException;
}
