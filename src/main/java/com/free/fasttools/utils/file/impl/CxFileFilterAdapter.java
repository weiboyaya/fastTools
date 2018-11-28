package com.free.utils.file.impl;

import com.free.utils.file.interfac.CxFileFilterI;

import java.util.ArrayList;
import java.util.List;

public class CxFileFilterAdapter implements CxFileFilterI
{
	private List<String> skipfolders = new ArrayList<String>();
	
	public void addSkipFolder(String folder)
	{
		skipfolders.add(folder);
	}
	
	protected boolean accept = true;
	public void display(String parent, String name) {
		
	}

	@Override
	public boolean isBreak() {
		return !accept;
	}

	@Override
	public void doBreak() {
		accept = false;
	}
	
	public CxFileFilterAdapter resetAccept(boolean value)
	{
		accept = value;
		return this;
	}

	@Override
	public List<String> getSkipFolders() {
		return skipfolders;
	}

	@Override
	public boolean validateFolder(String folderName)
	{
		if (folderName != null && folderName.endsWith(".svn")) return false;
		return true;
	}

}
