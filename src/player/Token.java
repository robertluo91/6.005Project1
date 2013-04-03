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
     * NoteLength ::= [0-9]*"/"?[0-9]*
     * Accidental ::= "^" | "^^" | "_" | "__" | "="
     * Basenote ::= [A-Ga-g]
     * Pitch ::= Accidental? Basenote Octave? NoteLength?
     * Rest ::= "z" note-length?
     * Tuplets ::= "([234]"
     * Barline ::= "|" | "||" | "[|" | "|]"
     * Repeat_First ::= [1
     * Repeat_Second ::= [2
     * ChordsBegin::= "["
     * ChordsEnd::= "]"
     * RepeatBegin ::="|:"
     * RepeatEnd ::=":|"
     * Comment ::= %.* 
     * Whitespace ::= " "
     */

    public static enum Type {
        M, C, K, L,Q, T, X, V, Rest, Pitch, Tuplets, ChordsBegin, ChordsEnd, 
        Barline, RepeatBegin, RepeatEnd, Repeat_first, Repeat_second, Whitespace, Comment
    }

    public final Type type;
    public final Pattern pattern;
    public String string;
    public int basenote;
    public int noteLength;
    public int octave;
    public int accid;
    public int chord;
    public int num;
    public int den;
    public boolean isNatural = false;
    
    /**
     * Converts regular expressions to strings as specified by the grammar
     * @param type: a regex which belongs to the types of regex defined in enum.
     * @param string: the string expression of the regex
     * @param basenote: an integer representation of the basenote  example: A is represented by 0
     * @param num: the numerator of the notelength of the pitch and rest in integer form
     * @param den: the denominator of the notelength of the pitch and rest in integer form
     * @param noteLength: an integer representation of the updated notelength of pitch and rest
     * @param accid: an integer representation of accidental 
     * @param octave: an integer representation of octave 
     * @param chord: an integer representation of the number of pitch/rest in a chord
     */
    public Token(Type type, String string, int basenote,int num, int den, int noteLength, int accid, int octave,int chord) {
        this.type = type;
        this.basenote = basenote;
        this.num = num;
        this.den = den;
        this.noteLength = noteLength;
        this.accid = accid;
        this.octave = octave;
        this.chord = chord;
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
            this.pattern = Pattern.compile("V:.*"); 
            break;
        case Rest:
            this.pattern = Pattern.compile("z([0-9]*/?[0-9]*)");
            break;
        case Pitch:
            this.pattern = Pattern.compile("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?([0-9]*/?[0-9]*)");   
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
            this.pattern = Pattern.compile("(\\||\\|\\||\\|\\])"); 
            break;
        case RepeatBegin:
            this.pattern = Pattern.compile("\\|:");
            break;
        case RepeatEnd:
            this.pattern = Pattern.compile(":\\|");
            break;
        case Repeat_first:
            this.pattern = Pattern.compile("\\[1"); 
            break;
        case Repeat_second:
            this.pattern = Pattern.compile("\\[2"); 
            break;
        case Whitespace:
            this.pattern = Pattern.compile(" ");
            break;
        case Comment:
            this.pattern = Pattern.compile("%.*");
            break;
        default:
            throw new RuntimeException("The input type is invalid");
        }
        this.string = string;
    }
    /**
     * test if 2 tokens are equal
     * @param o: an Object
     */
    public boolean equals(Object o){
        Token t = (Token)o;
        return (type == t.type)&&(pattern.equals(t.pattern))&&
                (string.equals(t.string))&&(basenote == t.basenote)&&
                (noteLength==t.noteLength)&&(octave==t.octave)&&
                (accid == t.accid)&&(chord == t.chord)&&(num == t.num)&&
                (den == t.den);
    }
}
