package com.free.utils.file.impl;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

public class JarFileX extends JarFile {

	private File file;
	
	public JarFileX(File file) throws IOException {
		super(file);
		this.file = file;
	}

	public JarFileX(String name) throws IOException {
		this(new File(name));
	}

	public File getFile() {
		return file;
	}
}
