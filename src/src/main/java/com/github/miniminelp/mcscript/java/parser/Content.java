/**
 * 
 */

package com.github.miniminelp.mcscript.java.parser;

import com.github.miniminelp.mcscript.java.util.MCScriptObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

public class Content implements MCScriptObject {
	
	private Object content;
	private String type;
	private int line;

	
	/**
	 * @param type the type {@link String}
	 * @param content the content {@link Object}
	 * @param line the line (integer)
	 */
	public Content(String type, Object content, int line) {

		this.type=type;
		this.content = content;
		this.line=line;
		
	}
	
	
	/**
	 * @param content the content to set {@link Object}
	 */
	public void setContent(Object content) {
		
		this.content=content;
		
	}
	
	
	/**
	 * @return the content {@link Object}
	 */
	public Object getContent() {
		
		return content;
		
	}
	
	
	/**
	 * @param type the type to set {@link String}
	 */
	public void setType(String type) {
		
		this.type=type;
		
	}
	
	
	/**
	 * @return the type {@link String}
	 */
	public String getType() {
		
		return type;
		
	}
	
	
	/**
	 * @return the line (integer)
	 */
	public int getLine() {
		
		return line;
		
	}
	
	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		if(this.content instanceof Object[]) {
			
			String ret = "";
			for(Object s : (Object[])this.content)ret += ","+s;
			ret = ret.replaceFirst(",", "");
			
			return this.getClass().getName()+": {"+LINESEPERATOR+"Type: \""+this.type+"\", "+LINESEPERATOR+"Content: {"+LINESEPERATOR+ret+LINESEPERATOR+"}"+LINESEPERATOR+"}";
		}
		
		return this.getClass().getName()+": {"+LINESEPERATOR+"Type: \""+this.type+"\", "+LINESEPERATOR+"Content: {"+LINESEPERATOR+""+this.content+LINESEPERATOR+"}"+LINESEPERATOR+"}";
	}
}
