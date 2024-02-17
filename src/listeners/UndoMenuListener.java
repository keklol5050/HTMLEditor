package listeners;

import main.View;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class UndoMenuListener implements MenuListener { // listens when editing is selected by the user

    private final View view;

    private final JMenuItem undoMenuItem;
    private final JMenuItem redoMenuItem;


    public UndoMenuListener(View view, JMenuItem undoMenuItem, JMenuItem redoMenuItem) {
        this.view = view;
        this.undoMenuItem = undoMenuItem;
        this.redoMenuItem = redoMenuItem;
    }

    @Override
    public void menuSelected(MenuEvent e) { // setting undo-redo actions visibility
        boolean canUndo = view.canUndo();
        undoMenuItem.setEnabled(canUndo);
        boolean canRedo = view.canRedo();
        redoMenuItem.setEnabled(canRedo);
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
