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

public class Parser implements MCScriptObject {
	
	private String code;
	private String file;
	
	/**
	 * @param code the code for the parser {@link String}
	 * @param file the file that contains the code (for errors) {@link String}
	 */
	public Parser(String code, String file) {
		
		code = "#file: "+file+LINESEPERATOR+code;
		this.code = code;
		this.file = file;
		
	}
	
	/**
	 * @param code the code for the parser {@link String}
	 */
	public Parser(String code) {
		this(code, "(Unknown File)");
	}
	
	/**
	 * @param code the code {@link String}
	 */
	public void setCode(String code) {
		
		this.code = code;
		
	}
	
	/**
	 * @return the code {@link String}
	 */
	public String getCode() {
		
		return this.code;
		
	}
	
	
	/**
	 * @return the parsed content {@link ParsedObject}
	 */
	public ParsedObject parse() {
		
		String cont = this.code+LINESEPERATOR;
		Parsable code = new Parsable(cont);
		ParsingExecutor executor = new ParsingExecutor(code, file);
		ParsedObject parsed = executor.parse();
		
		return parsed;
		
	}
}