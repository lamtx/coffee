package erika.app.coffee.action;

import android.content.Context;
import android.text.TextUtils;

import erika.app.coffee.component.TableListFragment;
import erika.app.coffee.model.ClientInfo;
import erika.app.coffee.model.args.SetHostArgs;
import erika.app.coffee.model.args.SetPasswordArgs;
import erika.app.coffee.model.args.SetUserNameArgs;
import erika.app.coffee.service.Client;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.Settings;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class SignInActions {
    public static DispatchAction signIn(Context context, String userName, String password, String host) {
        return dispatcher -> {
            // Validate
            if (TextUtils.isEmpty(userName)) {
                dispatcher.dispatch(MessageBoxActions.show("Tên người dùng bị thiếu", "Đăng nhập"));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                dispatcher.dispatch(MessageBoxActions.show("Mật khẩu bị thiếu", "Đăng nhập"));
                return;
            }

            if (TextUtils.isEmpty(host)) {
                dispatcher.dispatch(MessageBoxActions.show("Host bị thiếu", "Đăng nhập"));
                return;
            }

            ClientInfo clientInfo;
            try {
                clientInfo = new ClientInfo(userName, password, host);
            } catch (IllegalArgumentException ignored) {
                dispatcher.dispatch(MessageBoxActions.show("Host bị sai", "Đăng nhập"));
                return;
            }

            ServiceInterface serviceInterface = ServiceInterface.shared(context);
            serviceInterface.setCredentials(clientInfo);

            dispatcher.dispatch(LoadingDialogAction.show("Sign in..."));
            serviceInterface.connect().then(task -> {
                dispatcher.dispatch(LoadingDialogAction.dismiss());
                if (task.getResult() == Client.ConnectResult.OK) {
                    Settings.shared(context).setHost(host).setPassword(password).setUserName(userName);
                    dispatcher.dispatch(MainActions.setRoot(TableListFragment.class, "Bàn"));
                } else {
                    dispatcher.dispatch(MessageBoxActions.show("Error: " + task.getResult(), "Đăng nhập"));
                }
            });
        };
    }

    public static Action setUserName(String userName) {
        return new SetUserNameArgs(userName);
    }

    public static Action setPassword(String password) {
        return new SetPasswordArgs(password);
    }

    public static Action setHost(String host) {
        return new SetHostArgs(host);
    }
}
