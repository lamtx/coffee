package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import erika.app.coffee.R;
import erika.app.coffee.action.SignInActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.model.args.SetHostArgs;
import erika.app.coffee.model.args.SetPasswordArgs;
import erika.app.coffee.model.args.SetUserNameArgs;
import erika.app.coffee.state.SignInState;
import erika.core.redux.binding.DefaultTextWatcher;

public class SignInFragment extends BaseFragment<SignInState> {

    private EditText textUserName;
    private EditText textPassword;
    private EditText textHost;

    @Override
    public SignInState getStateFromStore(AppState appState) {
        return appState.signIn;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        textUserName = (EditText) view.findViewById(R.id.editTextUsername);
        textPassword = (EditText) view.findViewById(R.id.editTextPassword);
        textHost = (EditText) view.findViewById(R.id.editTextAddress);
        view.findViewById(R.id.buttonLogin).setOnClickListener(v -> {
            SignInState state = getState();
            dispatch(SignInActions.signIn(getActivity(), state.userName, state.password, state.host));
        });
        textUserName.addTextChangedListener(new DefaultTextWatcher() {

            @Override
            protected void onTextChanged(String value) {
                dispatch(new SetUserNameArgs(value));
            }
        });
        textPassword.addTextChangedListener(new DefaultTextWatcher() {

            @Override
            protected void onTextChanged(String value) {
                dispatch(new SetPasswordArgs(value));
            }
        });
        textHost.addTextChangedListener(new DefaultTextWatcher() {
            @Override
            protected void onTextChanged(String value) {
                dispatch(new SetHostArgs(value));
            }
        });
        return view;
    }

    @Override
    public void bindStateToView(SignInState state) {
        super.bindStateToView(state);
        if (!TextUtils.equals(textUserName.getText(), state.userName)) {
            textUserName.setText(state.userName);
        }
        if (!TextUtils.equals(textPassword.getText(), state.password)) {
            textPassword.setText(state.password);
        }
        if (!TextUtils.equals(textHost.getText(), state.host)) {
            textHost.setText(state.host);
        }
    }
}
