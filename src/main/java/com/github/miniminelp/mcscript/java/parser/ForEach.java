/**
 * 
 */
package com.github.miniminelp.mcscript.java.parser;

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.core.Main;
import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;

/**
 * @author Minimine
 * @since 0.0.3
 * @version 0.0.3
 *
 */
public class ForEach extends GeneratorRule {
	
	private static int forEachId = 0;
	
	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		
		List<Object> contentlist = list();
		FileEdit fe = FileEdit.getLastEdit();
		
		Object[] content = (Object[]) getContent().getContent();
		List<Content> statement = ((ParsedObject) content[0]).getContent();
		ParsedObject action = (ParsedObject) content[1];
		
		contentlist.addAll(generate(statement.get(0)));
		
		List<String> generated = generateStatement(statement.get(1).getContent().toString());
		List<String> doWhile = new LinkedList<String>();
		
		for(String s : generated) {
			doWhile.add("execute "+s+" run function "+Main.getActualDataFolder()+":mcscript/forEach"+forEachId);
		}
		
		contentlist.add("function "+Main.getActualDataFolder()+":mcscript/forEach"+forEachId);
		contentlist.add(new FileEdit("mcscript/forEach"+forEachId));
		
		contentlist.addAll(generate(action.actual()));
		while(action.hasNext()) {
			contentlist.addAll(generate(action.next()));
		}
		
		contentlist.addAll(generate(statement.get(2)));
		contentlist.addAll(doWhile);
		
		forEachId++;
		
		if(fe!=null)contentlist.add(fe);		
		return contentlist;
	}
	
	public List<String> generateStatement(String statement) {
		
		StatementParser parser = new StatementParser(statement, content.getLine(), obj.getFile());
		List<String> parsed = parser.parse();
		
		return parsed;
	}
}
