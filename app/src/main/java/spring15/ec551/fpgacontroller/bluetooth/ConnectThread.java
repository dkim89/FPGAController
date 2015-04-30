package spring15.ec551.fpgacontroller.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import spring15.ec551.fpgacontroller.activities.MainActivity;

/**
 * Created by davidkim on 3/26/15.
 */

// Behaves as a server
public class ConnectThread extends Thread {
    BluetoothDevice device;
    BluetoothAdapter adapter;
//    BluetoothSocket socket;
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        adapter = BluetoothAdapter.getDefaultAdapter();

        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    public void run() {
        adapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
            System.out.println("CONNECT");
            MainActivity.connectedThread = new ConnectedThread(mmSocket);
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
