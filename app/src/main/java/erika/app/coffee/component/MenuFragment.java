package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import erika.app.coffee.R;
import erika.app.coffee.action.MenuActions;
import erika.app.coffee.action.OrderActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.presentation.ExpandableDataSource;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.MenuItem;
import erika.app.coffee.state.MenuState;
import erika.app.coffee.utility.Utils;
import erika.core.redux.binding.DefaultTextWatcher;

public class MenuFragment extends BaseFragment<MenuState> {
    private ExpandableListView listView;
    private SwipeRefreshLayout refresher;
    private MenuAdapter adapter;
    private View loadingIndicator;
    private TextView emptyText;


    @Override
    public MenuState getStateFromStore(AppState appState) {
        return appState.menu;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expandable_list, container, false);
        listView = ((ExpandableListView) view.findViewById(android.R.id.list));
        emptyText = ((TextView) view.findViewById(android.R.id.empty));
        refresher = ((SwipeRefreshLayout) view.findViewById(R.id.refresher));
        refresher.setOnRefreshListener(this::refresh);
        adapter = new MenuAdapter(null,
                (layoutInflater, parent, groupPosition) -> new CategoryBinder(layoutInflater, parent),
                (layoutInflater, parent, position) -> new ItemBinder(layoutInflater, parent)
        );
        loadingIndicator = inflater.inflate(R.layout.item_loading_indicator, listView, false);
        listView.setAdapter(adapter);
        ((EditText) view.findViewById(R.id.textSearch)).addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //searchMenu(s.toString());
            }
        });
        return view;
    }

    @Override
    public void bindStateToView(MenuState state) {
        super.bindStateToView(state);
        refresher.setRefreshing(state.refreshing);
        adapter.setParents(state.items);
        if (state.loadState == LoadState.LOADING) {
            if (listView.getFooterViewsCount() == 0) {
                listView.addFooterView(loadingIndicator);
            }
        } else {
            listView.removeFooterView(loadingIndicator);
        }
        emptyText.setVisibility(state.items.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getState().items.isEmpty()) {
            refresh();
        }
    }

    private void refresh() {
        dispatch(MenuActions.setIsRefreshing(true));
        if (getState().loadState != LoadState.LOADING) {
            dispatch(MenuActions.fetchMenuItems(getActivity(), getState().keyword));
        }
    }


    private void searchMenu(String keyword) {
        dispatch(OrderActions.setMenuCategoryKeyword(keyword));
        dispatch(MenuActions.search(getState().noneFilteredItems, keyword));
    }

    private void addOneItem(MenuItem item) {
        dispatch(MenuActions.order(getActivity(), item, getState().tableId, 1));
    }

    private void addMenuItem(MenuItem item) {
        dispatch(MenuActions.showNumberDialog(getActivity(), item, getState().tableId));
    }

    private static class CategoryBinder extends ViewBinder<MenuCategory> {

        private final TextView textName;
        private final TextView textCount;

        CategoryBinder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_menu_category, parent, false));
            textName = (TextView) itemView.findViewById(R.id.textName);
            textCount = (TextView) itemView.findViewById(R.id.textCount);
        }

        @Override
        public void bind() {
            MenuCategory item = getItem();
            textName.setText(item.name);
            textCount.setText(textCount.getContext().getResources().getQuantityString(R.plurals.item_quantity, item.items.size(), item.items.size()));
        }
    }

    private class ItemBinder extends ViewBinder<MenuItem> {

        private final TextView textName;
        private final TextView textDescription;

        ItemBinder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_menu_item, parent, false));
            textName = (TextView) itemView.findViewById(R.id.textName);
            textDescription = (TextView) itemView.findViewById(R.id.textDescription);
            itemView.findViewById(R.id.buttonAdd).setOnClickListener(v -> {
                addOneItem(getItem());
            });
            itemView.setOnClickListener(v -> {
                addMenuItem(getItem());
            });
        }

        @Override
        public void bind() {
            MenuItem item = getItem();
            textName.setText(item.name);
            textDescription.setText(Utils.stringFrom(item.price));
        }
    }

    private class MenuAdapter extends ExpandableDataSource<MenuCategory, MenuItem> {
        MenuAdapter(List<MenuCategory> parents, HeaderViewBinderCreator<MenuCategory> headerViewBinderCreator, ViewBinderCreator<MenuItem> viewBinderCreator) {
            super(parents, headerViewBinderCreator, viewBinderCreator);
        }

        @Override
        protected List<MenuItem> getChildren(MenuCategory parent) {
            return parent.items;
        }

    }
}
