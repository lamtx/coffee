package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import erika.app.coffee.R;
import erika.app.coffee.action.MenuItemActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.MenuType;
import erika.app.coffee.presentation.ExpandableDataSource;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.service.communication.MenuCategory;
import erika.app.coffee.service.communication.MenuItem;
import erika.app.coffee.state.MenuState;

public class MenuFragment extends BaseFragment<MenuState> {
    private MenuType menuType;
    private ExpandableListView listView;
    private SwipeRefreshLayout refresher;
    private PackageAdapter adapter;
    private View loadingIndicator;

    public static MenuFragment newInstance(MenuType menuType) {
        MenuFragment instance = new MenuFragment();
        instance.menuType = menuType;
        return instance;
    }
    @Override
    public MenuState getStateFromStore(AppState appState) {
        return appState.order.menuStates.get(menuType.ordinal());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expandable_list, container, false);
        listView = ((ExpandableListView) view.findViewById(android.R.id.list));
        refresher = ((SwipeRefreshLayout) view.findViewById(R.id.refresher));
        refresher.setOnRefreshListener(() -> {
            refresh();
        });
        adapter = new PackageAdapter(null,
                (layoutInflater, parent, groupPosition) -> new PackageBinder(layoutInflater, parent),
                (layoutInflater, parent, position) -> new ComponentBinder(layoutInflater, parent)
        );
        loadingIndicator = inflater.inflate(R.layout.item_loading_indicator, listView, false);
        listView.setAdapter(adapter);
        setHasOptionsMenu(true);
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
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void refresh() {
        if (getState().items.isEmpty()) {
            dispatch(MenuItemActions.fetchMenuItems(getActivity(), menuType, getState().keyword));
        }
    }

    private void addMenuItem(MenuItem item) {

    }

    private static class PackageBinder extends ViewBinder<MenuCategory> {

        private final TextView textName;
        private final TextView textCount;

        PackageBinder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_menu_category, parent, false));
            textName = (TextView) itemView.findViewById(R.id.textName);
            textCount = (TextView) itemView.findViewById(R.id.textCount);
        }

        @Override
        public void bind() {
            MenuCategory item = getItem();
            textName.setText(item.name);
            textCount.setText(String.valueOf(item.items.size()));
        }
    }

    private class ComponentBinder extends ViewBinder<MenuItem> {

        private final TextView textName;
        private final TextView textCode;
        private final TextView textEnglishName;
        private final TextView textDescription;

        ComponentBinder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_menu_item, parent, false));
            textName = (TextView) itemView.findViewById(R.id.textName);
            textCode = (TextView) itemView.findViewById(R.id.textCode);
            textEnglishName = (TextView) itemView.findViewById(R.id.textEnglishName);
            textDescription = (TextView) itemView.findViewById(R.id.textDescription);
            itemView.findViewById(R.id.buttonAdd).setOnClickListener(v -> {
                addMenuItem(getItem());
            });
        }

        @Override
        public void bind() {
            MenuItem item = getItem();
            textName.setText(item.getName());
            textDescription.setText(String.format(Locale.getDefault(), "%,8d", item.getPrice()));
            textCode.setText(item.code);
            textEnglishName.setText(item.englishName);
        }
    }

    private class PackageAdapter extends ExpandableDataSource<MenuCategory, MenuItem> {
        PackageAdapter(List<MenuCategory> parents, HeaderViewBinderCreator<MenuCategory> headerViewBinderCreator, ViewBinderCreator<MenuItem> viewBinderCreator) {
            super(parents, headerViewBinderCreator, viewBinderCreator);
        }

        @Override
        protected List<MenuItem> getChildren(MenuCategory parent) {
            return parent.items;
        }
    }
}
