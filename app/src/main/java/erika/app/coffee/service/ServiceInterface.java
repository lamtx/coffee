package erika.app.coffee.service;

import android.content.Context;

import erika.app.coffee.App;
import erika.app.coffee.model.ClientInfo;
import erika.app.coffee.service.communication.requests.ListOfTableRequest;
import erika.app.coffee.service.communication.requests.MenuRequest;
import erika.app.coffee.service.communication.responses.ListOfTableResponse;
import erika.app.coffee.service.communication.responses.MenuResponse;
import erika.core.threading.Task;
import erika.core.threading.TaskFactory;

public class ServiceInterface {
    private static ServiceInterface instance;
    private final Client client;

    public static ServiceInterface shared(Context context) {
        if (instance == null) {
            App app = (App) context.getApplicationContext();
            instance = new ServiceInterface(app.getClient());
        }
        return instance;
    }

    private ServiceInterface(Client client) {
        this.client = client;
    }

    public Task<Client.SocketStatus> signIn(String userName, String password, String host, int port) {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.host = host;
        clientInfo.userName = userName;
        clientInfo.password = password;
        clientInfo.port = port;
        return client.connect(clientInfo);
    }

    private static <T> Task<T> delayedCompleted(T value) {
        return TaskFactory.startNew(() -> {
            Thread.sleep(1000);
            return value;
        });
    }

    public Task<ListOfTableResponse> fetchTable() {
        ListOfTableRequest request = new ListOfTableRequest();
        return client.sendRequest(request, ListOfTableResponse.class);
    }

    public Task<MenuResponse> fetchMenuItems() {
        return client.sendRequest(new MenuRequest(), MenuResponse.class);
    }
}
