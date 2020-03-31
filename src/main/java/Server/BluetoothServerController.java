package Server;

import Common.BaseObservable;
import Common.Constants;
import Common.DeviceConfiguration;
import Common.MouseAction;
import Utilities.Configuration.ConfigurationManager;
import Utilities.MouseActionExecuter;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;

public class BluetoothServerController extends BaseObservable<BluetoothServerController.Listener>
        implements MouseActionExecuter.Listener, BluetoothServer.Listener {

    public final String TAG = this.getClass().getName();

    public interface Listener {
        public void onServerRestart();
        public void onConfigurationLoaded(DeviceConfiguration conf);
    }

    private BluetoothServer bluetoothServer;
    private ConfigurationManager<DeviceConfiguration> confManager;

    public MouseActionExecuter mouseActionExecuter;

    public static BluetoothServerController newInstance(BluetoothServer bluetoothServer) {
        BluetoothServerController controller = new BluetoothServerController(bluetoothServer);
        controller.init();

        return controller;
    }

    public BluetoothServerController(BluetoothServer bluetoothServer) {
        this.bluetoothServer = bluetoothServer;
    }

    public void init() {
        mouseActionExecuter = new MouseActionExecuter();
        mouseActionExecuter.registerListener(this);

        bluetoothServer.registerListener(this);

        confManager = new ConfigurationManager<>(Constants.configFile);
    }

    public void startServer() {
        bluetoothServer.startServer();
    }

    public void endServer() {
        bluetoothServer.endServer();

        if (isConnected()) {
            DeviceConfiguration deviceConfiguration = new DeviceConfiguration(bluetoothServer.getRemoteDeviceAddress())
                    .setLinesToScroll(mouseActionExecuter.getNumberOfLinesToScroll())
                    .setSensitivity(mouseActionExecuter.getMotionCoef());

            confManager.write(deviceConfiguration, true);
        }
    }

    public void restartServer() {
        System.out.println(TAG + " restart bluetooth server");
        endServer();
        startServer();
    }

    public void setMouseSensitivity(int value) {
        mouseActionExecuter.setMotionCoef(value);
    }

    public void setLinesToScroll(int value) {
        mouseActionExecuter.setLinesToScroll(value);
    }

    public String getRemoteDeviceAddress() {
        return bluetoothServer.getRemoteDeviceAddress();
    }

    public String getLocalDeviceAddress() throws BluetoothStateException {
        return bluetoothServer.getLocalDeviceAddress();
    }

    public boolean isConnected() {
        return (bluetoothServer.getRemoteDeviceAddress() != null ? true : false);
    }

    @Override
    public void onAbortAction() {
        endServer();
        for (Listener listener : getListeners())
        {
            listener.onServerRestart();
        }
        startServer();
    }

    @Override
    public void onCommandReceived(MouseAction action) {
        mouseActionExecuter.executeAction(action);
    }

    @Override
    public void onDeviceConnected(RemoteDevice remoteDevice) {
        DeviceConfiguration configuration = confManager.get(new DeviceConfiguration(remoteDevice.getBluetoothAddress()));

        if (configuration!=null) {
            mouseActionExecuter.setLinesToScroll(configuration.linesToScroll);
            mouseActionExecuter.setMotionCoef(configuration.sensitivity);

            for (Listener listener: getListeners()) {
                listener.onConfigurationLoaded(configuration);
            }
        } else {
            configuration = new DeviceConfiguration(remoteDevice.getBluetoothAddress())
                    .setLinesToScroll(Constants.DEFAULT_LINES_TO_SCROLL)
                    .setSensitivity(Constants.DEFAULT_SENTITYVITY);
            confManager.write(configuration);
            mouseActionExecuter.setDefaultParameters();

            for (Listener listener: getListeners()) {
                listener.onConfigurationLoaded(configuration);
            }
        }
    }


}
