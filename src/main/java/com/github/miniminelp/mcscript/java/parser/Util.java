/**
 * 
 */

package com.github.miniminelp.mcscript.java.parser;

import com.github.miniminelp.mcscript.java.util.MCScriptObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

public class Util implements MCScriptObject {
	
	public static final String KEYWORDS = " then true false "
			+ "for as at asat positioned align dimension rotated "
			+ "anchored while stop continue "
			+ "default tag score ";
	
	public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_123456789";
	
	public static final String NUMBERS = "1234567890";
	
	public static final String IGNORED = " \r\n\t	";
	
	public static final char LINEBREAK = LINESEPERATOR.toCharArray()[0];
	
	public static final String COUNTINGOPERATORS = "+-*/";
	
	public static final String OPERATORS = "><=";
}
