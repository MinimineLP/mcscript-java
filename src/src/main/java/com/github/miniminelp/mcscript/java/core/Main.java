/**
 * 
 */

package com.github.miniminelp.mcscript.java.core;

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */

import java.util.Scanner;

import com.github.miniminelp.mcscript.java.filemanager.Filemanager;
import com.github.miniminelp.mcscript.java.filemanager.Files;
import com.github.miniminelp.mcscript.java.filemanager.SystemFiles;
import com.github.miniminelp.mcscript.java.generator.Generator;
import com.github.miniminelp.mcscript.java.generator.rules.ConstDeclaration;
import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.ParsedObject;
import com.github.miniminelp.mcscript.java.parser.Parser;
import com.github.miniminelp.mcscript.java.util.MCScriptObject;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.3
 *
 */
public class Main implements MCScriptObject {
	
	private static String actualDataFolder;

	/**
	 * @param args java main args {@link String}[]
	 * @throws IOException Can throw a {@link IOException}
	 */
	public static void main(String[] args) throws IOException {
		
		if(!(args.length>0)){
			System.err.println("Needing command as first argument");
			return;
		}
		
		switch (args[0]) {
		case "compile":
			
			if(!(args.length>1)) {
				System.err.println("Needing path as second argument!");
				return;
			}
			
			File f = new File(args[1]);

			if(!f.exists()) {
				System.err.println("Could not find File \""+args[1]+"\"");
				return;
			}
			
			String cont = "";
			
			File data = new File(f, "data");
			
			if(!data.exists()) {
				System.err.println("Datapack "+args[1]+" has no data folder");
				return;
			}
			
			for(File sub : data.listFiles()) {

				Main.setActualDataFolder(sub.getName());
				
				File scripts = new File(sub, "scripts");
				if(scripts.exists()) {
					

					HashMap<String, String> consts = new HashMap<String, String>();
					List<Content> modals = new LinkedList<Content>(); 
					
					
					//Read standart functions
					String standartfunctions = "";
					Scanner sc = new Scanner(Main.class.getResourceAsStream("standartfunctions.gl.mcscript"));
					while(sc.hasNextLine())standartfunctions+=sc.nextLine()+LINESEPERATOR;
					sc.close();
					Parser pa = new Parser(standartfunctions, "standartfunctions.gl.mcscript");
					ParsedObject pobj = pa.parse();
					
					for (Content c : pobj.getContstantDefines()) {
						ConstDeclaration constparser = new ConstDeclaration();
						
						constparser.setContent(c);
						constparser.setObj(pobj);
						constparser.setConstants(consts);
						constparser.generate();
						
						HashMap<String, String> cs = constparser.getConstants();
						for(Entry<String, String> en : cs.entrySet()) {
							String k = en.getKey();
							String v = en.getValue();
							
							if(!consts.containsKey(k))consts.put(k, v);
						}
					}
					modals.addAll(pobj.getMethods());
					
					HashMap<String,File> files = getAllSubFiles(scripts);
					
					
					
					// Map functions from global files
					for(Map.Entry<String, File> entry : files.entrySet()) {
						
						String pos = entry.getKey();
						File file = entry.getValue();
						
						if(pos.endsWith(".gl.mcscript")) {

							pos=replaceLast(pos, ".mcscript", "");
							Scanner s = new Scanner(file);
							cont = "";
							while(s.hasNextLine())cont += s.nextLine() + LINESEPERATOR;
							s.close();
							Parser p = new Parser(cont, pos);
							ParsedObject obj = p.parse();
							
							for (Content c : obj.getContstantDefines()) {
								ConstDeclaration constparser = new ConstDeclaration();
								
								constparser.setContent(c);
								constparser.setObj(obj);
								constparser.setConstants(consts);
								constparser.generate();
								
								HashMap<String, String> cs = constparser.getConstants();
								for(Entry<String, String> en : cs.entrySet()) {
									String k = en.getKey();
									String v = en.getValue();
									
									if(!consts.containsKey(k))consts.put(k, v);
								}
							}
							modals.addAll(obj.getMethods());
						}
					}
					
					for(Map.Entry<String, File> entry : files.entrySet()) {
						
						String pos = entry.getKey();
						File file = entry.getValue();
						
						if(pos.endsWith(".mcscript")) {

							pos=replaceLast(pos, ".mcscript", "");
							Scanner s = new Scanner(file);
							cont = "";
							while(s.hasNextLine())cont += s.nextLine() + LINESEPERATOR;
							s.close();
							Parser p = new Parser(cont, pos);
							ParsedObject obj = p.parse();
							
							Generator gen = new Generator(obj, modals, consts);
							gen.generate();
						}
					}
				}
				SystemFiles.readyLoadGeneration();
				Filemanager.writeDownInFS(Files.getFiles(), new File(sub,"functions"));
			}
			break;
		case "new":
			if(!(args.length>1)) {
				System.err.println("Needing name as second argument!");
				return;
			}
			
			File mainfolder = new File(args[1]);
			if(mainfolder.exists()) {
				System.err.println("A datapack \""+mainfolder.getAbsolutePath()+"\" already exists!");
				return;
			}
			
			mainfolder.mkdirs();
			
			String pack_meta_content = "{" + LINESEPERATOR + 
					"  \"pack\": {" + LINESEPERATOR +
					"    \"pack_format\": 1," + LINESEPERATOR +
					"    \"description\": \""+args[1]+" vanilla pack generated by mcscript\"" + LINESEPERATOR + 
					"  }" + LINESEPERATOR +
					"}";
			
			writeFile(new File(mainfolder, "pack.mcmeta"), pack_meta_content);
			
			String datafoldername = args[1]
					.toLowerCase()
					.replaceAll("0", "")
					.replaceAll("1", "")
					.replaceAll("2", "")
					.replaceAll("3", "")
					.replaceAll("4", "")
					.replaceAll("5", "")
					.replaceAll("6", "")
					.replaceAll("7", "")
					.replaceAll("8", "")
					.replaceAll("9", "")
					.replaceAll("\\(", "")
					.replaceAll("\\)", "")
					.replaceAll("\\[", "")
					.replaceAll("\\]", "")
					.replaceAll("\\{", "")
					.replaceAll("\\}", "")
					.replaceAll("\\=", "")
					.replaceAll("\\/", "")
					.replaceAll("\\\\", "");

			File datafolder = new File(mainfolder, "data");
			datafolder.mkdirs();
			
			File minecraft = new File(datafolder, "minecraft");
			minecraft.mkdirs();
			
			File tags = new File(minecraft, "tags");
			tags.mkdirs();
			
			File function_tags = new File(tags, "functions");
			function_tags.mkdirs();
			
			writeFile(new File(function_tags,"load.json"),"{" + LINESEPERATOR + 
					"  \"values\":[" + LINESEPERATOR +  
					"    \""+datafoldername+":load\"" + LINESEPERATOR + 
					"  ]" + LINESEPERATOR + 
					"}");
			
			writeFile(new File(function_tags,"tick.json"),"{" + LINESEPERATOR + 
					"  \"values\":[" + LINESEPERATOR +  
					"    \""+datafoldername+":main\"" + LINESEPERATOR + 
					"  ]" + LINESEPERATOR + 
					"}");
			
			File contentfolder = new File(datafolder, datafoldername);
			contentfolder.mkdirs();
			
			File functions = new File(contentfolder, "functions");
			functions.mkdirs();

			File tgs = new File(contentfolder, "tags");
			tgs.mkdirs();

			File advancements = new File(contentfolder, "advancements");
			advancements.mkdirs();

			File scripts = new File(contentfolder, "scripts");
			scripts.mkdirs();
			
			writeFile(new File(scripts, "load.mcscript"),"#file: ./load" + LINESEPERATOR + 
					"" + LINESEPERATOR + 
					"/**" + LINESEPERATOR +
					" * @author " + System.getProperty("user.name") + LINESEPERATOR +
					" * @project " + args[1] + LINESEPERATOR +
					" * @since 0.0.1 " + LINESEPERATOR +
					" * @version 0.0.1 " + LINESEPERATOR +
					" */" + LINESEPERATOR + 
					"" + LINESEPERATOR + 
					"" + LINESEPERATOR + 
					"// here goes your load content" + LINESEPERATOR +
					"" + LINESEPERATOR +
					"" + LINESEPERATOR);
			
			
			writeFile(new File(scripts, "main.mcscript"),"#file: ./main" + LINESEPERATOR + 
					"" + LINESEPERATOR + 
					"/**" + LINESEPERATOR +
					" * @author " + System.getProperty("user.name") + LINESEPERATOR +
					" * @project " + args[1] + LINESEPERATOR +
					" * @since 0.0.1 " + LINESEPERATOR +
					" * @version 0.0.1 " + LINESEPERATOR +
					" */" + LINESEPERATOR + 
					"" + LINESEPERATOR + 
					"" + LINESEPERATOR + 
					"// here goes your load content" + LINESEPERATOR +
					"" + LINESEPERATOR +
					"" + LINESEPERATOR);
			
			
			break;
		default:
			System.err.println("This subcommand does not exist");
			break;
		}
	}
	
	
	/**
	 * @param text the text to search in {@link String}
	 * @param regex the regex to search for {@link String}
	 * @param replacement the replacement {@link String}
	 * @return the replaced text {@link String}
	 */
	public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }
	
	
	/**
	 * @param f the file to get the subfiles from {@link File}
	 * @return all subfiles {@link HashMap}?extends {@link String}, {@link File}
	 */
	public static HashMap<String, File> getAllSubFiles(File f) {
		
		HashMap<String, File> files = new HashMap<String, File>();
		if(!f.isDirectory())return null;
		
		for(File sub : f.listFiles()) {
			if(sub.isDirectory()) {
				HashMap<String, File> subs = getAllSubFiles(sub);
				for(Map.Entry<String, File> entry : subs.entrySet()) {
					files.put(sub.getName()+"/"+entry.getKey(),entry.getValue());
				}
			}
			else {
				files.put(sub.getName(), sub);
			}
		}
		return files;
	}
	
	/**
	 * @param f the file to write {@link File}
	 * @param content the content to write into the file {@link String}
	 */
	public static void writeFile(File f,String content) {
		try {
			if(!f.exists())f.createNewFile();
			Formatter x = new Formatter(f);
			x.format("%s", content);
			x.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the actualDataFolder
	 */
	public static String getActualDataFolder() {
		return actualDataFolder;
	}


	/**
	 * @param actualDataFolder the actualDataFolder to set
	 */
	public static void setActualDataFolder(String actualDataFolder) {
		Main.actualDataFolder = actualDataFolder;
	}
}
