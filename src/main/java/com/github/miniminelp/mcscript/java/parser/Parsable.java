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

public class Parsable implements MCScriptObject, Cloneable {
	
	private char[] code;
	private int pos;
	
	
	/**
	 * @param code the code (char)
	 * @deprecated  use {@link Parsable#Parsable(String)} instead
	 */
	@Deprecated
	public Parsable( char[] code ) {
		
		this(new String(code));
		
	}
	
	
	/**
	 * @param code the code {@link String}
	 */
	public Parsable( String code ) {
		
		code = code.replaceAll("\n", LINESEPERATOR);
		this.pos = 0;
		this.code = code.toCharArray();
		
	}
	
	
	/**
	 * @deprecated Use {@link Parsable#previos()} instead
	 * @return the previos character {@link String}
	 */
	@Deprecated
	public String pre() {
		
		return previos();
		
	}
	
	/**
	 * @deprecated use {@link Parsable#hasPrevios()} instead
	 * @return has previos character
	 */
	@Deprecated
	public boolean hasPre() {
		return !previos().equals(null);
	}
	
	/**
	 * @return has previos character
	 */
	public boolean hasPrevios() {
		return previos()!=null;
	}
	
	
	/**
	 * @return the previos character {@link String}
	 */
	public String previos() {
		
		if(!has(pos-1)) return null;
		return Character.toString(this.code[pos-1]);
		
	}
	
	
	/**
	 * @return the actual character {@link String}
	 */
	public String actual() {

		if(!has(pos)) return null;
		return Character.toString(this.code[pos]);
		
	}
	
	
	/**
	 * @return the actual word {@link String}
	 */
	public String actualWord() {
		
		if(!isActualWord())return null;
		
		int pos = this.pos;
		String word = "";
		
		if(pos != 0)while (Util.LETTERS.contains(Character.toString(code[pos-1])) && pos > 1)
			pos--;
		
		while (Util.LETTERS.contains(Character.toString(code[pos])) && pos < code.length-1) {

			word += Character.toString(code[pos]);
			pos++;
			
		}
		
		return word;
		
	}
	
	
	/**
	 * @return the actual line {@link String}
	 */
	public String actualLine() {
		
		int pos = this.pos;
		
		if(pos>0)while (code[pos-1] != Util.LINEBREAK && pos > 1)
			pos--;
		if(pos==1)pos=0;
		
		String line = Character.toString(code[pos]);
		
		while ( pos < code.length-1) {

			pos++;
			if(code[pos] == Util.LINEBREAK) return line;
			line += Character.toString(code[pos]);
			
		}
		
		
		
		return line;
		
	}
	
	
	/**
	 * @param to char to get to (char)
	 * @return get text to next instance of this character (String)
	 */
	public String getToNext(char to) {
		
		int pos = this.pos;
		
		String cont = Character.toString(code[pos]);
		
		while ( pos < code.length-1) {

			pos++;
			if(code[pos] == to) return cont;
			cont += Character.toString(code[pos]);
			
		}
		
		return cont;
		
	}
	
	
	/**
	 * @param to char to skip at (char)
	 */
	public void skipToNext(char to) {
		
		while ( pos < code.length-1) {
			
			pos++;
			if(code[pos] == to) return;
			
		}
		
	}
	
	
	/**
	 * @return the next token {@link String}
	 */
	public String next() {
		
		if(!has(pos+1)) return null;
		pos++;
		return Character.toString(this.code[pos]);
		
	}
	
	
	/**
	 * 
	 */
	public void skipWord() {
		
		if(!isActualWord())return;
		
		while (Util.LETTERS.contains(Character.toString(code[pos])) && pos < code.length-1) {
			if(this.pos >= this.code.length-1) return;
			pos++;
		}
		
	}
	
	
	/**
	 * 
	 */
	public void skipLine() {
		
		while (this.code[this.pos] != Util.LINEBREAK) {

			if(this.pos >= this.code.length-1) return;
			this.pos++;
		
		}
	}
	
	
	/**
	 * @return the next token {@link String}
	 */
	public String peek () {
		
		return peek(1);
		
	}
	
	
	/**
	 * @param number peek number (integer)
	 * @return the character at the position {@link String}
	 */
	public String peek (int number) {
		
		if(!has(pos+number)) return null;
		return Character.toString(this.code[pos+number]);
		
	}
	
	
	/**
	 * @param pos the position (integer)
	 * @return the character at the position {@link String}
	 */
	public String get(int pos) {
		
		if(!has(pos)) return null;
		return Character.toString(this.code[pos]);
		
	}
	
	
	/**
	 * @param pos position to set (integer)
	 */
	public void setPosition(int pos) {
		
		this.pos=pos;
		
	}
	
	
	/**
	 * @deprecated use {@link Parsable#setPosition(int) instead}
	 * @param pos the position to go to (int)
	 */
	@Deprecated
	public void setPos(int pos) {
		
		this.pos = pos;
		
	}
	
	
	/**
	 * @return the position (int)
	 */
	public int getPosition() {
		
		return pos;
		
	}
	
	
	/**
	 * @deprecated use {@link Parsable#getPosition()} instead
	 * @return the position (int)
	 */
	@Deprecated
	public int getPos() {
		
		return pos;
		
	}
	
	
	/**
	 * @param pos the position to test for (int)
	 * @return contains this position (boolean)
	 */
	public boolean has(int pos) {
		
		if(pos < 0) return false;
		if(pos >= code.length)return false;
		return true;
		
	}
	
	
	/**
	 * @return test is has next (boolean)
	 */
	public boolean hasNext() {
		
		return has(this.pos+1);
		
	}
	
	
	/**
	 * @return the number of available tokens (int)
	 */
	public int getAvailableTokens() {
		
		return this.code.length - this.pos - 1;
		
	}
	
	
	/**
	 * @return is actual char content of a word (boolean)
	 */
	public boolean isActualWord() {
		
		return Util.LETTERS.contains(actual());
		
	}
	
	
	/**
	 * @return has next after skipping (boolean)
	 */
	public boolean skipIgnored() {
		
		while(Util.IGNORED.contains(actual())&&has(pos)){
			if(this.next()==null)return false;
		}
		return true;
		
	}
	
	
	/**
	 * @param number of tokens to skip (int)
	 * @return has next (boolean)
	 */
	public boolean skip(int number) {
		
		if(hasSpace(number)){
			pos += number;
			return true;
		}
		
		skipRest();
		return false;
		
	}
	
	
	/**
	 * @return has next (boolean)
	 */
	public boolean skip() {
		
		return skip(1);
		
	}
	
	
	/**
	 * @return the actual line number (int)
	 */
	public int getLine() {
		
		int pos = this.pos;
		int linebreaks = 1;
		
		while(pos>1) {
			if(code[pos]==Util.LINEBREAK)linebreaks++;
			pos--;
		}
		
		return linebreaks;
		
	}
	
	
	/**
	 * @return the actual column number in line (int)
	 * @deprecated use {@link Parsable#getColumn()} instead
	 */
	@Deprecated
	public int getCol() {

		return getColumn();
		
	}
	
	/**
	 * @return the actual column number in line (int)
	 */
	public int getColumn() {
		
		int pos = this.pos;
		int cols = 1;
		
		while(pos>1) {
			if(code[pos]==Util.LINEBREAK)return cols;
			cols++;
			pos--;
		}

		return cols;
		
	}
	
	
	/**
	 * @param number the number to have space (int)
	 * @return has enough space (boolean)
	 */
	public boolean hasSpace(int number) {
		
		return has(pos+number);
		
	}
	
	
	/**
	 * @param symbol the symbol to seach for (char)
	 * @return the distance to the symbol (integer)
	 */
	public int distanceToNext(char symbol) {
		
		for(int distance = 0; hasSpace(distance); distance++) 
			if(peek(distance).equals(Character.toString(symbol)))return distance;
		
		return Integer.MAX_VALUE;
		
	}
	
	
	/**
	 * @return the rest {@link String}
	 */
	public String getRest() {
		
		String cont = "";
		for(int i = 0; hasSpace(i); i++) 
			cont += get(pos+i);
		return cont;
		
	}
	
	
	/**
	 * 
	 */
	public void skipRest() {
		
		this.pos = this.code.length-1;
		
	}
	
	
	/**
	 * @deprecated Use {@link Parsable#actualNumber()} instead
	 * @return the actual number (String)
	 */
	@Deprecated
	public String actualNum() {
		
		return actualNumber();
		
	}
	
	
	/**
	 * @return the actual number (String)
	 */
	public String actualNumber() {
		
		Parsable clone = clone();
		String num = "";
		
		while(Util.NUMBERS.contains(clone.actual())) {
			num += clone.actual();
			if(!clone.hasNext())return num;
			clone.skip();
		}
		
		return num;
		
	}
	
	
	/**
	 * @deprecated Use {@link Parsable#isActualNumber()} instead
	 * @return is actual number
	 */
	@Deprecated
	public boolean isActualNum() {
		
		return isActualNumber();
		
	}
	
	
	/**
	 * @return is actual number
	 */
	public boolean isActualNumber() {
		
		return Util.NUMBERS.contains(actual());
		
	}
	
	
	/**
	 * @deprecated Use {@link Parsable#skipNumber()} instead
	 */
	@Deprecated
	public void skipNum() {
		
		skipNumber();
		
	}
	
	
	/**
	 * 
	 */
	public void skipNumber() {
		
		while(Util.NUMBERS.contains(actual())&&hasNext()) {
			skip();
		}
		
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Parsable clone() {
		try {
			return (Parsable)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName()+"@"+Integer.toHexString(hashCode())+": {"+LINESEPERATOR
				+new String(code)+LINESEPERATOR
				+"}";
	}
}