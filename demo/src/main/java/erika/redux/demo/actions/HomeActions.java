package erika.redux.demo.actions;

import erika.core.redux.Action;
import erika.core.redux.DispatchAction;
import erika.redux.demo.application.ActionType;
import erika.redux.demo.application.BaseAction;

public class HomeActions {
    public static Action setText(String text) {
        return new HomeSetTextAction(text);
    }

    public static DispatchAction loadAsync(String url) {
        return dispatcher -> {
        };
    }

    public static class HomeSetTextAction extends BaseAction {
        public final String text;

        public HomeSetTextAction(String text) {
            super(ActionType.HOME_UPDATE_TEXT);
            this.text = text;
        }
    }
}

