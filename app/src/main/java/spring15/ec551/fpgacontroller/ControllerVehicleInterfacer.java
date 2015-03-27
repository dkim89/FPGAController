package spring15.ec551.fpgacontroller;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;


/**
 * Created by davidkim on 3/26/15.
 * The object that will be used throughout the application to handle listening to the Accelerometer
 * and processing the information using the filter, and parse the information needed to
 * pass the information to the vehicle.
 */
public class ControllerVehicleInterfacer implements SensorEventListener, AccelerometerHighPassFilter.FilterListener {
    /** The filter object used to remove the pesky noise */
    private AccelerometerHighPassFilter mFilter;

    /** The filtered accelerometer valuesfrom AccelerometerHighPassFilter */
    private float mFilteredAccelX;
    private float mFilteredAccelY;
    private float mFilteredAccelZ;

    public ControllerVehicleInterfacer() {
        mFilter = new AccelerometerHighPassFilter(this);
    }

    /** Will initialize the configuration settings */
    public void setConfiguration(UserConfigurationObject userObject) {
        // TODO
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mFilter.onAccelerometerChanged(
                event.values[AccelerometerHighPassFilter.X],
                event.values[AccelerometerHighPassFilter.Y],
                event.values[AccelerometerHighPassFilter.Z]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("ACCURACYCHANGED: ", "Integer: " + accuracy);
    }

    @Override
    public void onFilterChangedListener(float valueX, float valueY, float valueZ) {
        mFilteredAccelX = valueX;
        mFilteredAccelY = valueY;
        mFilteredAccelZ = valueZ;
    }
}
