/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.3
 *
 */
public class BooleanDeclaration extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		if(!parsable.isActualWord())return false;

		String keyword = parsable.actualWord();
		int line = parsable.getLine();
		
		if(keyword.equals("boolean")||keyword.equals("bool")||keyword.equals("tag")) {
			
			
			parsable.skipWord();
			parsable.skipIgnored();
				
			if(!parsable.isActualWord())throwErr("Declaration of boolean without a name");
			String name = parsable.actualWord();
			String player = null;
				
			parsable.skipWord();
			parsable.skipIgnored();

			if(parsable.isActualWord()) {
				player = parsable.actualWord();
				parsable.skipWord();
				parsable.skipIgnored();
			}
			
			if(!parsable.actual().equals("="))throwErr("Boolean must have a value");
					
			parsable.skip();
			parsable.skipIgnored();
				
			if(!parsable.isActualWord()) throwErr("The value of a boolean can just be true of false or another boolean");
					
			Content c = new Content("booleandeclaration", new String[]{
				name, player, parsable.actualWord()
			}, line);
							
			parsed.add(c);
				
			parsable.skipWord();
			parsable.skipIgnored();
					
			return true;
		}
		return false;
	}
}
