package erika.app.coffee.state;

import android.app.Fragment;

public class PopupState implements Cloneable {
    public Class<? extends Fragment> popupContainer = null;
    public boolean cancellable = true;
}
