package erika.app.coffee.action;

import android.text.TextUtils;

import erika.app.coffee.component.NumberFragment;
import erika.app.coffee.model.args.SetNumberActionArgs;
import erika.app.coffee.model.args.SetNumberStateArgs;
import erika.app.coffee.model.args.SetNumberValueArgs;
import erika.app.coffee.model.args.ShowPopupArgs;
import erika.app.coffee.state.NumberState;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class NumberActions {

    public static DispatchAction addValue(String current, boolean decreaseMode, char tag, NumberState.Action action) {
        return dispatcher -> {
            int value;
            if (tag == '+') {
                value = decreaseMode ? -10 : 10;
            } else {
                value = Integer.parseInt(String.valueOf(tag));
                if (decreaseMode) {
                    value = -value;
                }
            }
            int newValue = Integer.parseInt(current) + value;
            dispatcher.dispatch(setNumberValue(String.valueOf(newValue)));
            if (tag != '+') {
                if (action != null) {
                    action.apply(newValue);
                }
                dispatcher.dispatch(PopupActions.dismiss());
            }
        };
    }

    public static DispatchAction appendChar(String current, boolean decreaseMode, char tag) {
        return dispatcher -> {
            if ('0' <= tag && tag <= '9') {
                if (TextUtils.isEmpty(current) || "0".equals(current)) {
                    if (tag != '0') {
                        dispatcher.dispatch(setNumberValue((decreaseMode ? "-" : "") + String.valueOf(tag)));
                    }
                } else {
                    dispatcher.dispatch(setNumberValue(current + tag));
                }
            } else if (tag == '.') {
                if (TextUtils.isEmpty(current) || "0".equals(current)) {
                    dispatcher.dispatch(setNumberValue(decreaseMode ? "-0." : "0."));
                } else if (!current.contains(".")) {
                    dispatcher.dispatch(setNumberValue(current + tag));
                }
            }
        };
    }

    public static DispatchAction removeChar(String current) {
        return dispatcher -> {
            if (TextUtils.isEmpty(current) || current.length() == 1 || (current.length() == 2 && current.charAt(0) == '-') || "-0.".equals(current)) {
                dispatcher.dispatch(setNumberValue("0"));
            } else {
                dispatcher.dispatch(setNumberValue(current.substring(0, current.length() - 1)));
            }
        };
    }

    public static DispatchAction show(String title, boolean decreaseMode, NumberState.Action action) {
        return dispatcher -> {
            dispatcher.dispatch(setNumberState(title, decreaseMode));
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

    public static Action setNumberState(String title, boolean decreaseMode) {
        return new SetNumberStateArgs(title, decreaseMode);
    }
}
