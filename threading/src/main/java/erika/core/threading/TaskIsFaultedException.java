package erika.core.threading;

public class TaskIsFaultedException extends IllegalStateException {

    public TaskIsFaultedException() {
        super();
    }

    public TaskIsFaultedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TaskIsFaultedException(String detailMessage) {
        super(detailMessage);
    }

    public TaskIsFaultedException(Throwable throwable) {
        super(throwable);
    }

    /**
     *
     */
    private static final long serialVersionUID = -7515623740299988089L;

}
