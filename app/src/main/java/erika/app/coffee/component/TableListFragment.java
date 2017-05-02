package erika.app.coffee.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.PopupMenu;
import android.widget.TextView;

import erika.app.coffee.R;
import erika.app.coffee.action.HomeActions;
import erika.app.coffee.action.MainActions;
import erika.app.coffee.action.OrderActions;
import erika.app.coffee.action.TableListActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.model.CheckableTable;
import erika.app.coffee.model.TableStatus;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.service.communication.Table;
import erika.app.coffee.state.TableListState;
import erika.app.coffee.utility.Utils;

public class TableListFragment extends BaseListFragment<TableListState, CheckableTable> {
    private static final String BUNDLE_TABLE_STATUS = "table-status";
    private TableStatus tableStatus = TableStatus.All;

    public static TableListFragment newInstance(TableStatus tableStatus) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_TABLE_STATUS, tableStatus.ordinal());
        TableListFragment fragment = new TableListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_table;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableStatus = TableStatus.values()[getArguments().getInt(BUNDLE_TABLE_STATUS)];
    }

    @Override
    protected ViewBinder<CheckableTable> createViewBinder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ItemHolder(inflater, parent);
    }

    @Override
    public TableListState getStateFromStore(AppState appState) {
        return appState.home.pages.get(tableStatus.ordinal());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getState().items.isEmpty()) {
            dispatch(HomeActions.fetchTable(getActivity(), tableStatus, true, null));
        }
    }

    private class ItemHolder extends ViewBinder<CheckableTable> {
        private final TextView textCustomerName;
        private final TextView textPrice;
        private final TextView textName;
        private final boolean enableLongClick;
        private final boolean isMultiSelection;
        private final View buttonMenu;

        ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_table, parent, false));
            this.enableLongClick = getState().enableLongClick;
            this.isMultiSelection = getState().isMultiSelection;

            textName = (TextView) itemView.findViewById(R.id.textName);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            textCustomerName = (TextView) itemView.findViewById(R.id.textCustomerName);
            textCustomerName.setVisibility(getState().showCustomer ? View.VISIBLE : View.GONE);
            buttonMenu = itemView.findViewById(R.id.buttonMenu);
            itemView.setOnClickListener(v -> {
                if (!isMultiSelection) {
                    openTable(getItem().table);
                } else {
                    Checkable checkable = (Checkable) v;
                    setTableChecked(getItem(), !checkable.isChecked());
                }
            });

            if (isEnableLongClick()) {
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
            textCustomerName.setText(item.table.customer == null ? "" : item.table.customer.name);
            textPrice.setText(Utils.stringFrom(item.table.price));
            itemView.setBackgroundResource(getColor(item.table.status));
            ((Checkable) itemView).setChecked(item.checked);
        }

        private boolean isEnableLongClick() {
            // True if status is pending-checkout or busy
            return enableLongClick && TableStatus.TamtinhAndBusy.contains(tableStatus);
        }
    }

    private static int getColor(TableStatus status) {
        switch (status) {
            case Available:
                return R.drawable.table_available;
            case Busy:
                return R.drawable.table_busy;
            case Hengio:
                return R.drawable.table_busy;
            case Tamtinh:
                return R.drawable.table_pending_checkout;
            case TamtinhAndBusy:
                return R.drawable.table_pdobs;
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

    private void setTableChecked(CheckableTable item, boolean checked) {
        dispatch(TableListActions.setCheckableTableChecked(tableStatus, item, checked));
    }

    private void openTable(Table table) {
        switch (table.status) {
            case Busy:
            case Tamtinh:
            case TamtinhAndBusy:
                dispatch(OrderActions.setTable(table, true));
                dispatch(MainActions.push(OrderFragment.class));
                break;
            case Available:
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
