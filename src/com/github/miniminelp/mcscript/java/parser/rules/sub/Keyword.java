/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;
import com.github.miniminelp.mcscript.java.parser.SubParsingExecutor;
import com.github.miniminelp.mcscript.java.parser.Util;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class Keyword extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		if(!parsable.isActualWord())return false;
		
		if(Util.KEYWORDS.contains(" " + parsable.actualWord() + " ")) {

			String keyword = parsable.actualWord();
			int line = parsable.getLine();
			String statement = null;
			ParsedObject cmds = null;
			
			parsable.skipWord();
			parsable.skipIgnored();
			
			if(parsable.actual().equals("(")) {
				
				statement = replaceLast(parseStatement().replaceFirst("\\(", ""), "\\)", "");
			}

			parsable.skipIgnored();
			

			if(parsable.actual().equals("{")) {
				
				String cont = replaceLast(parseStatement().replaceFirst("\\{", ""), "\\}", "");

				Parsable subp = new Parsable(cont);
				SubParsingExecutor exec = new SubParsingExecutor(subp, file,line);
				cmds = exec.parse();
				
			}
			
			else if (parsable.isActualWord()) {
				if(!Util.KEYWORDS.contains(" " + parsable.actualWord() + " ")) {
					char semicolon = ";".toCharArray()[0];
					int semicolonDistance = parsable.distanceToNext(semicolon);
					int enterDistance = parsable.distanceToNext(Util.LINEBREAK);
					
					String cmd;
					
					if(semicolonDistance < enterDistance) cmd = parsable.getToNext(semicolon);
					else if (semicolonDistance > enterDistance)  cmd = parsable.getToNext(Util.LINEBREAK);
					else {
						cmd = parsable.getRest();
						parsable.skipRest();
					}
					parsable.skipToNext(semicolon);
					
					Parsable p = new Parsable(cmd);
					SubParsingExecutor exec = new SubParsingExecutor(p, file,line);
					cmds = exec.parse();
					
				}
			}
			
			Object[] content = new Object[]{keyword,statement,cmds};
			
			Content action = new Content("keyword", content, line);
			parsed.add(action);
			
			setParsable(parsable);
			
			return true;
		}

		return false;
	}
}
