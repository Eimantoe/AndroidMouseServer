package Common;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class ObservableJPanel<LISTENER_TYPE> extends JPanel {

    private Set<LISTENER_TYPE> listeners = new HashSet<>();

    public ObservableJPanel() {
        super();
    }

    public ObservableJPanel(LayoutManager layoutManager){
        super(layoutManager);
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
