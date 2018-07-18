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
		
		if(parsable.actual().equals("/")){
			String command = parsable.getToNext(LINESEPERATOR.charAt(0)).replaceFirst("/", "");
			
			Content action = new Content("command", command, parsable.getLine());
			parsed.add(action);
			
			parsable.skipLine();
			return true;
		}
		
		
		if(parsable.isActualWord()&&parsable.hasSpace(4)) {
			if(parsable.actualWord().equals("run")&&parsable.peek(3).equals(":")) {
				String command = parsable.getToNext(LINESEPERATOR.charAt(0)).replaceFirst("run:", "");
				command=command.replaceAll("\t", "").replaceAll("\r", "").replaceAll("\n", "");
				while(command.startsWith(" "))command=command.replaceFirst(" ", "");
				
				Content action = new Content("command", command, parsable.getLine());
				parsed.add(action);
				
				parsable.skipLine();
				return true;
			}
		}
		
		
		return false;
	}

}
