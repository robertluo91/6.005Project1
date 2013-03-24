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
		C, K, L, M, Q, T, X, V, Rest, Pitch, Tuplets, ChordsBegin, ChordsEnd, Barline, RepeatBegin, RepeatEnd, Nrepeat
	}

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
		
		case M:
			this.pattern = Pattern.compile("M:.*");
			break;
		case K:
			this.pattern = Pattern.compile("K:.*");
			break;
		case T:
			this.pattern = Pattern.compile("T:.*");
			break;
		case L:
			this.pattern = Pattern.compile("L:.*");
			break;
		case Q:
			this.pattern = Pattern.compile("Q:.*");
			break;
		case C:
			this.pattern = Pattern.compile("C:.*");
			break;
		case X:
			this.pattern = Pattern.compile("X:.*");
			break;
		case V:
			this.pattern = Pattern.compile("V:d+");
			break;
		case Rest:
			this.pattern = Pattern.compile("z [[d* /? d+] | /]?");
			break;
		case Pitch:
			this.pattern = Pattern.compile("[\\^ | \\^\\^ | _ | __ | =]? [A-Ga-g] ['+ ,+]? [[d* /? d+] | /]?");
			break;
		case Tuplets:
			this.pattern = Pattern.compile("\\([234]");
			break;
		case ChordsBegin:
			this.pattern = Pattern.compile("\\[");
			break;
		case ChordsEnd:
			this.pattern = Pattern.compile("\\]");
			break;
		case Barline:
			this.pattern = Pattern.compile("\\| |  \\|\\| | \\[\\| | \\|\\]");
			break;
		case RepeatBegin: 
			this.pattern = Pattern.compile("\\|:");
			break;
		case RepeatEnd: 
			this.pattern = Pattern.compile(":\\|");
			break;
		case Nrepeat:
			this.pattern = Pattern.compile("\\[[12]");
			break;
		default:
			throw new RuntimeException("The input type is invalid");
		}
		this.string = string;

	}
}