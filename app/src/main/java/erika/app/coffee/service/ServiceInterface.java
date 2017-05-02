package erika.app.coffee.service;

import android.content.Context;

import erika.app.coffee.App;
import erika.app.coffee.model.MenuType;
import erika.app.coffee.model.TableStatus;
import erika.app.coffee.service.communication.Customer;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.Table;
import erika.app.coffee.service.communication.requests.ListOfTableRequest;
import erika.app.coffee.service.communication.requests.MenuRequest;
import erika.app.coffee.service.communication.requests.RecentMenuRequest;
import erika.app.coffee.service.communication.requests.Request;
import erika.app.coffee.service.communication.requests.StarredMenuRequest;
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
//        ClientInfo clientInfo = new ClientInfo();
//        clientInfo.host = host;
//        clientInfo.userName = userName;
//        clientInfo.password = password;
//        clientInfo.port = port;
//        return client.connect(clientInfo);
        return delayedCompleted(Client.SocketStatus.OK);
    }

    private static <T> Task<T> delayedCompleted(T value) {
        return TaskFactory.startNew(() -> {
            Thread.sleep(1000);
            return value;
        });
    }

    public Task<ListOfTableResponse> fetchTable(TableStatus tableStatus, boolean includeOutside, String keyword) {
//        ListOfTableRequest request = new ListOfTableRequest(tableStatus, includeOutside, keyword);
//        return client.sendRequest(request, ListOfTableResponse.class);
        Table[] tables = new Table[]{
            new Table("Table 1", new Customer("asds", "2343", 1)),
            new Table("Table 2", new Customer("asds", "2343", 1)),
            new Table("Table 3", new Customer("asds", "2343", 1)),
            new Table("Table 4", new Customer("asds", "2343", 1)),
        };
        return delayedCompleted(new ListOfTableResponse(tables));
    }

    public Task<MenuResponse> fetchMenuItems(MenuType menuType) {
        Request request;
        switch (menuType) {
            case MENU:
                request = new MenuRequest();
                break;
            case STARRED:
                request = new StarredMenuRequest();
                break;
            case RECENT:
            default:
                request = new RecentMenuRequest();
                break;
        }
        return client.sendRequest(request, MenuResponse.class);
    }
}
