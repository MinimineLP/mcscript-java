/**
 *
 */
package com.github.miniminelp.mcscript.java.parser;

import java.util.Collection;
import java.util.LinkedList;

import com.github.miniminelp.mcscript.java.generator.GeneratorFunctions;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRuleFunctions;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.3
 *
 */
public class StatementParser implements ParserRuleFunctions, GeneratorFunctions {
	
	private Parsable p;
	private int line;
	private String file;
	
	/**
	 * @param statement the statement {@link String}
	 * @param line the line {@link Integer}
	 * @param file the file {@link String}
	 */
	public StatementParser(String statement, int line, String file) {
		this(new Parsable(statement), line, file);
	}
	
	/**
	 * @param parsable the parsable {@link Parsable}
	 * @param line the line {@link Integer}
	 * @param file the file {@link String}
	 */
	public StatementParser(Parsable parsable, int line, String file) {
		this.setParsable(parsable);
		this.setLine(line);
		this.setFile(file);
	}

	/**
	 * @return the parsable {@link Parsable}
	 */
	public Parsable getParsable() {
		return p;
	}

	/**
	 * @param p the parsable to set {@link Parsable}
	 */
	public void setParsable(Parsable p) {
		this.p = p;
	}
	
	/**
	 * @return the line {@link Integer}
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @param line the line to set {@link Integer}
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * @return the file {@link String}
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file the file to set {@link String}
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the parsed Statement
	 */
	public StringList parse() {
		return parse(p);
	}
	
	/**
	 * @param p the parsable {@link Parsable}
	 * @return the parsed list {@link StringList}
	 */
	public StringList parse(Parsable p) {
		StringList statements = new StringList();
		String ui = "if";
		
		String actual = "";
		
		do {
			p.skipIgnored();
			ui = "if";
			while(p.actual().equals("!")) {
				if(ui.equals("if"))ui="unless";
				else ui = "if";
				p.skip();
				p.skipIgnored();
			}
			boolean run = true;
			String work = "";
			while ( run ) {
				if(!p.hasNext())run=false;
				
				if(p.actual().equals("|")&&p.peek().equals("|")) run = false;
				else if(p.actual().equals("&")&&p.peek().equals("&")) run = false;
				else {
					work += p.actual();
					p.skip();
				}
			}
			
			work=fixSelector(work);
			
			Parsable sp = new Parsable(work);
			
			if(sp.isActualWord()) {
				String word = sp.actualWord();
				sp.skipWord();
				sp.skipIgnored();
				
				// Test "execute if/unless" keywords
				if(word.equals("blocks")||word.equals("block")||word.equals("entity")||word.equals("score")) {
					
					actual += ui+" "+work+" ";
					
				}
				
				// Test var work
				if(sp.actual().equals(">")) {
					sp.skip();
					statements=generateVarWork(statements, ui, word, sp, new String[] {
						Formats.IFBIGGEREQUALSNUMBER,
						Formats.IFBIGGEREQUALSVARIABLE,
					});
				}
				else if(sp.actual().equals("<")) {
					statements=generateVarWork(statements, ui, word, sp, new String[] {
						Formats.IFSMALLEREQUALSNUMBER,
						Formats.IFSMALLEREQUALSVARIABLE,
					});					
				}
				else if(p.actual().equals("=")) {
					statements=generateVarWork(statements, ui, word, sp, new String[] {
						Formats.IFEQUALSNUMBER,
						Formats.IFEQUALSVARIABLE,
					});
				}
				
				else if(!(work.split(" ").length>2)){
					if(work.contains(" ")) {
						String[] split = work.split(" ");
						if(split[1].startsWith("@")){
							if(split[1].contains("[")) {
								split[1]=replaceLast(split[1], "\\]", "");
								split[1]+=",tag="+split[0]+"]";
								actual += ui+" entity "+split[1];
							}
							else {
								actual += ui+" entity "+split[1]+"[tag="+split[0]+"]";
							}
						}
						else {
							actual += ui+" entity @e[name="+split[1]+",tag="+split[0]+"] ";
						}
					} else {
						actual += ui+" entity @e[tag=mcscriptTags,tag="+work+"] ";
					}	
				}
				
				else throwErr("Unexpected token \""+p.actual()+"\"");
			}
			else {
				throwErr("Unexpected token \""+p.actual()+"\"");
			}
			
			p.skipIgnored();
			
			if(p.hasNext()) {
				if(p.actual().equals("&")) {
					p.skip(2);
					p.skipIgnored();
				}
				if(p.actual().equals("|")) {
					p.skip(2);
					p.skipIgnored();
					statements.add(actual);
					actual="";
				}
			}
		} while(p.hasNext());
		if(actual != "")statements.add(actual);
		return statements;
	}
	
	public void throwErr(String error) {
		System.err.println(error+" at "+file+".mcscript:"+line);
		System.exit(1);
	}
	
	public StringList generateVarWork(StringList actual, String ui, String var, Parsable p, String[] formats) {

		final String NUMBERFORMAT = formats[0];
		final String VARIABLEFORMAT = formats[1];
		String operator = p.previos();
		
		if(p.actual().equals("=")) {
			p.skip();
			p.skipIgnored();
			if(p.isActualNumber()) {
				actual.add(NUMBERFORMAT
					.replaceAll("%i", ui)
					.replaceAll("%v", var)
					.replaceAll("%n", p.actualNumber())+" ");
				
				p.skipNumber();
			}
			
			else if(p.isActualWord()) {
				actual.add(VARIABLEFORMAT
						.replaceAll("%i", ui)
						.replaceAll("%v", var)
						.replaceAll("%n", p.actualWord())+" ");
					
					p.skipWord();
			}
			
			else {
				throwErr("\""+p.actual()+"\"must be a number or a word!");
			}
			
		}
		else {
			
			if(operator.equals("=")) {
				throwErr("Can't use \"=\" as operator in if, needing \"==\"");
			}
			p.skipIgnored();
			
		}
		return actual;
	}

	private static class Formats {
		
		public static final String IFBIGGEREQUALSNUMBER = "%i score %v %v matches %n..";
		public static final String IFBIGGEREQUALSVARIABLE = "%i score %v %v >= %n %n";
		public static final String IFEQUALSNUMBER = "%i score %v %v matches %n";
		public static final String IFEQUALSVARIABLE = "%i score %v %v == %n %n";
		public static final String IFSMALLEREQUALSNUMBER = "%i score %v %v matches ..%n";
		public static final String IFSMALLEREQUALSVARIABLE = "%i score %v %v <= %n %n";
		
	}
	
	public static class StringList extends LinkedList<String> implements Cloneable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3635471384005708291L;
		
		/**
		 * @param s a String to add into the list {@link String}
		 */
		public StringList(String s) {
			super();
			this.add(s);
		}
		
		/**
		 * 
		 */
		public StringList() {
			super();
		}
		
		
		/**
		 * @param col the collection
		 */
		public StringList(Collection<String> col) {
			super(col);
		}
		
		/**
		 * @param add the String to add
		 */
		public void addToAll(String add) {
			for(int i=0;i<size();i++) {
				set(i, get(i)+add);
			}
		}
	}
	

	
	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorFunctions#replaceLast(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String replaceLast(String text, String regex, String replacement) {
		return GeneratorFunctions.super.replaceLast(text, regex, replacement);
	}
}
