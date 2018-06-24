/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

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
public class If extends ParserRule{

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {

		if(!parsable.isActualWord())return false;

		String keyword = parsable.actualWord();
		int line = parsable.getLine();
		
 
		if(keyword.equals("if")) {
			
			Object[] pib = parseIfBlock();
			String statement = (String) pib[0];
			ParsedObject parsed = (ParsedObject) pib[1];
			
			parsable.skipIgnored();
			List<Content> elseifs = new LinkedList<Content>();
			
			while(parsable.isActualWord()&&parsable.actualWord().equals("else")) {
				parsable.skipWord();
				parsable.skipIgnored();
				if(parsable.isActualWord()) {
					if(parsable.actualWord().equals("if")) {

						Object[] peib = parseIfBlock();
						String stm = (String) peib[0];
						ParsedObject pd = (ParsedObject) peib[1];
						Content cont = new Content("elseif", new Object[] {stm,pd}, parsable.getLine());
						elseifs.add(cont);
						
					}
					else {
						throwErr("Expected codeblock or \"if\" after keyword if");
					}
				}
				else if(parsable.actual().equals("{")) {

					Parsable p = new Parsable(replaceLast(parseStatement().replaceFirst("\\{", ""),"\\}",""));
					SubParsingExecutor exec = new SubParsingExecutor(p, statement);
					ParsedObject psd = exec.parse();
					
					Content cont = new Content("else", psd, parsable.getLine());
					elseifs.add(cont);
					
					
				}
				else {
					throwErr("Expected codeblock or \"if\" after keyword if");
				}
				parsable.skipIgnored();
			}
			
			Content c = new Content("if", new Object[]{statement,parsed,elseifs}, line);
			this.parsed.add(c);
			
			return true;
		}
		return false;
	}
	
	public Object[] parseIfBlock() {
		
		parsable.skipWord();
		parsable.skipIgnored();
			
		if(!parsable.actual().equals("("))throwErr("Needing \"(\" after keyword if");
		String statement = replaceLast(parseStatement().replaceFirst("\\(", ""), "\\)", "");
			
		parsable.skipIgnored();
		if(!parsable.actual().equals("{"))throwErr("Needing \"{\" after if condition");
		String method = replaceLast(parseStatement().replaceFirst("\\{", ""), "\\}", "");
		
		Parsable p = new Parsable(method);
		SubParsingExecutor exec = new SubParsingExecutor(p, method);
		ParsedObject psd = exec.parse();
		
		return new Object[] {statement,psd};
	}
}
