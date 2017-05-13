package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import erika.app.coffee.R;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.model.LoadState;
import erika.app.coffee.presentation.DataSource;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.state.BaseListState;

public abstract class BaseListFragment<State extends BaseListState<E>, E> extends BaseFragment<State> {
    private final DataSource<E> dataSource = new DataSource<>(null,
            this::createViewBinder,
            this::getViewType);
    private SwipeRefreshLayout refreshLayout;
    private TextView textEmpty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        RecyclerView listView = (RecyclerView) view.findViewById(android.R.id.list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresher);
        textEmpty = ((TextView) view.findViewById(android.R.id.empty));
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(this::onRefreshRequested);
        }
        if (listView.getLayoutManager() != null && listView.getLayoutManager().getClass() == LinearLayoutManager.class) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(listView.getContext(),
                    ((LinearLayoutManager) listView.getLayoutManager()).getOrientation());
            listView.addItemDecoration(dividerItemDecoration);
        }
        listView.setAdapter(dataSource);
        return view;
    }

    protected void onRefreshRequested() {
    }

    protected int getViewType(E e) {
        return 0;
    }

    protected abstract ViewBinder<E> createViewBinder(LayoutInflater inflater, ViewGroup parent, int viewType);

    @Override
    public void bindStateToView(State state) {
        super.bindStateToView(state);
        dataSource.setItems(state.getItems());
        if (textEmpty != null) {
            textEmpty.setVisibility(state.loadState != LoadState.LOADING && state.getItems().isEmpty() ? View.VISIBLE : View.GONE);
        }
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(state.refreshing || state.loadState == LoadState.LOADING);
        }
    }

    @LayoutRes
    protected int getLayoutId() {
        return R.layout.fragment_base_list;
    }
}
