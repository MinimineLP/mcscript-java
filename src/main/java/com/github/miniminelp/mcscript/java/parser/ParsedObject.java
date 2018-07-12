/**
 * 
 */

package com.github.miniminelp.mcscript.java.parser;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.util.MCScriptObject;

public class ParsedObject implements MCScriptObject{
	
	private List<Content> children;
	int actualpos;
	private String file;
	
	/**
	 * @param file the file {@link String}
	 */
	public ParsedObject(String file) {
		
		children = new LinkedList<Content>();
		actualpos = 0;
		this.setFile(file);
		
	}
	
	/**
	 * @param content the content to add {@link Content}
	 */
	public void add(Content content) {
		
		children.add(content);
		
	}
	
	/**
	 * @return the actual content {@link Content}
	 */
	public Content actual() {

		if(children.size()>actualpos)return children.get(actualpos);
		return null;
		
	}
	
	/**
	 * 
	 */
	public void skip() {
		
		actualpos++;
		
	}
	
	/**
	 * @return the next content {@link Content}
	 */
	public Content next() {
		
		actualpos++;
		if(children.size()>actualpos)return children.get(actualpos);
		return null;
	}
	
	
	/**
	 * @return the next content {@link Content}
	 */
	public Content peek() {
		
		return peek(1);
		
	}
	
	/**
	 * @param number number to peek (int)
	 * @return the content from there {@link Content}
	 */
	public Content peek(int number) {
		
		if(!has(number+actualpos))return null;
		return children.get(number+actualpos);
		
	}
	
	/**
	 * @param i index to test for (integer)
	 * @return has the index (boolean)
	 */
	public boolean has(int i) {
		
		return i<this.children.size();
		
	}
	
	/**
	 * @param i index to test for (integer)
	 * @return has that space (boolean)
	 */
	public boolean hasSpace(int i) {
		
		return has(i+this.actualpos);
		
	}
	
	/**
	 * @return has next (boolean)
	 */
	public boolean hasNext() {
		return hasSpace(1);
	}
	
	/** 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		String childrenret = "";
		
		for(int i = 0; i < children.size(); i++) {
			
			childrenret += children.get(i).toString().replaceAll(LINESEPERATOR, LINESEPERATOR+"	");
			if(i < children.size()-1)childrenret += ","+LINESEPERATOR;
			
		}
		
		return this.getClass().getName()+":("+file+") {"+LINESEPERATOR+childrenret+"\n}";
		
	}
	
	
	/**
	 * @return the methods {@link List}?extends {@link Content}
	 */
	public List<Content> getMethods() {
		List<Content> methods = new LinkedList<Content>();
		
		for(Content c : children) {
			if(c.getType().equals("methoddefine"))methods.add(c);
		}
		
		return methods;
	}
	
	/**
	 * @return the methods {@link List}?extends {@link Content}
	 */
	public List<Content> getContstantDefines() {
		List<Content> methods = new LinkedList<Content>();
		
		for(Content c : children) {
			if(c.getType().equals("constdeclaration"))methods.add(c);
		}
		
		return methods;
	}
	
	/**
	 * @return the content {@link List}?extends {@link Content}
	 */
	public List<Content> getContent() {
		return children;
	}

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}
}
