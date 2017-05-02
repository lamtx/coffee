package erika.app.coffee.model.args;

import erika.app.coffee.application.ActionType;

public class SetIsRefreshingArgs extends Args {
    public final Class<?> callingAction;
    public final boolean refreshing;

    public SetIsRefreshingArgs(Class<?> callingAction, boolean refreshing) {
        super(ActionType.SET_IS_REFRESHING);
        this.callingAction = callingAction;
        this.refreshing = refreshing;
    }
}
