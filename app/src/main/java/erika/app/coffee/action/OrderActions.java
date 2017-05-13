package erika.app.coffee.action;

import android.content.Context;

import java.util.List;

import erika.app.coffee.model.TableStatus;
import erika.app.coffee.model.args.SetLeftPanelWidthArgs;
import erika.app.coffee.model.args.SetMenuCategoryKeywordArgs;
import erika.app.coffee.model.args.SetMenuCategoryListArgs;
import erika.app.coffee.model.args.SetTableForOrderComponentArgs;
import erika.app.coffee.service.ServiceInterface;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.Table;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class OrderActions {
    public static Action setTable(Table table, boolean shouldReload) {
        return new SetTableForOrderComponentArgs(table, shouldReload);
    }

    public static Action setMenuCategoryList(List<MenuCategory> categories, List<MenuCategory> noneFilteredList) {
        return new SetMenuCategoryListArgs(categories, noneFilteredList);
    }

    public static Action setMenuCategoryKeyword(String keyword) {
        return new SetMenuCategoryKeywordArgs(keyword);
    }

    public static Action setLeftPanelWidth(float distance) {
        return new SetLeftPanelWidthArgs(distance);
    }

    public static DispatchAction cancelService(Context context, int tableId, String tableName) {
        return dispatcher -> {
            dispatcher.dispatch(MessageBoxActions.ask(
                    "Bạn có muốn hủy bàn " + tableName,
                    "Hủy phục vụ",
                    () -> {
                        ServiceInterface.shared(context).cancelTable(tableId).then(task -> {
                            if (task.isCompleted() && task.getResult().successful) {
                                dispatcher.dispatch(MessageBoxActions.show(task.getResult().toast,"", () -> {
                                    dispatcher.dispatch(TableListActions.setTableStatus(tableId, TableStatus.AVAILABLE));
                                    dispatcher.dispatch(MainActions.pop());
                                }));
                            } else {
                                dispatcher.dispatch(MessageBoxActions.show("Hủy phục vụ bàn thất bại", "Lỗi"));
                            }
                        });
                    }

            ));
        };
    }

    public static DispatchAction checkout(Context context, int tableId, String tableName, boolean shouldPrint) {
        return dispatcher -> {
            dispatcher.dispatch(MessageBoxActions.ask(
                    "Bạn có muốn thanh toán bàn " + tableName,
                    "Than toán" + (shouldPrint ? " và in" : ""),
                    () -> {
                        ServiceInterface.shared(context).checkout(tableId, shouldPrint).then(task -> {
                            if (task.isCompleted() && task.getResult().successful) {
                                dispatcher.dispatch(MessageBoxActions.show(task.getResult().toast,"", () -> {
                                    dispatcher.dispatch(TableListActions.setTableStatus(tableId, TableStatus.AVAILABLE));
                                    dispatcher.dispatch(MainActions.pop());
                                }));
                            } else {
                                dispatcher.dispatch(MessageBoxActions.show("Thanh toán bàn thất bại", "Lỗi"));
                            }
                        });
                    }
            ));
        };
    }
}
