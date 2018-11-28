package com.free.utils.file.impl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


import com.free.utils.file.interfac.CxFileFilterI;
import com.free.utils.file.interfac.FileFilterI;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class FileUtil extends FileFunc
{
	public static void delete(String folder, final String filter)
	{
		listFolder(folder, new CxFileFilterAdapter()
		{
			@Override
			public boolean validateFolder(String folderName)
			{
				if (folderName.endsWith(".svn")) return false;
				return super.validateFolder(folderName);
			}

			@Override
			public void display(String parent, String name)
			{
				if (!FilenameUtil.wildcardMatchs(name, filter)) return;
				FileUtil.delete(FilenameUtil.concat(parent, name));
			}
		});
	}
	
	public static void delete(String path)
	{
		File file = FileUtil.getFile(path);
		
		if (file.exists())
		{
			if (file.isDirectory())
			{
				try {
					FileUtil.deleteDirectory(file);
				} catch (IOException e) {
//					throw LangUtil.newRuntimeException(e);
					throw new RuntimeException(e);
				}
			}
			else
			{
				FileUtil.deleteQuietly(file);
			}
		}
	}
	
	public static void writeByteArrayToFile(String path, byte[] tData, boolean append)
	{
		try
		{
			FileUtils.writeByteArrayToFile(new File(path),tData,append);
		}
		catch (IOException e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	public static void writeByteArrayToFile(String path, byte[] tData)
	{
		try
		{
			FileUtils.writeByteArrayToFile(new File(path),tData);
		}
		catch (IOException e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	private static final String REGEX_MATCH = ".+\\.(jar|zip)(/.*|$)";
	/**
	 * 判断是否为jar格式的目录
	 * @param path
	 * @return
	 */
	public static boolean isJarFormat(String path){
		/*if(path.indexOf(".jar/") != -1 || path.indexOf(".zip/") != -1 
				|| path.endsWith(".jar") || path.endsWith(".zip"))
			return true;*/
		if(path != null && path.matches(REGEX_MATCH))
			return true;
		return false;
	}
	
	static class JarFormatPath {
		public String jarPath;
		public String childPath;
		@Override
		public String toString() {
			return "JarFormatPath [jarPath=" + jarPath + ", childPath="
					+ childPath + "]";
		}
	}
	
	private static final String REGEX_SPLIT = "(?<=\\.(jar|zip))/";
	
	private static JarFormatPath parseJarPath(String jarformatPath){
		if(!isJarFormat(jarformatPath))
			return  null;
		String[] paths = jarformatPath.split(REGEX_SPLIT);
		
		JarFormatPath jarFormatPath = new JarFormatPath();
		jarFormatPath.jarPath = paths[0];
		if(paths.length == 2 && paths[1] != null){
			jarFormatPath.childPath = paths[1];
		}
		
		return jarFormatPath;
	}
	
	public static String[] getPathSegments(String path){
		if(isJarFormat(path)){
			JarFormatPath jarFormatPath = parseJarPath(path);
//			if(jarFormatPath != null){
				return new String[] {jarFormatPath.jarPath, jarFormatPath.childPath};
//			}
		}else{
			return new String[] { path };
		}
		
//		return new String[0];
	}
	
	
	public static byte[] getJarEntryBytes(String zip, String entryname){
		InputStream entryInputStream = null;
		ZipFile zipFile = null;
		byte[] ret = null;
		try {
			zipFile = new ZipFile(zip);
			ZipEntry entry = zipFile.getEntry(entryname);
			if(entry == null)
				return null;
			entryInputStream = zipFile.getInputStream(entry);
			ret = inputStream2Bytes(entryInputStream);
		} catch (IOException e) {
//			CXLogger.error(e);
			e.printStackTrace();
		}finally{
			if(zipFile != null)
				try {
					zipFile.close();
				} catch (IOException e) {
//					CXLogger.error(e);
					e.printStackTrace();
				}
		}
		
		return ret;
	}
	
	public static String inputStream2Str(InputStream in){
		byte[] bytes = inputStream2Bytes(in);
		return new String(bytes);
	}
	
	public static byte[] inputStream2Bytes(InputStream in){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			IOUtils.copy(in, out);
		} catch (IOException e) {
//			CXLogger.error(e);
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(out);
		}
		
		return out.toByteArray();
	}
	
	public static byte[] readFileToByteArray(String path)
	{
		ByteArrayOutputStream bos = null;
		FileInputStream fis = null;
		InputStream in = null;
		try
		{
			if(isJarFormat(path)){
				JarFormatPath pathInfo = parseJarPath(path);
				if(pathInfo != null && pathInfo.childPath != null){
					return getJarEntryBytes(pathInfo.jarPath, pathInfo.childPath);
				}
			}
			
			return FileUtils.readFileToByteArray(new File(path)); //old
//			File file = new File(path);
//			fis = new FileInputStream(file);
//			
//			byte[] tData = new byte[(int) file.length()];
//			fis.read(tData);
//			return tData;
//			int len;
//			byte[] tData = new byte[1024 * 4];
//			bos = new ByteArrayOutputStream();
//			while ((len = fis.read(tData, 0 , tData.length)) > 0)
//			{
//				bos.write(tData, 0, len);
//			}
//			return bos.toByteArray();
		}
		catch (IOException e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
		finally
		{
			if (bos != null) CloseUtil.close(bos);
			if (fis != null) CloseUtil.close(fis);
			if(in != null) CloseUtil.close(in);
		}
	}
	
	public static void writeRandom(String path, String context)
	{
		RandomAccessFile rf1=null;
		
		try 
		{		
			File file = new File(path);
			if (!(file.exists()))
			{
				if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
				file.createNewFile();
			}
			
			rf1= new RandomAccessFile(file, "rw");
			rf1.seek(rf1.length());
			rf1.write(context.getBytes());
			rf1.close();
			return;
		} catch (Exception e) {
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}finally{
			try
			{
				if( rf1!=null )rf1.close();
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	
	public synchronized static void write(String path, String context, boolean append)
	{
		try
		{
			File file = new File(path);
			if (!file.getParentFile().exists()) makeDirs(file.getParent());
			write(file, context, "UTF-8", append);
		}
		catch (Exception e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	public static void moveFile(String source, String target)
	{
		try
		{
			File fTarget = new File(target);
			makeDirs(fTarget.getParentFile());
			deleteQuietly(fTarget);
			moveFile(new File(source), fTarget);
		} catch (IOException e) {
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	public static void moveDir(String source, String target)
	{
		try
		{
			File fTarget = new File(target);
			makeDirs(fTarget.getParentFile());
			moveDirectoryToDirectory(new File(source), fTarget, true);
		} catch (IOException e) {
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	public static File getFile(boolean careteDir, String path)
	{
		File file = FileUtils.getFile(path);
		if (careteDir && !file.getParentFile().exists()) makeDirs(file.getParentFile());
		
		return file;
	}
	
	public static File getFile(String... path)
	{
		File file = FileUtils.getFile(path);
		if (file.getParentFile() != null && !file.getParentFile().exists()) makeDirs(file.getParentFile());
		
		return file;
	}
	
	public static File createTmpFile(String path)
	{
		File file;
		path = path + ".temp";
		file = getFile(path);
		return file;
	}
	
	public static void createNewFile(String path)
	{
		File file = getFile(path);
		
			try {
				if (!file.exists()) file.createNewFile();
			} catch (IOException e) {
//			throw LangUtil.newRuntimeException(e);
				throw new RuntimeException(e);
			}
	}
	
	public static boolean exist(String path, String... paras)
	{
		String file = FilenameUtil.concat(path, paras);
		if(isJarFormat(file)){
			final JarFormatPath jarFormatPath = parseJarPath(file);
			if(jarFormatPath.childPath == null){
				return new File(jarFormatPath.jarPath).exists();
			}
			
			boolean exist = false;
			VirtualFile vf = VirtualFile.createVFile(jarFormatPath.jarPath);
			if(vf != null){
				vf = vf.getFile(jarFormatPath.childPath);
				exist = vf != null;
			}
			
			return exist;
		}else{
			return new File(file).exists();
		}
	}
	
	public static void copyFile(String source, String target)
	{
		File filesource, filetarget;
		
		filesource = FileUtils.getFile(source);
		if (!filesource.exists())
		{
//			throw LangUtil.newRuntimeException("source:[%s] not exist !!! ERROR, target:[%s]", filesource, target);
			throw new RuntimeException("source:"+filesource+" not exist !!! ERROR, target:"+target);
		}
		
		filetarget = FileUtils.getFile(target);
		if (filetarget.exists()) filetarget.delete();
		if (!filetarget.getParentFile().exists()) filetarget.getParentFile().mkdirs();
		
		try
		{
			FileUtils.copyFile(filesource, filetarget);
		}
		catch (IOException e)
		{
//			CXLogger.infor("Copy File ERROR !!! source:[%s],target:[%s]", filesource, target);
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	public static void makeDirs(File file)
	{
		if (!file.exists())
		{
			file.mkdirs();
		}
		else if (!file.isDirectory())
		{
			file = file.getParentFile();
			file.mkdirs();
		}
	}

	public static void makeDirs(String sPath)
	{
		File file = new File(sPath);
		if (!file.exists())
		{
			file.mkdirs();
		}
		else if (!file.isDirectory())
		{
			file = file.getParentFile();
			file.mkdirs();
		}
	}
	
	public static void listFilenames(final String rootDir, final String wildcard, final List<String> names, final boolean bSub, final boolean bCASE)
	{
		final FileFilterI filefilter = new FileFilterAdapter()
		{
			public boolean accept(File file, File dir, String name)
			{
				boolean fetch = FilenameUtil.wildcardMatch(name, wildcard, bCASE ? IOCase.SENSITIVE : IOCase.INSENSITIVE);
				if (fetch) names.add(FilenameUtil.subcat(file, new File(rootDir)));
				return false;
			}
			
			
			public boolean accept(File file)
			{
				if (file.isDirectory() && bSub)
				{
					file.listFiles((FileFilter) this);
				}
				else
				{
					accept(file, file.getParentFile(), file.getName());
				}
				return false;
			}
		};
		
		File file = FileUtil.getFile(rootDir);
		if (!file.exists() || !file.isDirectory()) throw new RuntimeException(rootDir+"路径异常");
		file.listFiles((FileFilter) filefilter);
	}
	
	public static void listFilenames(String roorDir, String wildcard, List<String> names, boolean bSub)
	{
		listFilenames(roorDir, wildcard, names, bSub, true);
	}
	
	public static List<String> listWildcardPath(String roorDir, String wildcard, boolean bSub)
	{
		 Collection<File> files;
		 List<String> paths = new ArrayList<String>(5);
		 FileFilter fileFilter = new WildcardFileFilter(wildcard);
		 if (bSub)
		 {
			 files = FileUtil.listFiles(new File(roorDir), FileFilterUtils.asFileFilter(fileFilter), FileFilterUtils.directoryFileFilter());
		 }
		 else
		 {
			 files = FileUtil.listFiles(new File(roorDir), FileFilterUtils.asFileFilter(fileFilter), null);
		 }
		 
		 for (File file : files)
		 {
			 try
			 {
				paths.add(file.getCanonicalPath());
			} catch (IOException e) {
//				throw LangUtil.newRuntimeException(e);
				 throw new RuntimeException(e);
			}
		 }
		 
		 return paths;
	}
	
	
	public static List<String> listFolder(String path)
	{
		final List<String> folders = new ArrayList<String>();
		if(isJarFormat(path)){
			final JarFormatPath pathInfo = parseJarPath(path);
			if(pathInfo != null){
				execJarFileTask(pathInfo.jarPath, new JarFileTask() {
					@Override
					public void run(JarFileX jarfile) {
						VirtualFile vf = VirtualFile.createVFile(jarfile);
						if(pathInfo.childPath != null){
							vf = vf.getFile(pathInfo.childPath, true);
						}
						if(vf != null){
							for(VirtualFile childvf: vf.getChildren()){
								if(childvf.isDirectory()){
									String result = FilenameUtil.concat(pathInfo.jarPath, childvf.toString());
									folders.add(result);
								}
							}
						}
					}
				});
			}
		}else{
			File dir = new File(path);
			
			dir.listFiles(new FileFilter()
			{
				public boolean accept(File pathname)
				{
					if (pathname.isDirectory())
						try {
							folders.add(pathname.getCanonicalPath().replace('\\', '/'));
						} catch (IOException e) {
							e.printStackTrace();
						}
					return false;
				}
			});
		}
		
		return folders;
	}
	
	public static List<String> listFolderAndFiles(final String path)
	{
		final List<String> folders = new ArrayList<String>();
		File dir = new File(path);
		
		dir.listFiles(new FileFilter() {
			
			public boolean accept(File pathname) {
				folders.add(FilenameUtil.subcat(pathname, path));
				return false;
			}
		});
		
		return folders;
	}
	
	public static List<String> listWildcardPath(String roorDir, String wildcard)
	{
		 Collection<File> files;
		 List<String> paths = new ArrayList<String>(5);
		 files = listWildcardFiles(roorDir, wildcard);
		 for (File file : files)
		 {
			 try
			 {
				paths.add(file.getCanonicalPath());
			} catch (IOException e) {
//				throw LangUtil.newRuntimeException(e);
				 throw new RuntimeException(e);
			}
		 }
		 
		 return paths;
	}
	
	public static Collection<File> listWildcardFiles(String roorDir, String wildcard)
	{
		return listWildcardFiles(new File(roorDir), wildcard);
	}
	
	public static Collection<File> listWildcardFiles(File rootDir, String wildcard)
	{
		FileFilter fileFilter = new WildcardFileFilter(wildcard);
		Collection<File> files = FileUtil.listFiles(rootDir, FileFilterUtils.asFileFilter(fileFilter), FileFilterUtils.directoryFileFilter());
		
		return files;
	}
	
	public static boolean isDirFile(String rootDir)
	{
		if(isJarFormat(rootDir)){
			final JarFormatPath jarFormatPath = parseJarPath(rootDir);
			if(jarFormatPath.childPath == null)
				return false;
			
			boolean isDir = false;
			VirtualFile vf = VirtualFile.createVFile(jarFormatPath.jarPath);
			if(vf != null){
				vf = vf.getFile(jarFormatPath.childPath, true);
				isDir = vf != null;
			}
			
			return isDir;
		}else{
			File file = new File(rootDir);
			if (file.exists() && file.isDirectory()) 
				return true;
			return false;
		}
	}
	
	
	public static String readFileToStringByDefault(String path)
	{
		try
		{
			return new String(readFileToByteArray(path));
//			return readFileToString(new File(path),"UTF-8");
		} catch (Exception e)
		{
			return "";
		}
	}
	
	public static String readFileToString(String path)
	{
		try {
//			return readFileToString(new File(path));
			return new String(readFileToByteArray(path),"UTF-8");
		} catch (Exception e) {
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	public static void readLines(List<String> lines, String path)
	{
		try {
			List<String> tmps = FileUtil.readLines(new File(path), "UTF-8");
			if (tmps != null) lines.addAll(tmps);
		} catch (IOException e) {
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	public static void readLines(List<String> lines, InputStream is)
	{
		InputStreamReader reader = new InputStreamReader(is);
		BufferedReader buffer = new BufferedReader(reader);
		
		String line;
		try
		{
			while ((line = buffer.readLine()) != null)
			{
				lines.add(line);
			}
		}
		catch (Exception e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
		finally
		{
			CloseUtil.close(buffer);
			CloseUtil.close(reader);
		}
	}

	public static String getLocation(URI uri)
	{
		File file = new File(uri.getPath());
		try
		{
			return file.getCanonicalPath();
		}
		catch (IOException e)
		{
//			throw LangUtil.newRuntimeException(e);
			throw new RuntimeException(e);
		}
	}
	
	public interface JarFileTask {
		void run(JarFileX jarfile);
	}
	
	public static void  execJarFileTask(String jarpath, JarFileTask task){
		JarFileX jf = null;
		try {
			jf = new JarFileX(jarpath);
			task.run(jf);
		} catch (IOException e) {
//			CXLogger.error(e);
			e.printStackTrace();
		}finally{
			if(jf != null)
				try {
					jf.close();
				} catch (IOException e) {
//					CXLogger.error(e);
					e.printStackTrace();
				}
		}
		
	}
	
	static class FileInfo {
		String path;
		boolean isDirectory;
		public FileInfo(String path, boolean isDirectory){
			this.path = path;
			this.isDirectory = isDirectory;
		}
	}
	
	
	public static void listFolder(final String folder, final CxFileFilterI filter){
		listFolder(folder, filter, true);
	}
	
	/**
	 * 遍历目录里的所有文件（支持jar）
	 * @param folder
	 * @param filter
	 * @param recursively 是否递归遍历
	 */
	public static void listFolder(final String folder, final CxFileFilterI filter, 
			final boolean recursively){
		if(!isJarFormat(folder)){
			if(!recursively){
				final List<FileInfo> folders = new ArrayList<FileInfo>();
				File dir = new File(folder);
				
				dir.listFiles(new FileFilter()
				{
					public boolean accept(File pathname)
					{
//						if (pathname.isDirectory())
							try {
								String path = pathname.getCanonicalPath().replace('\\', '/');
								folders.add(new FileInfo(path, pathname.isDirectory()));
							} catch (IOException e) {
								e.printStackTrace();
							}
						return false;
					}
				});
				
				//遍历
				for(FileInfo fileinfo: folders){
					String parent = fileinfo.isDirectory?fileinfo.path:FilenameUtil.getParent(fileinfo.path);
					String name = fileinfo.isDirectory?"":FilenameUtil.subcat(fileinfo.path, parent);
					filter.display(parent, name);
				}
			}else{
				FileFunc.listFolder(folder, filter);
			}
		}
		else{
			final JarFormatPath pathInfo = parseJarPath(folder);
			if(pathInfo != null){
				execJarFileTask(pathInfo.jarPath, new JarFileTask() {
					@Override
					public void run(JarFileX jarfile) {
						VirtualFile vf = VirtualFile.createVFile(jarfile);
						if(pathInfo.childPath != null){
							vf = vf.getFile(pathInfo.childPath, true);
						}
						if(vf != null){
							if(!recursively){
								for(VirtualFile childvf: vf.getChildren()){
									String fullname = childvf.toString();
									boolean isDirectory = childvf.isDirectory();
									fullname = FilenameUtil
											.concat(pathInfo.jarPath, fullname);
									String parent = isDirectory ? fullname : FilenameUtil.getParent(fullname);
									String name = isDirectory ? "" : FilenameUtil.subcat(fullname, parent);
									
									filter.display(parent, name);
								}
							}else{
								vf.accept(new VirtualFile.Visitor() {
									@Override
									public void visit(String entry, boolean isDirect) {
										if(!isDirect){
											int lastIndex = entry.lastIndexOf("/");
											String parent = "";
											String name = entry;
											if(lastIndex != -1){
												parent = entry.substring(0, lastIndex);
												name = entry.substring(lastIndex + 1);
											}
											filter.display(FilenameUtil.concat(pathInfo.jarPath, parent),name);
										}
									}
								});
							}
						}
					}
				});
			}
		}
	}
	
	
	public static void main(String[] args) {
//		String jar = "‪C:\\Users\\Jann\\Desktop\\ziptest.zip";
		String jar = "G:\\Users\\Jann\\Desktop\\com.cxsw.exec.jar";
//		System.out.println(FileUtil.exist(jar, "com/sunline/rdp/tran"));
//		System.out.println(FileUtil.isDirFile(FilenameUtil.concat(jar, "com/cxsw/plugin/form/Activator.class")));
//		System.out.println(jar.matches(REGEX_MATCH));
//		FileUtil.listFolder(jar, new CxFileFilterAdapter(){
//			@Override
//			public void display(String parent, String name) {
//				System.out.println(FilenameUtil.concat(parent, name));
//			}
//			
//		}, true);
		
//		System.out.println(FileUtil.listFolder(jar));
		
		JarFileX jf = null;
		try {
			jf = new JarFileX(jar);
			System.out.println(jf.getManifest().getEntries().keySet());;
			Enumeration<? extends ZipEntry> jarEntrys = jf.entries();
			ZipEntry jarentry = null;
			while (jarEntrys.hasMoreElements()) {
				jarentry = jarEntrys.nextElement();
				System.out.println(jarentry.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

	
}