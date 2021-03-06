package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import erika.app.coffee.R;
import erika.app.coffee.action.OrderActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.state.OrderState;
import erika.app.coffee.utility.HorizontalScrollDetectorView;

public class OrderFragment extends BaseFragment<OrderState> {


    private View leftPanel;

    @Override
    public OrderState getStateFromStore(AppState appState) {
        return appState.order;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_order, container, false);
        leftPanel = view.findViewById(R.id.leftPanel);
        ((HorizontalScrollDetectorView) view).setOnScrollListener(new HorizontalScrollDetectorView.OnScrollListener() {
            @Override
            public void onScroll(float distanceX) {
                int total = view.getWidth();
                float percent = getState().leftPanelWidth;
                dispatch(OrderActions.setLeftPanelWidth(getActivity(), (total * percent + distanceX) / total));
            }

            @Override
            public boolean onAcceptTouch(float x, float y) {
                return x > leftPanel.getWidth();
            }
        });
        view.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            int width = right - left;
            omitSize(width, getState());
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                dispatch(OrderActions.cancelService(getActivity(), getState().tableId, getState().tableName));
                break;
            case R.id.action_checkout:
                dispatch(OrderActions.checkout(getActivity(), getState().tableId, getState().tableName, false));
                break;
            case R.id.action_print:
                dispatch(OrderActions.checkout(getActivity(), getState().tableId, getState().tableName, true));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void bindStateToView(OrderState state) {
        super.bindStateToView(state);
        omitSize(getView().getWidth(), state);
    }

    private void omitSize(int width, OrderState state) {
        if (width > 0) {
            int w = (int) (state.leftPanelWidth * width);
            if (leftPanel.getWidth() != w) {
                ViewGroup.LayoutParams layoutParams = leftPanel.getLayoutParams();
                layoutParams.width = Math.max(w, leftPanel.getMinimumWidth());
                leftPanel.setLayoutParams(layoutParams);
            }
        }
    }
}
