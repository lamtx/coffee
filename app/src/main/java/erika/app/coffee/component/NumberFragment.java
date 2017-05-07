package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import erika.app.coffee.R;
import erika.app.coffee.action.NumberActions;
import erika.app.coffee.action.PopupActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.model.InputNumberMode;
import erika.app.coffee.state.NumberState;

public class NumberFragment extends BaseFragment<NumberState> {

    private TextView textValue;
    private TextView buttonZero;
    private View buttonDot;
    private View actionLayout;

    @Override
    public NumberState getStateFromStore(AppState appState) {
        return appState.number;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number, container, false);
        ViewGroup numberContainer = (ViewGroup) view.findViewById(R.id.numberContainer);
        buttonZero = (TextView) view.findViewById(R.id.buttonZero);
        buttonDot = view.findViewById(R.id.buttonDot);
        actionLayout = view.findViewById(R.id.actionLayout);
        textValue = ((TextView) view.findViewById(R.id.textValue));
        for (int i = 0; i < numberContainer.getChildCount(); i++) {
            View child = numberContainer.getChildAt(i);
            if (child instanceof Button) {
                child.setOnClickListener(v -> {
                    char tag = ((String) v.getTag()).charAt(0);
                    handleNumberButtonClick(tag);
                });
            }
        }
        view.findViewById(R.id.buttonClearBack).setOnClickListener(v -> {
            if (getState().mode == InputNumberMode.INTEGER) {
                dispatch(NumberActions.removeAll());
            } else {
                dispatch(NumberActions.removeChar(getState().value));
            }
        });
        view.setOnClickListener(v -> {
            dispatch(NumberActions.setNumberAction(null));
            dispatch(PopupActions.dismiss());
        });
        view.findViewById(R.id.buttonCancel).setOnClickListener(v -> {
            dispatch(NumberActions.setNumberAction(null));
            dispatch(PopupActions.dismiss());
        });
        view.findViewById(R.id.buttonOk).setOnClickListener(v -> {
            if (getState().action != null) {
                getState().action.apply(Double.parseDouble(getState().value));
            }
            dispatch(PopupActions.dismiss());
        });
        return view;
    }

    private void handleNumberButtonClick(char tag) {
        if (getState().mode == InputNumberMode.INTEGER) {
            dispatch(NumberActions.addValue(getState().value, tag));
            if (tag != '+') {
                if (getState().action != null) {
                    getState().action.apply(Double.parseDouble(getState().value));
                }
                dispatch(PopupActions.dismiss());
            }
        } else {
            dispatch(NumberActions.appendChar(getState().value, tag));
        }
    }

    @Override
    public void bindStateToView(NumberState state) {
        super.bindStateToView(state);
        textValue.setText(state.value);
        if (state.mode == InputNumberMode.DOUBLE) {
            actionLayout.setVisibility(View.VISIBLE);
            buttonZero.setText(R.string.num_0);
            buttonZero.setTag(R.string.num_0);
            buttonDot.setEnabled(true);
        } else {
            actionLayout.setVisibility(View.GONE);
            buttonZero.setText(R.string.num_plus_ten);
            buttonZero.setTag("+");
            buttonDot.setEnabled(false);
        }
    }
}
