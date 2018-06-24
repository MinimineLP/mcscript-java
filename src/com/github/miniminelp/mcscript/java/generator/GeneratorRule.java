/**
 *
 */
package com.github.miniminelp.mcscript.java.generator;

import java.util.HashMap;
import java.util.List;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.util.MCScriptObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public abstract class GeneratorRule implements MCScriptObject, GeneratorFunctions {
	
	protected Content content;
	protected ParsedObject obj;
	protected List<Content> methods;
	protected HashMap<String,String> consts;
	private Generator generator;
	
	/**
	 * @author Minimine
	 * @return the content {@link Content}
	 */
	public Content getContent() {
		return content;
	}

	
	/**
	 * @author Minimine
	 * @param content the content to set {@link Content}
	 */
	public void setContent(Content content) {
		this.content = content;
	}

	/**
	 * @author Minimine
	 * @param obj the obj to set {@link ParsedObject}
	 */
	public void setObj(ParsedObject obj) {
		this.obj = obj;
	}
	
	/**
	 * @author Minimine
	 * @return the obj {@link ParsedObject}
	 */
	public ParsedObject getObj() {
		return obj;
	}
	
	/**
	 * @return the rules {@link List}?extends {@link Content}
	 */
	public List<Content> getMethods() {
		return methods;
	}

	/**
	 * @param rules the rules to set {@link List}?extends {@link Content}
	 */
	public void setMethods(List<Content> rules) {
		this.methods = rules;
	}

	/**
	 * @author Minimine
	 * @return the consts {@link HashMap}?extends {@link String}, {@link String}
	 */
	public HashMap<String, String> getConstants() {
		return consts;
	}
	
	/**
	 * @author Minimine
	 * @param consts the consts to set {@link HashMap}?extends {@link String}, {@link String} 
	 */
	public void setConstants(HashMap<String, String> consts) {
		this.consts = consts;
	}
	
	/**
	 * @author Minimine
	 * @param generator the generator to set {@link Generator}
	 */
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}
	
	/**
	 * @author Minimine
	 * @param content the content {@link Content}
	 * @return the result
	 */
	public List<Object> generate(Content content) {
		return generator.generate(content);
	}
	
	/**
	 * @param content the content to generate
	 * @param filter the filter to apply
	 * @return the generated content
	 */
	public List<Object> generate(Content content, HashMap<String,String> filter) {
		return generator.generate(content,filter);
	}
	
	/**
	 * <h1>Generate function.</h1>
	 * <p>abstract</p>
	 * @return {@link List}?extends {@link Object}
	 */
	public abstract List<Object> generate();
}
