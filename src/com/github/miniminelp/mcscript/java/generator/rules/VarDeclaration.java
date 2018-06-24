/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.List;

import com.github.miniminelp.mcscript.java.filemanager.SystemFiles;
import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.parser.Util;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class VarDeclaration extends GeneratorRule {

	/**
	 * @author Minimine
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		
		if(content.getContent() instanceof String[]) {
			
			String[] content = (String[]) this.content.getContent();
			
			SystemFiles.createScoreboardObjective(content[0]);
			if(Util.NUMBERS.contains(Character.toString(content[2].charAt(0))))
				return list("scoreboard players set "+content[1]+" "+content[0]+" "+content[2]);
			else return list("scoreboard players operation "+content[1]+" "+content[0]+" = "+content[3]+" "+content[2]);
			
		} else {

			SystemFiles.addLoadContent("scoreboard objectives add "+this.content.getContent()+" dummy");
			
			return list();
		}
	}

}
