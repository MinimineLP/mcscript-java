/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;
import com.github.miniminelp.mcscript.java.parser.Util;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class CommentSingleLineParsable extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		if(parsable.actual().equals("#")) {
			String s = parsable.getToNext(Util.LINEBREAK);
			Content c = new Content("comment", s, parsable.getLine());
			parsed.add(c);
			parsable.skipLine();
			return true;
		}
		return false;
	}

}
