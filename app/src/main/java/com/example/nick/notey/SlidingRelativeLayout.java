package com.example.nick.notey;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by nick on 04/11/14.
 */

public class SlidingRelativeLayout extends RelativeLayout {

    public SlidingRelativeLayout(Context context) {
        super(context);
    }

    public SlidingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setYFraction(final float fraction) {
        float translationY = getHeight() * fraction;
        setTranslationY(translationY);
    }

    public float getYFraction() {
        if (getHeight() == 0) {
            return 0;
        }
        return getTranslationY() / getHeight();
    }
}
