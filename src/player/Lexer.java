package player;

import java.util.ArrayList;
import player.Token;
import java.util.List;
import player.Token.Type;

/**
 * A lexer takes a string and splits it into tokens that are meaningful to a
 * parser. For detailed description of token types, see Token.java
 */
public class Lexer {

    /**
     * Creates the lexer over the passed string.
     * 
     * @param string
     *            : The string to tokenize.
     */

    ArrayList<ArrayList<Token>> MusicBody;
    ArrayList<Token> MusicHeader; // you can call Header from outside to access
                                    // the arraylist of all the header tokens
    String Key;
    ArrayList<Token> voicecounter;
    int Tempo;
    ArrayList<Token> token;
    int currentlen;
    int parserPeekIndex;
    int Tick;

    public Lexer(String string) {

        // iterate through the input string and identify tokens;
        // append tokens to the global output list
        String Expression = new String(string);
        ArrayList<Token> output = new ArrayList<Token>();
        // create an arraylist "output" to put all the tokens generated inside
        int length = Expression.length();
        currentlen = 0;
        parserPeekIndex = 0;

        while (currentlen < length) {
            boolean anyMatchSoFar = false;
            for (int i = currentlen + 1; i < length; i++) {
                String currentString = Expression.substring(currentlen, i);
                Type[] typelist = { Type.M, Type.C, Type.K, Type.L, Type.Q,
                        Type.T, Type.X, Type.V, Type.Rest, Type.Pitch,
                        Type.Tuplets, Type.ChordsBegin, Type.ChordsEnd,
                        Type.Barline, Type.RepeatBegin, Type.RepeatEnd,
                        Type.Repeat_first, Type.Repeat_second, Type.Whitespace };
                for (Type t : typelist) {
                    Token testToken = new Token(t, "", "", 0.0, 0, 0,0);
                    if (testToken.pattern.matcher(currentString).matches()) {
                        // a token has been identified because its Matcher
                        // matches method == True
                        anyMatchSoFar = true;
                        currentlen = i;
                        output.add(new Token(t, currentString, "0", 0.0, 0, 0,0));

                    }
                }
            }
            if (!anyMatchSoFar) {
                // indicates a blank space in the very beginning of the string:
                // skip to the next position
                currentlen++;
            }
        }
        Chordcheck(output);

    }

    public void Chordcheck(ArrayList<Token> output) {
        int diff = 0; // check if output tokens have equal chordsbegin and
                        // chordsend
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.ChordsBegin)
                diff++;
            if (output.get(i).type == Type.ChordsEnd)
                diff--;
            if (diff > 1)
                throw new RuntimeException("2 or more consecutive chordbegin");
            else if (diff < 0)
                throw new RuntimeException("chordend more than chardbegin");
        }
        if (diff != 0)
            throw new RuntimeException(
                    "invalid input pattern: should have equal number of chordbegin and chordend");
        for (int i = 0; i < output.size(); i++) { // checks if output tokens
                                                    // have Whitespace within
                                                    // chords
            boolean inchord = false;
            if (output.get(i).type == Type.ChordsBegin) {
                inchord = true;
            } else if (output.get(i).type == Type.ChordsEnd) {
                inchord = false;
            }
            if (inchord = true) {
                if (output.get(i).type != Type.Pitch
                        && output.get(i).type != Type.Rest) {
                    throw new RuntimeException(
                            "there are types other than pitch and rest in chord");
                }
            }
        }
        Header(output);
    }

    // The following ChordGen method generates Chord type tokens, it's not used
    // at this point, maybe delete later
    public void ChordGen(String string) {
        String Expression = new String(string);
        ArrayList<Token> output = new ArrayList<Token>();
        int length = Expression.length();
        while (currentlen < length) {
            boolean anyMatchSoFar = false;
            for (int i = currentlen + 1; i < length; i++) {
                String currentString = Expression.substring(currentlen, i);
                Type[] typelist = { Type.M, Type.C, Type.K, Type.L, Type.Q,
                        Type.T, Type.X, Type.V, Type.Rest, Type.Pitch,
                        Type.Tuplets, Type.Chord, Type.Barline,
                        Type.RepeatBegin, Type.RepeatEnd, Type.Repeat_first,
                        Type.Repeat_second, Type.Whitespace };
                for (Type t : typelist) {
                    Token testToken = new Token(t, "", "", 0.0, 0, 0,0);
                    if (testToken.pattern.matcher(currentString).matches()) {
                        anyMatchSoFar = true;
                        currentlen = i;
                        output.add(new Token(t, currentString, "0", 0.0, 0, 0,0));
                    }
                }
            }
            if (!anyMatchSoFar) {
                currentlen++;
            }
        }
    }

    // adds all the header tokens into Headers and remove them from output
    // make sure V1, V2, etc is only added to header once if they exist
    public void Header(ArrayList<Token> output) {
        ArrayList<Token> Headers = new ArrayList<Token>();
        for (int i = 0; i < output.size(); i++) {
            String str = output.get(i).string;
            if (str.startsWith("C:") || str.startsWith("K:")
                    || str.startsWith("L:") || str.startsWith("M:")
                    || str.startsWith("Q:") || str.startsWith("T:")
                    || str.startsWith("X:")) {
                Headers.add(output.get(i));
                output.remove(i);
            }
            if (str.startsWith("V:")) {
                if (Headers.contains(output.get(i)) == false) {
                    Headers.add(output.get(i));
                    output.remove(i);
                }
            }
        }
        this.MusicHeader = Headers;

        KeyTempo(Headers);
    }

    public void KeyTempo(ArrayList<Token> Headers) {
        ArrayList<Token> voicecounter = new ArrayList<Token>();
        for (int i = 0; i < Headers.size(); i++) {
            int keychecker = 0;
            int Tempochecker = 0;
            String str = Headers.get(i).string;
            if (str.startsWith("V:")) {
                voicecounter.add(Headers.get(i));
            }
            if (str.startsWith("K:")) {
                keychecker = 1;
                str = str.substring(2);
                if (str.startsWith(" ")) {
                    str = str.substring(1);
                }
                this.Key = str;
            }
            if (str.startsWith("Q:")) {
                Tempochecker = 1;
                str = str.substring(2);
                if (str.startsWith(" ")) {
                    str = str.substring(1);
                }
                this.Tempo = Integer.parseInt(str);
            }
            if (keychecker == 0) {
                this.Key = "C"; // default Key
            }
            if (Tempochecker == 0) {
                this.Tempo = 100; // default Tempo
            }

        }
        this.voicecounter = voicecounter; // voicecounter is not used later
    }

    // NoteLength method updates all the note-lengths for pitch and rest tokens
    // also changes basenote, octave, accid fields for Rest, Pitch types
    public void NoteLength(ArrayList<Token> output) {
        for (int i = 0; i < output.size(); i++) {
            String str = output.get(i).string;
            if (output.get(i).type == Token.Type.Rest) {
                if (str.matches("z")) {
                    str.concat("1/1");
                    output.get(i).basenote = "z";
                    output.get(i).noteLength = 1.0;
                } else if (str.matches("z/")) {
                    str.replaceAll("/", "1/2");
                    output.get(i).basenote = "z";
                    output.get(i).noteLength = 0.5;

                } else if (str.matches("z/d+")) {
                    str.replaceAll("/", "1/");
                    int denom = Integer
                            .parseInt(str.substring(str.indexOf("/") + 1));
                    output.get(i).basenote = "z";
                    output.get(i).noteLength = 1.0 / denom;

                } else if (str.matches("zd+/d+")) {
                    for (int begin = 0; begin < str.length(); begin++) {
                        String substring = str.substring(begin);
                        if (substring.matches("d+/d+")) {
                            int num = Integer.parseInt(substring.substring(0,substring.indexOf("/")));
                            int denom = Integer.parseInt(substring
                                    .substring(substring.indexOf("/")) + 1);
                            long gcd = gcd((long) num, (long) denom);
                            num = num / (int) gcd;
                            denom = denom / (int) gcd;
                            str.replaceAll("d+/d+", Integer.toString(num) + "/"
                                    + Integer.toString(denom));
                            output.get(i).basenote = "z";
                            output.get(i).noteLength = num * 1.0 / denom;
                        }
                    }
                }
            } else if (output.get(i).type == Token.Type.Pitch) {
                for (int begin = 0; begin < str.length(); begin++) {
                    int hatcount = 0;
                    if (str.substring(begin, begin + 1).matches("[A-G]")) {
                        output.get(i).basenote = str
                                .substring(begin, begin + 1);
                    } else if (str.substring(begin, begin + 1).matches("[a-g]")) {
                        output.get(i).basenote = str
                                .substring(begin, begin + 1).toUpperCase();
                        output.get(i).octave = output.get(i).octave + 1;
                    } else if (str.substring(begin, begin + 1) == "^") {
                        output.get(i).accid = output.get(i).accid + 1;
                        hatcount += 1;
                    } else if (str.substring(begin, begin + 1) == "_") {
                        output.get(i).accid = output.get(i).accid - 1;
                        hatcount -= 1;
                    }
                    else if (str.substring(begin, begin + 1) == "'") {
                        output.get(i).octave = output.get(i).octave + 1;
                    } else if (str.substring(begin, begin + 1) == ",") {
                        output.get(i).octave = output.get(i).octave - 1;
                    }
                    if (hatcount > 2) {
                        throw new RuntimeException(
                                "accidental too high for this pitch");
                    } else if (hatcount < 2) {
                        throw new RuntimeException(
                                "accidental too low for this pitch");
                    }
                }
                if (str.matches("[\\^+\\_+=]?[A-Ga-g]['+,+]?/")) {
                    str.replaceAll("/", "1/2");
                    output.get(i).noteLength = 0.5;

                } else if (str.matches("[\\^+\\_+=]?[A-Ga-g]['+,+]?/d+")) {
                    str.replaceAll("/", "1/");
                    int denom = Integer
                            .parseInt(str.substring(str.indexOf("/") + 1));
                    output.get(i).noteLength = 1.0 / denom;

                } else if (str.matches("[\\^+\\_+=]?[A-Ga-g]['+,+]?")) {
                    str.concat("1/1");
                    output.get(i).noteLength = 1.0;

                } else if (str.matches("[\\^+\\_+=]?[A-Ga-g]['+,+]?d+/d+")) {
                    for (int begin = 0; begin < str.length(); begin++) {

                        String substring = str.substring(begin);
                        if (substring.matches("d+/d+")) {
                            int num = Integer.parseInt(substring.substring(0,substring.indexOf("/")));
                            int denom = Integer.parseInt(substring.substring(substring.indexOf("/")) + 1);
                            long gcd = gcd((long) num, (long) denom);
                            num = num / (int) gcd;
                            denom = denom / (int) gcd;
                            str.replaceAll("d+/d+", Integer.toString(num) + "/"+ Integer.toString(denom));
                            output.get(i).noteLength = num * 1.0 / denom;

                        }
                    }
                }
            }
        }
        token = output;
        Tupnotelen(output);
    }

    // updates notelength for all pitches and rests under chords and tuplets,
    // return exception if there are types other than Pitch, Rest, or 'chord' in tuplets
    public void Tupnotelen(ArrayList<Token> output) {
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Tuplets) {
                int tup = Integer.parseInt(output.get(i).string.substring(1));
                int k = 0;
                boolean chord = false;
                for (int start = i + 1; start < output.size(); start++) {
                    if (k < tup) {
                        if (output.get(start).type == Type.ChordsBegin) {
                            chord = true;
                        }
                        if (output.get(start).type == Type.ChordsEnd) {
                            chord = false;
                            k++;
                        }
                        if (chord) {
                            if (output.get(start).type == Type.Pitch) {
                                output.get(start).noteLength = output.get(start).noteLength* (tup - 1)/ tup;

                            } else if (output.get(start).type == Type.Rest) {
                                output.get(start).noteLength = output.get(start).noteLength* (tup - 1)/ tup;
                            }
                        } else {
                            if (output.get(start).type == Type.Pitch) {
                                output.get(start).noteLength = output.get(start).noteLength* (tup - 1)/tup;
                                k++;
                            } else if (output.get(start).type == Type.Rest) {
                                output.get(start).noteLength = output.get(start).noteLength* (tup - 1)/tup;
                                k++;
                            }
                            if (output.get(start).type != Type.Rest
                                    && output.get(start).type != Type.Pitch) {
                                throw new RuntimeException(
                                        "other types other than Pitch, Rest, or 'chord' in tuplets");
                            }
                        }
                    } else if (k == tup) {
                        break;
                    }
                }
            }
        }
        LCM(output);
        token = output;
    }

    // calculates all the least common mutiple for all the denominators in the
    // notelengths for pitch, rest
    public void LCM(ArrayList<Token> output) {
        ArrayList<Integer> Denom = new ArrayList<Integer>();
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Pitch
                    || output.get(i).type == Type.Rest) {
                if (output.get(i).noteLength == 1.0) {
                    Denom.add(1);
                } else if (output.get(i).noteLength != 1.0) {

                    int deno = 1;
                    String str;
                    String substring = "0";
                    if (output.get(i).noteLength < 1.0) {
                        str = Double.toString(output.get(i).noteLength);
                        substring = str.substring(str.indexOf(".") + 1);
                    } else if (output.get(i).noteLength > 1.0) {
                        str = Double.toString(output.get(i).noteLength);
                        substring = str.substring(str.indexOf(".") + 1);
                    }
                    for (int a = 0; a < substring.length(); a++) {
                        deno = deno * 10;
                    }
                    long gcd = gcd((long) Integer.parseInt(substring),
                            (long) deno);
                    Denom.add(deno / (int) gcd);
                }
            }
        }
        Ticker(output, Denom);
    }

    // Ticker method changes all the note-length into the actual tick time.
    public void Ticker(ArrayList<Token> output, ArrayList<Integer> Denom) {
        int Tick = lcmlist(Denom);
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Pitch
                    || output.get(i).type == Type.Rest) {
                output.get(i).noteLength = output.get(i).noteLength * Tick;
            }
        }
        token = output;
        this.Tick = Tick;
        MusicBody(token, voicecounter);
    }

    public void MusicBody(ArrayList<Token> output, ArrayList<Token> voicecounter) {
        ArrayList<ArrayList<Token>> Body = new ArrayList<ArrayList<Token>>();

        if (voicecounter.size() == 0) { // only has 1 voice
            ArrayList<Token> VoiceArray = new ArrayList<Token>();
            for (int i = 0; i < output.size(); i++) {
                VoiceArray.add(output.get(i));
            }
            Body.add(VoiceArray);
        } else { // >1 voice

            for (int a = 0; a < voicecounter.size(); a++) {
                ArrayList<Token> VoiceArray = new ArrayList<Token>();

                int counter = -1;
                for (int i = 0; i < output.size(); i++) {
                    String str = output.get(i).string;
                    if (str.startsWith("V:")) {
                        if (voicecounter.contains(output.get(i))) {
                            counter = voicecounter.indexOf(output.get(i));
                            continue;
                        } else {
                            throw new RuntimeException(
                                    "Voice in the music didn't appear in the header");
                        } // 1st error check to see if V in the body also
                            // appeared in the header
                    }

                    if (counter == a) {
                        VoiceArray.add(output.get(i));
                    }
                }
                String str = VoiceArray.get(-1).string;
                if (str.matches("\\| | \\|\\] | \\|\\| | :\\|") == false) {
                    throw new RuntimeException(
                            "The ending of the voice isn't marked with |, |], :|, or ||");
                } // 2nd error check for ending
                Body.add(VoiceArray);
            }
        }
        this.MusicBody = Body;

    }

    private static int lcm(int a, int b) {
        long A = a;
        long B = b;
        return (int) (A * (B / gcd(A, B)));
    }

    private static int lcmlist(List<Integer> input) {
        int current = input.get(0);
        for (int i = 1; i < input.size(); i++)
            current = lcm(current, input.get(i));
        return (int) current;
    }

    // since we are dealing with positive integers (the denominators of the a
    // notelength is positive)
    private static long gcd(long a, long b) {
        while (b > 0) {
            long exchange = b;
            b = a % b;
            a = exchange;
        }
        return a;
    }

    // helper methods for test
    /**
     * peek at the first token in the token list
     * 
     * @return the current first token in the token list
     */
    public Token peek() {
        if (parserPeekIndex >= token.size()) {
            return null;
        }
        return token.get(parserPeekIndex);
    }

    /**
     * get the token next to the current index
     * 
     * @return the next token to the current index
     * @throws Exception
     */
    public Token next() throws Exception {
        if (parserPeekIndex >= token.size()) {
            throw new Exception("Internal parser error");
        }
        return token.get(parserPeekIndex++);
    }

    /**
     * check if the type of the token at parserPeekIndex equals to the expected
     * token type
     * 
     * @param t
     *            the expected token type
     * @throws Exception
     */
    public void expect(Type t) throws Exception {
        if (parserPeekIndex >= token.size()) {
            throw new Exception("Internal parser error");
        }
        Token peeked = token.get(parserPeekIndex);
        if (peeked.type.equals(t)) {
            parserPeekIndex++;
        } else {
            throw new Exception("Syntax error at token " + peeked
                    + ", expecting " + t);
        }
    }
}
