package erika.app.coffee.action;

import android.app.Fragment;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.BackStackElement;
import erika.app.coffee.model.args.Args;
import erika.app.coffee.model.args.PushComponentArgs;
import erika.app.coffee.model.args.SetAppTitleArgs;
import erika.core.redux.Action;

public class MainActions {
    public static Action push(Class<? extends Fragment> component, String title) {
        return new PushComponentArgs(new BackStackElement(component, title));
    }

    public static Action pop() {
        return new Args(ActionType.POP);
    }

    public static Action setAppTitle(String title, String subtitle) {
        return new SetAppTitleArgs(title, subtitle);
    }
}
