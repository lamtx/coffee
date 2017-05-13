package erika.core.redux.binding;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class DefaultTextWatcher implements TextWatcher {
    private String previous = null;
    @Override
    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public final void afterTextChanged(Editable s) {
        String value = s.toString();
        if (!value.equals(previous)) {
            previous = value;
            onTextChanged(value);
        }
    }

    protected  abstract void onTextChanged(String value);
}
