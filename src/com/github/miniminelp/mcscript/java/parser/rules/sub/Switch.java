/**
 * @project mcscript
 * @package com.github.miniminelp.mcscript.java.parser.rules.sub
 * @file: Switch.java
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.SubParsingExecutor;
import com.github.miniminelp.mcscript.java.parser.Util;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class Switch extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		
		if(!parsable.isActualWord())return false;

		String keyword = parsable.actualWord();
		int line = parsable.getLine();
			

		if(keyword.equals("switch")) {
			
			parsable.skipWord();
			parsable.skipIgnored();
				
			if(!parsable.actual().equals("("))throwErr("Needing \"(\" after keyword switch");
			String key = replaceLast(parseStatement().replaceFirst("\\(", ""), "\\)", "");
				
			parsable.skipIgnored();
			if(!parsable.actual().equals("{"))throwErr("Needing \"{\" after switch condition");
			String method = replaceLast(parseStatement().replaceFirst("\\{", ""), "\\}", "");
			
			Parsable parsable = new Parsable(method);
			
			List<Content> contents = new LinkedList<Content>();
			
			while(parsable.hasNext()) {
				parsable.skipIgnored();
				if(parsable.actual().equals(","))parsable.skip();
				parsable.skipIgnored();
				
				if(parsable.hasNext()) {
					if(!parsable.isActualWord()) throwErr("Unexpected \""+parsable.actual()+"\"", parsable.getLine()+line, parsable.getColumn(), parsable.actualLine());
					
					if(parsable.actualWord().equals("case")) {
						
						parsable.skipWord();
						parsable.skipIgnored();
							
						if(!Util.OPERATORS.contains(parsable.actual()))throwErr("Needing operator () (given token: "+parsable.actual()+")", parsable.getLine()+line, parsable.getColumn()+1, parsable.actualLine());
							
						String operator = parsable.actual();
						parsable.skip();
						if(parsable.actual().equals("=")&&!operator.equals("=")) {
							operator += "=";
							parsable.skip();
						}
							
						parsable.skipIgnored();
						String num = "";
						if(parsable.isActualNumber()){
							num = parsable.actualNumber();
							parsable.skipNumber();
						}
						else if(parsable.isActualWord()) {
							num = parsable.actualWord();
							parsable.skipWord();
							parsable.skipIgnored();
							if(parsable.isActualWord()) {
								num += " "+parsable.actualWord();
								parsable.skipWord();
							}
						}
						else throwErr("Needing number", parsable.getLine()+line, parsable.getColumn(), parsable.actualLine());
	
						parsable.skipIgnored();
							
						if(!parsable.actual().equals("{"))throwErr("Needing \"{\"", parsable.getLine()+line, parsable.getColumn(), parsable.actualLine());
							
						StatementParseResult spr = parseStatement(parsable);
						parsable = spr.getParsable();
						String cont = replaceLast(spr.getResult().replaceFirst("\\{", ""), "\\}", "");
							
						Parsable pars = new Parsable(cont);
						SubParsingExecutor exec = new SubParsingExecutor(pars, this.file, line);
						ParsedObject res = exec.parse();
							
						Content c = new Content("case", new Object[]{operator, num, res}, parsable.getLine());
							
						contents.add(c);
							
					} else if(parsable.actualWord().equals("default")) {
							
						parsable.skipWord();
						parsable.skipIgnored();
							
						if(!parsable.actual().equals("{"))throwErr("Needing \"{\"");
							
						StatementParseResult spr = parseStatement(parsable);
						parsable = spr.getParsable();
						String cont = replaceLast(spr.getResult().replaceFirst("\\{", ""), "\\}", "");
							
						Parsable pars = new Parsable(cont);
						SubParsingExecutor exec = new SubParsingExecutor(pars, this.file, line);
						ParsedObject res = exec.parse();
							
						Content c = new Content("default", res, parsable.getLine());
							
						contents.add(c);
							
					}
					else {
						if(!parsable.isActualWord()) throwErr("Unexpected \""+parsable.actualWord()+"\"");
							throwErr("Unexpected \""+parsable.actual()+"\"");
						}
						
					parsable.skipIgnored();
				}
			}
			Content c = new Content("switch", new Object[]{key,contents}, line);
			parsed.add(c);
			return true;
		}
		return false;
	}

}
