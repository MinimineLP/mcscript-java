/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.ConstUseParser;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class Keyword extends GeneratorRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		return generateKeyword(content, true);
	}

	/**
	 * @param content the content {@link Content}
	 * @param ex should the keyword an execute? {@link Boolean}
	 * @return the generated content {@link List}?extends {@link Object}
	 */
	public List<Object> generateKeyword(Content content, boolean ex) {
		
		List<Object> contentlist = new LinkedList<Object>();
		
		Object[] cont = (Object[]) content.getContent();
		String keyword = (String) cont[0];
		String statement = (String)cont[1];
		ParsedObject action = (ParsedObject) cont[2];
		
		if(keyword.equals("then")) {
			if(statement != null)throwError("Expected action after then", content.getLine(), obj.getFile());
			if(action == null)throwError("Expected action after then", content.getLine(), obj.getFile());
			
			List<Object> generated = new LinkedList<Object>();
			
			for(Content a : action.getContent()) {
				Object[] slist = generate(a).toArray();
				
				for(Object sub : slist) {
					if(sub instanceof String) {
						String s = (String)sub;
						
						s = s.replaceAll("\n", "").replaceAll("\r", "");
						
						while(s.startsWith(" ")) {
							s=s.replaceFirst(" ", "");
						}
						if(s!=null&&!s.equals("")){
							generated.add("run " + s);
						}
					} else {
						generated.add(sub);
					}
				}
			}
			contentlist.addAll(generated);
		}
		
		if(" as at asat positioned align dimension rotated anchored".contains(" "+keyword+" ")) {
			
			if(statement==null)throwError("Missing statement for "+keyword+" block", content.getLine(), obj.getFile());
			if(action != null)throwError("Expected then after "+keyword, content.getLine(), obj.getFile());
			
			statement = fixSelector(statement);
			
			Content next = obj.peek();
			if(!next.getType().equals("keyword"))throwError("Expected then after asat", content.getLine(), obj.getFile());
			String nextkw = (String) ((Object[])next.getContent())[0];
			if(!" as at asat positioned align dimension rotated anchored then ".contains(" "+nextkw+" "))throwError("Expected then after asat", content.getLine(), obj.getFile());
			obj.skip();
			
			Object[] lines = generateKeyword(next, false).toArray();
			
			for(int i=0;i<lines.length;i++) {
				if(lines[i] instanceof String) {
					if(!lines[i].equals("")&&!((String)lines[i]).startsWith("#")){
						if(keyword.equals("asat"))lines[i] = "as "+statement+" at @s "+lines[i];
						if(keyword.equals("as"))lines[i] = "as "+statement+" "+lines[i];
						if(keyword.equals("at"))lines[i] = "at "+statement+" "+lines[i];
						if(keyword.equals("positioned"))lines[i] = "positioned "+statement+" "+lines[i];
						if(keyword.equals("align"))lines[i] = "align "+statement+" "+lines[i];
						if(keyword.equals("dimension"))lines[i] = "dimension "+statement+" "+lines[i];
						if(keyword.equals("rotated"))lines[i] = "rotated "+statement+" "+lines[i];
						if(keyword.equals("anchored"))lines[i] = "anchored "+statement+" "+lines[i];
						if(keyword.equals("if"))lines[i] = "if "+statement+" "+lines[i];
						
						if(ex)lines[i]="execute "+lines[i];
					}
				}
			}
			
			contentlist.addAll(Arrays.asList(lines));
		}
		
		else if(keyword.equals("for")) {
			
			if(statement==null)throwError("Missing statement for for block", content.getLine(), obj.getFile());
			if(action == null)throwError("Missing action for for block", content.getLine(), obj.getFile());
			
			String[] statementparts = statement.split(",");
			
			if(statementparts.length!=2) throwError("Needing two parts in the for statement", content.getLine(), obj.getFile());
			
			int from = Integer.valueOf(statementparts[0]);
			int to = Integer.valueOf(statementparts[1]);
			
			for(int i=from; i<=to; i++) {
				
				HashMap<String, String> filter = new HashMap<String, String>();
				filter.put("i", i+"");
				
				for(Content a : action.getContent()) {
				
					Object[] slist = generate(a).toArray();
						
					for(Object sub : slist) {
							
						if(sub instanceof String) {
							String s = (String)sub;
							if(s!=null&&!s.equals("")) {
								contentlist.add(ConstUseParser.applyFilter(s, filter));
							}
						}
							
						else if(sub instanceof FileEdit) {
							
							FileEdit fe = (FileEdit)sub;
							String filtered = ConstUseParser.applyFilter(fe.getFile(), filter);
							fe.setFile(filtered);
							contentlist.add(fe);
							
						}
						else {
							contentlist.add(sub);
						}
							
					}
				}
			}
		}
		return contentlist;
	}
}
