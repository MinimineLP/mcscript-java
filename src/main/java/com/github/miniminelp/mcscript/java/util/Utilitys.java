/**
 *
 */
package com.github.miniminelp.mcscript.java.util;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class Utilitys {
	public static final String IDTOKENS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_123456789";
	public static final int LENGTH=6;
	
	public static String generateRandomID() {
		String id = "";
		for(int i=0;i<LENGTH;i++) {
			int index = random(0, IDTOKENS.length()-1);
			id += IDTOKENS.charAt(index);
		}
		return id;
	}
	
	public static int random(int min, int max) {
		max -= min;
		return (int) Math.round(Math.random()*max+min);
	}
}
