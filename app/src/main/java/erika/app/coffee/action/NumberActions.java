package erika.app.coffee.action;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import erika.app.coffee.component.NumberFragment;
import erika.app.coffee.model.InputNumberMode;
import erika.app.coffee.model.args.SetNumberActionArgs;
import erika.app.coffee.model.args.SetNumberValueArgs;
import erika.app.coffee.model.args.ShowPopupArgs;
import erika.app.coffee.service.communication.MenuItem;
import erika.app.coffee.state.NumberState;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class NumberActions {

    public static DispatchAction addValue(String current, char tag) {
        return dispatcher -> {
            int value;
            if (tag == '+') {
                value = 10;
            } else {
                value = Integer.parseInt(String.valueOf(tag));
            }
            dispatcher.dispatch(setNumberValue(String.valueOf(Integer.parseInt(current) + value)));
        };
    }

    public static DispatchAction appendChar(String current, char tag) {
        return dispatcher -> {
            if ('0' <= tag && tag <= '9') {
                if (TextUtils.isEmpty(current) || "0".equals(current)) {
                    dispatcher.dispatch(setNumberValue(String.valueOf(tag)));
                } else {
                    dispatcher.dispatch(setNumberValue(current + tag));
                }
            } else if (tag == '.') {
                if (TextUtils.isEmpty(current) || "0".equals(current)) {
                    dispatcher.dispatch(setNumberValue("0."));
                } else if (!current.contains(".")) {
                    dispatcher.dispatch(setNumberValue(current + tag));
                }
            }
        };
    }

    public static DispatchAction removeChar(String current) {
        return dispatcher -> {
            if (TextUtils.isEmpty(current) || current.length() == 1) {
                dispatcher.dispatch(setNumberValue("0"));
            } else {
                dispatcher.dispatch(setNumberValue(current.substring(0, current.length() - 1)));
            }
        };
    }

    public static DispatchAction show(NumberState.Action action) {
        return dispatcher -> {
            dispatcher.dispatch(setNumberAction(action));
            dispatcher.dispatch(setNumberValue("0"));
            dispatcher.dispatch(new ShowPopupArgs(NumberFragment.class));
        };
    }

    public static Action removeAll() {
        return setNumberValue("0");
    }

    private static Action setNumberValue(String value) {
        return new SetNumberValueArgs(value);
    }

    public static Action setNumberAction(NumberState.Action action) {
        return new SetNumberActionArgs(action);
    }
}
