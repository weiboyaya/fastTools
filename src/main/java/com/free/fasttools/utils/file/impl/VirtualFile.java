package com.free.utils.file.impl;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;




class VirtualFile {
	
	private String name;
	private boolean isDirectory;
	private boolean isRoot;
	private VirtualFile parent;
	//子元素用Hashmap存储，加块查找速度
	Map<String, VirtualFile> children = new HashMap<String, VirtualFile>();
//	List<VirtualFile> children = new ArrayList<>();
	
	private VirtualFile rootVFile = null;
	
	private static Map<String, VirtualFile> caches = 
			new HashMap<String, VirtualFile>();
	
	public static VirtualFile createVFile(String jarpath){
		JarFileX jf =null;
		try {
			jf = new JarFileX(jarpath);
			
			String cacheKey = jarpath + jf.getFile().lastModified();
//			CXLogger.detial("jar key:[%s] , caches:[%s]", cacheKey, caches);
			if(caches.containsKey(cacheKey)){
				return caches.get(cacheKey);
			}
			
			VirtualFile vf = createVFile(jf);
			caches.put(cacheKey, vf);
			return vf;
		}catch(Throwable e){
			e.printStackTrace();
//			CXLogger.error(e);
		}finally{
			if(jf != null)
				try {
					jf.close();
				} catch (IOException e) {
					e.printStackTrace();
//					CXLogger.error(e);
				}
		}
		
		return null;
	}
	
	public static VirtualFile createVFile(JarFileX jarfile){
		//获取缓存
		String jarpath = jarfile.getFile().getAbsolutePath();
		jarpath = FilenameUtil.separatorsToUnix(jarpath);
		String cacheKey = jarpath + jarfile.getFile().lastModified();
//		CXLogger.detial("jar key:[%s] , caches:[%s]", cacheKey, caches);
		if(caches.containsKey(cacheKey)){
			return caches.get(cacheKey);
		}
		
		VirtualFile root = newRootVFile();
		try {
			Enumeration<? extends ZipEntry> jarEntrys = jarfile.entries();
			ZipEntry jarentry = null;
			while (jarEntrys.hasMoreElements()) {
				jarentry = jarEntrys.nextElement();
//				System.out.println("```````````" + jarentry.getName());
				initVFile(root, root, jarentry.getName());
			}
		}catch(Throwable e){
			e.printStackTrace();
//			CXLogger.error(e);
		}
		
		caches.put(cacheKey, root);
		
		return root;
	}
	
	private static void initVFile(VirtualFile root, VirtualFile parent, String jarentry){
		boolean initDirectory = false;
		if(jarentry.endsWith("/")){
			initDirectory = true;
		}
		String[] elems = jarentry.split("\\/");
		int length = elems.length;
		VirtualFile p = parent;
		for(int i = 0; i < length; i++){
			String elem = elems[i];
			boolean isdirectory = initDirectory ? true : i != length -1;
			VirtualFile n = getVFile(p, elem, isdirectory);
			if(n == null)
				n = new VirtualFile(p, elem, isdirectory);
			p = n;
			p.rootVFile = root;
		}
	}
	
	private static VirtualFile getVFile(VirtualFile parent, String name, boolean isDirectory){
		VirtualFile file = null;
		
		file = parent.getChild(name, isDirectory);
		
//		for(VirtualFile child: parent.children){
//			if(child.name.equals(name) && child.isDirectory == isDirectory){
//				file = child;
//				break;
//			}
//		}
		return file;
	}
	
	private static VirtualFile getVFile(VirtualFile parent, String name){
		VirtualFile file = null;
		file = parent.getChild(name, false);
		if(file == null)
			file = parent.getChild(name, true);
//		for(VirtualFile child: parent.children){
//			if(child.name.equals(name)){
//				file = child;
//				break;
//			}
//		}
		return file;
	}
	
	private static VirtualFile newRootVFile(){
		VirtualFile rootVFile = new VirtualFile(); 
		rootVFile.parent = null;
		rootVFile.isDirectory = true;
		rootVFile.name = "root";
		rootVFile.isRoot = true;
		return rootVFile;
	}
	
	private VirtualFile(){
	}
	

	private VirtualFile(VirtualFile parent, String name, boolean isDirectory) {
		super();
		this.parent = parent;
		this.name = name;
		this.isDirectory = isDirectory;
		parent.children.put(getMapKey(name, isDirectory), this);
	}
	
	
	private String getMapKey(String name, boolean isDirectory){
		return name + isDirectory;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public VirtualFile getParent() {
		return parent;
	}

	public void setParent(VirtualFile parent) {
		this.parent = parent;
	}

	public Collection<VirtualFile> getChildren() {
		return children.values();
	}
	
	public VirtualFile getChild(String name, boolean isDirectory){
		return this.children.get(getMapKey(name, isDirectory));
	}


	public String getPath(){
		String dir = parent !=null ? parent.getPath() + name : name;
		if(isDirectory)
			dir += "/";
		return dir;
	}
	
	public String toString(){
		return trim(getPath());
	}
	
	
//	public List<VirtualFile> getChildren() {
//		return children;
//	}



	public static interface Visitor {
		void visit(String entry, boolean isDirect);
	}
	
	public void accept(Visitor visitor){
		visitor.visit(trim(getPath()), isDirectory);
		for(VirtualFile child: children.values()){
			child.accept(visitor);
		}
	}
	
	private String trim(String path){
		int firstSepIndex = path.indexOf("/");
		return path.substring(firstSepIndex + 1);
	}
	
	
	public VirtualFile getFile(String path, boolean isDirectory){
//		VFile root = isRoot? this: this.rootVFile;
		String[] elems = path.split("\\/");
		int length = elems.length;
		VirtualFile p = this;
		for(int i = 0; i < length; i++){
			String elem = elems[i];
			boolean isdirectory = isDirectory?true:i != length -1;
			VirtualFile n = getVFile(p, elem, isdirectory);
			if(n == null)
				return null;
			p = n;
		}
		
		return p;
	}
	
	public VirtualFile getFile(String path){
		String[] elems = path.split("\\/");
		int length = elems.length;
		VirtualFile p = this;
		for(int i = 0; i < length; i++){
			String elem = elems[i];
			VirtualFile n = getVFile(p, elem);
			if(n == null)
				return null;
			p = n;
		}
		
		return p;
	}
	
	public static void main(String[] args) {
		String jarString = "C:\\Users\\Jann\\Desktop\\com.cxsw.plugin.form.jar";
		VirtualFile file = VirtualFile.createVFile(jarString);
		file.accept(new Visitor() {
			@Override
			public void visit(String entry, boolean isDirect) {
				System.out.println(entry);
			}
		});
		
		//getfile
		System.out.println(file.getFile("com/cxsw/cxcode", true));
	}
	
//	public static void main(String[] args) {
//		String path = "C:/Users/Jann/Desktop/com.cxsw.plugin.form.jar";
//		String[] p = path.split(".jar/");
//		System.out.println(Arrays.toString(p));
//	}
}
