/**
 * 
 */

package com.github.miniminelp.mcscript.java.filemanager;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

public class SystemFiles extends OutFile {
	
	private static List<String> scoreboardobjectives = new LinkedList<String>();
	
	/**
	 * @param filename the filename
	 */
	public SystemFiles(String filename) {
		super(filename);
		content = "#######" + LINESEPERATOR +  
				  "# File: ./"+filename + LINESEPERATOR +
				  "# " + LINESEPERATOR +
				  "# mcscript system file" + LINESEPERATOR +
				  "# please do not touch this file!" + LINESEPERATOR + 
			      "# it is used by the compiler!" + LINESEPERATOR +
			      "# " + LINESEPERATOR +
			      "# Generated by Minecraft Script for 1.13" + LINESEPERATOR +
				  "######" + LINESEPERATOR + 
				  LINESEPERATOR +
				  "# Create MCScript boolean Armor Stand" + LINESEPERATOR +
				  "execute unless entity @e[tag=mcscriptTags] at @p run summon armor_stand ~ ~ ~ {Tags:[mcscriptTags],Invisible:1,Invulnerable:1,NoGravity:1}"+LINESEPERATOR+LINESEPERATOR;
	}
	
	/**
	 * @see OutFile#getContent()
	 */
	@Override
	public String getContent() {
		return content;
	}
	
	/**
	 * @param content the content to set
	 */
	@Override
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * @see OutFile#write(java.lang.String)
	 */
	@Override
	public void write(String cmd) {
		super.write(cmd);
	}
	
	/**
	 * @param cmd the command to execute
	 */
	public static void addLoadContent(String cmd) {
		
		if(!Files.hasFile("mcscript/load")) createLoadFile();
		Files.getFile("mcscript/load").write(cmd);
		
	}
	
	/**
	 * @return the load file content {@link String}
	 */
	public static String getLoad() {
		
		if(!Files.hasFile("mcscript/load"))return null;
		return Files.getFile("mcscript/load").getContent();
		
	}
	
	public static boolean createLoadFile() {
		if(!Files.hasFile("mcscript/load")) {
			Files.setFile(new SystemFiles("mcscript/load"));
			return true;
		}
		return false;
	}
	
	public static void createScoreboardObjective(String name) {
		for(String s : scoreboardobjectives) {
			if(s.equals(name))return;
		}
		scoreboardobjectives.add(name);
	}
	
	/**
	 * <p>This method finishes the load generation and finaly creates
	 *  all scoreboard objectives. You have to call it in the main function,
	 *  before write the content down with the filesystem</p> 
	 */
	public static void readyLoadGeneration() {
		for(String s : scoreboardobjectives) {
			addLoadContent("scoreboard objecteves add "+s+" dummy");
		}
	}
}