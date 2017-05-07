package erika.app.coffee.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class LayoutInflaterWrapper extends LayoutInflater {
    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app."
    };
    private static Typeface regularTypeface = null;
    private static Typeface boldTypeface = null;

    public LayoutInflaterWrapper(Context context) {
        super(context);
    }

    protected LayoutInflaterWrapper(LayoutInflater original, Context newContext) {
        super(original, newContext);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new LayoutInflaterWrapper(newContext);
    }

    private static Typeface getRegularTypeface(Context context) {
        if (regularTypeface == null) {
            regularTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        }
        return regularTypeface;
    }

    private static Typeface getBoldTypeface(Context context) {
        if (boldTypeface == null) {
            boldTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        }
        return boldTypeface;
    }


    @Override
    protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        View view = null;
        for (String prefix : sClassPrefixList) {
            try {
                view = createView(name, prefix, attrs);
                if (view != null) {
                    break;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (view == null) {
            view = super.onCreateView(name, attrs);
        }
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            boolean bold = textView.getTypeface().getStyle() == Typeface.BOLD;
            textView.setTypeface(bold ? getBoldTypeface(getContext()) : getRegularTypeface(getContext()));
        }
        return view;
    }
}
