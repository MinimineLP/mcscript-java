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
public class ConstantDeclaration extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		if(!parsable.isActualWord())return false;
		

		String keyword = parsable.actualWord();
		int line = parsable.getLine();

		if(keyword.equals("const")) {
			
			parsable.skipWord();
			parsable.skipIgnored();
			
			if(!parsable.isActualWord())throwErr("Declaration of "+keyword+" without a name");
			String name = parsable.actualWord();
			
			parsable.skipWord();
			parsable.skipIgnored();
			
			if(parsable.actual().equals("=")) {
				
				parsable.skip();
				parsable.skipIgnored();
				
				String val = "";
				
				if(parsable.isActualNumber()) {
					
					val = parsable.actualNumber();
					
					parsable.skipNumber();
					
				} else if(parsable.actual().equals("\"")||parsable.actual().equals("'")){
					
					val = replaceLast(parseStatement().replaceFirst("\\\"", ""), "\\\"", "");;
					
					parsable.skipWord();
					
				} else {
					throwErr("A var variable can just contain a number");
				}
				Content c = new Content("constdeclaration", new String[]{
					name, val,
				}, line);
						
				parsed.add(c);
				
				return true;
				
			} else {
				throwErr("A const must get a value");
			}
		}
		return false;
	}
}
