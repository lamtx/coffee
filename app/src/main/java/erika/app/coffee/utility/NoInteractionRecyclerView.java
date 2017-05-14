package erika.app.coffee.utility;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NoInteractionRecyclerView extends RecyclerView {
    public NoInteractionRecyclerView(Context context) {
        super(context);
    }

    public NoInteractionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoInteractionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
