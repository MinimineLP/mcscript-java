/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.ConstUseParser;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
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
		
		FileEdit fe = new FileEdit(this.obj.getFile());
		
		List<Object> ret = new LinkedList<Object>();

		Object[] content = (Object[]) this.content.getContent();
		String functionname = (String)content[0];
		List<String> givenargs = (List<String>)content[1];
		
		functionname = ConstUseParser.applyFilter(functionname, consts);
		for(int i=0;i<givenargs.size();i++) {
			String s=givenargs.get(i);
			givenargs.remove(s);
			givenargs.add(ConstUseParser.applyFilter(s, consts));
		}
		
		for(Content method : methods) {
			Object[] mtc = (Object[])method.getContent();
			if(((String)mtc[0]).equals(functionname)) {
				
				List<String> neededargs =  (List<String>) mtc[1];
				
				ParsedObject methodcontent =  (ParsedObject) mtc[2];
				
				HashMap<String, String> args = new HashMap<String, String>();
				
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
							String arg = givenargs.get(i);
							arg = ConstUseParser.applyFilter(arg, consts);
							args.put(neededargs.get(i), arg);
						}
					}
				}
				else {
					for(int i=0;i<neededargs.size();i++){
						String arg = givenargs.get(i);
						arg = ConstUseParser.applyFilter(arg, consts);
						args.put(neededargs.get(i), arg);
					}
				}
				
				HashMap<String, String> filter = consts;
				filter.putAll(args);
				
				for(Content cmd : methodcontent.getContent()) {
					ret.addAll(generate(cmd, filter));
				}
				
				ret.add(fe);
				
				return ret;
			}
		}
		
		throwError("Called undefined function", this.content.getLine(), obj.getFile());
		
		return ret;
	}
}
