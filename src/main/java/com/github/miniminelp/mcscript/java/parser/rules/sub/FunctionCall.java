/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class FunctionCall extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		if(!parsable.isActualWord())return false;
		
		// Create clone of p
		Parsable clone = parsable.clone();
		
		// Do skips with clone
		clone.skipWord();
		clone.skipIgnored();
		
		// Test if is FunctionCall
		if(clone.actual().equals("(")){
			
			String called = parsable.actualWord();
			int line = parsable.getLine();
			
			// Now continue working with p
			
			// Do skips
			parsable.skipWord();
			parsable.skipIgnored();
			parsable.skip();
			
			List<String> arguments = new LinkedList<String>();
			
			// Now get arguments (if exists)
			if(!parsable.actual().equals(")")) {
				
				String arg = "";
				
				while(!parsable.actual().equals(")")){
					if(!parsable.has(parsable.getPosition()))
						throwErr("Unclosed method call");
					
					if(parsable.actual().equals(",")){
						if(arg.equals(""))throwErr("Empty function argument");
						arguments.add(arg);
						arg = "";
						parsable.skip();
					}
					
					else if(parsable.actual().equals("\"")||parsable.actual().equals("'")||parsable.actual().equals("{")||parsable.actual().equals("(")||parsable.actual().equals("[")) arg += parseStatement(parsable);
					
					else {
						arg += parsable.actual();
						parsable.skip();
					}
					
				}
				
				if(arg.equals(""))throwErr("Empty function argument");
				arguments.add(arg);
			}
			
			parsable.skip();
			
			Content action = new Content("functioncall", new Object[]{called, arguments}, line);
			parsed.add(action);
			
			return true;
		}
		
		
		return false;
	}
}
