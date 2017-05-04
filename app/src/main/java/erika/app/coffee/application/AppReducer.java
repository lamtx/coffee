package erika.app.coffee.application;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.reducer.TableListReducer;
import erika.app.coffee.reducer.LoadingDialogReducer;
import erika.app.coffee.reducer.MainReducer;
import erika.app.coffee.reducer.MessageBoxReducer;
import erika.app.coffee.reducer.OrderReducer;
import erika.app.coffee.reducer.SignInReducer;
import erika.core.redux.Action;
import erika.core.redux.Reducer;

public class AppReducer implements Reducer<AppState> {
    private final MainReducer main = new MainReducer();
    private final LoadingDialogReducer loadingDialog = new LoadingDialogReducer();
    private final SignInReducer signIn = new SignInReducer();
    private final TableListReducer tableList = new TableListReducer();
    private final MessageBoxReducer messageBox = new MessageBoxReducer();
    private final OrderReducer order = new OrderReducer();

    @NonNull
    @Override
    public AppState reduce(@Nullable AppState state, Action action) {
        if (state == null) {
            state = new AppState();
        }
        AppState appState = new AppState();

        appState.main = main.reduce(state.main, action);
        appState.loadingDialog = loadingDialog.reduce(state.loadingDialog, action);
        appState.signIn = signIn.reduce(state.signIn, action);
        appState.tableList = tableList.reduce(state.tableList, action);
        appState.messageBox = messageBox.reduce(state.messageBox, action);
        appState.order = order.reduce(state.order, action);

        return appState;
    }
}
