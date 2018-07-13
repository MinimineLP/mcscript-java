/**
 * 
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.parser.ConstUseParser;

/**
 * @author Minimine
 * @since 0.0.3
 * @version 0.0.3
 *
 */
public class Debug extends GeneratorRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		
		String[] content = (String[]) getContent().getContent();
		String type = ConstUseParser.applyFilter(content[0], consts);
		String message = ConstUseParser.applyFilter(content[1], consts);

		boolean die = false;
		
		if(type.equalsIgnoreCase("message"))type="Info";
		if(type.equalsIgnoreCase("err"))type="Error";
		if(type.equalsIgnoreCase("error"))type="Error";
		if(type.equalsIgnoreCase("break")) {
			type="Exception";
			die=true;
		}
		
		System.out.println("[Generator][Debug-Logging]["+obj.getFile()+":"+getContent().getLine()+"]["+type+"] "+message);
		if(die)System.exit(1);
		
		return list();
	}

}
