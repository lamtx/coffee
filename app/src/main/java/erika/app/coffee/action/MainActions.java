package erika.app.coffee.action;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import erika.app.coffee.R;
import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.BackStackElement;
import erika.app.coffee.model.args.Args;
import erika.app.coffee.model.args.PushComponentArgs;
import erika.app.coffee.model.args.SetAppTitleArgs;
import erika.app.coffee.model.args.SetClientStatus;
import erika.app.coffee.model.args.SetRootArgs;
import erika.app.coffee.service.Client;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.Settings;
import erika.app.coffee.utility.Utils;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class MainActions {
    public static Action push(Class<? extends Fragment> component, String title) {
        return push(component, title, null);
    }

    public static Action push(Class<? extends Fragment> component, String title, String subTitle) {
        return new PushComponentArgs(new BackStackElement(component, title, subTitle));
    }

    public static Action pop() {
        return new Args(ActionType.POP);
    }

    public static Action setRoot(Class<? extends Fragment> component, String title) {
        return new SetRootArgs(new BackStackElement(component, title));
    }

    public static Action setAppTitle(String title, String subtitle) {
        return new SetAppTitleArgs(title, subtitle);
    }

    public static DispatchAction signOut(Context context) {
        return dispatcher -> {
            DispatchAction messageAction = new MessageBoxActions.Builder()
                    .title("Đăng xuất")
                    .message("Bạn có muốn đăng xuất")
                    .negative(R.string.no, null)
                    .positive(R.string.yes, () -> {
                        dispatcher.dispatch(signOutAfterConfirming(context));
                    }).build();
            dispatcher.dispatch(messageAction);
        };

    }

    private static DispatchAction signOutAfterConfirming(Context context) {
        return dispatcher -> {
            ServiceInterface.shared(context).disconnect();
            Settings settings = Settings.shared(context);
            settings.setPassword(null);
            dispatcher.dispatch(new Args(ActionType.SIGN_OUT));
            dispatcher.dispatch(SignInActions.setUserName(settings.getUserName()));
            dispatcher.dispatch(SignInActions.setHost(settings.getHost()));
        };
    }

    public static Action setClientStatus(Client.Status status, String message) {
        return new SetClientStatus(status, message);
    }

    public static DispatchAction closeKeyboard(Context context) {
        return dispatcher -> {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                Utils.hideKeyboard(activity);
            } else {
                throw new UnsupportedOperationException("context must be a activity");
            }
        };
    }

}
