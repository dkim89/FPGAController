package spring15.ec551.fpgacontroller;
import static android.util.FloatMath.sqrt;

/**
 * Created by davidkim on 3/26/15.
 * A filtering object that was implemented off of Apple's SDK implementation of Kalman filtering.
 * This algorithm will apply a cutoff for frequency and filter noises while
 * normalizing the averaged values over a continuous frequency cycle set of x,y,z values
 * received from the accelerometer.
 *
 * Implementation found at StackOverflow:
 * http://stackoverflow.com/questions/1638864/filtering-accelerometer-data-noise
 */
public class AccelerometerHighPassFilter {
    private static final boolean SET_FILTER = true;
    private static final float MAX_FLOAT = 1.0f;
    private static final float ZERO_FLOAT = 0.0f;
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;

    /** Implement two float arrays which respond to X,Y,Z coordinates in ascending order */
    float mPrevAccel[];
    float mAccel[];

    /**
     *  mUpdateFreq     The rate of readings from accelerometer
     *  mCutoffFreq     The cutoff frequency
     *  mRecurse        The recursive filter
     *  mDelta          The delta value
     *  mFilterConstant The constant generated from mRecurse and mDelta
     *  mAlpha          To smooth out the filtered output
     *  kAccelerometerMinStep
     *  kAccelerometerNoiseAttenuation
     */
    float mUpdateFreq, mCutoffFreq, mRecurse, mDelta, mFilterConstant, mAlpha,
        kAccelerometerMinStep, kAccelerometerNoiseAttenuation;

    /** Will relay the filtered values back to the ControllerVehicleInterfacer */
    private FilterListener mListener;


    public AccelerometerHighPassFilter(ControllerVehicleInterfacer interfacer) {
        setDefaultConfiguration();
        mListener = interfacer;
    }

    public AccelerometerHighPassFilter(ControllerVehicleInterfacer interfacer, UserConfigurationObject mConfigObject) {
        setConfiguration(mConfigObject);
        mListener = interfacer;
    }

    /** Will apply the filter, then send the values back to the object listening for it */
    public void onAccelerometerChanged(float accelX, float accelY, float accelZ) {
        if (SET_FILTER) {
            float d = clamp(Math.abs(norm(mAccel[X], mAccel[Y], mAccel[Z]) - norm(accelX, accelY, accelZ)) / kAccelerometerMinStep - MAX_FLOAT);
            mAlpha = d * mFilterConstant / kAccelerometerNoiseAttenuation + (MAX_FLOAT - d) * mFilterConstant;
        }

        mAccel[X] = (float) (mAlpha * (mAccel[X] + accelX - mPrevAccel[X]));
        mAccel[Y] = (float) (mAlpha * (mAccel[Y] + accelY - mPrevAccel[Y]));
        mAccel[Z] = (float) (mAlpha * (mAccel[Z] + accelZ - mPrevAccel[Z]));

        mPrevAccel[X] = accelX;
        mPrevAccel[Y] = accelY;
        mPrevAccel[Z] = accelZ;

        mListener.onFilterChangedListener(mAccel[X], mAccel[Y], mAccel[Z]);
    }

    /** Sets the upper and lower bound limit */
    public float clamp(float value) {
        if (value > MAX_FLOAT) {
            return 1.0f;
        } else if (value < ZERO_FLOAT) {
            return 0.0f;
        } else {
            return value;
        }
    }

    /** Gets normalized vector sqrt(X^2 + Y^2 + Z^2) */
    public float norm(float valueX, float valueY, float valueZ) {
        return sqrt((valueX*valueX) + (valueY*valueY) + valueZ*valueZ);
    }

    /** Default configuration settings for the controller */
    public void setDefaultConfiguration() {
        mUpdateFreq = 30;
        mCutoffFreq = 0.9f;
        mRecurse = MAX_FLOAT / mCutoffFreq;
        mDelta = MAX_FLOAT / mUpdateFreq;
        mFilterConstant = mRecurse / (mDelta + mRecurse);
        mAlpha = mFilterConstant;
        kAccelerometerMinStep = 0.033f;
        kAccelerometerNoiseAttenuation = 3.0f;
    }

    private void setConfiguration(UserConfigurationObject mConfigObject) {
        // TODO
    }

    public interface FilterListener {
        public void onFilterChangedListener(float valueX, float valueY, float valueZ);
    }

}
