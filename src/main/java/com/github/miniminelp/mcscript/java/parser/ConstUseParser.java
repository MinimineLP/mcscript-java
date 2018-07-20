/**
 * 
 */

package com.github.miniminelp.mcscript.java.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRuleFunctions;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.4
 *
 */

public class ConstUseParser {
	

	/**
	 * @param s the String to apply filter {@link String}
	 * @param filter the filter to apply {@link HashMap}?extends {@link String}, {@link String}
	 * @return the filtered text {@link String}
	 */
	public static String applyFilter(String s, HashMap<String, String> filter) {
		return new ConstUseParsingObject(s, filter).parse();
	}
	
	
	
	/**
	 * @author Minimine
	 * @param strings the Strings to apply filter {@link String}[]
	 * @param filter the filter to apply {@link HashMap}?extends {@link String}, {@link String}
	 * @return the filtered text {@link String}
	 */
	public String[] applyFilter(String[] strings, HashMap<String,String> filter) {
		
		List<String> ret = new LinkedList<String>();
		
		for(String s : strings) {
			ret.add(applyFilter(s, filter));
		}
		return (String[]) ret.toArray();
	}
	
	/** 
	 * @param o the object to filter {@link Object}
	 * @param filter the filter {@link HashMap}?extends {@link String}, {@link String}
	 * @return the filtered object {@link Object}
	 */
	public static Object applyFilter(Object o, HashMap<String,String> filter) {
		
		if(o instanceof String) {
			String s = (String)o;
			return applyFilter(s, filter);
		}
		
		else if (o instanceof FileEdit) {
			
			FileEdit edit = (FileEdit)o;
			String s = edit.getFile();
			s = applyFilter(s, filter);
			edit.setFile(s);
			return edit;
			
		}
		
		return o;
	}
	

	
	/**
	 * @param objects Objects to filter {@link List}?extends Object
	 * @param filter the filter {@link HashMap}?extends {@link String} {@link String}
	 * @return the filtered Objects
	 */
	public static List<Object> applyFilter(List<Object> objects, HashMap<String,String> filter) {
		
		List<Object> ret = new LinkedList<Object>();
		
		for(Object o : objects) {
			ret.add(applyFilter(o, filter));
		}
		
		return ret;
		
	}
	
	/**
	 * @param objects the Objects to apply filter {@link String}[]
	 * @param filter the filter to apply {@link HashMap}?extends {@link String}, {@link String}
	 * @return the filtered text {@link String}
	 */
	public Object[] applyFilter(Object[] objects, HashMap<String,String> filter) {
		
		return (Object[]) applyFilter(Arrays.asList(objects), filter).toArray();
		
	}

	/**
	 * @param text the String to apply filter {@link String}
	 * @param filter the filter to apply {@link HashMap}?extends {@link String}, {@link String}
	 * @return the filtered text {@link String}
	 */
	public static String filter(String text, HashMap<String, Object> filter) {
		
		HashMap<String, String> the_filter = listSubs(filter);
		
		return applyFilter(text, the_filter);
	}
	
	/**
	 * @author Minimine
	 * @since 0.0.3
	 * @version 0.0.4
	 *
	 */
	public static class ConstUseParsingObject implements ParserRuleFunctions {
		
		private String s;
		private HashMap<String, String> filter;
		private Parsable p;
		private String ret;

		public ConstUseParsingObject(String s, HashMap<String, String> filter) {
			
			if(filter == null) throw new NullPointerException("Error: Constant list mustn't be null");
			if(s == null) throw new NullPointerException("Error: String to apply filter mustn't be null");
			
			this.s = s;
			this.filter=filter;
			this.p = new Parsable(s);
			this.ret = "";
			
		}
		
		/**
		 * @return the filtered text {@link String}
		 */
		public String parse() {
			
			ret = "";
			
			if(s.length()>0) {
				doSubParsing();
			}
			while(p.hasNext()) {
				doSubParsing();
			}

			return ret;
		}

		private void doSubParsing() {
			if(!p.hasSpace(2)) {
				ret += p.actual();
				p.skip();
				return;
			}
			
			if(!(p.actual().equals("$")&&p.peek().equals("("))) { 
				ret += p.actual();
				p.skip();
				return;
			}
			
			Parsable clone = p.clone();
			clone.skip(2);
			clone.skipIgnored();
			
			if(!clone.isActualWord()) {
				ret += p.actual();
				p.skip();
				return;
			}
			
			String var = clone.actualWord();
			clone.skipWord();
			clone.skipIgnored();

			while(clone.actual().equals(".")||clone.actual().equals("[")) {
				if(clone.actual().equals(".")) {
					clone.skip();
					clone.skipIgnored();
					if(!clone.isActualWord()) {
						ret += p.actual();
						p.skip();
						return;
					}
					var+="."+clone.actualWord();
					clone.skipWord();
					clone.skipIgnored();
				}
				else {
					StatementParseResult x = parseStatement(clone, "(Unknown File)");
					clone = x.getParsable();
					String s = replaceLast(x.getResult().replaceFirst("\\[", ""), "\\]", "");
					var += "."+fixStatement(s);
					clone.skipIgnored();
				}
			}
			String val;
			
			if(!(clone.actual().equals(")")&&filter.containsKey(var))) {
				ret += p.actual();
				p.skip();
				return;
			}
			val = filter.get(var);
			

			clone.skip();
			p = clone.clone();
			clone.skipIgnored();
			if(!clone.actual().equals(".")) {
				ret+=val;
				return;
			}
			clone.skip();
			clone.skipIgnored();
			if(!clone.isActualWord()) {
				ret+=val;
				return;
			}
			if(clone.actualWord().equals("repl")) {
				
				clone.skipWord();
				clone.skipIgnored();
				
				if(!clone.actual().equals("(")) {
					ret+=val;
					return;
				}
				
				clone.skip();
				clone.skipIgnored();
				
				if(!clone.actual().equals("\"")&&!clone.actual().equals("'")) {
					ret+=val;
					return;
				}
				StatementParseResult res = parseStatement(clone, "(Unknown file)");
				clone = res.getParsable();
				String search;
				if(res.getResult().startsWith("\""))search = replaceLast(res.getResult().replaceFirst("\"", ""),"\"","");
				else search = replaceLast(res.getResult().replaceFirst("'", ""),"'","");

				search = applyFilter(search, filter);
				
				clone.skipIgnored();
				
				if(!clone.actual().equals(",")) {
					ret+=val;
					return;
				}
				
				clone.skip();
				clone.skipIgnored();
				
				if(!clone.actual().equals("\"")) {
					ret+=val;
					return;
				}
				
				res = parseStatement(clone, "(Unknown file)");
				clone = res.getParsable();
				String replacement = replaceLast(res.getResult().replaceFirst("\"", ""),"\"","");
				
				replacement = applyFilter(replacement, filter);
				
				clone.skipIgnored();
				if(!clone.actual().equals(")")) {
					ret+=val;
					return;
				}
				clone.skip();
				p = clone;
				
				//test regex
				String zw = search;
				
				if(zw.startsWith("/")&&zw.endsWith("/")) {
					zw = replaceLast(zw.replaceFirst("\\/", ""),"\\/", "");
					ret+=val.replaceFirst(zw,replacement);
				}
				else if(zw.startsWith("/")&&zw.endsWith("/g")) {
					zw = replaceLast(zw.replaceFirst("\\/", ""),"\\/g", "");
					ret+=val.replaceAll(zw,replacement);
				}
				else ret+=val.replaceFirst(Pattern.quote(search),Matcher.quoteReplacement(replacement));
				
				return;
			}
			else {
				ret+=val;
				return;
			}
		}
		
		/**
		 * @param s the text to set
		 */
		public void setText(String s) {
			this.p = new Parsable(s);
			this.s = s;
		}
		
		/**
		 * @return the text
		 */
		public String getText() {
			return this.s;
		}
		
		/**
		 * @param filter the filter to set
		 */
		public void setFilter(HashMap<String, String> filter) {
			this.filter = filter;
		}
		
		/**
		 * @return the filter
		 */
		public HashMap<String, String> getFilter() {
			return this.filter;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> listSubs(HashMap<String, Object> hm) {
		
		HashMap<String, String> ret = new HashMap<String, String>();
		for(Entry<String, Object> e : hm.entrySet()) {
			if(e.getValue() instanceof HashMap) {
				for(Entry<String, String> entry : listSubs((HashMap<String, Object>) e.getValue()).entrySet()) {
					ret.put(e.getKey()+"."+entry.getKey(), entry.getValue());
				}
			}
			ret.put(e.getKey(),e.getValue().toString());
		}
		return ret;
	}
}
