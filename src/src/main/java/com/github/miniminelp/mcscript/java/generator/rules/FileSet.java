/**
 *
 */
package com.github.miniminelp.mcscript.java.generator.rules;

import java.util.List;

import com.github.miniminelp.mcscript.java.generator.GeneratorRule;
import com.github.miniminelp.mcscript.java.parser.ConstUseParser;

/**
 * @author Minimine
 * @since 0.0.1
 * @version 0.0.1
 *
 */
public class FileSet extends GeneratorRule {

	/**
	 * @see com.github.miniminelp.mcscript.java.generator.GeneratorRule#generate()
	 */
	@Override
	public List<Object> generate() {
		String file = ConstUseParser.applyFilter(((String) content.getContent()), this.consts);
		return list(new FileEdit(file));
	}
	
	/**
	 * @author Minimine
	 * @since 0.0.1
	 * @version 0.0.1
	 *
	 */
	public static class FileEdit {
		
		private static FileEdit lastedit = null;
		private String file;
		
		/**
		 * @param file the file to set {@link String}
		 */
		public FileEdit(String file) {
			this.setFile(file);
			setLastEdit(this);
		}
		
		/**
		 * @return the file {@link String}
		 */
		public String getFile() {
			return file;
		}

		
		/**
		 * @param file the file to set {@link String}
		 */
		public void setFile(String file) {
			this.file = file;
		}
		
		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return getClass().getName()+"@"+Integer.toHexString(hashCode())+":\t\""+file+"\"";
		}

		/**
		 * @param file the file name {@link String}
		 * @return te new FileEdit Object {@link FileEdit}
		 */
		public static FileEdit create(String file) {
			return new FileEdit(file);
		}


		/**
		 * @return the lastedit
		 */
		public static FileEdit getLastEdit() {
			return lastedit;
		}


		/**
		 * @param lastedit the lastedit to set
		 */
		public static void setLastEdit(FileEdit lastedit) {
			FileEdit.lastedit = lastedit;
		}
	}
}
