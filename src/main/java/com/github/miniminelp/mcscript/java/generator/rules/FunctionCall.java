/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.HashMap;
import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.ConstUseParser;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.4
 *
 */
public class FunctionCall extends GeneratorRule {

	/**
	 * @author Minimine
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> generate() {
		
		FileEdit fe = FileEdit.getLastEdit();
		
		List<Object> ret = list();

		Object[] content = (Object[]) this.content.getContent();
		String functionname = (String)content[0];
		List<Object> givenargs = (List<Object>)content[1];
		
		functionname = ConstUseParser.filter(functionname, consts);
		for(int i=0;i<givenargs.size();i++) {
			Object s=givenargs.get(i);
			givenargs.remove(s);
			
			if(s instanceof String) {
				if(consts.containsKey(fixIgnored((String) s))) {
					givenargs.add(consts.get(fixIgnored((String) s)));
				}
				else givenargs.add(fixSelector(ConstUseParser.filter((String) s, consts)));
			}else{
				givenargs.add(s);
			}
		}
		
		HashMap<String, Object> subs = listSubs(consts);
		
		Object f = subs.get(functionname);
		if(f==null)throwError("Function \""+functionname+" does not exists",this.content.getLine(),obj.getFile());
		if(!(f instanceof Content)) throwError("\""+functionname+" is not a function",this.content.getLine(),obj.getFile());
				
		Content method = (Content)f;
				
		if(!method.getType().equals("methoddefine"))throwError("\""+functionname+" is not a function",this.content.getLine(),obj.getFile());
				
		Object[] mtc = (Object[])method.getContent();
					
		List<String> neededargs =  (List<String>) mtc[1];
		
		ParsedObject methodcontent =  (ParsedObject) mtc[2];
		
		HashMap<String, Object> args = new HashMap<String, Object>();
					
		if(neededargs.size() < givenargs.size())throwError("To many arguments given in call of function "+functionname, this.content.getLine(), obj.getFile());
		if(neededargs.size() > givenargs.size()) {
			for(int i=0;i<neededargs.size();i++) {
				if(givenargs.size()<=i) {
					String s = neededargs.get(i);
					if(s.contains("=")) {
						String[] split = s.split("=",2);
	
						args.put(fixSelector(split[0]), fixSelector(split[1]));
					}
					else throwError("Not enough arguments given in call of function "+functionname, this.content.getLine(), obj.getFile()); 
				} else {
					Object arg = givenargs.get(i);
					args.put(neededargs.get(i), arg);
				}
			}
		}
		else {
			for(int i=0;i<neededargs.size();i++){
				Object arg = givenargs.get(i);
				args.put(neededargs.get(i), arg);
			}
		}
				
		HashMap<String, Object> filter = consts;
		filter.putAll(args);
					
		for(Content cmd : methodcontent.getContent()) {
			ret.addAll(generate(cmd, filter));
		}
					
		if(fe != null) ret.add(fe);
				
		return ret;
	}
}
