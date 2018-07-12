/**
 * 
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.ParsingExecutor;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.3
 * @version 0.0.3
 *
 */
public class DoWhile extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		int line = parsable.getLine();
		
		if(!parsable.isActualWord())return false;
		if(!parsable.actualWord().equals("do"))return false;
		
		parsable.skipWord();
		parsable.skipIgnored();
		
		if(!parsable.actual().equals("{")) throwErr("Needing \"{\"");
		String run = replaceLast(parseStatement().replaceFirst("\\{", ""), "\\}", "");
		
		parsable.skipIgnored();
		
		if(!parsable.actualWord().equals("while"))throwErr("Needing \"while\"");
		
		parsable.skipWord();
		parsable.skipIgnored();

		if(!parsable.actual().equals("(")) throwErr("Needing \"(\"");
		String statement = replaceLast(parseStatement().replaceFirst("\\(", ""), "\\)", "");
		
		parsable.skipIgnored();
		
		Parsable p = new Parsable(run);
		ParsingExecutor exec = new ParsingExecutor(p, file);
		ParsedObject psd = exec.parse();
		
		Content content = new Content("dowhile", new Object[] {statement, psd}, line);
		parsed.add(content);
		
		return true;
	}

}
