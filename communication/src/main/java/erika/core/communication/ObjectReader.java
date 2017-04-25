package erika.core.communication;

public interface ObjectReader<T> {

    T createFromReader(Reader reader) throws MissingFieldException;

    T[] newArray(int size);
}
