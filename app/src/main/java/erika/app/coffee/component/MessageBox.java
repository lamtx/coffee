package erika.app.coffee.component;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import erika.app.coffee.R;
import erika.app.coffee.action.PopupActions;
import erika.app.coffee.application.AppState;
import erika.app.coffee.application.BaseFragment;
import erika.app.coffee.state.MessageBoxState;

public class MessageBox extends BaseFragment<MessageBoxState> {
    private TextView textMessage;
    private TextView textTitle;
    private TextView buttonPositive;
    private TextView buttonNegative;
    private TextView buttonNeutral;

    @Override
    public MessageBoxState getStateFromStore(AppState appState) {
        return appState.messageBox;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_box, container, false);
        textMessage = ((TextView) view.findViewById(android.R.id.text1));
        textTitle = ((TextView) view.findViewById(android.R.id.title));
        buttonPositive = ((TextView) view.findViewById(android.R.id.button1));
        buttonNegative = ((TextView) view.findViewById(android.R.id.button2));
        buttonNeutral = ((TextView) view.findViewById(android.R.id.button3));
        return view;
    }

    @Override
    public void bindStateToView(MessageBoxState state) {
        super.bindStateToView(state);
        bindText(textMessage, state.message, state.messageResId);
        bindText(textTitle, state.title, state.titleResId);
        bindButton(buttonPositive, state.positiveButton);
        bindButton(buttonNegative, state.negativeButton);
        bindButton(buttonNeutral, state.neutralButton);
    }

    private static void bindText(TextView textView, CharSequence title, int titleResId) {
        if (TextUtils.isEmpty(title) && titleResId == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            if (titleResId != 0) {
                textView.setText(titleResId);
            } else {
                textView.setText(title);
            }
        }
    }

    private void bindButton(TextView textView, MessageBoxState.Button button) {
        if (button != null) {
            bindText(textView, button.title, button.titleResId);
            textView.setOnClickListener(v -> {
                dismiss();
                if (button.action != null) {
                    button.action.onClick();
                }
            });
        } else {
            textView.setVisibility(View.GONE);
            textView.setOnClickListener(null);
        }
    }

    public void dismiss() {
        dispatch(PopupActions.dismiss());
    }
}
