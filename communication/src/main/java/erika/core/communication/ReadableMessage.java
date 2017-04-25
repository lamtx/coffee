package erika.core.communication;

public abstract class ReadableMessage {
    private int sequenceId;

    public int getSequenceId() {
        return sequenceId;
    }

    protected void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
}
