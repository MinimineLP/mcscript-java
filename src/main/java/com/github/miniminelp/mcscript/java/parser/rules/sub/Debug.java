/**
 * 
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.3
 * @version 0.0.3
 *
 */
public class Debug extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		int line = parsable.getLine();
		
		if(!parsable.isActualWord())return false;
		if(!parsable.actualWord().equals("debug")) return false;
		
		Parsable backup = parsable.clone();
		
		parsable.skipWord();
		parsable.skipIgnored();
		
		if(!parsable.isActualWord()) {
			parsable = backup;
			return false;
		}
		
		String type = parsable.actualWord();
		
		parsable.skipWord();
		
		if(!parsable.actual().equals(":")) {
			parsable = backup;
			return false;
		}
		
		parsable.skip();
		parsable.skipIgnored();
			
		String message = parsable.getToNext(LINESEPERATOR.charAt(0));
		parsable.skipLine();
			
		Content c = new Content("debug", new String[] {type,message}, line);
		parsed.add(c);
		
		return true;
	}

}
