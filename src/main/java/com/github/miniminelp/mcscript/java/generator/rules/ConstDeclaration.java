/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.HashMap;
import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.parser.ConstUseParser;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.4
 *
 */
public class ConstDeclaration extends GeneratorRule {

	/**
	 * @author Minimine
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> generate() {
		
		Object[] content = (Object[]) this.content.getContent();
		
		String name = ConstUseParser.filter((String) content[0], consts);
		Object value = content[1];
		if(content[1] instanceof String)value = ConstUseParser.filter((String) content[1], consts);
		
		if(consts.containsKey(name))throwError("Can't double define a constant", this.content.getLine(), obj.getFile());
		
		if(name.contains(".")) {
			String[] parts = name.split("\\.");
			HashMap<String, Object>[] objects = (HashMap<String, Object>[]) new HashMap[parts.length-1];
			if(consts.containsKey(parts[0])) {
				if(consts.get(parts[0]) instanceof HashMap) objects[0] = (HashMap<String, Object>) consts.get(parts[0]);
				else objects[0] = new HashMap<String, Object>();
			}
			else objects[0] = new HashMap<String, Object>();
			
			for(int i=1;i<parts.length-1;i++) {
				if(objects[i-1].containsKey(parts[i])) {
					if(objects[i-1].get(parts[i]) instanceof HashMap) objects[0] = (HashMap<String, Object>) objects[i-1].get(parts[i]);
					else objects[i] = new HashMap<String, Object>();
				}
				else objects[i] = new HashMap<String, Object>();
			}
			
			objects[objects.length-1].put(parts[objects.length], value);
			
			for(int i=parts.length-3;i>=0;i--) {
				objects[i].put(parts[i+1], objects[i+1]);
			}
			
			consts.put(parts[0], objects[0]);
		}
		else consts.put(name, value);
		
		return list();
	}
}
