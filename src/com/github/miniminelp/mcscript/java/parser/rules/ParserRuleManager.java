/**
 * 
 */
package com.github.miniminelp.mcscript.java.parser.rules;

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.rules.main.*;
import com.github.miniminelp.mcscript.java.parser.rules.sub.*;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class ParserRuleManager {
	
	/**
	 * @param parsable the parsable {@link Parsable}
	 * @param parsed the parsed object {@link ParsedObject}
	 * @param file the file {@link String}
	 * @return the SubParser rules {@link List}?extends {@link ParserRule}
	 * 
	 * 
	 * <p><b>Note</b>: The order of the rules is important!</p>
	 */
	public static List<ParserRule> getSubParserRules(Parsable parsable, ParsedObject parsed, String file) {
		
		List<ParserRule> rules = new LinkedList<ParserRule>();

		rules.add(new CommentFile());
		rules.add(new CommentMultiLine());
		rules.add(new CommentSingleLineParsable());
		rules.add(new CommentSingleLineUnparsable());
		rules.add(new Command());
		rules.add(new Switch());
		rules.add(new If());
		rules.add(new BooleanDeclaration());
		rules.add(new VariableDeclaration());
		rules.add(new ConstantDeclaration());
		rules.add(new Keyword());
		rules.add(new FunctionCall());
		rules.add(new VarEdit());
		rules.add(new AllowSemicolon());
		
		return rules;
		
	}
	
	
	/**
	 * @param parsable the parsable {@link Parsable}
	 * @param parsed the parsed object {@link ParsedObject}
	 * @param file the file {@link String}
	 * @return the Parser rules {@link List}?extends {@link ParserRule} 
	 * 
	 * <p><b>Note</b>: The order of the rules is important!</p>
	 */
	public static List<ParserRule> getParserRules(Parsable parsable, ParsedObject parsed, String file) {

		List<ParserRule> rules = getSubParserRules(parsable, parsed, file);
		rules.add(new ModalCreation());
		return rules;
		
	}
}
