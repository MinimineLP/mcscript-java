/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;
import com.github.miniminelp.mcscript.java.parser.Util;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class VarEdit extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		if(!parsable.isActualWord())return false;
		
		// Create clone of p
		Parsable clone = parsable.clone();
		
		clone.skipWord();
		clone.skipIgnored();
		
		if(clone.actual().equals("+")&&clone.peek().equals("+")) {
			int line = parsable.getLine();
			String varname = parsable.actualWord();
			parsable.skipWord();
			parsable.skipIgnored();
			parsable.skip(2);
			
			Content c = new Content("varedit", new String[]{varname,"+","1"}, line);
			parsed.add(c);
		}
		
		else if(clone.actual().equals("-")&&clone.peek().equals("-")) {
			int line = parsable.getLine();
			String varname = parsable.actualWord();
			parsable.skipWord();
			parsable.skipIgnored();
			parsable.skip(2);
			
			Content c = new Content("varedit", new String[]{varname,"-","1"}, line);
			parsed.add(c);
		}
		
		else if(Util.COUNTINGOPERATORS.contains(clone.actual())&&clone.next().equals("=")) {
			int line = parsable.getLine();
			String varname = parsable.actualWord();

			parsable.skipWord();
			parsable.skipIgnored();
			
			String operator = parsable.actual();
			String val = "";
			parsable.skip();
			parsable.skip();
			parsable.skipIgnored();
			
			if(parsable.isActualNumber()) {
				val = parsable.actualNumber();
				parsable.skipNumber();
			}
			else if(parsable.isActualWord()) {	
				val = parsable.actualWord();
				parsable.skipWord();
			}
			else {
				throwErr("Can't count with "+parsable.actual());
			}
			
			Content c = new Content("varedit", new String[]{varname,operator,val}, line);
			parsed.add(c);
			
			return true;
			
		} else if(clone.actual().equals("=")) {
			int line = parsable.getLine();
			String varname = parsable.actualWord();

			parsable.skipWord();
			parsable.skipIgnored();
			parsable.skip();
			parsable.skipIgnored();
			
			if(parsable.isActualNumber()) {
				
				String val = parsable.actualNumber();
				
				parsable.skipNumber();
				
				Content c = new Content("varedit", new String[]{varname,"=",val}, line);
				
				parsed.add(c);
				
			} else if(parsable.isActualWord()){
				
				String val = parsable.actualWord();
				
				parsable.skipWord();
				

				if((val.equals("true")||val.equals("false"))) {
					Content c = new Content("booleandeclaration", new String[]{varname,val}, line);
					parsed.add(c);
					return true;
				}
				
				Content c = new Content("varedit", new String[]{varname,"=",val}, line);
					
				parsed.add(c);
				
			} else {
				throwErr("A var variable can just contain a number");
			}
		}
		
		
		return false;
	}

}
