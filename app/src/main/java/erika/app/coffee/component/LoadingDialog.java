package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import erika.app.coffee.R;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.state.LoadingDialogState;

public class LoadingDialog extends BaseFragment<LoadingDialogState> {

    private TextView textMessage;

    @Override
    public LoadingDialogState getStateFromStore(AppState appState) {
        return appState.loadingDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading_dialog, container, false);
        textMessage = ((TextView) view.findViewById(R.id.textMessage));
        return view;
    }

    @Override
    public void bindStateToView(LoadingDialogState state) {
        super.bindStateToView(state);
        textMessage.setText(state.message);
    }
}
