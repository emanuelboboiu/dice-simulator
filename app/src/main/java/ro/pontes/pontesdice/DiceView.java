package ro.pontes.pontesdice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/** A sharp, scalable die that stays readable at every screen density. */
public class DiceView extends View {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int value;

    public DiceView(Context context, int value) {
        super(context);
        this.value = value;
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float size = Math.min(getWidth(), getHeight()) - 12 * getResources().getDisplayMetrics().density;
        float left = (getWidth() - size) / 2;
        float top = (getHeight() - size) / 2;
        float radius = size * 0.2f;
        paint.setColor(Color.WHITE);
        paint.setShadowLayer(size * 0.07f, 0, size * 0.04f, 0x33000000);
        canvas.drawRoundRect(left, top, left + size, top + size, radius, radius, paint);
        paint.clearShadowLayer();
        paint.setColor(getResources().getColor(R.color.pip));
        float near = size * 0.25f;
        float far = size * 0.75f;
        float mid = size * 0.5f;
        float pip = size * 0.075f;
        if (value == 1 || value == 3 || value == 5) dot(canvas, left + mid, top + mid, pip);
        if (value >= 2) {
            dot(canvas, left + near, top + near, pip);
            dot(canvas, left + far, top + far, pip);
        }
        if (value >= 4) {
            dot(canvas, left + far, top + near, pip);
            dot(canvas, left + near, top + far, pip);
        }
        if (value == 6) {
            dot(canvas, left + near, top + mid, pip);
            dot(canvas, left + far, top + mid, pip);
        }
    }

    private void dot(Canvas canvas, float x, float y, float radius) {
        canvas.drawCircle(x, y, radius, paint);
    }
}
