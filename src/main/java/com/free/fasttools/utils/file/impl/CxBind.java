package com.free.utils.file.impl;

public class CxBind<T>
{
	private T value;

	public CxBind()
	{
		
	}
	
	public CxBind(T t)
	{
		this.value = t;
	}
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
}
