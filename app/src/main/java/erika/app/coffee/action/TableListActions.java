package erika.app.coffee.action;

import android.content.Context;

import java.util.List;

import erika.app.coffee.component.OrderFragment;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.TableStatus;
import erika.app.coffee.model.args.SetIsRefreshingArgs;
import erika.app.coffee.model.args.SetLoadStateArgs;
import erika.app.coffee.model.args.SetTableListResultArgs;
import erika.app.coffee.model.args.SetTableStatus;
import erika.app.coffee.reducer.TableListReducer;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.communication.Table;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class TableListActions {
    public static DispatchAction fetchTable(Context context) {
        return dispatcher -> {
            dispatcher.dispatch(setLoadState(LoadState.LOADING));
            ServiceInterface.shared(context).fetchTable().then(task -> {
                dispatcher.dispatch(setIsRefreshing(false));
                if (task.isCompleted()) {
                    dispatcher.dispatch(setLoadState(LoadState.NONE));
                    dispatcher.dispatch(setTableListResult(task.getResult().tables));
                } else {
                    dispatcher.dispatch(setLoadState(LoadState.FAILED));
                }
            });
        };
    }

    public static DispatchAction serveTable(Context context, Table table) {
        return dispatcher -> {
            dispatcher.dispatch(MessageBoxActions.ask(
                    "Bạn có muốn phục vụ bàn " + table.name + "?",
                    "Bàn chưa phục vụ",
                    () -> {
                        dispatcher.dispatch(serveTableAfterConfirm(context, table));
                    }
            ));
        };
    }

    private static DispatchAction serveTableAfterConfirm(Context context, Table table) {
        return dispatcher -> {
            ServiceInterface.shared(context).serveTable(table.id).then(task -> {
                if (task.isCompleted()) {
                    if (task.getResult().isSuccessful()) {
                        dispatcher.dispatch(OrderActions.setTable(table, true));
                        dispatcher.dispatch(setTableStatus(table.id, TableStatus.BUSY));
                        dispatcher.dispatch(MainActions.push(OrderFragment.class, table.name));
                    } else {
                        dispatcher.dispatch(MessageBoxActions.show(task.getResult().message));
                    }
                }
            });
        };
    }

    private static Action setTableListResult(List<Table> items) {
        return new SetTableListResultArgs(items);
    }

    public static Action setLoadState(LoadState loadState) {
        return new SetLoadStateArgs(TableListActions.class, loadState);
    }

    public static Action setIsRefreshing(boolean refreshing) {
        return new SetIsRefreshingArgs(TableListReducer.class, refreshing);
    }

    public static Action setTableStatus(int tableId, TableStatus status) {
        return new SetTableStatus(tableId, status);
    }
}
