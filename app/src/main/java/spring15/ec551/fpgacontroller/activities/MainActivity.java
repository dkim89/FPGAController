package spring15.ec551.fpgacontroller.activities;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    View mControllerIndicator;
    View mVehicleIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hides action-menu bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mControllerIndicator = findViewById(R.id.controller_indicator);
        mVehicleIndicator = findViewById(R.id.vehicle_indicator);

        // If no fragment is visible
        if (savedInstanceState == null) {
            initializeMenuFragment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        if (mCurrentFragmentTag.equals(CONTROLLER_SETTINGS_FRAGMENT)) {
//            mSensorManager.registerListener(
//                    (SensorEventListener) getFragmentManager().findFragmentByTag(CONTROLLER_SETTINGS_FRAGMENT),
//                    mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Disable the sensor when application is not being used to convserve battery
//        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
//            if (mCurrentFragmentTag.equals(CONTROLLER_SETTINGS_FRAGMENT)) {
//                mSensorManager.unregisterListener(
//                        (SensorEventListener) getFragmentManager().findFragmentByTag(CONTROLLER_SETTINGS_FRAGMENT));
//            }
//        }
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
    }

    /** ControllerSettingsFragment */
    private void initializeControllerSettingsFragment() {
        ControllerFragment controllerSettingFragment = ControllerFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, controllerSettingFragment, CONTROLLER_SETTINGS_FRAGMENT);
        transaction.addToBackStack(CONTROLLER_SETTINGS_FRAGMENT);
        transaction.commit();
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
