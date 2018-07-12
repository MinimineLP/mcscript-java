/**
 * 
 */

package com.github.miniminelp.mcscript.java.parser;

import com.github.miniminelp.mcscript.java.parser.rules.ParserRuleManager;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

public class ParsingExecutor extends SubParsingExecutor {

	/**
	 * @param p the parsable {@link Parsable} 
	 * @param file the file {@link String}
	 */
	public ParsingExecutor(Parsable p, String file) {
		super(p, file);
		this.rules=ParserRuleManager.getParserRules(p, parsed, file);
	}
	
	
}