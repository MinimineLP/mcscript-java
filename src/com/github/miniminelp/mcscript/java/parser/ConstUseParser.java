/**
 * 
 */

package com.github.miniminelp.mcscript.java.parser;

import java.util.Arrays;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;

public class ConstUseParser {
	
	/**
	 * @author Minimine
	 * @param s the String to apply filter {@link String}
	 * @param filter the filter to apply {@link HashMap}?extends {@link String}, {@link String}
	 * @return the filtered text {@link String}
	 */
	public static String applyFilter(String s, HashMap<String, String> filter) {
		
		if(filter == null) throw new NullPointerException("Error: Constant list mustn't be null");
		
		for(Entry<String, String> e : filter.entrySet()) {
			s = s.replaceAll("\\$\\("+e.getKey()+"\\)", e.getValue());
		}
		
		return s;
		
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
	 * @author Minimine
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
	 * @author Minimine
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
	 * @author Minimine
	 * @param objects the Objects to apply filter {@link String}[]
	 * @param filter the filter to apply {@link HashMap}?extends {@link String}, {@link String}
	 * @return the filtered text {@link String}
	 */
	public Object[] applyFilter(Object[] objects, HashMap<String,String> filter) {
		
		return (Object[]) applyFilter(Arrays.asList(objects), filter).toArray();
		
	}
	
}
