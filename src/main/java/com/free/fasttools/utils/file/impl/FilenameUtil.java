package com.free.utils.file.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

public class FilenameUtil extends FilenameUtils
{
	public static String getLocationPath(String filename, String ...strings)
	{
		String path = concat(filename, strings);
		return concat(System.getProperty("user.dir"), path);
	}
	public static String getPath(String filename)
	{
		return concat(System.getProperty("user.dir"), filename);
	}
	public static String transferToFilepath(String path)
	{
		path = path.replaceAll("%20", " ");
		return path;
	}

	public static String getFilenameid(String name)
	{
		name = FilenameUtil.getName(name);
		int index = name.indexOf(".");
		if (index < 0) return name;
		return name.substring(0, index);
	}


	public static String getParent(String path)
	{
		path = path.replace('\\', '/');
		if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
		if (path.lastIndexOf('/') > 0) path = path.substring(0, path.lastIndexOf('/'));
		return path;
	}

	public static String concat(String base, String name)
	{
		return concat(base, new String[]{name});
	}

	/**
	 * concat("log") 返回C:/zbsource/rdp.app.front/log
	 * @param base
	 * @param names
	 * @return
	 */
	public static String concat(String base, String... names)
	{
		if (names == null || names.length == 0) return base;
		if (base == null || base.trim().length() == 0)
		{
			base = "";
			for (String name : names)
			{
				if (name == null || name.trim().length() == 0) continue;
				base = FilenameUtils.concat(base, name);
			}

			return base.replace('\\', '/');
		}

		for (String name : names)
		{
			if (name == null || name.trim().length() == 0) continue;
			base = FilenameUtils.concat(base, name);
		}
		if (base == null) return base;

		return base.replace('\\', '/');
	}


	public static boolean wildcardMatchs(String name, String filters)
	{
		if (filters == null || filters.trim().length() == 0) return true;

		String[] tFitler = filters.split(",");
		for (String filter : tFitler)
		{
			if (wildcardMatch(name, filter)) return true;
		}
		return false;
	}


	public static String replaceSeparator(String path)
	{
		return replaceSeparator(path, '\\', '/');
	}

	public static String replaceSeparator(String path, char source, char target)
	{
		if (path == null || path.indexOf(source) < 0) return path;
		StringBuffer sbText = new StringBuffer("");

		char last = (char) -1;

		for (int i = 0; i < path.length(); i++)
		{
			if (path.charAt(i) == source || path.charAt(i) == target)
			{
				if (last != source && last != target) sbText.append(target);
			}
			else
			{
				sbText.append(path.charAt(i));
			}
			last = path.charAt(i);
		}

		return sbText.toString();
	}

	public static String subcat(File file, File base)
	{
		try
		{
			return subcat(file.getCanonicalPath(), base.getCanonicalPath());
		}
		catch (IOException e)
		{
//			throw RuntimeException(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}




	public static String subcat(File file, String base)
	{
		try
		{
			return subcat(file.getCanonicalPath(), base);
		}
		catch (IOException e)
		{
//			throw LangUtil.newRuntimeException(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public static String subcat(String path, String base)
	{
		if (path == null || base == null)
            throw new RuntimeException("路径不正确:path"+ path+",base"+ base);
//		    throw LangUtil.newRuntimeException("路径不正确:[%s]:{%s}", path, base);

		File fPath = new File(path);
		File fBase = new File(base);

		try {
			path = fPath.getCanonicalPath().substring(fBase.getCanonicalPath().length()).replace('\\', '/');
			path = path.substring(1);
		} catch (IOException e) {
//			throw LangUtil.newRuntimeException(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return path;
	}

	public static String getLocation(File file)
	{
		try
		{
			return file.getCanonicalPath().replace('\\', '/');
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static String getParentName(String path)
	{
		File file = new File(path);

		return file.getParentFile().getName();
	}

	public static String getParentPath(File file)
	{
		try
		{
			if (file == null || file.getParentFile() == null) return null;
			return file.getParentFile().getCanonicalPath().replace('\\', '/');
		} catch (IOException e) {
//			throw LangUtil.newRuntimeException(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String getParentPath(String path)
	{
		File file = new File(path);

		try
		{
			if (file == null || file.getParentFile() == null) return path;
			return file.getParentFile().getCanonicalPath().replace('\\', '/');
		} catch (IOException e) {
//			throw LangUtil.newRuntimeException(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}



	public static String getLocation(String val)
	{
		File file = FileUtil.getFile(val);
		return getLocation(file);
	}


	public static String getFolderName(String name)
	{
		if (name.endsWith("/")) name = name.substring(0, name.length() - 1);
		if (name.contains("/")) return name.substring(name.lastIndexOf('/'));
		else
			return name;
	}
	public static String getPath(File file) {

		try
		{
			return file.getCanonicalPath().replace('\\', '/');
		} catch (IOException e) {
//			throw LangUtil.newRuntimeException(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
