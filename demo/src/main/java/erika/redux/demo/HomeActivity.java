package erika.redux.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import erika.core.redux.binding.DefaultTextWatcher;
import erika.redux.demo.actions.HomeActions;
import erika.redux.demo.application.AppState;
import erika.redux.demo.application.BaseActivity;
import erika.redux.demo.states.HomeState;

public class HomeActivity extends BaseActivity<HomeState> {
    private TextView textResult;

    @Override
    public HomeState getStateFromStore(AppState appState) {
        return appState.home;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textResult = (TextView) findViewById(R.id.textResult);

        ((EditText) findViewById(R.id.textSource)).addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                dispatch(new HomeActions.HomeSetTextAction(s.toString()));
            }
        });
    }


    @Override
    public void bindStateToView(HomeState homeState) {
        super.bindStateToView(homeState);
        textResult.setText(homeState.text);
    }
}
