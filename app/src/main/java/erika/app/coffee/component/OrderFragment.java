package erika.app.coffee.component;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import erika.app.coffee.R;
import erika.app.coffee.action.OrderActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.state.OrderState;
import erika.app.coffee.utility.HorizontalScrollDetectorView;

class Reference {
    boolean handling = true;
}

public class OrderFragment extends BaseFragment<OrderState> {


    private View leftPanel;

    @Override
    public OrderState getStateFromStore(AppState appState) {
        return appState.order;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        leftPanel = view.findViewById(R.id.leftPanel);
        ((HorizontalScrollDetectorView) view).setOnScrollListener(new HorizontalScrollDetectorView.OnScrollListener() {
            @Override
            public void onScroll(float distanceX, float distanceY) {
                dispatch(OrderActions.setLeftPanelWidth(getState().leftPanelWidth + distanceX));
            }

            @Override
            public boolean onAcceptTouch(float x, float y) {
                return x > getState().leftPanelWidth;
            }
        });
        return view;
    }

    @Override
    public void bindStateToView(OrderState state) {
        super.bindStateToView(state);
    }

    @Override
    public void willReceiveState(OrderState state) {
        super.willReceiveState(state);
        if (getState() != null && state.leftPanelWidth != getState().leftPanelWidth) {
            ViewGroup.LayoutParams layoutParams = leftPanel.getLayoutParams();
            layoutParams.width = Math.max((int) state.leftPanelWidth, leftPanel.getMinimumWidth());
            leftPanel.setLayoutParams(layoutParams);
        }
    }
}
