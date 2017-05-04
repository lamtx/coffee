package erika.app.coffee.service;

import android.content.Context;

import erika.app.coffee.App;
import erika.app.coffee.model.ClientInfo;
import erika.app.coffee.service.communication.requests.ListOfOrderedMenuRequest;
import erika.app.coffee.service.communication.requests.ListOfTableRequest;
import erika.app.coffee.service.communication.requests.MenuRequest;
import erika.app.coffee.service.communication.responses.ListOfOrderedMenuResponse;
import erika.app.coffee.service.communication.responses.ListOfTableResponse;
import erika.app.coffee.service.communication.responses.MenuResponse;
import erika.app.coffee.utility.Mock;
import erika.core.threading.Task;

public class ServiceInterface {
    private static ServiceInterface instance;
    private final Client client;
    private static boolean MOCK = true;
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

    public Task<Client.ConnectResult> signIn(String userName, String password, String host, int port) {
        if (MOCK) {
            return Mock.signIn();
        }
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.host = host;
        clientInfo.userName = userName;
        clientInfo.password = password;
        clientInfo.port = port;
        return client.connect(clientInfo);
    }

    public Task<ListOfTableResponse> fetchTable() {
        if (MOCK) {
            return Mock.fetchTable();
        }
        ListOfTableRequest request = new ListOfTableRequest();
        return client.sendRequest(request, ListOfTableResponse.class);
    }

    public Task<MenuResponse> fetchMenuItems() {
        if (MOCK) {
            return Mock.fetchMenuItems();
        }
        return client.sendRequest(new MenuRequest(), MenuResponse.class);
    }

    public Task<ListOfOrderedMenuResponse> fetchOrderedMenuItems(int tableId) {
        if (MOCK) {
            return Mock.fetchOrderedMenuItems(tableId);
        }
        return client.sendRequest(new ListOfOrderedMenuRequest(tableId), ListOfOrderedMenuResponse.class);
    }
}
