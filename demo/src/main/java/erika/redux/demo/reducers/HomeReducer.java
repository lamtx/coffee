package erika.redux.demo.reducers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;
import erika.redux.demo.actions.HomeActions;
import erika.redux.demo.application.ActionType;
import erika.redux.demo.states.HomeState;

public class HomeReducer implements Reducer<HomeState> {
    @NonNull
    @Override
    public HomeState reduce(@Nullable HomeState state, Action action) {
        if (state ==null) {
            state = new HomeState();
        }
        switch (action.getType()) {
            case ActionType.HOME_UPDATE_TEXT:
                return createHome(state, (HomeActions.HomeSetTextAction) action);
            default:
                return state;
        }
    }

    private static HomeState createHome(HomeState state, HomeActions.HomeSetTextAction action) {
        return Redux.clone(state, x -> {
            x.text = action.text;
        });
    }
}
