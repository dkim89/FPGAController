package spring15.ec551.fpgacontroller.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import spring15.ec551.fpgacontroller.fragments.MenuFragment;
import spring15.ec551.fpgacontroller.fragments.MenuInterfaceListener;
import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.fragments.ControllerFragment;

public class MainActivity extends ActionBarActivity implements MenuInterfaceListener {

    final String CONTROLLER_SETTINGS_FRAGMENT = "controller_settings_fragment";
    final String MENU_FRAGMENT = "Menu Fragment";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer = null;
//    private ControllerVehicleInterfacer mInterfacer;

    View mControllerIndicator;
    View mVehicleIndicator;

    String mCurrentFragmentTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hides action-menu bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mControllerIndicator = findViewById(R.id.controller_indicator);
        mVehicleIndicator = findViewById(R.id.vehicle_indicator);

        // Initialize the sensors
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        // If no Accelerometer, Sensor will remain null
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // TODO: Implement saving configurations and retrieving them here
//        mInterfacer = new ControllerVehicleInterfacer();

        // If no fragment is visible
        if (savedInstanceState == null) {
            initializeMenuFragment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCurrentFragmentTag.equals(CONTROLLER_SETTINGS_FRAGMENT)) {
            mSensorManager.registerListener(
                    (SensorEventListener) getFragmentManager().findFragmentByTag(CONTROLLER_SETTINGS_FRAGMENT),
                    mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Disable the sensor when application is not being used to convserve battery
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            if (mCurrentFragmentTag.equals(CONTROLLER_SETTINGS_FRAGMENT)) {
                mSensorManager.unregisterListener(
                        (SensorEventListener) getFragmentManager().findFragmentByTag(CONTROLLER_SETTINGS_FRAGMENT));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /** Listener for MenuFragment ListView */
    @Override
    public void onSettingsClickListener() {
        initializeControllerSettingsFragment();
    }


    /** MenuFragment Transactions */
    private void initializeMenuFragment() {
        MenuFragment menuFragment = MenuFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, menuFragment, MENU_FRAGMENT);
        transaction.addToBackStack(MENU_FRAGMENT);
        transaction.commit();

        mCurrentFragmentTag = MENU_FRAGMENT;
    }

    /** ControllerSettingsFragment */
    private void initializeControllerSettingsFragment() {
        ControllerFragment controllerSettingFragment = ControllerFragment.newInstance();

        mSensorManager.registerListener(controllerSettingFragment, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, controllerSettingFragment, CONTROLLER_SETTINGS_FRAGMENT);
        transaction.addToBackStack(CONTROLLER_SETTINGS_FRAGMENT);
        transaction.commit();

        mCurrentFragmentTag = CONTROLLER_SETTINGS_FRAGMENT;
    }


    /** Menu Bar Listener */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
