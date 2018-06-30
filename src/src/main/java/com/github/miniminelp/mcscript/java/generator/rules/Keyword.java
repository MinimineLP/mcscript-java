/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.github.miniminelp.mcscript.java.filemanager.SystemFiles;
import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.ConstUseParser;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.StatementParser;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class Keyword extends GeneratorRule {

	public static int whileid=0;
	public static int raycastid=0;
	
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
		
		FileEdit fe = new FileEdit(this.obj.getFile());
		
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
			if(action != null) {
				
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
				
				Object[] lines = generated.toArray(); 
				
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
			else {
				
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
							
							FileEdit tfe = (FileEdit)sub;
							String filtered = ConstUseParser.applyFilter(tfe.getFile(), filter);
							tfe.setFile(filtered);
							contentlist.add(tfe);
							
						}
						else {
							contentlist.add(sub);
						}
							
					}
				}
			}
		}
		
		if(keyword.equals("while")) {
			if(statement==null)throwError("Missing statement for while block", content.getLine(), obj.getFile());
			if(action == null)throwError("Missing action for while block", content.getLine(), obj.getFile());
			
			List<String> statements = generateStatement(statement);
			
			for(String s : statements)
				contentlist.add("execute "+s+"run function mcscript/while"+whileid);
			
			contentlist.add(new FileEdit("mcscript/while"+whileid));
			
			for(Content c : action.getContent())contentlist.addAll(generate(c));
			
			for(String s : statements) {
				contentlist.add("execute "+s+"run function mcscript/while"+whileid);
			}
			System.out.println(contentlist);
			whileid++;
			
		}
		if(keyword.equals("raycast")) {
			
			if(action == null)throwError("Missing action for raycast block", content.getLine(), obj.getFile());
			
			String blocks = "100";
			String target = "air";
			String ui = "unless";
			boolean entity = false;
			
			if(statement!=null) {
				String[] s = statement.split(",");
				if(s.length>0)blocks=fixSelector(s[0]);
				if(s.length>1) {
					s[1]=fixSelector(s[1]);
					while(s[1].startsWith("!")) {
						s[1]=s[1].replaceFirst("\\!", "");
						if(ui.equals("if"))ui="unless";
						else ui = "if";
					}
					s[1]=fixSelector(s[1]);
					if(s[1].startsWith("block ")) {
						s[1]=s[1].replaceFirst("block ", "");
					}
					else if(s[1].startsWith("entity ")) {
						s[1]=s[1].replaceFirst("entity ", "");
						entity=true;
					}
					target=fixSelector(s[1]);
				}
			}

			SystemFiles.createScoreboardObjective("mcscript_raycast");

			contentlist.add("scoreboard players set raycast"+raycastid+" mcscript_raycast 0");
			contentlist.add("execute positioned ~ ~1 ~ run function mcscript/raycast"+raycastid);
			contentlist.add(new FileEdit("mcscript/raycast"+raycastid));
			
			contentlist.add("scoreboard players add raycast"+raycastid+" mcscript_raycast 1");
			
			List<Object> generated = list();
			for(Content c : action.getContent()) {
				generated.addAll(generate(c));
			}
			
			if(entity) {
				if(target.startsWith("@")&&target.contains("[")&&fixSelector(target).endsWith("]")) {
					target = fixSelector(target);
					target = target.substring(0, target.length()-1);
					target += ",distance=..0.7,sort=nearest]";
				}
				else if(target.startsWith("@")) {
					target += "[distance=..0.7,sort=nearest]";
				}
				else {
					target = "@e[name=\""+target+"\",distance=..0.7,sort=nearest]";
				}
				contentlist.add("execute positioned ^ ^ ^1 positioned ~ ~-1 ~ "+ui+" entity "+target+" run tag @s add mcscriptStop");
			}
			else contentlist.add("execute positioned ^ ^ ^1 "+ui+" block ~ ~ ~ "+target+" run tag @s add mcscriptStop");
			for(Object o : generated) {
				if(o instanceof String) {
					String s = (String)o;
					s=s.replaceAll("\r", "")
					   .replaceAll("\n", "")
					   .replaceAll("\t", "");
					while(s.startsWith(" "))s=s.replaceFirst(" ", "");
					contentlist.add("execute if entity @s[tag=mcscriptStop] run "+s);
				}
			}

			contentlist.add("execute positioned ^ ^ ^1 if score raycast"+raycastid+" mcscript_raycast matches .."+blocks+" if entity @s[tag=!mcscriptStop] run function mcscript/raycast"+raycastid);
			contentlist.add("tag @s[tag=mcscriptStop] remove mcscriptStop");
			raycastid++;
		}
		
		contentlist.add(fe);
		return contentlist;
	}

	public List<String> generateStatement(String statement) {
		
		StatementParser parser = new StatementParser(statement, content.getLine(), obj.getFile());
		List<String> parsed = parser.parse();
		
		return parsed;
	}
}