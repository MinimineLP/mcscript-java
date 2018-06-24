/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.Util;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class Switch extends GeneratorRule {

	/**
	 * @author Minimine
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> generate() {
		FileEdit fe = new FileEdit(this.obj.getFile());
		
		List<Object> ret = new LinkedList<Object>();
		
		Object[] obj = (Object[]) content.getContent();
		
		String s = fixSelector((String) obj[0]);
		String[] key = new String[] {s,s};
		
		if(s.contains(" ")) {
			String[] parts = s.split(" ");
			key[0] = parts[0];
			key[1] = parts[1];
		}
		
		String start = "execute if score "+key[1]+" "+key[0]+" ";
		
		List<Content> contents = (List<Content>) obj[1];
		
		for(Content cont : contents) {
			
			if(cont.getType().equals("case")) {
				
				Object[] cobj = (Object[]) cont.getContent();
				String operator = (String) cobj[0];
				String num = (String) cobj[1];
				ParsedObject run = (ParsedObject) cobj[2];
				
				List<Object> generatedSubs = new LinkedList<Object>();

				//generate run
				List<Object> generated = generate(run.actual());
				
				for(Object o : generated) {
					if(o instanceof String) generatedSubs.add(fixForExecute((String)o));
					else generatedSubs.add(o);
				}
				
				
				while(run.hasNext()) {
					List<Object> objs = generate(run.next());
					
					for(Object o : objs) {
						if(o instanceof String) generatedSubs.add(fixForExecute((String)o));
						else generatedSubs.add(o);
					}
				}
				
				
				if(Util.NUMBERS.contains(num.substring(0, 1))) {
						
					int number = Integer.valueOf(num);
						
					if(operator.equals(">=")) {
						for(Object sub : generatedSubs)  {
							if(sub instanceof String) ret.add(start+"matches .."+number+" if entity @s[tag=!mcscriptSwitch] run "+sub);
							else ret.add(sub);
						}
							
						
						ret.add(start+"matches .."+number+" if entity @s[tag=!mcscriptSwitch] run tag @s add mcscriptSwitch");
					}
					
					else if(operator.equals("<=")) {
						for(Object sub : generatedSubs) {
							if(sub instanceof String) ret.add(start+"matches "+number+".. if entity @s[tag=!mcscriptSwitch] run "+sub);
							else ret.add(sub);
						}
						ret.add(start+"matches "+number+".. if entity @s[tag=!mcscriptSwitch] run tag @s add mcscriptSwitch");
					}
					else if(operator.equals("=")) {
						for(Object sub : generatedSubs) {
							if(sub instanceof String) ret.add(start+"matches "+number+" if entity @s[tag=!mcscriptSwitch] run "+sub);
							else ret.add(sub);
						}
						ret.add(start+"matches "+number+" if entity @s[tag=!mcscriptSwitch] run tag @s add mcscriptSwitch");
					}
					else if(operator.equals(">")) {
						number++;
						for(Object sub : generatedSubs) {
							if(sub instanceof String) ret.add(start+"matches .."+number+" if entity @s[tag=!mcscriptSwitch] run "+sub);
							else ret.add(sub);
						}
						ret.add(start+"matches .."+number+" if entity @s[tag=!mcscriptSwitch] run tag @s add mcscriptSwitch");
					}
					else if(operator.equals("<")) {
						number--;
						for(Object sub : generatedSubs) {
							if(sub instanceof String) ret.add(start+"matches "+number+".. if entity @s[tag=!mcscriptSwitch] run "+sub);
							else ret.add(sub);
						}
						ret.add(start+"matches "+number+".. if entity @s[tag=!mcscriptSwitch] run tag @s add mcscriptSwitch");
					}
				} else {
					for(Object sub : generatedSubs) {
						if(sub instanceof String) {
							
							if(num.contains(" ")) {
								String[] split = num.split(" ");
								ret.add(start+operator+" "+split[1]+" "+split[0]+" if entity @s[tag=!mcscriptSwitch] run "+sub);
							}
							else ret.add(start+operator+" "+num+" "+num+" if entity @s[tag=!mcscriptSwitch] run "+sub);
						}
						else ret.add(sub);
					}
					if(num.contains(" ")) {
						String[] split = num.split(" ");
						ret.add(start+operator+" "+split[1]+" "+split[0]+" if entity @s[tag=!mcscriptSwitch] run tag @s add mcscriptSwitch");
					}
					else ret.add(start+operator+" "+num+" "+num+" if entity @s[tag=!mcscriptSwitch] run tag @s add mcscriptSwitch");
				}
			} else if (cont.getType().equals("default")){
				
				ParsedObject run = (ParsedObject) cont.getContent();

				List<Object> generatedSubs = new LinkedList<Object>();
				
				//generate run
				List<Object> generated = generate(run.actual());
				
				for(Object o : generated) {
					if(o instanceof String) generatedSubs.add(fixForExecute((String)o));
					else generatedSubs.add(o);
				}
				
				while(run.hasNext()) {
					generated = generate(run.actual());
				
					for(Object o : generated) {
						if(o instanceof String) generatedSubs.add(fixForExecute((String)o));
						else generatedSubs.add(o);
					}
				}
				for(Object sub : generatedSubs) 
					if(sub instanceof String)ret.add("execute if entity @s[tag=!mcscriptSwitch] run "+sub);
					else ret.add(sub);

				ret.add("execute if entity @s[tag=!mcscriptSwitch] run tag @s add mcscriptSwitch");
			}
		}
		ret.add("tag @s remove mcscriptSwitch");
		ret.add(fe);
		return ret;
	}
}
