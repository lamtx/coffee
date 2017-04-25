package erika.core.redux;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface Reducer<State> {
    @NonNull
    State reduce(@Nullable State state, Action action);
}
