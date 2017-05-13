package erika.app.coffee.application;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.reducer.LoadingDialogReducer;
import erika.app.coffee.reducer.MainReducer;
import erika.app.coffee.reducer.MenuReducer;
import erika.app.coffee.reducer.MessageBoxReducer;
import erika.app.coffee.reducer.MessageListReducer;
import erika.app.coffee.reducer.NumberReducer;
import erika.app.coffee.reducer.OrderReducer;
import erika.app.coffee.reducer.OrderedListReducer;
import erika.app.coffee.reducer.SignInReducer;
import erika.app.coffee.reducer.TableListReducer;
import erika.core.redux.Action;
import erika.core.redux.Reducer;

public class AppReducer implements Reducer<AppState> {
    private final MainReducer main = new MainReducer();
    private final LoadingDialogReducer loadingDialog = new LoadingDialogReducer();
    private final SignInReducer signIn = new SignInReducer();
    private final TableListReducer tableList = new TableListReducer();
    private final MessageBoxReducer messageBox = new MessageBoxReducer();
    private final OrderReducer order = new OrderReducer();
    private final MenuReducer menu = new MenuReducer();
    private final OrderedListReducer orderedList = new OrderedListReducer();
    private final NumberReducer number = new NumberReducer();
    private final MessageListReducer messageList = new MessageListReducer();

    @NonNull
    @Override
    public AppState reduce(@Nullable AppState state, Action action) {
        if (state == null || ActionType.SIGN_OUT.equals(action.getType())) {
            // Reset state
            state = new AppState();
        }
        AppState appState = new AppState();

        appState.main = main.reduce(state.main, action);
        appState.loadingDialog = loadingDialog.reduce(state.loadingDialog, action);
        appState.signIn = signIn.reduce(state.signIn, action);
        appState.tableList = tableList.reduce(state.tableList, action);
        appState.messageBox = messageBox.reduce(state.messageBox, action);
        appState.order = order.reduce(state.order, action);
        appState.orderedList = orderedList.reduce(state.orderedList, action);
        appState.menu = menu.reduce(state.menu, action);
        appState.number = number.reduce(state.number, action);
        appState.messageList = messageList.reduce(state.messageList, action);

        return appState;
    }
}
