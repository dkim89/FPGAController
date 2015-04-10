package spring15.ec551.fpgacontroller.resources;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import spring15.ec551.fpgacontroller.R;

/**
 * Created by davidkim on 4/9/15.
 */
public class ThrottleSlider extends SeekBar {

    public ThrottleSlider(Context context) {
        super(context);
        initializeProgressBar();
    }

    public ThrottleSlider(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeProgressBar();

    }

    public ThrottleSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeProgressBar();
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setProgress(getMax() - (int) (getMax() * event.getY() / getHeight()));
                onSizeChanged(getWidth(), getHeight(), 0, 0);
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("ACTIONCANCEL!");
                break;
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        return true;
    }

    private void initializeProgressBar() {
        setProgressDrawable(getResources().getDrawable(R.drawable.throttle_seekbar));
        setThumb(getResources().getDrawable(R.drawable.throttle_thumb));
    }



    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        super.setOnSeekBarChangeListener(l);
    }

    public void resetPosition() {
        while (getProgress() != 100) {
            System.out.println("changing...");
            if (getProgress() < 100) {
                setProgress(getProgress()+1);
            }
            else
                setProgress(getProgress()-1);
        }
    }
}
