package erika.app.coffee.reducer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import erika.app.coffee.application.ActionType;
import erika.app.coffee.model.args.AddMessageArgs;
import erika.app.coffee.model.args.ChangeMessageStatusArgs;
import erika.app.coffee.model.args.RemoveMessageArgs;
import erika.app.coffee.state.MessageListState;
import erika.core.redux.Action;
import erika.core.redux.Reducer;
import erika.core.redux.Redux;

public class MessageListReducer implements Reducer<MessageListState> {
    @NonNull
    @Override
    public MessageListState reduce(@Nullable MessageListState state, Action action) {
        if (state == null) {
            state = new MessageListState();
        }
        switch (action.getType()) {
            case ActionType.ADD_MESSAGE:
                return addMessage(state, (AddMessageArgs) action);
            case ActionType.REMOVE_MESSAGE:
                return removeMessage(state, (RemoveMessageArgs) action);
            case ActionType.CHANGE_MESSAGE_STATUS:
                return changeMessageStatus(state, (ChangeMessageStatusArgs) action);
            default:
                return state;
        }
    }

    private MessageListState addMessage(MessageListState state, AddMessageArgs action) {
        return Redux.copy(state, x -> {
            x.items = Redux.add(x.items, action.message);
        });
    }

    private MessageListState removeMessage(MessageListState state, RemoveMessageArgs action) {
        return Redux.copy(state, x -> {
            x.items = Redux.remove(x.items, e -> e.id == action.messageId);
        });
    }

    private MessageListState changeMessageStatus(MessageListState state, ChangeMessageStatusArgs action) {
        return Redux.copy(state, x -> {
            x.items = Redux.map(x.items, message -> {
               if (message.id == action.messageId) {
                   return Redux.copy(message, e -> {
                       e.status = action.status;
                   });
               } else {
                   return message;
               }
            });
        });
    }
}
