package spring15.ec551.fpgacontroller;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;

import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity implements MenuInterfaceListener{

    final String CONTROLLER_SETTINGS_FRAGMENT = "controller_settings_fragment";
    final String MENU_FRAGMENT = "Menu Fragment";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer = null;
    private ControllerVehicleInterfacer mInterfacer;

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

        // Initialize the sensors
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        // If no Accelerometer, Sensor will remain null
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // TODO: Implement saving configurations and retrieving them here
        mInterfacer = new ControllerVehicleInterfacer();

        if (savedInstanceState == null) {
            initializeMenuFragment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        // Disable the sensor when application is not being used to convserve battery
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mSensorManager.unregisterListener(mInterfacer);
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

    }

    /** ControllerSettingsFragment */
    private void initializeControllerSettingsFragment() {
        ControllerSettingsFragment controllerSettingFragment = ControllerSettingsFragment.newInstance();

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
