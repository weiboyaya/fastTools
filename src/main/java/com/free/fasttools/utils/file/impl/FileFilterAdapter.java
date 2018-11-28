package com.free.utils.file.impl;

import com.free.utils.file.interfac.FileFilterI;

import java.io.File;


public class FileFilterAdapter implements FileFilterI
{
	protected long startIndex;
	protected int count;
	
	public FileFilterAdapter()
	{
		
	}
	
	public FileFilterAdapter (long startIndex, int count)
	{
		this.startIndex = startIndex;
		this.count = count;
	}
	public boolean accept(File file)
	{
		return true;
	}

	public boolean accept(File dir, String name)
	{
		return true;
	}

	public long getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(long startIndex) {
		this.startIndex = startIndex;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
