/**
 * 
 */
package com.github.miniminelp.mcscript.java.parser.rules.sub;

import com.github.miniminelp.mcscript.java.parser.Content;
import com.github.miniminelp.mcscript.java.parser.Parsable;
import com.github.miniminelp.mcscript.java.parser.Util;
import com.github.miniminelp.mcscript.java.parser.rules.ParserRule;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class CommentFile extends ParserRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.parser.rules.ParserRule#apply()
	 */
	@Override
	public boolean apply() {
		if(parsable.actual().equals("#")) {
			
			Parsable clone = parsable.clone();
			clone.skip();
			
			while(clone.actual().equals(" ")||clone.actual().equals("	"))
				clone.skip();
			
			if(!clone.isActualWord())return false;
			if(!clone.actualWord().equals("file"))return false;
			clone.skipWord();
			if(!clone.actual().equals(":")) return false;
			clone.skip();
			while(clone.actual().equals(" ")||clone.actual().equals("	"))
				clone.skip();
			
			String filename = clone.getToNext(Util.LINEBREAK);
			filename = filename.replaceAll("	", "_")
								.replaceAll(" ", "_")
							   .replaceAll("\r", "")
							   .replaceAll("\n", "")
							   .replaceAll(":", "")
							   .replaceAll(";", "")
							   .replaceAll("\\*", "")
							   .replaceAll("\\?", "")
							   .replaceAll("\\<", "")
							   .replaceAll("\\>", "")
							   .replaceAll("\\\"", "")
							   .replaceAll("\\|", "")
							   .replaceAll("\\\\", "/");
			
			Content c = new Content("fileset", filename, parsable.getLine());
			parsed.add(c);
			parsable.skipLine();
			
			
			return true;
		}
		return false;
	}

}
