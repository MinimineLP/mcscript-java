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
 * @version 0.0.3
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
		String[] varname = new String[] {parsable.actualWord(),parsable.actualWord()};
		int line = parsable.getLine();
		clone.skipCompleteIgnored();
		
		if(clone.actual().equals("@")) {
			clone.skip();
			
			if(clone.isActualWord()) {
				varname[1]="@"+clone.actualWord();
				clone.skipWord();
				clone.skipCompleteIgnored();
			}
			else return false;
		}
		else if(clone.isActualWord()) {
			varname[1]=clone.actualWord();
			clone.skipWord();
			clone.skipCompleteIgnored();
		}
		
		if(clone.actual().equals("+")&&clone.peek().equals("+")) {
			
			parsable.skipWord();
			parsable.skipCompleteIgnored();
			parsable.skip(2);
			
			Content c = new Content("varedit", new Object[]{varname,"+",new String[] {"1"}}, line);
			parsed.add(c);
			return true;
		}
		
		else if(clone.actual().equals("-")&&clone.peek().equals("-")) {
			parsable = clone;
			
			Content c = new Content("varedit", new Object[]{varname,"-",new String[] {"1"}}, line);
			parsed.add(c);
			return true;
		}
		
		else if(Util.COUNTINGOPERATORS.contains(clone.actual())&&clone.peek().equals("=")) {
			
			parsable=clone;
			parsable.skipWord();
			parsable.skipCompleteIgnored();
			String operator = parsable.actual();
			parsable.skip(2);
			parsable.skipCompleteIgnored();
			
			String[] val = null;
			
			if(parsable.isActualNumber()) {
				val = new String[] {parsable.actualNumber()};
				parsable.skipNumber();
			}
			else if(parsable.isActualWord()) {	
				val = new String[] {parsable.actualWord(),parsable.actualWord()};
				parsable.skipWord();
				parsable.skipCompleteIgnored();
				if(parsable.isActualWord()) {
					val[1] = parsable.actualWord();
					parsable.skipWord();
				}
			}
			else {
				throwErr("Can't count with \""+parsable.actual()+"\"");
			}
			
			Content c = new Content("varedit", new Object[]{varname,operator,val}, line);
			parsed.add(c);
			
			return true;
			
		}
		else if(clone.actual().equals("=")) {

			parsable = clone;
			parsable.skip();
			parsable.skipCompleteIgnored();
			
			if(parsable.isActualNumber()) {
				
				String[] val = new String[] {parsable.actualNumber()};
				
				parsable.skipNumber();
				
				Content c = new Content("varedit", new Object[]{varname,"=",val}, line);
				parsed.add(c);
				return true;
				
			} else if(parsable.isActualWord()){
				
				String[] val = new String[] {parsable.actualWord(),parsable.actualWord()};
				
				parsable.skipWord();
				parsable.skipCompleteIgnored();
				
				if((val[0].equals("true")||val[0].equals("false"))) {
					Content c = new Content("booleandeclaration", new Object[]{varname,val[0]}, line);
					parsed.add(c);
					return true;
				}
				else if(parsable.isActualWord()) {
					val[1] = parsable.actualWord();

					parsable.skipWord();
					parsable.skipCompleteIgnored();
				}
				
				Content c = new Content("varedit", new Object[]{varname,"=",val}, line);
				parsed.add(c);
				return true;
				
			} else {
				throwErr("A var variable can just contain a number");
			}
		}
		return false;
	}

}
