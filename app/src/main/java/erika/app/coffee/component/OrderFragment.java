package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import erika.app.coffee.R;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.state.OrderState;
import erika.core.redux.Reducer;

public class OrderFragment extends BaseFragment<OrderState> {

    @Override
    public OrderState getStateFromStore(AppState appState) {
        return appState.order;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        View leftPanel = view.findViewById(R.id.leftPanel);
        view.findViewById(R.id.dragLayout).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return Math.abs((int) event.getX() - leftPanel.getWidth()) < 15;
                case MotionEvent.ACTION_MOVE:
                    ViewGroup.LayoutParams layoutParams = leftPanel.getLayoutParams();
                    layoutParams.width = Math.max((int) event.getX(), leftPanel.getMinimumWidth());
                    leftPanel.setLayoutParams(layoutParams);
                    return true;
                default:
                    return false;
            }
        });
        return view;
    }
}
