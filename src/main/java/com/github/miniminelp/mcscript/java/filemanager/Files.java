/**
 * 
 */
package com.github.miniminelp.mcscript.java.filemanager;

import java.util.HashMap;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class Files {
	private static HashMap<String, OutFile> files = new HashMap<String, OutFile>();
	
	/**
	 * @param file file to write {@link String}
	 * @param content content to write into file {@link String}
	 */
	public static void write(String file, String content) {

		file = fix(file);
		if(!hasFile(file))createFile(file);
		files.get(file).write(content);
		
	}
	
	/**
	 * @param file file to get {@link String}
	 * @return the file {@link OutFile}
	 */
	public static OutFile getFile(String file) {

		file = fix(file);
		if(!files.containsKey(file))createFile(file);
		return files.get(file);
		
	}
	
	public static void setFile(OutFile content) {
		
		files.put(fix(content.getFilename()), content);
		
	}
	
	/**
	 * @return all the files {@link HashMap}?extends {@link String} {@link OutFile}
	 */
	public static HashMap<String, OutFile> getFiles() {
	
		return files;
		
	}
	
	/**
	 * @param file the file to test for {@link String}
	 * @return if that file exists
	 */
	public static boolean hasFile(String file) {

		file = fix(file);
		return files.containsKey(file);
		
	}

	/**
	 * @param file the file to create
	 */
	public static void createFile(String file) {
		
		file = fix(file);
		if(!hasFile(file))files.put(file, new OutFile(file));
		
	}
	
	
	/**
	 * @param tofix filename to fix
	 * @return fixed filename
	 */
	public static String fix(String tofix) {
		
		if(tofix.startsWith("./"))
			tofix = tofix.replaceFirst("\\.\\/", "");
		
		return tofix;
	}
	
	
	/**
	 * @param file the file to open
	 * @return the opened file
	 */
	public static FileContext open(String file) {
		
		return FileContext.open(file);
		
	}
}
