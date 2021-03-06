package erika.app.coffee.action;

import erika.app.coffee.model.LoadState;
import erika.app.coffee.model.args.AddMessageArgs;
import erika.app.coffee.model.args.ChangeMessageStatusArgs;
import erika.app.coffee.model.args.RemoveMessageArgs;
import erika.core.redux.Action;
import erika.core.redux.DispatchAction;

public class MessageActions {
    private static int messageId;

    public static int obtainMessageId() {
        return messageId++;
    }

    public static Action addMessage(String message, int messageId, LoadState status) {
        return new AddMessageArgs(message, messageId, status);
    }

    public static Action removeMessage(int messageId) {
        return new RemoveMessageArgs(messageId);
    }

    public static DispatchAction changeMessageStatus(int messageId, LoadState status) {
        return dispatcher -> {
            switch (status) {
                case NONE:
                    dispatcher.dispatchDelayed(removeMessage(messageId), 3000);
                    break;
            }
            dispatcher.dispatch(new ChangeMessageStatusArgs(messageId, status));
        };
    }
}
