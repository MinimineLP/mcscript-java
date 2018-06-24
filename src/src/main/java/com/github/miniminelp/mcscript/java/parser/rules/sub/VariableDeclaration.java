/**
 * @project mcscript
 * @package com.github.miniminelp.mcscript.java.parser.rules.sub
 * @file: VarDeclaration.java
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class VariableDeclaration extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		if(!parsable.isActualWord())return false;
		

		String keyword = parsable.actualWord();
		int line = parsable.getLine();

		if(keyword.equals("var")) {
			
			parsable.skipWord();
			parsable.skipIgnored();
				
			if(!parsable.isActualWord())throwErr("Declaration of "+keyword+" without a name");
			String[] name = new String[] {parsable.actualWord(),parsable.actualWord()};
				
			parsable.skipWord();
			parsable.skipIgnored();
			
			if(parsable.isActualWord()) {
				
				name[1]=parsable.actualWord();

				parsable.skipWord();
				parsable.skipIgnored();
				
			}
			
			if(parsable.actual().equals("=")) {
					
				parsable.skip();
				parsable.skipIgnored();
					
				if(parsable.isActualNumber()) {
					
					String val = parsable.actualNumber();
						
					parsable.skipNumber();
						
					Content c = new Content("vardeclaration", new String[]{
						name[0], name[1], val,
					}, line);
						
					parsed.add(c);
						
				} else if(parsable.isActualWord()){
						
					String[] val = new String[] {parsable.actualWord(),parsable.actualWord()};
					
					parsable.skipWord();
					parsable.skipIgnored();
					
					if(parsable.isActualWord()) {
						val[1] = parsable.actualWord();
						parsable.skipWord();
						parsable.skipIgnored();
					}
					
					Content c = new Content("vardeclaration", new String[]{
						name[0], name[1], val[0], val[1]
					}, line);
							
					parsed.add(c);
						
				} else {
					throwErr("A var variable can just contain a number");
				}
					
				return true;
				
			} else {
				Content c = new Content("vardeclaration", name, line);
				parsed.add(c);
				return true;
			}
		}
		return false;
	}
}
