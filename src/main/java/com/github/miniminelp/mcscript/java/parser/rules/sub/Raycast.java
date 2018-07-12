package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.SubParsingExecutor;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.3
 * @version 0.0.3
 *
 */
public class Raycast extends ParserRule{

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		if(!parsable.isActualWord())return false;
		
		if(parsable.actualWord().equals("raycast")) {

			int line = parsable.getLine();
			String statement = null;
			ParsedObject cmds = null;
			ParsedObject cmds2 = null;
			
			parsable.skipWord();
			parsable.skipIgnored();
			
			if(parsable.actual().equals("(")) {
				statement = replaceLast(parseStatement().replaceFirst("\\(", ""), "\\)", "");
			}
			

			parsable.skipIgnored();
			

			if(!parsable.actual().equals("{"))throwErr("Needing \"{\""); 
				
			String cont = replaceLast(parseStatement().replaceFirst("\\{", ""), "\\}", "");

			Parsable subp = new Parsable(cont);
			SubParsingExecutor exec = new SubParsingExecutor(subp, file,line);
			cmds = exec.parse();
			
			parsable.skipIgnored();
			if(parsable.actual().equals(",")) {
				
				parsable.skip();
				parsable.skipIgnored();
				
				if(!parsable.actual().equals("{"))throwErr("Needing \"{\""); 
				
				cont = replaceLast(parseStatement().replaceFirst("\\{", ""), "\\}", "");

				subp = new Parsable(cont);
				exec = new SubParsingExecutor(subp, file,line);
				cmds2 = exec.parse();
			}
			
			Object[] content = new Object[]{statement,cmds,cmds2};
			
			Content action = new Content("raycast", content, line);
			parsed.add(action);
			
			return true;
		}
		return false;
	}
}
