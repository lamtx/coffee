package erika.app.coffee.state;

import android.content.DialogInterface;
import android.support.annotation.StringRes;

import erika.core.redux.Action;

public class MessageBoxState implements Cloneable {
    public interface Action {
        void onClick();
    }

    public static class Button {
        public Action action;
        public CharSequence title;
        @StringRes
        public int titleResId;

        public Button(CharSequence title, Action action) {
            this.action = action;
            this.title = title;
        }

        public Button(@StringRes int title, Action action) {
            this.action = action;
            this.titleResId = title;
        }
    }

    public CharSequence message;
    public CharSequence title;
    @StringRes
    public int messageResId;
    @StringRes
    public int titleResId;

    public Button positiveButton;
    public Button negativeButton;
    public Button neutralButton;

    public boolean cancellable = true;

    public int defaultButton = DialogInterface.BUTTON_NEGATIVE;
}
