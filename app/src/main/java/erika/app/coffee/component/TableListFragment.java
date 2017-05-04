package erika.app.coffee.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.PopupMenu;
import android.widget.TextView;

import erika.app.coffee.R;
import erika.app.coffee.action.MainActions;
import erika.app.coffee.action.OrderActions;
import erika.app.coffee.action.TableListActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.TableStatus;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.service.communication.Table;
import erika.app.coffee.state.TableListState;
import erika.app.coffee.utility.Utils;

public class TableListFragment extends BaseListFragment<TableListState, CheckableTable> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_table;
    }

    @Override
    protected ViewBinder<CheckableTable> createViewBinder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ItemHolder(inflater, parent);
    }

    @Override
    public TableListState getStateFromStore(AppState appState) {
        return appState.tableList;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getState().items.isEmpty()) {
            refresh();
        }
    }

    @Override
    protected void onRefreshRequested() {
        super.onRefreshRequested();
        refresh();
    }

    protected void refresh() {
        dispatch(TableListActions.setIsRefreshing(true));
        if (getState().loadState != LoadState.LOADING) {
            dispatch(TableListActions.fetchTable(getActivity()));
        }
    }

    private class ItemHolder extends ViewBinder<CheckableTable> {
        private final TextView textPrice;
        private final TextView textName;
        private final boolean enableMenu;
        private final View buttonMenu;

        ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_table, parent, false));
            this.enableMenu = getState().enableMenu;

            textName = (TextView) itemView.findViewById(R.id.textName);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
            itemView.setOnClickListener(v -> {
                openTable(getItem().table);
            });

            if (enableMenu) {
                buttonMenu.setOnClickListener(v -> {
                    showMenu(v, getItem().table);
                });
            } else {
                buttonMenu.setVisibility(View.GONE);
            }
        }

        @Override
        public void bind() {
            CheckableTable item = getItem();
            textName.setText(item.table.name);
            textPrice.setText(Utils.stringFrom(item.table.price));
            itemView.setBackgroundResource(getColor(item.table.status));
            ((Checkable) itemView).setChecked(item.checked);
        }
    }

    private static int getColor(TableStatus status) {
        switch (status) {
            case AVAILABLE:
                return R.drawable.table_available;
            case BUSY:
                return R.drawable.table_busy;
            default:
                return R.drawable.table_available;
        }
    }

    private void showMenu(View v, final Table table) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_merge_table:
                    mergeTable(table);
                    break;
                case R.id.action_switch_customer:
                    switchCustomer(table);
                    break;
                case R.id.action_switch_table:
                    switchTable(table);
                    break;
            }
            return true;
        });
        popup.inflate(R.menu.busy_or_pending_table);
        popup.show();
    }

    private void openTable(Table table) {
        switch (table.status) {
            case BUSY:
                dispatch(OrderActions.setTable(table, true));
                dispatch(MainActions.push(OrderFragment.class));
                break;
            case AVAILABLE:
//                Intent i = new Intent(getActivity(), SelectCustomerActivity.class);
//                i.putExtras(SelectCustomerActivity.newArgs(table));
//                startActivityForResult(i, REQUEST_BOOKING);
                break;
            default:
                break;
        }
    }

    private void switchTable(Table table) {

    }

    private void switchCustomer(Table table) {

    }

    private void mergeTable(Table table) {

    }

}
