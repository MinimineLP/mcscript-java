/**
 *
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
public class Command extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		// check command
		if(!parsable.hasPrevios()){
			if(parsable.actual().equals("/")) {
				String command = parsable.actualLine().replaceFirst("/", "");
				
				Content action = new Content("command", command, parsable.getLine());
				parsed.add(action);
				
				parsable.skipLine();
				return true;
			}else{
				return false;
			}
		}
		if(parsable.actual().equals("/")){

			String command = parsable.actualLine().replaceFirst("/", "");
			
			Content action = new Content("command", command, parsable.getLine());
			parsed.add(action);
			
			parsable.skipLine();
			return true;
		}
		return false;
	}

}
