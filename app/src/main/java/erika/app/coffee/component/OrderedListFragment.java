package erika.app.coffee.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import erika.app.coffee.R;
import erika.app.coffee.action.OrderedListActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.app.coffee.state.OrderedListState;
import erika.app.coffee.utility.Utils;

public class OrderedListFragment extends BaseListFragment<OrderedListState, OrderedMenuItem> {

    @Override
    public OrderedListState getStateFromStore(AppState appState) {
        return appState.orderedList;
    }

    @Override
    public void onStart() {
        super.onStart();
        List<OrderedMenuItem> items = getState().getItems();
        if (items == null || items.isEmpty()) {
            refresh();
        }
    }

    @Override
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
        private final TextView textTotal;

        ItemBinder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_ordered, parent, false));
            textName = (TextView) itemView.findViewById(R.id.textName);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            textQuantity = (TextView) itemView.findViewById(R.id.textQuantity);
            textTotal = (TextView) itemView.findViewById(R.id.textTotal);
            itemView.setOnClickListener(v -> {
                showMenu(v, getItem());
            });
        }

        @Override
        public void bind() {
            OrderedMenuItem item = getItem();
            textName.setText(item.menuItem.name);
            textPrice.setText(Utils.stringFrom(item.menuItem.price));
            textQuantity.setText(Utils.stringFrom(item.quantity));
            textTotal.setText(Utils.stringFrom(item.quantity * item.menuItem.price));
        }
    }
    private void showMenu(View v, final OrderedMenuItem menuItem) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_add:
                    break;
                case R.id.action_remove:
                    break;
                case R.id.action_clear:
                    dispatch(OrderedListActions.clear(getActivity(), getState().tableId, menuItem.id, menuItem.menuItem));
                    break;
            }
            return true;
        });
        popup.inflate(R.menu.ordered_item);
        popup.show();
    }

}
