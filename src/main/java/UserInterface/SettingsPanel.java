package UserInterface;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SettingsPanel extends JPanel implements ChangeListener {

    private Set<Listener> listeners = new HashSet<Listener>();

    public interface Listener{
        public void stateChanged(ChangeEvent e);
    }

    public static SettingsPanel newInstance()
    {
        SettingsPanel panel = new SettingsPanel();
        panel.init();

        return panel;
    }

    private JSlider sensitivitySlider;
    private JLabel sensitivitySliderLabel;

    private JSpinner linesToScrollSpinner;
    private JLabel linesToScrollLabel;

    private final GridBagConstraints gbc = new GridBagConstraints();

    private void init() {

        sensitivitySliderLabel = new JLabel("Sensitivity");

        add(sensitivitySliderLabel, createGbc(0, 0));

        sensitivitySlider = new JSlider(JSlider.HORIZONTAL, 30, 200, 50);
        sensitivitySlider.addChangeListener(this);

        add(sensitivitySlider, createGbc(1,0));

        linesToScrollLabel = new JLabel("Lines to scroll");

        add(linesToScrollLabel, createGbc(0, 1));

        linesToScrollSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));

        add(linesToScrollSpinner, createGbc(1,1));

    }

    public SettingsPanel() {
        super(new GridBagLayout());
    }

    public void registerListener(Listener listener)
    {
        listeners.add(listener);
    }

    public void unregisterListener(Listener listener)
    {
        listeners.remove(listener);
    }

    public void setSensitivitySliderValue(int value) {
        this.sensitivitySlider.setValue(value);
    }

    public void setLinesToScrollSpinnerValue(int value) {
        this.sensitivitySlider.setValue(value);
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        for (Listener listener : listeners)
        {
            listener.stateChanged(e);
        }
    }

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.anchor = (x == 0) ? GridBagConstraints.NORTHWEST : GridBagConstraints.NORTHEAST;
        //gbc.fill = (x == 0) ? GridBagConstraints.BOTH
        //        : GridBagConstraints.HORIZONTAL;

        //gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;

        gbc.insets = new Insets(1,1,1,1);
        gbc.weightx = (x == 0) ? 0.1 : 1.0;
        gbc.weighty = 1;
        return gbc;
    }
}
