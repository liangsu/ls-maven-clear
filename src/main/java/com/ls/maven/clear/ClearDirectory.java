package com.ls.maven.clear;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ClearDirectory {

	private static Set<String> CanDelFiles = new HashSet<String>();
	
	static{
		CanDelFiles.add("pom");
		CanDelFiles.add("repositories");
		CanDelFiles.add("sha1");
		CanDelFiles.add("properties");
	}
	
	public static void main(String[] args) {
		String baseDir = "D:\\Program Files (x86)\\apache-maven-3.3.9\\repository";
//		baseDir = "D:\\Program Files (x86)\\apache-maven-3.3.9\\repository\\org\\springframework\\spring-beans\\4.3.8.RELEASE";
		
		File file = new File(baseDir);
		if(!file.isDirectory()){
			return;
		}
		
		clearChildenDir(file);
	}
	
	public static void clearChildenDir(File file){
		if(!file.isDirectory()){
			return;
		}
		
		File[] fs = file.listFiles();
		
		// 判断该文件夹下是否全是文件
		boolean isAllFile = true;
		if(fs != null && fs.length > 0){
			for(int i = 0; i < fs.length; i++){
				if(fs[i].isDirectory()){
					isAllFile = false;
					break;
				}
			}
			
			//全是文件
			if(isAllFile){
				clearFile(fs);
				
			}else{// 不全是文件
				for(int i = 0; i < fs.length; i++){
					if(fs[i].isDirectory()){
						clearChildenDir(fs[i]);
					}
				}
			}
		}
	}
	
	public static void clearFile(File[] files){
		boolean hasBinJar = false;
		boolean hasSourceJar = false;
		for (File file : files) {
			if(file.getName().endsWith("-sources.jar")){
				hasSourceJar = true;
				
			}else if(file.getName().endsWith(".jar")){
				hasBinJar = true;
			}
		}
		
		if(hasBinJar && !hasSourceJar){
			for (File file : files) {
				if(file.getName().endsWith("-sources.jar.sha1")){
					deleteFile(file);
					System.out.println("---------------");
					break;
				}
			}
			
		}else if(!hasBinJar && hasSourceJar){
			for (File file : files) {
				if(!file.getName().contains("-sources.jar")){
					deleteFile(file);
				}
			}
			System.out.println("---------------");
			
		}else if(!hasBinJar && !hasSourceJar){
			File parentFile = files[0].getParentFile();
			int delFileNum = 0;
			for (File file : files) {
				delFileNum += deleteFile(file) ? 1 : 0;
			}
			if(delFileNum == files.length){
				deleteFile(parentFile);
			}
			System.out.println("---------------");
		}
		
	}
	
	public static boolean deleteFile(File file){
		boolean success = false;
		String fileName = file.getName();
		
		if(file.isDirectory()){// 删除文件夹
			success = file.delete();
			System.out.println("delete file: [" + fileName+"] " + success);
			
		}else{// 删除文件
			String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
			if(CanDelFiles.contains(ext)){
				success = file.delete();
				System.out.println("delete file: [" + fileName+"] " + success);
			}
		}
		
		return success;
	}
}
