package Common;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ObservableJFrame<LISTENER_TYPE> extends JFrame {

    private Set<LISTENER_TYPE> listeners = new HashSet<>();

    public ObservableJFrame() {
        super();
    }

    public ObservableJFrame(String applicationName) {
        super(applicationName);
    }

    public void registerListener(LISTENER_TYPE listener){
        listeners.add(listener);
    }

    public void unregisterListener(LISTENER_TYPE listener) {
        listeners.remove(listener);
    }

    public Set<LISTENER_TYPE> getListeners(){
        return Collections.unmodifiableSet(listeners);
    }

}
