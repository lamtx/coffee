package erika.app.coffee.model;

public class Message implements Cloneable {
    public String message;
    public LoadState status;
    public int id;

    public Message(String message, int messageId, LoadState status) {
        this.message = message;
        this.id = messageId;
        this.status = status;
    }
}

