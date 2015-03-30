package spring15.ec551.fpgacontroller.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import spring15.ec551.fpgacontroller.resources.UserConfigurationObject;

/**
 * Created by davidkim on 3/26/15.
 * The controller object that will be used throughout the application.  This class
 * implements the accelerometer, processing its information with the the high pass filter.
 * It will output base, filtered, and net values.  In addition, it will output angle degrees
 * using a precision value.  It will also be able to save and load configurations.
 */
public class ControllerObject implements SensorEventListener {
    final int X = 0;
    final int Y = 1;
    final int Z = 2;

    // Sensor Objects
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    /** The filter used to remove the pesky noise */
    // TODO allow users to change this?
    private AccelerometerHighPassFilter mFilter;
    final float KFACTOR_LIMIT = 1.0f;
    final float KFACTOR_INCREMENT = 0.05f;
    public ControllerInterfaceListener mInterface;


    /** The filtered accelerometer values from AccelerometerHighPassFilter */
    float mBaseValues[];
    float mFilterValues[];
    float mNetValues[];

    final float ZERO_FLOAT = 0.0f;

    /** Used to calculate the angle **/
    float mAnglePrecision;
    int mLeftBound;               // Uses integer for precision purposes
    int mRightBound;              // Uses integer for precision purposes
    final float ANGLE_SENSITIVITY_HIGH_LIMIT = 10.0f;
    final float ANGLE_INCREMENT = 0.5f;
    final float MAX_ANGLE = 360.0f;

    /** Implements default values */
    public ControllerObject(Context context, ControllerInterfaceListener interfacer) {
       this(context,interfacer, null);
    }

    public ControllerObject(Context context, ControllerInterfaceListener interfacer,
                            UserConfigurationObject object) {
        mInterface = interfacer;
        mBaseValues = new float[]{0.0f, 0.0f, 0.0f};
        mFilterValues = new float[]{0.0f, 0.0f, 0.0f};

        /* Either initialize default values or user config values */
        if (object == null) {
            mNetValues = new float[]{0.0f, 0.0f, 0.0f};
            mLeftBound = 5;
            mRightBound = 5;
            mAnglePrecision = 1.0f;
            mFilter = new AccelerometerHighPassFilter(0.65f);
        } else {
            mNetValues = object.getNetValues();
            mLeftBound = object.getLeftBound();
            mRightBound = object.getRightBound();
            mAnglePrecision = object.getAngleSensitivty();
            mFilter = new AccelerometerHighPassFilter(object.getKFactor());
        }

        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        registerSensor();

    }

    public float getFilterValue() {
        return mFilter.kFilteringFactor;
    }

    public void increaseFilterValue() {
        if (mFilter.kFilteringFactor < KFACTOR_LIMIT) {
            if (mFilter.kFilteringFactor > KFACTOR_LIMIT - KFACTOR_INCREMENT) {
                mFilter.kFilteringFactor = KFACTOR_LIMIT;
            } else {
                mFilter.kFilteringFactor += KFACTOR_INCREMENT;
            }
        }
    }

    public void decreaseFilterValue() {
        if (mFilter.kFilteringFactor > ZERO_FLOAT) {
            if (mFilter.kFilteringFactor < ZERO_FLOAT + KFACTOR_INCREMENT) {
                mFilter.kFilteringFactor = ZERO_FLOAT;
            } else {
                mFilter.kFilteringFactor -= KFACTOR_INCREMENT;
            }
        }
    }

    public float getAnglePrecision() {
        return mAnglePrecision;
    }

    public void setAnglePrecision(float precision) {
        mAnglePrecision = precision;
    }

    public void increaseAngleSensitivity() {
        if (mAnglePrecision < ANGLE_SENSITIVITY_HIGH_LIMIT) {
            if (mAnglePrecision > ANGLE_SENSITIVITY_HIGH_LIMIT - ANGLE_INCREMENT) {
                mAnglePrecision = ANGLE_SENSITIVITY_HIGH_LIMIT;
            } else {
                mAnglePrecision += ANGLE_INCREMENT;
            }
        }
    }

    public void decreaseAngleSensitivity() {
        if (mAnglePrecision > ZERO_FLOAT) {
            if (mAnglePrecision < ZERO_FLOAT + ANGLE_INCREMENT) {
                mAnglePrecision = ZERO_FLOAT;
            } else {
                mAnglePrecision -= ANGLE_INCREMENT;
            }
        }
    }

    public void resetNetValues() {
        mNetValues[X] = ZERO_FLOAT;
        mNetValues[Y] = ZERO_FLOAT;
        mNetValues[Z] = ZERO_FLOAT;
    }

    public void registerSensor() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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

        mNetValues[X] += mFilterValues[X];
        mNetValues[Y] += mFilterValues[Y];
        mNetValues[Z] += mFilterValues[Z];

        mInterface.onBaseChangedListener(mBaseValues);
        mInterface.onFilterChangedListener(mFilterValues, mNetValues);

        if (mLeftBound!= 0 && mRightBound !=0) {
            calculateAngle();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ACCURACYCHANGED: ", "Integer: " + accuracy);
    }

    public void setLeftBound(int[] leftBound) {
        this.mLeftBound = Math.abs(leftBound[0]) + Math.abs(leftBound[1]);
    }

    public void setRightRound(int[] rightRound) {
        this.mRightBound = Math.abs(rightRound[0]) + Math.abs(rightRound[1]);
    }

    /** Calculates the angle value */
    public void calculateAngle() {
        float xySum = (Math.abs(mNetValues[X]) + Math.abs(mNetValues[Y])) / 2;

        if (mNetValues[Y] < 0) {
            int rawAngle = (int)((xySum/ (float)mLeftBound) * 90);
            if (rawAngle > 90) {
                mInterface.onAngleChangeListener(-90);
            } else {
                mInterface.onAngleChangeListener(-1*rawAngle);
            }
        } else {
            int rawAngle = (int)((xySum/ (float)mRightBound) * 90);
            if (rawAngle > 90) {
                mInterface.onAngleChangeListener(90);
            } else {
                mInterface.onAngleChangeListener(rawAngle);
            }
        }
    }

}

