package erika.app.coffee.action;

import android.support.annotation.StringRes;

import erika.app.coffee.component.MessageBox;
import erika.app.coffee.model.args.SetMessageBoxArgs;
import erika.app.coffee.model.args.ShowPopupArgs;
import erika.app.coffee.state.MessageBoxState;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class MessageBoxActions {

    public static class Builder {
        private MessageBoxState state = new MessageBoxState();

        public Builder message(CharSequence message) {
            state.messageResId = 0;
            state.message = message;
            return this;
        }

        public Builder title(CharSequence title) {
            state.titleResId = 0;
            state.title = title;
            return this;
        }

        public Builder message(@StringRes int message) {
            state.messageResId = message;
            state.message = null;
            return this;
        }

        public Builder title(@StringRes int title) {
            state.titleResId = title;
            state.title = null;
            return this;
        }

        public Builder positive(CharSequence title, MessageBoxState.Action action) {
            state.positiveButton = new MessageBoxState.Button(title, action);
            return this;
        }

        public Builder negative(CharSequence title, MessageBoxState.Action action) {
            state.negativeButton = new MessageBoxState.Button(title, action);
            return this;
        }

        public Builder neutral(CharSequence title, MessageBoxState.Action action) {
            state.neutralButton = new MessageBoxState.Button(title, action);
            return this;
        }

        public Builder positive(@StringRes int title, MessageBoxState.Action action) {
            state.positiveButton = new MessageBoxState.Button(title, action);
            return this;
        }

        public Builder negative(@StringRes int title, MessageBoxState.Action action) {
            state.negativeButton = new MessageBoxState.Button(title, action);
            return this;
        }

        public Builder neutral(@StringRes int title, MessageBoxState.Action action) {
            state.neutralButton = new MessageBoxState.Button(title, action);
            return this;
        }

        public Action build() {
            return new SetMessageBoxArgs(state);
        }
    }

    public static DispatchAction show(String message) {
        return show(message, null);
    }

    public static DispatchAction show(String message, String title) {
        return show(message, title, null);
    }

    public static DispatchAction show(String message, String title, MessageBoxState.Action okAction) {
        return dispatcher -> {
            Action action = new Builder().message(message).title(title).positive(android.R.string.ok, okAction).build();
            dispatcher.dispatch(action);
            dispatcher.dispatch(show());
        };
    }

    public static Action show() {
        return new ShowPopupArgs(MessageBox.class);
    }
}
