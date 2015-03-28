package spring15.ec551.fpgacontroller.activities;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import spring15.ec551.fpgacontroller.fragments.MenuFragment;
import spring15.ec551.fpgacontroller.fragments.MenuInterfaceListener;
import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.fragments.ExamineAccelFragment;

public class MainActivity extends ActionBarActivity implements MenuInterfaceListener {

    final String CONTROLLER_SETTINGS_FRAGMENT = "controller_settings_fragment";
    final String MENU_FRAGMENT = "Menu Fragment";

    View mControllerIndicator;
    View mVehicleIndicator;
    Button mBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hides action-menu bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mControllerIndicator = findViewById(R.id.controller_indicator);
        mVehicleIndicator = findViewById(R.id.vehicle_indicator);
        mBackButton = (Button) findViewById(R.id.back_button);
        mBackButton.setVisibility(View.INVISIBLE);

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

    /** Listener for MenuFragment ListView */
    @Override
    public void onSettingsClickListener(String itemName) {
        if (itemName.equals(MenuFragment.EXAMINE_ACCEL))
            initializeControllerSettingsFragment();
    }


    /** MenuFragment Transactions */
    private void initializeMenuFragment() {
        MenuFragment menuFragment = MenuFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, menuFragment, MENU_FRAGMENT);
        transaction.addToBackStack(MENU_FRAGMENT);
        transaction.commit();

        mBackButton.setVisibility(View.INVISIBLE);
    }

    /** ControllerSettingsFragment */
    private void initializeControllerSettingsFragment() {
        ExamineAccelFragment controllerSettingFragment = ExamineAccelFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, controllerSettingFragment, CONTROLLER_SETTINGS_FRAGMENT);
        transaction.addToBackStack(CONTROLLER_SETTINGS_FRAGMENT);
        transaction.commit();

        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeMenuFragment();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
