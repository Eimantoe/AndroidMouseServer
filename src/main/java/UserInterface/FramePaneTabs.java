package UserInterface;

import javax.swing.*;

public class FramePaneTabs extends JTabbedPane {


    public static FramePaneTabs newInstance()
    {
        FramePaneTabs framePaneTabs = new FramePaneTabs();
        framePaneTabs.init();

        return framePaneTabs;
    }

    protected FramePaneTabs()
    {
        super();
    }

    private void init() {

    }
}
