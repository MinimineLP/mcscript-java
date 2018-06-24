/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class ConstDeclaration extends GeneratorRule {

	/**
	 * @author Minimine
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		
		String[] content = (String[]) this.content.getContent();
		
		String name = content[0];
		String value = content[1];
		
		if(consts.containsKey(name))throwError("Can't double define a constant", this.content.getLine(), obj.getFile());
		
		consts.put(name, value);
		
		return list();
	}
	
	
	
}
