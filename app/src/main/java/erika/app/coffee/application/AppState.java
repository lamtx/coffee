package erika.app.coffee.application;


import erika.app.coffee.state.LoadingDialogState;
import erika.app.coffee.state.MainState;
import erika.app.coffee.state.MessageBoxState;
import erika.app.coffee.state.OrderState;
import erika.app.coffee.state.SignInState;
import erika.app.coffee.state.TableListState;

public class AppState {
    public MainState main;
    public SignInState signIn;
    public LoadingDialogState loadingDialog;
    public TableListState tableList;
    public MessageBoxState messageBox;
    public OrderState order;
}
