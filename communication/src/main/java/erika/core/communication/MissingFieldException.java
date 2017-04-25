package erika.core.communication;

public class MissingFieldException extends Exception {
    public MissingFieldException() {
        super();
    }

    public MissingFieldException(String detailMessage) {
        super(detailMessage);
    }

    public MissingFieldException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MissingFieldException(Throwable throwable) {
        super(throwable);
    }
}
