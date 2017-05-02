package erika.app.coffee.action;

import erika.app.coffee.component.LoadingDialog;
import erika.app.coffee.model.args.SetLoadingMessageArgs;
import erika.app.coffee.model.args.ShowPopupArgs;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class LoadingDialogAction {
    public static DispatchAction show(String message) {
        return dispatcher -> {
            dispatcher.dispatch(new SetLoadingMessageArgs(message));
            dispatcher.dispatch(new ShowPopupArgs(LoadingDialog.class));
        };
    }

    public static Action dismiss() {
        return PopupActions.dismiss();
    }
}
