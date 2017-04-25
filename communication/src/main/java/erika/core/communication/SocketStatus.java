package erika.core.communication;

public enum SocketStatus {
    CannotConnect("Cannot connect"),
    ConnectionTimeout("Connection timeout"),
    ParseError("Parse Error"),
    Ok("Ok");

    private final String description;

    SocketStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
