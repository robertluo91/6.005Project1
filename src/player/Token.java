package player;

import java.util.regex.Pattern;

/**
 * A token is a lexical item that the parser uses.
 */
public class Token {
	/**
	 * All the types of tokens that can be made. Group symbols into token types
	 * as follows: (all in regular expression format)
	 * 
	 * octave ::= ("'"+) | (","+) note-length ::= [d* /? d+] \\ /
	 * 
	 * accidental ::= "^" | "^^" | "_" | "__" | "=" 
	 * basenote ::= [A-Ga-g] 
	 * pitch ::= accidental? basenote octave? note-length? 
	 * rest ::= "z"note-length?
	 * dup-spec ::= "(2" 
	 * tri-spec ::= "(3" 
	 * quad-spec ::= "(4" 
	 * barline ::= "|" | "||" | "[|" | "|]" | ":|" | "|:" nth-repeat ::= "["d+
	 */
	public static enum Type {
		 Rest, Pitch, Dupspec, Trispec, Quadspec, Barline, Nrepeat
	}

	// create 3 variables (memory spaces?)

	public final Type type;
	public final Pattern pattern;
	public final String string;

	/**
	 * Method Token converts regular expressions which are used as grammars to
	 * strings
	 * 
	 * @param type
	 *            : a regex which belongs to the types of regex defined in enum.
	 *            string: the string expression of the regex
	 */

	public Token(Type type, String string) {
		this.type = type;
		switch (type) {
		/**
		case NoteLength:
			this.pattern = Pattern.compile("[d* /? d+] \\ /");
			break;
		case Octave:

			this.pattern = Pattern.compile("'+ \\,+");
			break;
		case Accidental:
			this.pattern = Pattern.compile("^ \\ ^^ \\ _ \\ __ \\ =");
			break;
		case Basenote:
			this.pattern = Pattern.compile("[A-Ga-g]");
			break;
			*/
		case Rest:
			this.pattern = Pattern.compile("z [[d* /? d+] \\ /]?");
			break;
		case Pitch:
			this.pattern = Pattern.compile("[^ \\ ^^ \\ _ \\ __ \\ =]? [A-Ga-g] ['+ \\,+]? [[d* /? d+] \\ /]?");
			break;
		case Dupspec:
			this.pattern = Pattern.compile("(2");
			break;
		case Trispec:
			this.pattern = Pattern.compile("(3");
			break;
		case Quadspec:
			this.pattern = Pattern.compile("(4");
			break;
		case Barline:
			this.pattern = Pattern.compile("| \\ || \\ [| \\ |] \\ :| \\ |:");
			break;
		case Nrepeat:
			this.pattern = Pattern.compile("[d+");
			break;
		default:
			throw new RuntimeException("The input type is invalid");
		}
		this.string = string;

	}
}