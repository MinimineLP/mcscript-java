/**
 * 
 */
package com.github.miniminelp.mcscript.java.parser.rules;

import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.util.MCScriptObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public abstract class ParserRule implements ParserRuleFunctions, MCScriptObject {
	
	protected Parsable parsable;
	protected ParsedObject parsed;
	protected String file;
	
	/**
	 * @return the success
	 */
	public abstract boolean apply();
	
	/**
	 * @return the parsable
	 */
	public Parsable getParsable() {
		return parsable;
	}

	/**
	 * @param parsable the parsable to set
	 */
	public void setParsable(Parsable parsable) {
		this.parsable = parsable;
	}

	/**
	 * @return the parsed
	 */
	public ParsedObject getParsed() {
		return parsed;
	}

	/**
	 * @param parsed the parsed to set
	 */
	public void setParsed(ParsedObject parsed) {
		this.parsed = parsed;
	}


	/**
	 * @param err The error String
	 */
	protected void throwErr(String err) {
		throwErr(err, parsable, file);
	}

	/**
	 * @return the statement
	 */
	protected String parseStatement() {
		StatementParseResult res = parseStatement(parsable);
		parsable = res.getParsable();
		return res.getResult();
	}
	
	/**
	 * @param parsable The Parsable
	 * @return the statement parse result {@link StatementParseResult}
	 */
	protected StatementParseResult parseStatement(Parsable parsable) {
		return parseStatement(parsable, file);
	}


	/**
	 * @param err the error
	 * @param line the line
	 * @param column the column
	 * @param actualLine the actual line content
	 */
	protected void throwErr(String err, int line, int column, String actualLine) {
		throwErr(err, line, column, actualLine, file);
	}
	
	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}
	
	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}
}
