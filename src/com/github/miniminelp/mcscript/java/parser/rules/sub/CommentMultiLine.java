/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class CommentMultiLine extends ParserRule {
	
	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		if(parsable.actual().equals("/")&&parsable.peek().equals("*")){
			parsable.skip(2);
			while(!(parsable.actual().equals("*")&&parsable.peek().equals("/"))) {
				parsable.skip();
			}
			parsable.skip(2);
			return true;
		}
		return false;
	}
}
