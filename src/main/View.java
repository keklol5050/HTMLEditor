package main;

import listeners.FrameListener;
import listeners.TabbedPaneChangeListener;
import listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {

    private Controller controller;
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JTextPane htmlTextPane = new JTextPane();
    private final JEditorPane plainTextPane = new JEditorPane();
    private final UndoManager undoManager = new UndoManager();
    private final UndoListener undoListener = new UndoListener(undoManager);

    public View() {
    }

    @Override
    public void actionPerformed(ActionEvent e) { // listening and implements menu item clicks
        String command = e.getActionCommand();
        switch (command) {
            case "Новый" -> controller.createNewDocument();
            case "Открыть" -> controller.openDocument();
            case "Сохранить" -> controller.saveDocument();
            case "Сохранить как..." -> controller.saveDocumentAs();
            case "О программе" -> showAbout();
            case "Выход" -> exit();
        }
    }

    public void undo() { // undo last action using UndoManager
        try {
            undoManager.undo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void redo() { // redo last action using UndoManager
        try {
            undoManager.redo();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }
    public boolean canUndo() { //can we cancel the last action
        return undoManager.canUndo();
    }

    public boolean canRedo() { // can we redo the last action
        return undoManager.canRedo();
    }

    public void initMenuBar() { // initialize menu bar
        JMenuBar menuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, menuBar); // file menu
        MenuHelper.initEditMenu(this, menuBar); // edit menu
        MenuHelper.initStyleMenu(this, menuBar); // style menu
        MenuHelper.initAlignMenu(this, menuBar); // align menu
        MenuHelper.initColorMenu(this, menuBar); // color menu
        MenuHelper.initFontMenu(this, menuBar); // fonts menu
        MenuHelper.initHelpMenu(this, menuBar); // help menu
        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    public void initEditor() { // initialize redactor panel
        htmlTextPane.setContentType("text/html");
        JScrollPane htmlScrollPane = new JScrollPane(htmlTextPane);
        tabbedPane.addTab("HTML", htmlScrollPane);
        JScrollPane plainScrollPane = new JScrollPane(plainTextPane);
        tabbedPane.addTab("Текст", plainScrollPane);
        tabbedPane.setPreferredSize(null);
        tabbedPane.addChangeListener(new TabbedPaneChangeListener(this));
        getContentPane().add(tabbedPane,BorderLayout.CENTER);
    }

    public void initGui() { // initialize visual interface
        initMenuBar();
        initEditor();
        pack();
        setSize(900, 900);

    }
    public void init() { // initialize view
        initGui();
        FrameListener listener = new FrameListener(this);
        addWindowListener(listener); // adding new window listener
        setVisible(true); // setting visibility to visible
        setLocationRelativeTo(null); // frame position on center of screen
    }

    public void exit() { // exit program
        this.controller.exit();
    }

    public Controller getController() { // controller getter
        return controller;
    }

    public void setController(Controller controller) { // controller setter
        this.controller = controller;
    }

    public void selectedTabChanged() { // implements tab switching
        if (tabbedPane.getSelectedIndex()==0) {
            controller.setPlainText(plainTextPane.getText());
        } else {
            plainTextPane.setText(controller.getPlainText());
        }
        resetUndo();
    }

    public boolean isHtmlTabSelected() { // returns true if HTML tab selected
       return tabbedPane.getSelectedIndex()==0;
    }

    public void selectHtmlTab() { //
        tabbedPane.setSelectedIndex(0); // set html tab selected
        resetUndo(); // reset all undo
    }

    public void update() { // getting document from controller and setting it in html pane
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void showAbout() { // opening "show about" message dialog
        JOptionPane.showMessageDialog(getContentPane(), "Version 1.0", "About", JOptionPane.INFORMATION_MESSAGE);
    }
    public UndoListener getUndoListener() {
        return undoListener;
    }
}
