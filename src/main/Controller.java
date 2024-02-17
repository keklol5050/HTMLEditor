package main;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {

    private final View view;
    private final HTMLEditorKit kit = new HTMLEditorKit();
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public void init() { // initialize controller
        createNewDocument();
    }

    public void exit() { // exit controller
        System.exit(0);
    }

    public HTMLDocument getDocument() {
        return document;
    }

    public void resetDocument() { // deleting current document and setting new empty document
        if (document != null) document.removeUndoableEditListener(view.getUndoListener());
        document = (HTMLDocument) kit.createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }

    public void setPlainText(String text) { // setting text with html tags into document
        resetDocument();
        StringReader reader = new StringReader(text);
        try {
            kit.read(reader, document, 0);
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText() { // getting text with html tags from document
        StringWriter writer = new StringWriter();
        try {
            kit.write(writer, document, 0, document.getLength());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
        return writer.toString();
    }

    public void createNewDocument() { // creating new empty document
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML Редактор");
        currentFile = null;
    }

    public void openDocument() {
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new HTMLFileFilter());
        fileChooser.setDialogTitle("Open File");
        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());

            try (FileReader fileReader = new FileReader(currentFile)) {
                kit.read(fileReader, document, 0);
            } catch (IOException | BadLocationException e) {
                ExceptionHandler.log(e);
            }

            view.resetUndo();
        }
    }

    public void saveDocument() { // saving document to file
        if (currentFile == null) {
            saveDocumentAs();
            return;
        }
        try {
            view.selectHtmlTab();
            try (FileWriter writer = new FileWriter(currentFile)) {
                kit.write(writer, document, 0, document.getLength());
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public void saveDocumentAs() { // saving document to file with new name
        try {
            view.selectHtmlTab();
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new HTMLFileFilter());
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                view.setTitle(currentFile.getName());
                try (FileWriter writer = new FileWriter(currentFile)) {
                    kit.write(writer, document, 0, document.getLength());
                }
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public static void main(String[] args) { // creating and initialization view and controller
        View view = new View();
        view.setSize(1600,1600);
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }
}
