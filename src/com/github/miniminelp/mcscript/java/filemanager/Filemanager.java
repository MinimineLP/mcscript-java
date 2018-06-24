/**
 *
 */
package com.github.miniminelp.mcscript.java.filemanager;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class Filemanager {
	
	/**
	 * @param files the files to write down {@link HashMap}?extends {@link String}, {@link OutFile}
	 * @param dir the directory to write the files in {@link File}
	 * @throws IOException Can throw an {@link IOException}
	 */
	public static void writeDownInFS(HashMap<String, OutFile> files, File dir) throws IOException {		
		if(!dir.exists())mkdir_force(dir);
		for(Map.Entry<String, OutFile> entry : files.entrySet()) {
			OutFile file = entry.getValue();
			
			File f = new File(dir.getAbsolutePath()+"/"+entry.getKey()+".mcfunction"); 
			mkdir_force(f.getParentFile());
			
			if(f.exists())f.delete();
			
			f.createNewFile();
			Formatter x = new Formatter(f);
			x.format("%s", file.getContent());
			x.close();
			
		}
	}
	
	public static void mkdir_force (File f) {
		
		String[] parts = f.getAbsolutePath().replaceAll("\\\\", "/").split("/");
		
		for(int i=0;i<parts.length;i++) {
			
			String path = "";
			
			for(int c=0;c<=i;c++)
				path+=parts[c]+"/";
			
			File file = new File(path);
			if(!file.exists())file.mkdirs();
		}
		
	}
	
}
