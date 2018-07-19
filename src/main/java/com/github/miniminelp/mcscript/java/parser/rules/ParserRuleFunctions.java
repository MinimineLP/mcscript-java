/**
 * 
 */
package com.github.miniminelp.mcscript.java.parser.rules;

import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.Util;
import com.github.miniminelp.mcscript.java.util.MCScriptObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.4
 *
 */
public interface ParserRuleFunctions extends MCScriptObject {
	
	/**
	 * @param parsable the parsable {@link Parsable}
	 * @param file the file {@link String}
	 * @return the statement parse result {@link StatementParseResult}
	 */
	public default StatementParseResult parseStatement(Parsable parsable, String file) { 
		String search;
		String ret;
		
		if(parsable.actual().equals("(")) search = ")";
		else if(parsable.actual().equals("{")) search = "}";
		else if(parsable.actual().equals("[")) search = "]";
		else if(parsable.actual().equals("\"") || parsable.actual().equals("'")) {
			search = parsable.actual();
			ret = parsable.actual();
			ret += parsable.next();
			while(!(parsable.actual().equals(search)&&!parsable.previos().equals("\\"))) {
				if(!parsable.hasNext())throwErr("String without end", parsable, file);
				ret += parsable.next();
			}
			parsable.skip();
			return new StatementParseResult(ret, parsable);
		}
		else {
			return new StatementParseResult("", parsable);
		}
		
		ret = parsable.actual();
		parsable.skip();
		
		String opens = "({[\"'";
		
		while(!parsable.actual().equals(search)) {

			if(parsable.actual().equals("/")&&parsable.peek().equals("*"))
				while(!(parsable.actual().equals("*")&&parsable.peek().equals("/"))) {
					ret += parsable.actual();
					parsable.skip();
				}
			else if(parsable.previos().equals(Character.toString(Util.LINEBREAK))&&parsable.actual().equals("/")){
				ret += LINESEPERATOR+parsable.actualLine();
			}
			else if(parsable.previos().equals(Character.toString(Util.LINEBREAK))&&parsable.actual().equals("#")){
				ret += LINESEPERATOR+parsable.actualLine();
			}
			else if(parsable.actual().equals("/")&&parsable.peek().equals("/"))parsable.skipLine();
			else {
				if(!parsable.hasNext())throwErr("Method without end", parsable, opens);
				
				if(opens.contains(parsable.actual())) ret += parseStatement(parsable,file);
				else {
					ret += parsable.actual();
					parsable.skip();
				}
			}
		}
		
		ret += parsable.actual();
		parsable.skip();
		
		return new StatementParseResult(ret, parsable);
	}
	
	/**
	 * @param err the error {@link String}
	 * @param parsable the parsable {@link Parsable}
	 * @param file the file that contains the code {@link String}
	 */
	public default void throwErr(String err, Parsable parsable, String file) {
		throwErr(err, parsable.getLine(), parsable.getColumn(), parsable.actualLine(), file);
	}
	
	/**
	 * @param err the error {@link String}
	 * @param line the line that contains the error (integer) 
	 * @param col the column that creates the error (integer)
	 * @param actualLine the line content that creates the error {@link String}
	 * @param file the file that contains the code {@link String}
	 */
	public default void throwErr(String err, int line, int col, String actualLine, String file) {
		
		actualLine = actualLine.replaceAll(LINESEPERATOR, "");
		while(actualLine.startsWith("	")) {
			actualLine = actualLine.replaceFirst("	", "    ");
			col+=3;
		}
		String pointer = "";
		
		for(int i = 2; i<col; i++) pointer += " ";
		pointer += "^";
		System.err.println(err + " at position: ("+line+":"+col+") at file '"+file+".mcscript'");
		System.err.println(LINESEPERATOR+"Here: (\""+file+"\", line "+line+", column "+col+"):"+LINESEPERATOR+LINESEPERATOR+actualLine+LINESEPERATOR+pointer);
		System.exit(1);
		
	}
	
	
	/**
	 * @param text the text where to search {@link String}
	 * @param regex the regex to search for {@link String}
	 * @param replacement the replacement for the regex {@link String}
	 * @return The replaced String {@link String}
	 */
	public default String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }

	/**
	 * @param statement the statement to fix {@link String}
	 * @return the fixed statement {@link String}
	 */
	public default String fixStatement(String statement) {

		while(statement.startsWith(" "))statement=statement.replaceFirst(" ", "");
		while(statement.endsWith(" "))statement=replaceLast(statement," ", "");
		
		if(statement.startsWith("\""))statement=statement.replaceFirst("\\\"", "");
		else if(statement.startsWith("'"))statement=statement.replaceFirst("\\'", "");
		
		if(statement.endsWith("\""))statement=replaceLast(statement, "\\\"", "");
		else if(statement.endsWith("'"))statement=replaceLast(statement, "\\'", "");
		
		return statement;
	}
	
	/**
	 * @author Minimine
	 * @since 0.0.1
	 * @version 0.0.1
	 *
	 */
	public static class StatementParseResult {
		
		private String res;
		private Parsable p;
		
		
		/**
		 * @param res the result {@link String}
		 * @param p the parsable {@link Parsable}
		 */
		public StatementParseResult(String res, Parsable p) {
			this.setResult(res);
			this.setParsable(p);
		}
		
		
		/**
		 * @return the parsable {@link Parsable}
		 */
		public Parsable getParsable() {
			return p;
		}
		
		
		/**
		 * @param p parsable to set {@link Parsable}
		 */
		public void setParsable(Parsable p) {
			this.p = p;
		}

		
		/**
		 * @return the result {@link String}
		 */
		public String getResult() {
			return res;
		}
		
		
		/**
		 * @param res the result to set {@link String}
		 */
		public void setResult(String res) {
			this.res = res;
		}
		
		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return getResult();
		}
	}
}
