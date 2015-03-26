package spring15.ec551.fpgacontroller;

import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.bda.controller.Controller;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends ActionBarActivity implements MenuInterfaceListener, BluetoothConnectionListener {

    // Controller object
    Controller mController = null;
    public static UUID mUUID;
    public static String mServiceString = "application_server";
    public static BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    BluetoothServerSocket mBluetoothServerSocket;
    BluetoothSocket mBluetoothSocket;
    AcceptThread mAcceptThread;

    ConnectThread mConnectThread;


    View mControllerIndicator;
    View mVehicleIndicator;
    final String MENU_FRAGMENT = "Menu Fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        mControllerIndicator = findViewById(R.id.controller_indicator);
        mVehicleIndicator = findViewById(R.id.vehicle_indicator);

        mController = Controller.getInstance(this);
        mController.init();


        pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mUUID = device.getUuids()[0].getUuid();
                mServiceString = device.getName();
                mAcceptThread = new AcceptThread(MainActivity.this);
                mAcceptThread.start();
//                mConnectThread = new ConnectThread(MainActivity.this, device);
//                mConnectThread.start();
            }
        }

        initializeMenuFragment();

    }

    private void initializeMenuFragment() {
        MenuFragment menuFragment = MenuFragment.newInstance();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, menuFragment, MENU_FRAGMENT);
        transaction.addToBackStack(MENU_FRAGMENT);
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

    public void isControllerConnected() {
        if (mController != null) {
            if (mController.getState(Controller.STATE_CONNECTION) == Controller.ACTION_CONNECTED) {
                mControllerIndicator.setBackgroundResource(R.drawable.status_connected);
            }
        } else {
            mControllerIndicator.setBackgroundResource(R.drawable.status_not_connected);
        }
    }

    @Override
    protected void onDestroy() {
        // Destroys controller object
        if (mController != null) {
            mController.exit();
        }
        super.onDestroy();
    }

    @Override
    public void onSuccessfulPairing(BluetoothServerSocket socket) {
        System.out.println("CONNECTED!!");
        mBluetoothServerSocket = socket;
        isControllerConnected();
    }
}
