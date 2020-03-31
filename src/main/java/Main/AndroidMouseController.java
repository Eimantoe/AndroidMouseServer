package Main;

import Common.Constants;
import Common.DeviceConfiguration;
import Server.BluetoothServer;
import Server.BluetoothServerController;
import UserInterface.ApplicationFrame;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.bluetooth.BluetoothStateException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.io.*;

public class AndroidMouseController
        implements ApplicationFrame.Listener, BluetoothServerController.Listener {


    public static void main(String[] args) throws IOException {

        ApplicationFrame applicationFrame = ApplicationFrame.newInstance();
        BluetoothServerController serverController = BluetoothServerController.newInstance(BluetoothServer.newInstance());

        AndroidMouseController androidMouseController = AndroidMouseController.newInstance(applicationFrame, serverController);
        androidMouseController.startApplication();
    }

    private void startApplication() {
        this.serverController.startServer();
    }

    public static AndroidMouseController newInstance(ApplicationFrame applicationFrame, BluetoothServerController serverController) {
        AndroidMouseController androidMouseController = new AndroidMouseController(applicationFrame, serverController);
        androidMouseController.init();

        return androidMouseController;
    }

    private final ApplicationFrame applicationFrame;
    private final BluetoothServerController serverController;

    public AndroidMouseController(ApplicationFrame applicationFrame, BluetoothServerController serverController) {
        this.applicationFrame = applicationFrame;
        this.serverController = serverController;
    }

    public void init()
    {
        this.applicationFrame.setQRCode(makeServerAddressQRCode());
        this.applicationFrame.changeStatusPanelText(Constants.DISCONNECTED);
        this.applicationFrame.registerListener(this);
        this.serverController.registerListener(this);

        this.applicationFrame.setVisible(true);
    }

    public byte[] makeServerAddressQRCode()
    {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;

        try {
            bitMatrix = qrCodeWriter.encode(serverController.getLocalDeviceAddress(), BarcodeFormat.QR_CODE, 350, 350);
        } catch (WriterException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (BluetoothStateException e) {
            e.printStackTrace();
            System.exit(1);
        }

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        try {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }

    @Override
    public void onSensitivityStateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();

        if (!source.getValueIsAdjusting()) {
            serverController.setMouseSensitivity(source.getValue());
        }
    }

    @Override
    public void onRefreshServerButtonClicked() {
        this.applicationFrame.changeStatusPanelText(Constants.DISCONNECTED);
        serverController.restartServer();
    }

    @Override
    public void onServerRestart() {
        this.applicationFrame.changeStatusPanelText(Constants.DISCONNECTED);
    }

    @Override
    public void onConfigurationLoaded(DeviceConfiguration conf) {
        this.applicationFrame.setLinesToScrollSpinnerValue(conf.getLinesToScroll());
        this.applicationFrame.setSensitivitySliderValue(conf.getSensitivity());
        this.applicationFrame.changeStatusPanelText(Constants.CONNECTED);
    }
}