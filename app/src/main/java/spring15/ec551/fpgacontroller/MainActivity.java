package spring15.ec551.fpgacontroller;

import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.bda.controller.Controller;

import java.util.List;

public class MainActivity extends ActionBarActivity implements MenuInterfaceListener {
//        BluetoothConnectionListener {

    Controller object;
//    public static UUID mUUID;
//    public static String mServiceString = "application_server";
//    public static BluetoothAdapter mBluetoothAdapter;
//    Set<BluetoothDevice> mPairedDevices;
//    BluetoothServerSocket mBluetoothServerSocket;
//    BluetoothSocket mBluetoothSocket;
//    AcceptThread mAcceptThread;
//
//    ConnectThread mConnectThread;


    View mControllerIndicator;
    View mVehicleIndicator;
    final String MENU_FRAGMENT = "Menu Fragment";
    private InputMethodManager mInputMethodManager;
    private Keyboard mKeyboard;
    private static String MOGA_UNIVERSAL_PACKAGE = "net.obsidianx.android.mogaime";

    private final String SETTINGS_FRAGMENT_CHANGE = "Settings_Fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        mControllerIndicator = findViewById(R.id.controller_indicator);
        mVehicleIndicator = findViewById(R.id.vehicle_indicator);

        // Checks for the Moga Universal Driver
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = mInputMethodManager.getEnabledInputMethodList();

        final int N = mInputMethodProperties.size();

        for (int i=0; i < N; i++) {
            InputMethodInfo imi = mInputMethodProperties.get(i);
            if (imi.getPackageName().equals(MOGA_UNIVERSAL_PACKAGE)) {
                isControllerDetected();
            }
        }

//        pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                mUUID = device.getUuids()[0].getUuid();
//                mServiceString = device.getName();
//                mAcceptThread = new AcceptThread(MainActivity.this);
//                mAcceptThread.start();
////                mConnectThread = new ConnectThread(MainActivity.this, device);
////                mConnectThread.start();
//            }
//        }

        if (savedInstanceState == null) {
            initializeMenuFragment();
        }
    }

    private void initializeMenuFragment() {
        MenuFragment menuFragment = MenuFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, menuFragment, MENU_FRAGMENT);
        transaction.addToBackStack(MENU_FRAGMENT);
        transaction.commit();

    }

    private void initializeControllerSettingsFragment() {
        ControllerSettingFragment controllerSettingFragment = ControllerSettingFragment.newInstance();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, controllerSettingFragment, SETTINGS_FRAGMENT_CHANGE);
        transaction.addToBackStack(SETTINGS_FRAGMENT_CHANGE);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void isControllerDetected() {
            mControllerIndicator.setBackgroundResource(R.drawable.status_not_calibrated);
    }

    public void isControllerCalibrated() {
        mControllerIndicator.setBackgroundResource(R.drawable.status_connected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onSuccessfulPairing(BluetoothServerSocket socket) {
//        System.out.println("CONNECTED!!");
//        mBluetoothServerSocket = socket;
//        isControllerConnected();
//    }

    @Override
    public void onSettingsClickListener() {
        initializeControllerSettingsFragment();
    }
}
