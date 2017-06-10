package erika.core.communication;

public interface Writable {
    void writeToWriter(Writer writer);

    default String toMessage() {
        Writer writer = new Writer();
        writeToWriter(writer);
        return writer.toMessage();
    }
}
