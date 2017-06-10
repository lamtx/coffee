package erika.app.coffee.action;

import android.content.Context;

import java.util.List;

import erika.app.coffee.model.TableStatus;
import erika.app.coffee.model.args.SetLeftPanelWidthArgs;
import erika.app.coffee.model.args.SetMenuCategoryKeywordArgs;
import erika.app.coffee.model.args.SetMenuCategoryListArgs;
import erika.app.coffee.model.args.SetCurrentTableArgs;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.Settings;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.Table;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class OrderActions {
    public static Action setTable(Table table, boolean shouldReload) {
        return new SetCurrentTableArgs(table, shouldReload);
    }

    public static Action setMenuCategoryList(List<MenuCategory> categories, List<MenuCategory> noneFilteredList) {
        return new SetMenuCategoryListArgs(categories, noneFilteredList);
    }

    public static Action setMenuCategoryKeyword(String keyword) {
        return new SetMenuCategoryKeywordArgs(keyword);
    }

    public static DispatchAction setLeftPanelWidth(Context context, float distance) {
        return dispatcher -> {
            Settings.shared(context).setLeftPanelWidth(distance);
            dispatcher.dispatch(new SetLeftPanelWidthArgs(distance));
        };
    }

    public static DispatchAction cancelService(Context context, int tableId, String tableName) {
        return dispatcher -> {
            dispatcher.dispatch(MessageBoxActions.ask(
                    "Bạn có muốn hủy bàn " + tableName,
                    "Hủy phục vụ",
                    () -> {
                        dispatcher.dispatch(LoadingDialogAction.show("Hủy phục vụ..."));
                        ServiceInterface.shared(context).cancelTable(tableId).then(task -> {
                            dispatcher.dispatch(LoadingDialogAction.dismiss());
                            if (task.isCompleted() && task.getResult().successful) {
                                dispatcher.dispatch(TableListActions.setTableStatus(tableId, TableStatus.AVAILABLE));
                                dispatcher.dispatch(MainActions.pop());
                            } else {
                                dispatcher.dispatch(MessageBoxActions.show("Hủy phục vụ thất bại"));
                            }
                        });
                    }

            ));
        };
    }

    public static DispatchAction checkout(Context context, int tableId, String tableName, boolean shouldPrint) {
        return dispatcher -> {
            String title = "Than toán" + (shouldPrint ? " và in" : "");
            dispatcher.dispatch(MessageBoxActions.ask(
                    "Bạn có muốn thanh toán bàn " + tableName,
                    title,
                    () -> {
                        dispatcher.dispatch(LoadingDialogAction.show(title));
                        ServiceInterface.shared(context).checkout(tableId, shouldPrint).then(task -> {
                            dispatcher.dispatch(LoadingDialogAction.dismiss());
                            if (task.isCompleted() && task.getResult().successful) {
                                dispatcher.dispatch(MessageBoxActions.show(task.getResult().toast, "", () -> {
                                    dispatcher.dispatch(TableListActions.setTableStatus(tableId, TableStatus.AVAILABLE));
                                    dispatcher.dispatch(MainActions.pop());
                                }));
                            } else {
                                dispatcher.dispatch(MessageBoxActions.show(title + " thất bại"));
                            }
                        });
                    }
            ));
        };
    }
}
