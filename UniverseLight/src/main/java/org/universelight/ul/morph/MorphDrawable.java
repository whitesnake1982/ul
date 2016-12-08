package org.universelight.ul.morph;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.Property;

/**
 * Created by hsinheng on 16/7/18.
 */
public class MorphDrawable extends Drawable {

    private Paint paint;
    private float cornerRadius;

    public static final Property<MorphDrawable, Float> CORNER_RADIUS = new Property<MorphDrawable, Float>(Float.class, "cornerRadius") {

        @Override
        public void set(MorphDrawable morphDrawable, Float value) {
            morphDrawable.setCornerRadius(value);
        }

        @Override
        public Float get(MorphDrawable morphDrawable) {
            return morphDrawable.getCornerRadius();
        }
    };

    public static final Property<MorphDrawable, Integer> COLOR = new Property<MorphDrawable, Integer>(Integer.class, "color") {

        @Override
        public void set(MorphDrawable morphDrawable, Integer value) {
            morphDrawable.setColor(value);
        }

        @Override
        public Integer get(MorphDrawable morphDrawable) {
            return morphDrawable.getColor();
        }
    };

    public MorphDrawable(@ColorInt int color, float cornerRadius) {
        this.cornerRadius = cornerRadius;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidateSelf();
    }

    public int getColor() {
        return paint.getColor();
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(getBounds().left, getBounds().top, getBounds().right, getBounds()
                .bottom, cornerRadius, cornerRadius, paint);//hujiawei
    }

    @Override
    public void getOutline(Outline outline) {
        outline.setRoundRect(getBounds(), cornerRadius);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return paint.getAlpha();
    }

}
