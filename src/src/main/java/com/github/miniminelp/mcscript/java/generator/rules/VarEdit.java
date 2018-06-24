/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.parser.Util;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class VarEdit extends GeneratorRule {

	/**
	 * @author Minimine
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		
		 Object[] cont = (Object[]) content.getContent(); 
		 String varname = (String) cont[0];
		 String operator = (String) cont[1];
		 String val2 = (String) cont[2];
		 
		 if(Util.NUMBERS.contains(Character.toString(val2.charAt(0))) && operator.equals("+")) 
			 return list("scoreboard players add "+varname+" "+varname+" "+val2);
		 
		 if(Util.NUMBERS.contains(Character.toString(val2.charAt(0))) && operator.equals("-")) 
			 return list("scoreboard players remove "+varname+" "+varname+" "+val2);
		 
		 if(operator.equals("=")) {
			 if(Util.NUMBERS.contains(Character.toString(val2.charAt(0)))) {
				 return list("scoreboard players set "+varname+" "+varname+" "+val2);
			 } else {
				 return list("scoreboard players operation "+varname+" "+varname+" = "+val2+" "+val2);
			 }
		 }
		 
		 return list("scoreboard players operation "+varname+" "+varname+" "+operator+"= "+val2+" "+val2);
	}
}
