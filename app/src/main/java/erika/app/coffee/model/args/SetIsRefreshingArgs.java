package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;
import erika.core.redux.Reducer;

public class SetIsRefreshingArgs extends Args {
    public final Class<? extends Reducer> target;
    public final boolean refreshing;

    public SetIsRefreshingArgs(Class<? extends Reducer> target, boolean refreshing) {
        super(ActionType.SET_IS_REFRESHING);
        this.target = target;
        this.refreshing = refreshing;
    }
}
