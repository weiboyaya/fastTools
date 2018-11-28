package com.free.utils.file.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import com.free.utils.file.interfac.FileUtilMACRO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import com.free.utils.file.interfac.CxFileFilterI;




public class FileFunc extends FileUtils implements FileUtilMACRO
{
//	public static void main(String[] args)
//	{
//		listFolder("E:/bbb/com.sunline.rdp.site/features", new CxFileFilterAdapter()
//		{
//			public void display(String parent, String name)
//			{
//				CXLogger.detial("[%s]:[%s]", parent, name);
//				System.out.println(name);
//			}
//		});
//	}
	
	
	public static void listFolder(final String folder, final CxFileFilterI filter)
	{
		FileFilterAdapter dirFilter = new FileFilterAdapter()
		{
			public boolean accept(File subFile)
			{
				if (subFile.isFile())
				{
					try
					{
						filter.display(subFile.getParentFile().getCanonicalPath(),subFile.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else if (subFile.isDirectory() && !filter.isBreak())
				{
					if (filter.getSkipFolders().contains(subFile.getName())) return false;
					try {
						if (!filter.validateFolder(subFile.getCanonicalPath().replace('\\', '/'))) return false;
					} catch (IOException e) {
//						throw LangUtil.newRuntimeException(e);
						throw new RuntimeException(e);
					}
					subFile.listFiles((FilenameFilter) this);
					subFile.listFiles((FileFilter) this);
				}
				
				return false;
			}
		};
		new File(folder).listFiles((FileFilter) dirFilter);
	}
	
	public static void listWildcardDirs(File root, final String wildcard, final List<File> files)
	{
		FileFilterAdapter dirFilter = new FileFilterAdapter()
		{
			public boolean accept(File dir, String name)
			{
				boolean fetch = FilenameUtils.wildcardMatch(name, wildcard, IOCase.SENSITIVE);
				if (fetch)
				{
					files.add(new File(FilenameUtil.concat(dir.getPath(), name)));
				}
				else
				{
					listWildcardDirs(new File(FilenameUtil.concat(dir.getPath(), name)), wildcard, files);
				}
				return false;
			}
			
			public boolean accept(File dir)
			{
				if (dir.isDirectory())
				{
					dir.listFiles((FilenameFilter) this);
					dir.listFiles((FileFilter) this);
				}
				return false;
			}
		};
		
		root.listFiles((FileFilter) dirFilter);
	}
}
