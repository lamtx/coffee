package erika.app.coffee.model;

public class Message implements Cloneable {
    public enum Status {
        PROCESSING, FINISHED, FAILED
    }

    public String message;
    public Status status;
    public int id;

    public Message(String message, int messageId, Status status) {
        this.message = message;
        this.id = messageId;
        this.status = status;
    }
}

