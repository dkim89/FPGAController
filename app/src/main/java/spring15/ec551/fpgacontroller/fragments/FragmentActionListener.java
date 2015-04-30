package spring15.ec551.fpgacontroller.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.ByteArrayInputStream;

/**
 * Created by davidkim on 3/25/15.
 */
public interface FragmentActionListener {
    /** Listens for the particular settings item clicked
     * @param itemName the string value associated with the settings item */
    public void onMenuItemClickListener(String itemName);

    /** Invoke an update to saved configuration state of controller */
    public void updateControllerSavedStateDisplay();

    public void connectToBTSocket(BluetoothDevice bt);

    public void adjustActivityForMainMenu();

    public void adjustActivityForSettings();

    public void adjustActivityForPlay();

    public void sendbits(byte[] bytes_);

}
