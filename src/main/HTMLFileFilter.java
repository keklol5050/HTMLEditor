package main;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class HTMLFileFilter extends FileFilter { // choosing file

    @Override
    public boolean accept(File f) { // accepting only html or htm files
        if (f.isDirectory()) return true;
        String extension = f.getName().substring(f.getName().lastIndexOf(".")+1);
        return extension.equalsIgnoreCase("html") || extension.equalsIgnoreCase("htm");
    }

    @Override
    public String getDescription() { // window description
        return "HTML и HTM файлы";
    }
}
