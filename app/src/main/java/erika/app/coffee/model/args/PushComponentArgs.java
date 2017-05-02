package erika.app.coffee.model.args;

import android.app.Fragment;
import android.os.Bundle;

import erika.app.coffee.application.ActionType;

public class PushComponentArgs extends Args {
    public final Class<? extends Fragment> component;

    public PushComponentArgs(Class<? extends Fragment> component) {
        super(ActionType.PUSH);
        this.component = component;
    }
}
