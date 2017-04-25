package erika.redux.demo.reducers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.redux.demo.states.UserProfileState;

public class UserProfileReducer implements Reducer<UserProfileState> {
    @NonNull
    @Override
    public UserProfileState reduce(@Nullable UserProfileState state, Action action) {
        if (state == null) {
            state = new UserProfileState();
        }
        return state;
    }
}
