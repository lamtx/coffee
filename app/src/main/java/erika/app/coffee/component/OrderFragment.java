package erika.app.coffee.component;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        View leftPanel = view.findViewById(R.id.leftPanel);
        boolean landscape = getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        view.findViewById(R.id.dragLayout).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (landscape) {
                        return Math.abs((int) event.getX() - leftPanel.getWidth()) < 15;
                    }
                    return Math.abs((int) event.getY() - leftPanel.getHeight()) < 15;
                case MotionEvent.ACTION_MOVE:
                    ViewGroup.LayoutParams layoutParams = leftPanel.getLayoutParams();
                    if (landscape) {
                        layoutParams.width = Math.max((int) event.getX(), leftPanel.getMinimumWidth());
                    } else {
                        layoutParams.height = Math.max((int) event.getY(), leftPanel.getMinimumHeight());
                    }
                    leftPanel.setLayoutParams(layoutParams);
                    return true;
                default:
                    return false;
            }
        });
        return view;
    }

    @Override
    public void bindStateToView(OrderState state) {
        super.bindStateToView(state);
    }
}
