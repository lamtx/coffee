package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
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
    @Override
    public SignInState getStateFromStore(AppState appState) {
        return appState.signIn;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        view.findViewById(R.id.buttonLogin).setOnClickListener(v -> {
            SignInState state = getState();
            dispatch(SignInActions.signIn(getActivity(), state.userName, state.password, state.host));
        });
        ((EditText) view.findViewById(R.id.editTextUsername)).addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                dispatch(new SetUserNameArgs(s.toString()));
            }
        });
        ((EditText) view.findViewById(R.id.editTextPassword)).addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                dispatch(new SetPasswordArgs(s.toString()));
            }
        });
        ((EditText) view.findViewById(R.id.editTextAddress)).addTextChangedListener(new DefaultTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                dispatch(new SetHostArgs(s.toString()));
            }
        });
        return view;
    }
}
