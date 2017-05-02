package erika.app.coffee;

import android.support.annotation.NonNull;

import erika.app.coffee.application.AppState;
import erika.app.coffee.application.AppStore;
import erika.app.coffee.model.ResponseHandler;
import erika.app.coffee.service.Client;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.communication.responses.Response;
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
        public void statusChanged(Client.ConnectionStatus status, Client.ConnectionStatus tryingStatus) {

        }
    };
    private final Client client = new Client(clientHandler);

    public Client getClient() {
        return client;
    }
}
