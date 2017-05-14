package erika.app.coffee.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class HorizontalScrollDetectorView extends FrameLayout {
    public interface OnScrollListener {
        boolean onAcceptTouch(float x, float y);

        void onScroll(float distanceX);
    }

    private OnScrollListener onScrollListener;
    private boolean sequenceAccepted = false;
    private boolean scrollInitialized = false;
    private float x;
    private float y;
    private boolean swiping = false;

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
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                swiping = false;
                return false;
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
                if (!scrollInitialized && (Math.abs(distanceX) < Math.abs(distanceY) * 1.5 || Math.abs(distanceX) < 3)) {
                    return false;
                }
                swiping = true;
                scrollInitialized = true;
                if (onScrollListener != null) {
                    onScrollListener.onScroll(distanceX);
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return swiping;
    }
}
