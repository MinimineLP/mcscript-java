/**
 * 
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.List;

import com.github.miniminelp.mcscript.java.core.Main;
import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.StatementParser;

/**
 * @author Minimine
 * @since 0.0.3
 * @version 0.0.3
 *
 */
public class DoWhile extends GeneratorRule{
	
	private static int dowhileid = 0;
	
	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		
		List<Object> contentlist = list();
		FileEdit fe = FileEdit.getLastEdit();
		
		Object[] content = (Object[]) getContent().getContent();
		String statement = (String) content[0];
		ParsedObject action = (ParsedObject) content[1];
		
		List<String> statements = generateStatement(statement);
		
		contentlist.add("function "+Main.getActualDataFolder()+":mcscript/dowhile"+dowhileid);
		
		contentlist.add(new FileEdit("mcscript/dowhile"+dowhileid));
		
		for(Content c : action.getContent())contentlist.addAll(generate(c));
		
		for(String s : statements) {
			contentlist.add("execute "+s+"run function "+Main.getActualDataFolder()+":mcscript/dowhile"+dowhileid);
		}
		dowhileid++;
		
		if(fe!=null)contentlist.add(fe);
		
		return contentlist;
	}
	
	public List<String> generateStatement(String statement) {
		
		StatementParser parser = new StatementParser(statement, content.getLine(), obj.getFile());
		List<String> parsed = parser.parse();
		
		return parsed;
	}
}
