package erika.core.threading;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

public final class CancellationTokenSource {
    private final CancellationToken token = new CancellationToken() {

        @Override
        public boolean isCancellationRequested() {
            return isCancelled;
        }

        @Override
        public void throwIfRequested() throws CancellationException {
            if (isCancelled) {
                throw new CancellationException("Cancellation requested");
            }
        }

        @Override
        public void onCancel(Runnable action) {
            cancelledAction.add(new WeakReference<>(action));
        }
    };

    private boolean isCancelled = false;
    private final List<WeakReference<Runnable>> cancelledAction = new ArrayList<>();

    public CancellationToken getToken() {
        return token;
    }

    public void cancel() {
        isCancelled = true;
        notifyListener();
    }

    private void notifyListener() {
        for (WeakReference<Runnable> action : cancelledAction) {
            if (action.get() != null) {
                action.get().run();
            }
        }
        cancelledAction.clear();
    }

    /**
     * Instance the {@code CancellationToken} has never trigger cancel
     *
     */
    public static CancellationToken emptyToken() {
        return new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @Override
            public void throwIfRequested() throws CancellationException {
            }

            @Override
            public void onCancel(Runnable action) {
            }
        };
    }
}
