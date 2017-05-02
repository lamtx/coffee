package erika.app.coffee.action;

import android.app.Fragment;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.Args;
import erika.app.coffee.model.args.PushComponentArgs;
import erika.core.redux.Action;

public class MainActions {
    public static Action push(Class<? extends Fragment> component) {
        return new PushComponentArgs(component);
    }

    public static Action pop() {
        return new Args(ActionType.POP);
    }
}
