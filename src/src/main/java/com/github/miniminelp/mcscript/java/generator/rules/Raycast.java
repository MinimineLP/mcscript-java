/**
 * 
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.List;

import com.github.miniminelp.mcscript.java.core.Main;
import com.github.miniminelp.mcscript.java.filemanager.SystemFiles;
import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.generator.rules.FileSet.FileEdit;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;

/**
 * @author Minimine
 * @since 0.0.3
 * @version 0.0.3
 *
 */
public class Raycast extends GeneratorRule {

	private static int raycastid = 0;

	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		FileEdit fe = FileEdit.getLastEdit();
		
		Object[] cont = (Object[]) content.getContent();
		String statement = (String)cont[0];
		ParsedObject action = (ParsedObject) cont[1];
		ParsedObject action2 = (ParsedObject) cont[2];
		
		if(action == null)throwError("Missing action for raycast block", content.getLine(), obj.getFile());
		
		String blocks = "100";
		String gt = "air";
		String ui = "unless";
		String ui2 = "if";
		String target = null;

		boolean entity = false;
		boolean entity2 = false;
			
		if(statement!=null) {
			String[] s = statement.split(",");
			if(s.length>0)blocks=fixSelector(s[0]);
			if(s.length>1) {
				s[1]=fixSelector(s[1]);
				while(s[1].startsWith("!")) {
					s[1]=s[1].replaceFirst("\\!", "");
					if(ui.equals("if"))ui="unless";
					else ui = "if";
					s[1]=fixSelector(s[1]);
				}
				s[1]=fixSelector(s[1]);
				if(s[1].startsWith("block ")) {
					s[1]=s[1].replaceFirst("block ", "");
				}
				else if(s[1].startsWith("entity ")) {
					s[1]=s[1].replaceFirst("entity ", "");
					entity=true;
				}
				gt=fixSelector(s[1]);
				if(entity) {
					if(gt.startsWith("@")&&gt.contains("[")&&gt.endsWith("]")) {
						gt = fixSelector(gt);
						gt = gt.substring(0, gt.length()-1);
						gt += ",distance=..0.7,sort=nearest]";
					}
					else if(gt.startsWith("@")) {
						gt += "[distance=..0.7,sort=nearest]";
					}
					else {
						gt = "@e[name=\""+gt+"\",distance=..0.7,sort=nearest]";
					}
				}
			}
			if(s.length>2) {
				s[2]=fixSelector(s[2]);
				while(s[2].startsWith("!")) {
					s[2]=s[2].replaceFirst("\\!", "");
					if(ui2.equals("if"))ui2="unless";
					else ui2 = "if";
					s[2]=fixSelector(s[2]);
				}
				s[2]=fixSelector(s[2]);
				if(s[2].startsWith("block ")) {
					s[2]=s[2].replaceFirst("block ", "");
				}
				else if(s[2].startsWith("entity ")) {
					s[2]=s[2].replaceFirst("entity ", "");
					entity2=true;
				}
				target=fixSelector(s[2]);
				if(entity2) {
					if(target.startsWith("@")&&target.contains("[")&&target.endsWith("]")) {
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
				}
			}
		}

		SystemFiles.createScoreboardObjective("mcscript_raycast");

		List<Object> contentlist = list();
		contentlist.add("scoreboard players set raycast"+raycastid+" mcscript_raycast 0");
		contentlist.add("execute positioned ~ ~1 ~ run function "+Main.getActualDataFolder()+":mcscript/raycast"+raycastid);
		
		contentlist.add(new FileEdit("mcscript/raycast"+raycastid));
		if(action2!=null)for(Content c : action2.getContent())contentlist.addAll(generate(c));
		contentlist.add(new FileEdit("mcscript/raycast"+raycastid));
			
		contentlist.add("scoreboard players add raycast"+raycastid+" mcscript_raycast 1");
			
		List<Object> generated = list();
		for(Content c : action.getContent()) {
			generated.addAll(generate(c));
		}
		
		String add = "execute positioned ^ ^ ^1 ";
		if(target!=null) {
			if(entity2) add += "positioned ~ ~-1 ~ "+ui2+" entity "+target+" positioned ~ ~1 ~ ";
			else add += ui2+" block ~ ~ ~ "+target+" ";
		}
		
		if(entity) add += "positioned ~ ~-1 ~ "+ui+" entity "+gt+" run tag @s add mcscriptStop";
		else add += ui+" block ~ ~ ~ "+gt+" run tag @s add mcscriptStop";
		
		contentlist.add(add);
		contentlist.add("execute positioned ^ ^ ^1 if entity @s[tag=!mcscriptStop] if score raycast"+raycastid+" mcscript_raycast matches .."+blocks+" run function "+Main.getActualDataFolder()+":mcscript/raycast"+raycastid);
		
		for(Object o : generated) {
			if(o instanceof String) {
				String s = (String)o;
				s=s.replaceAll("\r", "")
				   .replaceAll("\n", "")
				   .replaceAll("\t", "");
				while(s.startsWith(" "))s=s.replaceFirst(" ", "");
				contentlist.add("execute if score raycast"+raycastid+" mcscript_raycast matches .."+blocks+" if entity @s[tag=mcscriptStop] run "+s);
			}
		}

		contentlist.add("tag @s[tag=mcscriptStop] remove mcscriptStop");
		if(fe != null)contentlist.add(fe);
		raycastid++;
		return contentlist;
	}
}
