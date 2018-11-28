package com.free.utils.file.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;


public class CloseUtil
{
	public static void close(InputStream... inputStreams)
	{
		for (int i = 0; i < inputStreams.length; i++)
		{
			try
			{
				if (inputStreams[i] != null) inputStreams[i].close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void close(OutputStream...outputStreams)
	{
		for (int i = 0; i < outputStreams.length; i++)
		{
			try
			{
				if (outputStreams[i] != null)
				{
					outputStreams[i].flush();
					outputStreams[i].close();
				}
			}
			catch (IOException e)
			{
//			throw LangUtil.newRuntimeException(e);
				throw new RuntimeException(e);
			}
		}
	}

	public static void close(Reader ... readers) {
		for (int i = 0; i < readers.length; i++)
		{
			try
			{
				if (readers[i] != null) readers[i].close();
			}
			catch (IOException e)
			{
//			throw LangUtil.newRuntimeException(e);
				throw new RuntimeException(e);
			}
		}		
	}
	
	public static void close(Writer ... readers) {
		for (int i = 0; i < readers.length; i++)
		{
			try
			{
				if (readers[i] != null) readers[i].close();
			}
			catch (IOException e)
			{
//			throw LangUtil.newRuntimeException(e);
				throw new RuntimeException(e);
			}
		}		
	}
}
