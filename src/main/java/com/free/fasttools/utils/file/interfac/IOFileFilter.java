package com.free.utils.file.interfac;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public interface IOFileFilter extends FileFilter, FilenameFilter {
        boolean accept(File arg0);
        boolean accept(File arg0, String arg1);
}
