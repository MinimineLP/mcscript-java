/**
 * 
 */

package com.github.miniminelp.mcscript.java.parser;

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRuleFunctions;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRuleManager;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

import com.github.miniminelp.mcscript.java.util.MCScriptObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

public class SubParsingExecutor implements MCScriptObject, ParserRuleFunctions {
	
	protected Parsable p;
	protected ParsedObject parsed;
	protected String file;
	protected int addline;
	protected List<ParserRule> rules = new LinkedList<ParserRule>();
	
	/**
	 * @param p the parsable {@link Parsable}
	 * @param file the file {@link String}
	 * @param addline line to add (int)
	 */
	public SubParsingExecutor(Parsable p, String file, int addline) {
		this.p = p;
		this.parsed = new ParsedObject(file);
		this.file=file;
		this.addline=addline;
		this.rules=ParserRuleManager.getSubParserRules(p, parsed, file);
	}
	
	
	/**
	 * @param p the parsable {@link Parsable}
	 * @param file the file {@link String}
	 */
	public SubParsingExecutor(Parsable p, String file) {
		this(p,file,0);
	}
	
	
	/**
	 * @return the parsed object {@link ParsedObject}
	 */
	public ParsedObject parse() {
		
		while(hasNext()) {
			
			parseNext();
			
		}
		
		return parsed;
	}
	
	/**
	 * @param err the error {@link String}
	 */
	public void throwErr(String err) {		throwErr(err, p, file);
	}
	
	/**
	 * 
	 */
	public void parseNext() {

		// Skip unimportant tokens
		p.skipIgnored();
		
		if(!p.hasNext())return;
		
		// Save start position
		int startpos = p.getPosition();
		
		/* Check Parser Rules */
		
		for(ParserRule rule : rules) {
			rule.setParsable(p);
			rule.setParsed(parsed);
			rule.setFile(file);
			if(rule.apply()) {
				this.p = rule.getParsable();
				this.parsed = rule.getParsed();
				return;
			}
		}
		
		if(startpos==p.getPosition()) {
			
			// Actual word could not be parsed
			if(p.isActualWord()) {
				throwErr("Error: unparsable word '"+p.actualWord()+"'");
				p.skipWord();
				//return false;
			}
			
			// Actual token could not be parsed
			throwErr("Error: unparsable token '"+p.actual().replaceAll(LINESEPERATOR, "\\\\n")+"'");
			p.skip();
			//return false;
		}
		return;
		
	}
	
	
	/**
	 * @return if has next (boolean)
	 */
	public boolean hasNext() {
		return p.hasNext();
	}

	/**
	 * @param p the parsable {@link Parsable}
	 * @return the result {@link StatementParseResult}
	 */
	public StatementParseResult parseStatement(Parsable p) {
		return parseStatement(p, file);
	}
	
	/**
	 * @return the statement {@link String}
	 */
	public String parseStatement() {
		StatementParseResult res = parseStatement(p);
		p = res.getParsable();
		return res.getResult();
	}


	/**
	 * @return the rules {@link List}?extends {@link ParserRule}
	 */
	public List<ParserRule> getRules() {
		return rules;
	}


	/**
	 * @param rules the rules to set {@link List}?extends {@link ParserRule}
	 */
	public void setRules(List<ParserRule> rules) {
		this.rules = rules;
	}
}