/**
 *
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.SubParsingExecutor;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;
import com.github.miniminelp.mcscript.java.parser.Util;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.4
 *
 */
public class VarEdit extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		if(!parsable.isActualWord())return false;
		// Create clone of p
		Parsable clone = parsable.clone();
		
		clone.skipWord();
		String[] varname = new String[] {parsable.actualWord(),parsable.actualWord()};
		int line = parsable.getLine();
		clone.skipIgnored();
		
		if(clone.actual().equals("@")) {
			clone.skip();
			
			if(clone.isActualWord()) {
				varname[1]="@"+clone.actualWord();
				clone.skipWord();
				clone.skipCompleteIgnored();
			}
			else return false;
		}
		
		else if(clone.actual().equals(".")||clone.actual().equals("[")) {
			String name = parsable.actualWord();
			parsable = clone;
			while(parsable.actual().equals(".")||parsable.actual().equals("[")) {
				if(parsable.actual().equals(".")) {
					parsable.skip();
					parsable.skipIgnored();
					if(!parsable.isActualWord())throwErr("Needing word");
					name+="."+parsable.actualWord();
					parsable.skipWord();
					parsable.skipIgnored();
				}
				else {
					String x = replaceLast(parseStatement().replaceFirst("\\[", ""), "\\]", "");
					name += "."+fixStatement(x);
					parsable.skipIgnored();
				}
			}
			
			if(parsable.actual().equals("=")) {
				
				parsable.skip();
				parsable.skipIgnored();
				
				Object val = parseConstValue();
				
				Content c = new Content("constdeclaration", new Object[]{
					name, val,
				}, line);
				
				parsed.add(c);
					
				return true;
					
			} else {
				throwErr("Needing value");
			}
		}
		
		else if(clone.isActualWord()) {
			varname[1]=clone.actualWord();
			clone.skipWord();
			clone.skipCompleteIgnored();
		}
		
		if(clone.actual().equals("+")&&clone.peek().equals("+")) {
			
			parsable.skipWord();
			parsable.skipCompleteIgnored();
			parsable.skip(2);
			
			Content c = new Content("varedit", new Object[]{varname,"+",new String[] {"1"}}, line);
			parsed.add(c);
			return true;
		}
		
		else if(clone.actual().equals("-")&&clone.peek().equals("-")) {
			parsable = clone;
			
			Content c = new Content("varedit", new Object[]{varname,"-",new String[] {"1"}}, line);
			parsed.add(c);
			return true;
		}
		
		else if(Util.COUNTINGOPERATORS.contains(clone.actual())&&clone.peek().equals("=")) {
			
			parsable=clone;
			parsable.skipWord();
			parsable.skipCompleteIgnored();
			String operator = parsable.actual();
			parsable.skip(2);
			parsable.skipCompleteIgnored();
			
			String[] val = null;
			
			if(parsable.isActualNumber()) {
				val = new String[] {parsable.actualNumber()};
				parsable.skipNumber();
			}
			else if(parsable.isActualWord()) {	
				val = new String[] {parsable.actualWord(),parsable.actualWord()};
				parsable.skipWord();
				parsable.skipCompleteIgnored();
				if(parsable.isActualWord()) {
					val[1] = parsable.actualWord();
					parsable.skipWord();
				}
			}
			else {
				throwErr("Can't count with \""+parsable.actual()+"\"");
			}
			
			Content c = new Content("varedit", new Object[]{varname,operator,val}, line);
			parsed.add(c);
			
			return true;
			
		}
		else if(clone.actual().equals("=")) {

			parsable = clone;
			parsable.skip();
			parsable.skipCompleteIgnored();
			
			if(parsable.isActualNumber()) {
				
				String[] val = new String[] {parsable.actualNumber()};
				
				parsable.skipNumber();
				
				Content c = new Content("varedit", new Object[]{varname,"=",val}, line);
				parsed.add(c);
				return true;
				
			} else if(parsable.isActualWord()){
				
				String[] val = new String[] {parsable.actualWord(),parsable.actualWord()};
				
				parsable.skipWord();
				parsable.skipCompleteIgnored();
				
				if((val[0].equals("true")||val[0].equals("false"))) {
					Content c = new Content("booleandeclaration", new Object[]{varname,val[0]}, line);
					parsed.add(c);
					return true;
				}
				else if(parsable.isActualWord()) {
					val[1] = parsable.actualWord();

					parsable.skipWord();
					parsable.skipCompleteIgnored();
				}
				
				Content c = new Content("varedit", new Object[]{varname,"=",val}, line);
				parsed.add(c);
				return true;
			} else if(parsable.actual().equals("{")||parsable.actual().equals("[")) {
				
				parsable.skip();
				parsable.skipIgnored();
				
				Object val = parseConstValue();
				
				Content c = new Content("constdeclaration", new Object[]{
					varname, val,
				}, line);
				
				parsed.add(c);
					
				return true;
			} else {
				throwErr("A var variable can just contain a number");
			}
		}
		return false;
	}
	
	/**
	 * @return the parsed value
	 */
	public Object parseConstValue() {
		
		Object[] s = parseConstValue(parsable);
		
		parsable = (Parsable) s[0];
		Object val = s[1];
		
		return val;
	}
	

	/**
	 * @param parsable the parsable {@link Parsable}
	 * @return an {@link Object} containing [{@link Parsable}]
	 */
	public Object[] parseConstValue(Parsable parsable) {
		
		Object val;
		
		if(parsable.isActualNumber()) {
		
			val = parsable.actualNumber();
			parsable.skipNumber();
					
		} else if(parsable.actual().equals("\"")){
			
			StatementParseResult res = parseStatement(parsable);
			parsable = res.getParsable();
			String s = res.getResult();
			val = s;
			
		} else if (parsable.actual().equals("'")) {

			StatementParseResult res = parseStatement(parsable);
			parsable = res.getParsable();
			String s = res.getResult();
			val = "\""+replaceLast(s.replaceFirst("'", ""), "'", "")+"\"";
				
		} else if (parsable.actual().equals("{")) {

			StatementParseResult tr = parseStatement(parsable);
			parsable = tr.getParsable();
			String r = tr.getResult();
			String s = replaceLast(r.replaceFirst("\\{", ""),"\\}","");
			Parsable p = new Parsable(s);
			
			HashMap<String, Object> object = new HashMap<String,Object>();
			
			while(p.hasNext()) {
				p.skipIgnored();
				if(!p.isActualWord())throwErr("Needing word",p,file);
				String key = p.actualWord();
				p.skipWord();
				p.skipIgnored();
				if(!p.actual().equals(":"))throwErr("Needing \":\"",p,file);
				p.skip();
				p.skipIgnored();
				Object[] res = parseConstValue(p);
				p = (Parsable) res[0];
				Object value = res[1];
				object.put(key,value);
				p.skipIgnored();
				if(p.actual().equals(","))p.skip();
				p.skipIgnored();
			}
			
			val = object;
			
		} else if (parsable.actual().equals("[")) {

			StatementParseResult tr = parseStatement(parsable);
			parsable = tr.getParsable();
			String r = tr.getResult();
			String s = replaceLast(r.replaceFirst("\\[", ""),"\\]","");
			
			Parsable p = new Parsable(s);
			
			HashMap<String, Object> object = new HashMap<String,Object>();
			
			int key = 0;
			
			while(p.hasNext()) {
				p.skipIgnored();
				Object[] res = parseConstValue(p);
				p = (Parsable) res[0];
				Object value = res[1];
				object.put(key+"",value);
				p.skipIgnored();
				if(p.actual().equals(","))p.skip();
				p.skipIgnored();
				key++;
			}
			
			val = object;
				
		}
		
		else if(parsable.isActualWord()&&parsable.actualWord().equals("modal")) {
			parsable.skipWord();
			parsable.skipIgnored();
			
			if(!parsable.actual().equals("("))throwErr("Can't create a modal without '()' ",parsable,file);
			
			parsable.skip();
				
			List<String> arguments = new LinkedList<String>();
				
			// Now get arguments (if exists)
			if(!parsable.actual().equals(")")) {
				
				String arg = "";
						
				while(!parsable.actual().equals(")")){
					
					if(!parsable.has(parsable.getPosition()))
						throwErr("Unclosed method definition",parsable,file);
							
					if(parsable.actual().equals(",")) {
						if(arg.equals(""))throwErr("Empty required method argument",parsable,file);
						arguments.add(arg);
						arg = "";
					}
							
					else arg += parsable.actual();
					parsable.skip();
					
				}
					
				if(arg.equals(""))throwErr("Empty required method argument",parsable,file);
				arguments.add(arg);
			}
					
			parsable.skip();
			parsable.skipIgnored();
			
			if(!parsable.actual().equals("{")) throwErr("Method without content");

			StatementParseResult tr = parseStatement(parsable);
			parsable = tr.getParsable();
			String r = tr.getResult();
			String statement = replaceLast(r.replaceFirst("\\{", ""),"\\}","");
			
			Parsable subp = new Parsable(statement);
			SubParsingExecutor exec = new SubParsingExecutor(subp, file);
			ParsedObject res = exec.parse();
			
			if(!res.getMethods().isEmpty())throwErr("Can't define a method in a method");
			
				
			Content action = new Content("methoddefine", new Object[]{"(Unknown)", arguments, res}, parsable.getLine());
			val = action;
		}
		
		else {
			int distanceToSemicolon = parsable.distanceToNext(";".charAt(0));
			int distanceToLinebreak = parsable.distanceToNext(LINESEPERATOR.charAt(0));
			int distanceToComma = parsable.distanceToNext(LINESEPERATOR.charAt(0));
				
			if(distanceToSemicolon > distanceToLinebreak&&distanceToSemicolon > distanceToComma) {
				val = parsable.getToNext(LINESEPERATOR.charAt(0));
				parsable.skipToNext(LINESEPERATOR.charAt(0));
			}
			else if(distanceToComma > distanceToLinebreak&&distanceToSemicolon < distanceToComma) {
				val = parsable.getToNext(LINESEPERATOR.charAt(0));
				parsable.skipToNext(LINESEPERATOR.charAt(0));
			}
			else {
				val = parsable.getToNext(";".charAt(0));
				parsable.skipToNext(";".charAt(0));
			}
		}
		return new Object[] {parsable,val};
	}
}
