package listeners;

import main.View;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPaneChangeListener implements ChangeListener { //listens and processes the state of the tab bar

    private final View view;

    public TabbedPaneChangeListener(View view) {
        this.view = view;
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        view.selectedTabChanged();
    }
}
