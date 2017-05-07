package erika.app.coffee.utility;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import erika.app.coffee.model.TableStatus;
import erika.app.coffee.service.Client;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.MenuItem;
import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.app.coffee.service.communication.Table;
import erika.app.coffee.service.communication.responses.ListOfOrderedMenuResponse;
import erika.app.coffee.service.communication.responses.ListOfTableResponse;
import erika.app.coffee.service.communication.responses.MenuResponse;
import erika.app.coffee.service.communication.responses.ServeTableResponse;
import erika.app.coffee.service.communication.responses.SuccessResponse;
import erika.core.threading.Task;
import erika.core.threading.TaskFactory;

public class Mock {
    private final static SparseArray<List<OrderedMenuItem>> caches = new SparseArray<>();
    private static int orderedItemId = 0;

    public static Task<ListOfTableResponse> fetchTable() {
        return TaskFactory.startNew(() -> {
            Thread.sleep(1000);
            ArrayList<Table> tables = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                tables.add(new Table(i, "Table " + i, caches.get(i) != null ? TableStatus.BUSY : TableStatus.AVAILABLE, 0));
            }
            return new ListOfTableResponse(tables);
        });
    }

    public static Task<Client.ConnectResult> signIn() {
        return TaskFactory.startNew(() -> {
            Thread.sleep(1000);
            return Client.ConnectResult.OK;
        });
    }

    public static Task<MenuResponse> fetchMenuItems() {
        return TaskFactory.startNew(() -> {
            ArrayList<MenuCategory> result = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                ArrayList<MenuItem> menuItems = new ArrayList<>();
                for (int j = 0; j < ((i % 3) + 1) * 4; j++) {
                    menuItems.add(new MenuItem(j, "Item " + j, j * 10000));
                }
                result.add(new MenuCategory("Category " + i, menuItems));
            }
            return new MenuResponse(result);
        });
    }

    public static Task<ListOfOrderedMenuResponse> fetchOrderedMenuItems(int tableId) {
        return TaskFactory.startNew(() -> {
            Thread.sleep(1000);
            List<OrderedMenuItem> items = readItemFromCache(tableId);
            return new ListOfOrderedMenuResponse(tableId, "Table " + tableId, items);
        });
    }

    @NonNull
    private static List<OrderedMenuItem> readItemFromCache(int tableId) {
        List<OrderedMenuItem> items = caches.get(tableId);
        if (items == null) {
            items = new ArrayList<>();
        } else {
            items = new ArrayList<>(items);
        }
        return items;
    }

    public static Task<SuccessResponse> orderItem(MenuItem menuItem, int tableId, double quantity) {
        return TaskFactory.startNew(() -> {
            Thread.sleep(300);
            List<OrderedMenuItem> orderedMenuItems = readItemFromCache(tableId);
            orderedMenuItems.add(new OrderedMenuItem(menuItem, quantity, ++orderedItemId));
            caches.put(tableId, orderedMenuItems);
            return new SuccessResponse(true, "Add complete");
        });
    }

    public static Task<ServeTableResponse> serveTable(int tableId) {
        return TaskFactory.startNew(() -> {
            Thread.sleep(300);
            List<OrderedMenuItem> items = caches.get(tableId);
            if (items == null) {
                caches.put(tableId, new ArrayList<>());
                return new ServeTableResponse("");
            }
            return new ServeTableResponse("Bàn đã được phục vụ");
        });
    }
}
