package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import erika.app.coffee.R;
import erika.app.coffee.action.MessageActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.model.Message;
import erika.app.coffee.presentation.ViewBinder;
import erika.app.coffee.state.MessageListState;

public class MessageFragment extends BaseListFragment<MessageListState, Message> {

    @Override
    public MessageListState getStateFromStore(AppState appState) {
        return appState.messageList;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected ViewBinder<Message> createViewBinder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new ItemBinder(inflater, parent);
    }

    private void removeMessage(Message item) {
        dispatch(MessageActions.removeMessage(item.id));
    }

    private class ItemBinder extends ViewBinder<Message> {

        private final TextView textMessage;
        private final View loadingIndicator;
        private final View checkedIndicator;
        private final View failedIndicator;


        public ItemBinder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_message, parent, false));
            textMessage = ((TextView) itemView.findViewById(R.id.textMessage));
            loadingIndicator = itemView.findViewById(R.id.loadingIndicator);
            checkedIndicator = itemView.findViewById(R.id.checkedIndicator);
            failedIndicator = itemView.findViewById(R.id.failedIndicator);
            itemView.findViewById(R.id.buttonClose).setOnClickListener(v -> {
                removeMessage(getItem());
            });
        }

        @Override
        public void bind() {
            Message item = getItem();
            textMessage.setText(item.message);
            switch (item.status) {
                case NONE:
                    loadingIndicator.setVisibility(View.GONE);
                    failedIndicator.setVisibility(View.GONE);
                    checkedIndicator.setVisibility(View.VISIBLE);
                    break;
                case FAILED:
                    loadingIndicator.setVisibility(View.GONE);
                    failedIndicator.setVisibility(View.VISIBLE);
                    checkedIndicator.setVisibility(View.GONE);
                    break;
                case LOADING:
                    loadingIndicator.setVisibility(View.VISIBLE);
                    failedIndicator.setVisibility(View.GONE);
                    checkedIndicator.setVisibility(View.GONE);
                    break;

            }
        }
    }
}
