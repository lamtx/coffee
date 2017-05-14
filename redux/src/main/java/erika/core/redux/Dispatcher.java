package erika.core.redux;

import android.os.Handler;

public interface Dispatcher {
    void dispatch(Action action);

    void dispatch(DispatchAction action);

    Handler getHandler();

    void dispatchDelayed(Action action, long milliseconds);

    void dispatchDelayed(DispatchAction action, long milliseconds);
}
