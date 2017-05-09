package erika.app.coffee.action;

import erika.app.coffee.model.Message;
import erika.app.coffee.model.args.AddMessageArgs;
import erika.app.coffee.model.args.ChangeMessageStatusArgs;
import erika.app.coffee.model.args.RemoveMessageArgs;
import erika.core.redux.Action;

public class MessageActions {
    private static int messageId;

    public static int obtainMessageId() {
        return messageId++;
    }

    public static Action addMessage(String message, int messageId, Message.Status status) {
        return new AddMessageArgs(message, messageId, status);
    }

    public static Action removeMessage(int messageId) {
        return new RemoveMessageArgs(messageId);
    }

    public static Action changeMessageStatus(int messageId, Message.Status status) {
        return new ChangeMessageStatusArgs(messageId, status);
    }
}