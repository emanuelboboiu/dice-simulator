package ro.pontes.pontesdice;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {

    /*
     * The gForce that is necessary to register as shake. Must be greater than
     * 1G (one earth gravity unit). You can install "G-Force", by Blake La
     * Pierre from the Google Play Store and run it to see how many G's it takes
     * to register a shake
     */
    private static final float SENSITIVE_THRESHOLD_GRAVITY = 1.6F;
    private static final float MEDIUM_THRESHOLD_GRAVITY = 1.8F;
    private static final float STRONG_THRESHOLD_GRAVITY = 2.2F;
    private static final int SHAKE_SLOP_TIME_MS = 700;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 15000;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;
    private boolean mWasAboveThreshold;
    private float shakeThresholdGravity = MEDIUM_THRESHOLD_GRAVITY;

    public void setShakeStrength(int shakeStrength) {
        if (shakeStrength == 0) shakeThresholdGravity = SENSITIVE_THRESHOLD_GRAVITY;
        else if (shakeStrength == 2) shakeThresholdGravity = STRONG_THRESHOLD_GRAVITY;
        else shakeThresholdGravity = MEDIUM_THRESHOLD_GRAVITY;
        mWasAboveThreshold = false;
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        boolean onShake(int count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            boolean isAboveThreshold = gForce > shakeThresholdGravity;
            if (isAboveThreshold && !mWasAboveThreshold) {
                final long now = System.currentTimeMillis();
                // Ignore separate shake impulses that arrive too close together.
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    mWasAboveThreshold = true;
                    return;
                }

                // Reset the shake count after a longer period without an accepted shake.
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                int nextShakeCount = mShakeCount + 1;
                if (mListener.onShake(nextShakeCount)) {
                    mShakeTimestamp = now;
                    mShakeCount = nextShakeCount;
                }
            }
            mWasAboveThreshold = isAboveThreshold;
        }
    }
}
