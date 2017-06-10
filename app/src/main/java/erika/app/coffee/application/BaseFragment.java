package erika.app.coffee.application;

import android.content.res.Configuration;

import erika.core.redux.ReduxFragment;

public abstract class BaseFragment<State> extends ReduxFragment<AppState, State> {

    public boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
