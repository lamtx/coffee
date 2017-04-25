package erika.core.threading;

import java.util.concurrent.CancellationException;

public interface CancellationToken {

    boolean isCancellationRequested();

    void throwIfRequested() throws CancellationException;

    void onCancel(Runnable action);

}
