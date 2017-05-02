package erika.app.coffee.action;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.Args;
import erika.core.redux.Action;

public class PopupActions {

    public static Action dismiss() {
        return new Args(ActionType.DISMISS_POPUP);
    }
}
