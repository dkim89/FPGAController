package spring15.ec551.fpgacontroller.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by davidkim on 3/26/15.
 * The controller object that will be used throughout the application.  This class
 * implements the accelerometer, processing its information with the the high pass filter,
 * and storing configuration changes.  In addition it will translate the processed values
 * into values used to handle vehicle steering.
 */
public class ControllerObject implements SensorEventListener {
    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    /** The filter object used to remove the pesky noise */
    private AccelerometerHighPassFilter mFilter;
    public ControllerInterfaceListener mInterface;


    /** The filtered accelerometer values from AccelerometerHighPassFilter */
    float mBaseValues[] = {0.0f, 0.0f, 0.0f};
    float mFilterValues[] = {0.0f, 0.0f, 0.0f};

    final int DELAY_FACTOR = 100000;
    final int DELAY_VALUE_LIMIT = 20000;
    private int mDelayValue;

    public ControllerObject(Context context, ControllerInterfaceListener interfacer) {
        mInterface = interfacer;
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        mDelayValue = SensorManager.SENSOR_DELAY_NORMAL;
        registerSensor();
        mFilter = new AccelerometerHighPassFilter(0.5f);
    }

    public float getFilterValue() {
        return mFilter.kFilteringFactor;
    }

    public int getDelayValue() {
        return mDelayValue * DELAY_FACTOR;
    }

    public void increaseFilterValue() {
        if (mFilter.kFilteringFactor < 1.0f) {
            if (mFilter.kFilteringFactor > 0.95f) {
                mFilter.kFilteringFactor = 1.0f;
            } else {
                mFilter.kFilteringFactor += 0.05f;
            }
        }
    }

    public void decreaseFilterValue() {
        if (mFilter.kFilteringFactor > 0.0f) {
            if (mFilter.kFilteringFactor < 0.05f) {
                mFilter.kFilteringFactor = 0.0f;
            } else {
                mFilter.kFilteringFactor -= 0.05f;
            }
        }
    }

    public void increaseDelayValue() {
        if (mDelayValue < DELAY_VALUE_LIMIT) {
            mDelayValue += 1;
            unregisterSensor();
            registerSensor();
        }
    }

    public void decreaseDelayValue() {
        if (mDelayValue > 0) {
            mDelayValue -= 1;
            unregisterSensor();
            registerSensor();
        }
    }


    public void registerSensor() {
        mSensorManager.registerListener(this, mAccelerometer, mDelayValue);
    }
    public void unregisterSensor() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mBaseValues[X] = event.values[X];
        mBaseValues[Y] = event.values[Y];
        mBaseValues[Z] = event.values[Z];

        mFilterValues = mFilter.getFilteredAccelerometerValues(
                event.values[X],
                event.values[Y],
                event.values[Z]);

        mInterface.onBaseChangedListener(mBaseValues[X], mBaseValues[Y], mBaseValues[Z]);
        mInterface.onFilterChangedListener(mFilterValues[X], mFilterValues[Y], mFilterValues[Z]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ACCURACYCHANGED: ", "Integer: " + accuracy);
    }

}

