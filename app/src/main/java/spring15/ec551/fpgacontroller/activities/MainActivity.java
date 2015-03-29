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

import spring15.ec551.fpgacontroller.R;
import spring15.ec551.fpgacontroller.fragments.ControllerSettingsFragment;
import spring15.ec551.fpgacontroller.fragments.ExamineAccelFragment;
import spring15.ec551.fpgacontroller.fragments.FragmentActionListener;
import spring15.ec551.fpgacontroller.fragments.MenuFragment;
import spring15.ec551.fpgacontroller.resources.UserConfigurationObject;

public class MainActivity extends ActionBarActivity implements FragmentActionListener {

    // Tags used for Fragment Transactions
    final String MENU_FRAGMENT = "MENU_FRAGMENT";
    final String EXAMINE_ACCEL_FRAGMENT = "EXAMINE_ACCEL_FRAGMENT";
    final String CONTROLLER_SETTINGS_FRAGMENT = "CONTROLLER_SETTINGS_FRAGMENT";

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

        // If no fragment is visible
        if (savedInstanceState == null) {
            initializeMenuFragment(false);
        }
    }

    /** Listener for MenuFragment ListView */
    @Override
    public void onSettingsMenuItemClickListener(String itemName) {
        if (itemName.equals(MenuFragment.EXAMINE_ACCEL))
            initializeExamineAccelFragment();
        else if (itemName.equals(MenuFragment.CONTROLLER_SETTINGS)) {
            initializeControllerSettingsFragment();
        }
    }

    /** MenuFragment Transactions */
    private void initializeMenuFragment(boolean backstack) {
        if (!backstack) {
            MenuFragment menuFragment = MenuFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, menuFragment, MENU_FRAGMENT);
            transaction.commit();
        }
        // Menu Fragment doesn't have a back button
        mBackButton.setVisibility(View.INVISIBLE);
    }

    /** ExamineAccelFragment initializer */
    public void initializeExamineAccelFragment() {
        if (getFragmentManager().findFragmentByTag(EXAMINE_ACCEL_FRAGMENT) == null) {
            ExamineAccelFragment examineAccelFragment = ExamineAccelFragment.newInstance();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, examineAccelFragment, EXAMINE_ACCEL_FRAGMENT);
            transaction.addToBackStack(EXAMINE_ACCEL_FRAGMENT);
            transaction.commit();
        }
    }

    /** ControllerSettingsFragment initializer */
    private void initializeControllerSettingsFragment() {
        if (getFragmentManager().findFragmentByTag(CONTROLLER_SETTINGS_FRAGMENT) == null) {
            ControllerSettingsFragment controllerSettingsFragment = ControllerSettingsFragment.newInstance();
            FragmentTransaction transation = getFragmentManager().beginTransaction();
            transation.replace(R.id.fragment_container, controllerSettingsFragment, CONTROLLER_SETTINGS_FRAGMENT);
            transation.addToBackStack(CONTROLLER_SETTINGS_FRAGMENT);
            transation.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /** Action Bar Listener */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Sets the onClickListener back button for ExamineAccelFragment */
    @Override
    public void initializeExamineAccelerometerBackButton() {
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                mBackButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void initializeControllerSettingsBackButton() {
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                mBackButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onConfigurationSettingsImplemented(UserConfigurationObject userConfig) {
        // TODO
    }
}
