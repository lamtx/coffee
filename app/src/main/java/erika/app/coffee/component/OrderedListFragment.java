package erika.app.coffee.component;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import erika.app.coffee.application.AppState;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.service.communication.OrderedMenuItem;
import erika.app.coffee.state.OrderedListState;


public class OrderedListFragment extends BaseListFragment<OrderedListState, OrderedMenuItem> {
    @Override
    public OrderedListState getStateFromStore(AppState appState) {
        return appState.orderedList;
    }

    @Override
    protected ViewBinder<OrderedMenuItem> createViewBinder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return null;
    }
}
