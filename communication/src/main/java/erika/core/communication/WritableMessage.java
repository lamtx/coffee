package erika.core.communication;

import android.support.annotation.CallSuper;

public class WritableMessage implements Writable {
    private int sequenceId;
    private final String name = getClass().getSimpleName();

    @CallSuper
    public void writeToWriter(Writer writer) {
        writer.write(name).write(sequenceId);
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }

}
