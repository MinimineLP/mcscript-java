/**
 *
 */
package com.github.miniminelp.mcscript.java.filemanager;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class FileContext {
	
	private String file;

	/**
	 * @param file the file {@link String}
	 */
	private FileContext(String file) {
		this.setFile(file);
	}
	
	
	/**
	 * @param content the content to write {@link String}
	 */
	public void write(String content) {
		Files.write(file, content);
	}
	
	
	/**
	 * @return the file {@link String} 
	 */
	public String getFile() {
		return file;
	}
	
	
	/**
	 * @param file the file to set {@link String}
	 */
	public void setFile(String file) {
		this.file = file;
	}
	
	/**
	 * @param file the file {@link String}
	 * @return the file {@link FileContext}
	 */
	public static FileContext open(String file) {
		return new FileContext(file);
	}
}
