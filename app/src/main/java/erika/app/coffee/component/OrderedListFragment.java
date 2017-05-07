package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import erika.app.coffee.R;
import erika.app.coffee.action.OrderedListActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.presentation.DataSource;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.app.coffee.state.BaseListState;
import erika.app.coffee.state.OrderedListState;
import erika.app.coffee.utility.Utils;

public class OrderedListFragment extends BaseFragment<OrderedListState> {
    private final DataSource<OrderedMenuItem> dataSource = new DataSource<>(null,
            this::createViewBinder,
            null);
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list, container, false);
        RecyclerView listView = (RecyclerView) view.findViewById(android.R.id.list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresher);
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(this::onRefreshRequested);
        }
        listView.setAdapter(dataSource);
        return view;
    }

    @Override
    public OrderedListState getStateFromStore(AppState appState) {
        return appState.orderedList;
    }

    @Override
    public void bindStateToView(OrderedListState state) {
        super.bindStateToView(state);
        dataSource.setItems(state.list.get(state.tableId));
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(state.refreshing || state.loadState == LoadState.LOADING);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        List<OrderedMenuItem> items = getState().list.get(getState().tableId);
        if (items == null || items.isEmpty()) {
            refresh();
        }
    }

    protected void onRefreshRequested() {
        refresh();
    }

    protected void refresh() {
        dispatch(OrderedListActions.setIsRefreshing(true));
        if (getState().loadState != LoadState.LOADING) {
            dispatch(OrderedListActions.fetchItems(getActivity(), getState().tableId));
        }
    }

    protected ViewBinder<OrderedMenuItem> createViewBinder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ItemBinder(inflater, parent);
    }

    private class ItemBinder extends ViewBinder<OrderedMenuItem> {
        private final TextView textName;
        private final TextView textPrice;
        private final TextView textQuantity;

        ItemBinder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_ordered, parent, false));
            textName = (TextView) itemView.findViewById(R.id.textName);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            textQuantity = (TextView) itemView.findViewById(R.id.textQuantity);
            itemView.findViewById(R.id.buttonAdd).setOnClickListener(v -> {
            });
        }

        @Override
        public void bind() {
            OrderedMenuItem item = getItem();
            textName.setText(item.menuItem.name);
            textPrice.setText(Utils.stringFrom(item.menuItem.price));
            textQuantity.setText(Utils.stringFrom(item.quantity));
        }
    }
}
