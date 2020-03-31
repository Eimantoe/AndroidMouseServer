package Server;

import Common.*;
import Utilities.ThreadWithAbort;
import com.google.gson.Gson;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.obex.SessionNotifier;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BluetoothServer extends BaseObservable<BluetoothServer.Listener> {

    public interface Listener {
        public void onCommandReceived(MouseAction action);
        public void onDeviceConnected(RemoteDevice remoteDevice);
    }

    private final UUID    uuid                = new UUID("882f88b3202043c5b02e75c177202d4c", false);
    private final String  connectionString    = "btspp://localhost:" + uuid +";name=Sample SPP Server";

    private volatile RemoteDevice remoteDevice;

    private BluetoothConnectionThread mBtConnectionThread;

    public static BluetoothServer newInstance() {
        BluetoothServer bluetoothServer = new BluetoothServer();
        bluetoothServer.init();

        return bluetoothServer;
    }

    public void init() {
        LocalDevice localDevice = null;
        try {

            localDevice = LocalDevice.getLocalDevice();

            System.out.println("Address: " + localDevice.getBluetoothAddress());
            System.out.println("Name: " + localDevice.getFriendlyName());

        } catch (BluetoothStateException e) {
            System.out.println(getClass() + ": getting local device" + e.getMessage());
            return;
        }
    }

    public String getRemoteDeviceAddress() {
        if (remoteDevice != null) {
            return remoteDevice.getBluetoothAddress();
        }
        return null;
    }

    public String getLocalDeviceAddress() throws BluetoothStateException {
        return LocalDevice.getLocalDevice().getBluetoothAddress();
    }

    public void startServer() {
        mBtConnectionThread = new BluetoothConnectionThread();
        mBtConnectionThread.start();
    }

    public void endServer() {
        mBtConnectionThread.abort();
    }

    public class BluetoothConnectionThread extends ThreadWithAbort {

        private Gson gson = new Gson();

        public BluetoothConnectionThread() {
            super("BluetoothConnectionThread");
        }

        @Override
        public void run() {
            MouseAction action;

            System.out.println(getName() + " started running");

            try {

                StreamConnectionNotifier    streamConnNotifier  = (StreamConnectionNotifier) Connector.open(connectionString);
                StreamConnection            connection          = streamConnNotifier.acceptAndOpen();

                remoteDevice = RemoteDevice.getRemoteDevice(connection);

                for (Listener listener: getListeners()) {
                    listener.onDeviceConnected(remoteDevice);
                }

                System.out.println("Device retrieved: " + remoteDevice.getBluetoothAddress());

                InputStream     inStream = connection.openInputStream();

                BufferedReader  bReader = new BufferedReader(new InputStreamReader(inStream), 60);

                while (getIsRunning()) {

                    String lineRead = bReader.readLine();

                    if (lineRead != null) {
                        action = gson.fromJson(lineRead, MouseAction.class);
                    } else {
                        continue;
                    }

                    System.out.println("Received: " + lineRead);

                    for (Listener listener: getListeners()) {
                        listener.onCommandReceived(action);
                    }
                }

                bReader.close();
                inStream.close();
                connection.close();
                streamConnNotifier.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
