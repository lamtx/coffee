package erika.app.coffee;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import erika.app.coffee.action.MainActions;
import erika.app.coffee.action.SignInActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.AppStore;
import erika.app.coffee.component.TableListFragment;
import erika.app.coffee.model.ClientInfo;
import erika.app.coffee.model.ResponseHandler;
import erika.app.coffee.service.Client;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.Settings;
import erika.app.coffee.service.communication.response.Response;
import erika.core.redux.ReduxApplication;
import erika.core.redux.Store;


public class App extends ReduxApplication<AppState> {
    @NonNull
    @Override
    public Store<AppState> createStore() {
        return new AppStore();
    }

    private final ResponseHandler clientHandler = new ResponseHandler() {
        @Override
        public boolean handle(Response response) {
            return false;
        }

        @Override
        public void statusChanged(Client.Status status, Client.Status tryingStatus) {
            String message;
            switch (tryingStatus) {
                case CONNECTING:
                    message = "Đang kết nối lại";
                    break;
                case DISCONNECTED:
                    message = "Mất kết nối";
                    break;
                default:
                    message = "";
            }
            dispatch(MainActions.setClientStatus(status, message));
        }
    };
    private final Client client = new Client(clientHandler);

    public Client getClient() {
        return client;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Settings settings = Settings.shared(this);
        String host = settings.getHost();
        String userName = settings.getUserName();
        String password = settings.getPassword();

        dispatch(SignInActions.setHost(host));
        dispatch(SignInActions.setUserName(userName));
        dispatch(SignInActions.setPassword(password));

        if (!TextUtils.isEmpty(host) &&
                !TextUtils.isEmpty(userName) &&
                !TextUtils.isEmpty(password)) {
            ServiceInterface.shared(this).setCredentials(new ClientInfo(userName, password, host));
            ServiceInterface.shared(this).connect();
            dispatch(MainActions.setRoot(TableListFragment.class, "Table"));
        }
    }
}
