/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.main;

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.SubParsingExecutor;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class ModalCreation extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		if(!parsable.isActualWord())return false;
		
		if(parsable.actualWord().equals("modal")){
			
			parsable.skipWord();
			parsable.skipIgnored();
			if(!parsable.isActualWord())throwErr("Can't create a modal without name");
			
			String name = parsable.actualWord();
			int line = parsable.getLine();
			
			parsable.skipWord();
			parsable.skipIgnored();
			
			if(!parsable.actual().equals("("))throwErr("Can't create a modal without '()' ");
			
			parsable.skip();
				
			List<String> arguments = new LinkedList<String>();
				
			// Now get arguments (if exists)
			if(!parsable.actual().equals(")")) {
				
				String arg = "";
						
				while(!parsable.actual().equals(")")){
					
					if(!parsable.has(parsable.getPosition()))
						throwErr("Unclosed method definition");
							
					if(parsable.actual().equals(",")) {
						if(arg.equals(""))throwErr("Empty required method argument");
						arguments.add(arg);
						arg = "";
					}
							
					else arg += parsable.actual();
					parsable.skip();
					
				}
					
				if(arg.equals(""))throwErr("Empty required method argument");
				arguments.add(arg);
			}
					
			parsable.skip();
			parsable.skipIgnored();
			
			if(!parsable.actual().equals("{")) throwErr("Method without content");
				
			String statement = replaceLast(parseStatement().replaceFirst("\\{", ""), "\\}", "");
			Parsable subp = new Parsable(statement);
			SubParsingExecutor exec = new SubParsingExecutor(subp, file);
			ParsedObject res = exec.parse();
			
			if(!res.getMethods().isEmpty())throwErr("Can't define a method in a method");
			
				
			Content action = new Content("methoddefine", new Object[]{name, arguments, res}, line);
			super.parsed.add(action);
			return true;
		}
		
		
		return false;
	}

}
