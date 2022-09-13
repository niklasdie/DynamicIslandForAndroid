package niklasdie.dynamicislandforandroid;

import static android.content.Context.WINDOW_SERVICE;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class DynamicIslandView extends View {

    private Paint phPaint;
    private int phRadius;
    private int[] phPos;
    private int pillLength;
    private int phTextHeight;
    private boolean isPill;
    private boolean showText;
    private String leftText;
    private String rightText;

    private final View mView;
    private final LayoutInflater layoutInflater;
    private WindowManager.LayoutParams mParams;
    private WindowManager windowManager;
    private WindowMetrics windowMetrics;

    public DynamicIslandView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.DynamicIsland,
                0, 0);

        try {
            phTextHeight = a.getInteger(R.styleable.DynamicIsland_textHeight, 10);
            showText = a.getBoolean(R.styleable.DynamicIsland_showText, false);
            phRadius = a.getInteger(R.styleable.DynamicIsland_holeRadius, 10);
            pillLength = a.getInteger(R.styleable.DynamicIsland_pillLength, 0);
            isPill = a.getBoolean(R.styleable.DynamicIsland_isPill, false);
            leftText = a.getString(R.styleable.DynamicIsland_leftText);
            rightText = a.getString(R.styleable.DynamicIsland_rightText);
        } finally {
            a.recycle();
        }
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.dynamic_island_window, null);
        mParams.gravity = Gravity.CENTER;
        this.init(context);
    }

    private void init(Context context) {
        windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowMetrics = windowManager.getCurrentWindowMetrics();

        phPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        phPaint.setColor(Color.BLACK);
        phPaint.setStyle(Paint.Style.FILL);
        phRadius = 20;
        phTextHeight = 15;
        phPos = new int[2];
//        phPos[0] = Math.abs((int) (windowMetrics.getBounds().width() / 2));
        phPos[0] = 720;
        phPos[1] = 15;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(phPos[0], phPos[1], phRadius, phPaint);
    }

    public void open() {
        try {
            // check if the view is already
            // inflated or present in the window
            if (mView.getWindowToken() == null) {
                if (mView.getParent() == null) {
                    windowManager.addView(mView, mParams);
                }
            }
        } catch (Exception e) {
            Log.d("Error1", e.toString());
        }
    }

    // get get available size of space
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        // Account for the label
        if (showText) xpad += 10; // TODO

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        // Figure out how big we can make the pie.
        float diameter = Math.min(ww, hh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) - this.getMaxTextWidth() + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(minh, heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    private int getMaxTextWidth() {
        int width = 0;
        String allText = leftText + rightText;
        for (int i = 0; i < allText.length(); i++) {
            width += phTextHeight;
        }
        return width;
    }
}
