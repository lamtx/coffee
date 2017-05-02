package erika.app.coffee.model.args;

import android.app.Fragment;

import erika.app.coffee.application.ActionType;

public class ShowPopupArgs extends Args {
    public Class<? extends Fragment> popupContainer;

    public ShowPopupArgs(Class<? extends Fragment> popupContainer) {
        super(ActionType.SHOW_POPUP);
        this.popupContainer = popupContainer;
    }
}
