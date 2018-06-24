package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.StatementParser;
import com.github.miniminelp.mcscript.java.util.Utilitys;

public class If extends GeneratorRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> generate() {
		FileEdit fe = new FileEdit(this.obj.getFile());
		String ifid=Utilitys.generateRandomID();
		String tag = "mcscriptIf"+ifid;

		List<Object> ret = new LinkedList<Object>();
		
		Object[] obj = (Object[]) content.getContent();
		List<String> statements = generateStatement(fixSelector((String) obj[0]));
		ParsedObject action = (ParsedObject) obj[1];
		List<Content> elses = (List<Content>) obj[2];

		List<Object> gen = list();
		while(action.actual()!=null) {
			gen.addAll(generate(action.actual())); 
			action.skip();
		}
		for(String statement : statements) {
			for(Object o : gen) {
				if(o instanceof String) {
					String s = (String)o;
					s=s.replaceAll("\r","").replaceAll("\n", "").replaceAll("\t", "");
					while(s.startsWith(" "))s=s.replaceFirst(" ", "");
					ret.add("execute "+statement+"run "+s);
				} else {
					ret.add(o);
				}
			}
			
			ret.add("execute "+statement+"run tag @e[tag=mcscriptTags,tag=!"+tag+"] add "+tag);
			
			for(Content cont : elses) {
				if(cont.getType().equals("elseif")) {
					ret.addAll(generateElseIf(cont, tag));
				} else if (cont.getType().equals("else")) {
					ret.addAll(generateElse(cont, tag));
				}
			}
		}
		ret.add("tag @e[tag=mcscriptTags,tag="+tag+"] remove "+tag);
		
		ret.add(fe);
		return ret;
	}
	
	/**
	 * @param statement the statement {@link String}
	 * @param c the content {@link Content}
	 * @param tag the tag that te elseblock generates
	 * @return the generated else if block 
	 */
	private List<Object> generateElseIf(Content c, String tag) {
		
		List<Object> ret = list();
		
		Object[] obj = (Object[]) c.getContent();
		List<String> statements = generateStatement(fixSelector((String) obj[0]));
		ParsedObject act = (ParsedObject) obj[1];
		
		List<Object> gent = list();
		
		while(act.actual()!=null) {
			gent.addAll(generate(act.actual()));
			act.skip();
		}
		
		for(String statement : statements) {
			for(Object o : gent) {
				if(o instanceof String) {
					String s = (String)o;
					s=s.replaceAll("\r","").replaceAll("\n", "").replaceAll("\t", "");
					while(s.startsWith(" "))s=s.replaceFirst(" ", "");
					ret.add("execute if entity [tag=!"+tag+"] "+statement+"run "+s);
				} else {
					ret.add(o);
				}
			}
			ret.add("execute if entity [tag=!"+tag+"] "+statement+"run tag @e[tag=mcscriptTags,tag=!"+tag+"] add "+tag);
		}
		return ret;
	}
	
	private List<Object> generateElse(Content c, String tag) {
		List<Object> ret = list();

		ParsedObject act = (ParsedObject) c.getContent();
		
		List<Object> gent = list();
		
		while(act.actual()!=null) {
			gent.addAll(generate(act.actual()));
			act.skip();
		}
		
		for(Object o : gent) {
			if(o instanceof String) {
				String s = (String)o;
				s=s.replaceAll("\r","").replaceAll("\n", "").replaceAll("\t", "");
				while(s.startsWith(" "))s=s.replaceFirst(" ", "");
				ret.add("execute if entity [tag=!"+tag+"] run "+s);
			} else {
				ret.add(o);
			}
			ret.add("execute if entity [tag=!"+tag+"] run tag @e[tag=mcscriptTags,tag=!"+tag+"] add "+tag);
		}
		return ret;
	}

	public List<String> generateStatement(String statement) {
		
		StatementParser parser = new StatementParser(statement, content.getLine(), obj.getFile());
		List<String> parsed = parser.parse();
		
		return parsed;
	}
}