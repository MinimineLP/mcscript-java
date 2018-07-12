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
public class ForEach extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		int line = parsable.getLine();
		
		if(!parsable.isActualWord())return false;
		if(!parsable.actualWord().equals("forEach"))return false;
		
		parsable.skipWord();
		parsable.skipIgnored();

		if(!parsable.actual().equals("("))throwErr("Needing \"(\"");
		String the_statement = replaceLast(parseStatement().replaceFirst("\\(", ""), "\\)", "").replaceAll(";", LINESEPERATOR);
		
		//Parse statement
		String[] split = the_statement.split(LINESEPERATOR);
		if(split.length!=3)throwErr("Wrong number of commands in statement in dowhile");
		ParsedObject statement_part0 = new ParsingExecutor(new Parsable(split[0]), file).parse();
		String statement_part1 = split[1];
		ParsedObject statement_part2 = new ParsingExecutor(new Parsable(split[2]), file).parse();
		
		ParsedObject statement = new ParsedObject(file);
		statement.add(statement_part0.actual());
		statement.add(new Content("statement",statement_part1,line));
		statement.add(statement_part2.actual());
		
		parsable.skipIgnored();
		
		if(!parsable.actual().equals("{"))throwErr("Needing \"{\"");
		String the_action = replaceLast(parseStatement().replaceFirst("\\{", ""), "\\}", "").replaceAll(";", LINESEPERATOR);
		ParsedObject action = new ParsingExecutor(new Parsable(the_action), file).parse();
		
		Content content = new Content("foreach", new Object[] {statement,action}, line);
		parsed.add(content);
		
		return true;
	}

}
