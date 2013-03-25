package player;

import java.util.regex.Pattern;

/**
 * 
 * A token is a lexical item that the parser uses.
 */

public class Token {
	/**
	 * All the types of tokens that can be made. Group symbols into token types as follows: (all in regular expression format)
	 * C ::= .*
	 * K ::= .*
	 * L ::= .*
	 * M ::= .*
	 * Q ::= .*
	 * T ::= .*
	 * X ::= .*
	 * V ::= .*
	 * Octave ::= ("'"+) | (","+)
	 * NoteLength ::= [d* /? d+] | /
	 * Accidental ::= "^" | "^^" | "_" | "__" | "="
	 * Basenote ::= [A-Ga-g]
	 * Pitch ::= Accidental? Basenote Octave? NoteLength?
	 * Rest ::= "z"note-length?
	 * Tuplets ::= "([234]"
	 * Barline ::= "|" | "||" | "[|" | "|]"
	 * Nrepeat ::= "["[12]
	 * ChordsBegin::= "["
	 * ChordsEnd::= "]"
	 * RepeatBegin ::="|:"
	 * RepeatEnd ::=":|"
	 */

	public static enum Type {
		M, C, K, L,Q, T, X, V, Rest, Pitch, Tuplets, ChordsBegin, ChordsEnd, Barline, RepeatBegin, RepeatEnd, Nrepeat
	}

	public final Type type;
	public final Pattern pattern;
	public final String string;
	public String basenote;
	public double noteLength;
	public int octave;
	public int accid;
	
	
	/**
	 * Method Token converts regular expressions which are used as grammars to strings
	 * @param type: a regex which belongs to the types of regex defined in enum.
	 * string: the string expression of the regex
	 */

	public Token(Type type, String string, String basenote, double noteLength, int octave, int accid) {
		this.type = type;
		switch (type) {
		case M:
			this.pattern = Pattern.compile("M:.*");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case K:
			this.pattern = Pattern.compile("K:.*");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case T:
			this.pattern = Pattern.compile("T:.*");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case L:
			this.pattern = Pattern.compile("L:.*");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case Q:
			this.pattern = Pattern.compile("Q:.*");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case C:
			this.pattern = Pattern.compile("C:.*");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case X:
			this.pattern = Pattern.compile("X:.*");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case V:
			this.pattern = Pattern.compile("V:d+");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case Rest:
			this.pattern = Pattern.compile("z [[d* /? d+] | /]?");
			this.basenote = "z";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case Pitch:
			this.pattern = Pattern.compile("[\\^ | \\^\\^ | _ | __ | =]? [A-Ga-g] ['+ ,+]? [[d* /? d+] | /]?");
			this.basenote = Pattern.compile("[A-Ga-g]").toString();
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			
			break;
		case Tuplets:
			this.pattern = Pattern.compile("\\([234]");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case ChordsBegin:
			this.pattern = Pattern.compile("\\[");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case ChordsEnd:
			this.pattern = Pattern.compile("\\]");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case Barline:
			this.pattern = Pattern.compile("\\| |  \\|\\| | \\[\\| | \\|\\]");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case RepeatBegin:
			this.pattern = Pattern.compile("\\|:");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;

		case RepeatEnd:
			this.pattern = Pattern.compile(":\\|");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		case Nrepeat:
			this.pattern = Pattern.compile("\\[[12]");
			this.basenote = "0";
			this.noteLength = 0.0;
			this.octave = 0;
			this.accid = 0; 
			break;
		default:
			throw new RuntimeException("The input type is invalid");
		}
		this.string = string;
	}
}
