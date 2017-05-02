package erika.app.coffee.application;


import erika.app.coffee.state.HomeState;
import erika.app.coffee.state.LoadingDialogState;
import erika.app.coffee.state.MainState;
import erika.app.coffee.state.MessageBoxState;
import erika.app.coffee.state.OrderState;
import erika.app.coffee.state.PopupState;
import erika.app.coffee.state.SignInState;

public class AppState {
    public MainState main;
    public PopupState popup;
    public SignInState signIn;
    public LoadingDialogState loadingDialog;
    public HomeState home;
    public MessageBoxState messageBox;
    public OrderState order;
}
