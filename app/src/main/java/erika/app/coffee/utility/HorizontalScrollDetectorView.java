package erika.app.coffee.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class HorizontalScrollDetectorView extends FrameLayout {
    public interface OnScrollListener {
        boolean onAcceptTouch(float x, float y);

        void onScroll(float distanceX, float distanceY);
    }

    private OnScrollListener onScrollListener;
    private boolean sequenceAccepted = false;
    private boolean scrollInitialized = false;
    private float x;
    private float y;
    public HorizontalScrollDetectorView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HorizontalScrollDetectorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalScrollDetectorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public HorizontalScrollDetectorView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (sequenceAccepted || ev.getAction() == MotionEvent.ACTION_DOWN) {
            sequenceAccepted = onDetectScroll(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    private boolean onDetectScroll(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                scrollInitialized = false;
                return onScrollListener != null && onScrollListener.onAcceptTouch(x, y);
            case MotionEvent.ACTION_MOVE:
                float distanceX = event.getX() - x;
                float distanceY = event.getY() - y;
                x = event.getX();
                y = event.getY();
                if (!scrollInitialized && Math.abs(distanceY) > 10) {
                    return false;
                }
                scrollInitialized = true;
                if (onScrollListener != null) {
                    onScrollListener.onScroll(distanceX, distanceY);
                }
                return true;
            default:
                return false;
        }
    }
}
