package UserInterface;

import Common.Constants;
import Common.CustomOutputStream;
import Common.ObservableJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;

public class MainPanel extends ObservableJPanel<MainPanel.Listener> implements ActionListener {

    public interface Listener {
        public void onRefreshServerButtonClicked();
    }

    private JLabel statusLabel;
    private JLabel qrLabel;

    private JButton refreshButton;

    private JScrollPane logTextScrollPane;
    private JTextArea logTextArea;

    private final String newLine = "\\n";

    public static MainPanel newInstance() {
        MainPanel panel = new MainPanel();
        panel.init();
        return panel;
    }

    protected MainPanel()
    {
        super(new BorderLayout());
    }

    public void init()
    {
        qrLabel = new JLabel();
        qrLabel.setSize(new Dimension(800, 800));

        add(qrLabel, BorderLayout.NORTH);

        statusLabel = new JLabel();

        add(statusLabel, BorderLayout.WEST);

        refreshButton = new JButton();
        refreshButton.setText(Constants.REFRESH_SERVER);
        refreshButton.addActionListener(this);

        add(refreshButton, BorderLayout.EAST);

        logTextArea = new JTextArea(10, 1);
        logTextArea.setLineWrap(true);
        logTextArea.setEditable(false);

        if (!Constants.DEBUG) {

            PrintStream printStream = new PrintStream(new CustomOutputStream(logTextArea));

            System.setOut(printStream);
            System.setErr(printStream);
        }

        logTextScrollPane = new JScrollPane(logTextArea);
        logTextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add(logTextScrollPane, BorderLayout.SOUTH);
    }

    public void setQRCode(ImageIcon imageIcon) {
        qrLabel.setIcon(imageIcon);
    }

    public void changeStatusString(String text)
    {
        statusLabel.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Listener listener: getListeners()) {
            listener.onRefreshServerButtonClicked();
        }
    }
}
