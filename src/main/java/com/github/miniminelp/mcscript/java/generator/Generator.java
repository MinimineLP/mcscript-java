/**
 * 
 */

package com.github.miniminelp.mcscript.java.generator;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.3
 *
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.miniminelp.mcscript.java.parser.ConstUseParser;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ForEach;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.filemanager.FileContext;
import com.github.miniminelp.mcscript.java.filemanager.Files;
import com.github.miniminelp.mcscript.java.generator.rules.*;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.util.MCScriptObject;

public class Generator implements MCScriptObject, GeneratorFunctions {
	
	private ParsedObject obj;
	private List<Content> methods;
	private HashMap<String, String> consts = new HashMap<String,String>();
	private List<Content> globalmethods = new LinkedList<Content>();
	private HashMap<String, String> globalconsts = new HashMap<String,String>();
	private HashMap<String,GeneratorRule> rules = new HashMap<String,GeneratorRule>();
	
	public Generator(ParsedObject obj) {
		this.obj = obj;

		this.rules.put("command", new Command());
		this.rules.put("keyword", new Keyword());
		this.rules.put("switch", new Switch());
		this.rules.put("if", new If());
		this.rules.put("functioncall", new FunctionCall());
		this.rules.put("comment", new Comment());
		this.rules.put("vardeclaration", new VarDeclaration());
		this.rules.put("fileset", new FileSet());
		this.rules.put("constdeclaration", new ConstDeclaration());
		this.rules.put("booleandeclaration", new BooleanDeclaration());
		this.rules.put("varedit", new VarEdit());
		this.rules.put("raycast", new Raycast());
		this.rules.put("dowhile", new DoWhile());
		this.rules.put("foreach", new ForEach());
	}
	
	public Generator(ParsedObject obj, List<Content> methods, HashMap<String,String> consts) {
		this(obj);
		globalconsts = consts;
		globalmethods = methods;
	}
	
	public void generate() {
		
		
		methods = globalmethods;
		methods.addAll(obj.getMethods());
		
		consts = globalconsts;
		
		while(obj.hasSpace(0)){
			generateContent(obj.actual());
			obj.skip();
		}
	}
	
	public void generateContent(Content c) {
		
		
		for(Entry<String, GeneratorRule> entry : rules.entrySet()) {
			
			String key = entry.getKey();
			GeneratorRule rule = entry.getValue();
			
			if(c.getType().equals(key)) {
				
				rule.setContent(c);
				rule.setObj(obj);
				rule.setMethods(methods);
				rule.setConstants(consts);
				rule.setGenerator(this);
				
				List<Object> l = rule.generate();
				c=rule.getContent();
				this.obj=rule.getObj();
				this.methods=rule.getMethods();
				this.consts=rule.getConstants();
				
				FileContext file = Files.open(obj.getFile());
				for(Object o : l) {
					if(o instanceof FileEdit) {
						
						FileEdit fe = (FileEdit)o;
						String f = fe.getFile();
						
						f = ConstUseParser.applyFilter(f, consts);
						fe.setFile(f);
						
						obj.setFile(f);
						file = Files.open(f);
						
					} else {
						
						if(!c.getType().equals("comment")) {
							String s = o.toString();
							s=s.replaceAll("\r", "").replaceAll("\n", "");
							while(s.startsWith("\t"))s=s.replaceFirst("\t", "");
							while(s.startsWith(" "))s=s.replaceFirst(" ", "");
							file.write(ConstUseParser.applyFilter(s, consts));
						}
						else {
							String s = o.toString();
							s=s.replaceAll("\r", "").replaceAll("\n", "");
							while(s.startsWith("\t"))s=s.replaceFirst("\t", "");
							while(s.startsWith(" "))s=s.replaceFirst(" ", "");
							file.write(s);
						}
					}
				}
			}
		}
	}
	
	public List<Object> generate(Content c) {
		return generate(c, consts);
	}
	public List<Object> generate(Content c, HashMap<String,String> filter) {
		
		List<Object> ret = new LinkedList<Object>();
		
		for(Entry<String, GeneratorRule> entry : rules.entrySet()) {
			
			String key = entry.getKey();
			GeneratorRule rule = entry.getValue();
			
			if(c.getType().equals(key)) {
				
				rule.setContent(c);
				rule.setObj(obj);
				rule.setMethods(methods);
				rule.setConstants(filter);
				rule.setGenerator(this);
				
				List<Object> zw = rule.generate();
				
				for(Object o : zw) {
					if(o instanceof String) {
						String s = (String)o;
						s = ConstUseParser.applyFilter(s, filter);
						ret.add(s);
					}
					else if(o instanceof FileEdit) {
						FileEdit fe = (FileEdit)o;
						String s = fe.getFile();
						s = ConstUseParser.applyFilter(s, filter);
						fe.setFile(s);
						ret.add(fe);
						
					}
					else {
						ret.add(o);
					}
				}
			}
		}
		return ret;
	}
}
