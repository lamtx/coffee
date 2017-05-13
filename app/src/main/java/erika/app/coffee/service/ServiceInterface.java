package erika.app.coffee.service;

import android.content.Context;

import erika.app.coffee.App;
import erika.app.coffee.model.ClientInfo;
import erika.app.coffee.model.TableAction;
import erika.app.coffee.service.communication.MenuItem;
import erika.app.coffee.service.communication.request.ActionTableRequest;
import erika.app.coffee.service.communication.request.ListOfOrderedMenuRequest;
import erika.app.coffee.service.communication.request.ListOfTableRequest;
import erika.app.coffee.service.communication.request.MenuItemChangedRequest;
import erika.app.coffee.service.communication.request.MenuRequest;
import erika.app.coffee.service.communication.request.ServeTableRequest;
import erika.app.coffee.service.communication.response.ListOfOrderedMenuResponse;
import erika.app.coffee.service.communication.response.ListOfTableResponse;
import erika.app.coffee.service.communication.response.MenuResponse;
import erika.app.coffee.service.communication.response.ServeTableResponse;
import erika.app.coffee.service.communication.response.SuccessResponse;
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

    public void setCredentials(ClientInfo clientInfo) {
        client.setCredentials(clientInfo);
    }

    public Task<Client.ConnectResult> connect() {
        if (MOCK) {
            return Mock.signIn();
        }
        return client.connect();
    }

    public Task<ListOfTableResponse> fetchTable() {
        if (MOCK) {
            return Mock.fetchTable();
        }
        return client.sendRequest(new ListOfTableRequest(), ListOfTableResponse.class);
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

    public Task<SuccessResponse> orderItem(MenuItem menuItem, int tableId, double quantity) {
        return editOrderedItem(menuItem, tableId, quantity, 0);
    }

    public Task<SuccessResponse> editOrderedItem(MenuItem menuItem, int tableId, double quantity, int id) {
        if (MOCK) {
            return Mock.orderItem(menuItem, tableId, quantity);
        }
        return client.sendRequest(new MenuItemChangedRequest(tableId, menuItem.id, quantity, id), SuccessResponse.class);
    }

    public Task<ServeTableResponse> serveTable(int tableId) {
        if (MOCK) {
            client.sendRequest(new MenuRequest());
            return Mock.serveTable(tableId);
        }
        return client.sendRequest(new ServeTableRequest(tableId), ServeTableResponse.class);
    }

    public Task<SuccessResponse> cancelTable(int tableId) {
        if (MOCK) {
            return Task.completedTask(new SuccessResponse(true, "Hủy bàn thành công"));
        }
        ActionTableRequest request = new ActionTableRequest(TableAction.CANCEL_SERVICE, tableId);
        return client.sendRequest(request, SuccessResponse.class);
    }

    public Task<SuccessResponse> checkout(int tableId, boolean shouldPrint) {
        if (MOCK) {
            return Task.completedTask(new SuccessResponse(true, "Thanh toán thành công"));
        }
        ActionTableRequest request = new ActionTableRequest(shouldPrint ? TableAction.CHECK_OUT : TableAction.CHECK_OUT_AND_PRINT, tableId);
        return client.sendRequest(request, SuccessResponse.class);
    }

    public void disconnect() {
        client.setCredentials(null);
        client.disconnect();
    }


}
