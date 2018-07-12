/**
 *
 */
package com.github.miniminelp.mcscript.java.generator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public interface GeneratorFunctions {
	
	public default void throwError(String error, int line, String file) {
		
		System.err.println(error+" at line "+line+" in file "+file);
		System.exit(1);
		
	}
	
	public default String arrayJoin(String[] array, String join) {
		
		int i=0;
		
		String ret = "";
		
		for(String s : array) {
			ret += s;
			i++;
			if(i<array.length)ret+=join;
		}
		
		return ret;
		
	}
	
	/**
	 * @param text the text to apply filter {@link String}
	 * @param regex the regular expression to search {@link String}
	 * @param replacement the replacement {@link String}
	 * @return the replaced string {@link String}
	 */
	public default String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }
	
	/**
	 * @param s String to fix {@link String}
	 * @return Fixed String {@link String}
	 */
	public default String fixForExecute(String s) {
		s=s.replaceAll("	", "    ").replaceAll("\n", "").replaceAll("\r", "");
		while(s.startsWith(" "))s=s.replaceFirst(" ", "");
		return s;
	}

	/**
	 * @param o Object to add {@link Object}
	 * @return List with the object {@link List}?extends {@link Object}
	 */
	public default List<Object> list(Object o) {
		List<Object> list = new LinkedList<Object>();
		list.add(o);
		return list;
	}
	
	/**
	 * @return enpty List {@link List}?extends Object
	 */
	public default List<Object> list() {
		return new LinkedList<Object>();
	}
	
	/**
	 * @param selector the selector to fix {@link String}
	 * @return the fixed selector {@link String}
	 */
	public default String fixSelector(String selector) {

		while(selector.startsWith(" "))selector=selector.replaceFirst(" ", "");
		while(selector.endsWith(" "))selector=replaceLast(selector," ", "");
		
		if(selector.startsWith("\""))selector=selector.replaceFirst("\\\"", "");
		else if(selector.startsWith("'"))selector=selector.replaceFirst("\\'", "");
		
		if(selector.endsWith("\""))selector=replaceLast(selector, "\\\"", "");
		else if(selector.endsWith("'"))selector=replaceLast(selector, "\\'", "");
		
		return selector;
	}
}
