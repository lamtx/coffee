package erika.redux.demo.application;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.redux.demo.reducers.HomeReducer;
import erika.redux.demo.reducers.UserProfileReducer;

public class AppReducer implements Reducer<AppState> {
    private final HomeReducer home = new HomeReducer();
    private final UserProfileReducer userProfile = new UserProfileReducer();

    @NonNull
    @Override
    public AppState reduce(@Nullable AppState state, Action action) {
        if (state == null) {
            state = new AppState();
        }
        AppState appState = new AppState();

        appState.home = home.reduce(state.home, action);
        appState.userProfile = userProfile.reduce(state.userProfile, action);

        return appState;
    }
}
