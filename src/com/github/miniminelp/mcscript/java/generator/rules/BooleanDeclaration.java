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
public class BooleanDeclaration extends GeneratorRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		List<Object> ret = list();
		
		String[] s = (String[]) content.getContent();
		String name = s[0];
		String player = s[1];
		String value = s[2];
		
		if(value.equals("true")) {
			if(player != null)ret.add("tag @e[name="+player+",tag=!"+name+"] add "+name);
			else ret.add("tag @e[tag=mcscriptTags,tag=!"+name+"] add "+name);
		}
		else if(value.equals("false")) {
			if(player != null)ret.add("tag @e[name="+player+",tag="+name+"] remove "+name);
			else ret.add("tag @e[tag=mcscriptTags,tag="+name+"] remove "+name);
		}
		else {
			if(player != null) {

				ret.add("tag @e[name="+player+",tag="+value+",tag=!"+name+"] add "+name);
				ret.add("tag @e[name="+player+",tag=!"+value+",tag="+name+"] remove "+name);
			} else {
				ret.add("tag @e[tag=mcscriptTags,tag="+value+",tag=!"+name+"] add "+name);
				ret.add("tag @e[tag=mcscriptTags,tag=!"+value+",tag="+name+"] remove "+name);
			}
		}
		
		return ret;
	}
	
}
