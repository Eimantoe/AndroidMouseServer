package UserInterface;

import Common.Constants;
import Common.ObservableJFrame;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class ApplicationFrame extends ObservableJFrame<ApplicationFrame.Listener> implements SettingsPanel.Listener, MainPanel.Listener {

    public interface Listener {
        public void onSensitivityStateChanged(ChangeEvent e);
        public void onRefreshServerButtonClicked();
    }

    private MainPanel mainPanel;
    private SettingsPanel settingsPanel;
    private FramePaneTabs paneTabs;

    public static ApplicationFrame newInstance()
    {
        ApplicationFrame applicationFrame = new ApplicationFrame(Constants.APPLICATION_NAME);
        applicationFrame.init();

        return applicationFrame;
    }

    public ApplicationFrame(String applicationName) {
        super(applicationName);
    }

    public void init()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setResizable(false);

        paneTabs = FramePaneTabs.newInstance();

        mainPanel = MainPanel.newInstance();
        mainPanel.registerListener(this);

        paneTabs.add(mainPanel, "Main");

        settingsPanel = SettingsPanel.newInstance();
        settingsPanel.registerListener(this);
        paneTabs.add(settingsPanel, "Settings");

        getContentPane().add(paneTabs, BorderLayout.NORTH);

        pack();
    }

    public void setQRCode(byte[] qrByteArr)
    {
        mainPanel.setQRCode(new ImageIcon(qrByteArr));
        pack();
    }

    public void changeStatusPanelText(String string)
    {
        mainPanel.changeStatusString(string);
        pack();
    }

    public void setSensitivitySliderValue(int value) {
        this.settingsPanel.setSensitivitySliderValue(value);
    }

    public void setLinesToScrollSpinnerValue(int value) {
        this.settingsPanel.setLinesToScrollSpinnerValue(value);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        for(Listener listener: getListeners())
        {
            listener.onSensitivityStateChanged(e);
        }
    }


    @Override
    public void onRefreshServerButtonClicked() {
        for (Listener listener: getListeners()) {
            listener.onRefreshServerButtonClicked();
        }
    }
}
