package com.free.utils.file.interfac;

import java.util.List;

public interface CxFileFilterI
{
	void display(String parent, String name);
	public boolean isBreak();
	public boolean validateFolder(String folderName);
	public void doBreak();
	public List<String> getSkipFolders();
}
