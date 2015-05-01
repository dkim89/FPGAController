package spring15.ec551.fpgacontroller.bluetooth;

import android.bluetooth.BluetoothServerSocket;

/**
 * Created by davidkim on 3/26/15.
 * Allows for classes that implement this interface to listen to any incoming signals from
 * bluetooth connected device. Originally used to implement receiving camera data.
 */
public interface BluetoothConnectionListener {
    /* Notifies listeners that bluetooth has been successfully paired */
    public void hasSuccessfullyPaired(BluetoothServerSocket socket);

    /* Redirects incoming bytestream from bluetooth object */
    public void onReceiveByteStream(byte[] incomingStream);

}
