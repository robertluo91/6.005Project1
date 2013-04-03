package player;
import java.lang.RuntimeException;
import java.util.ArrayList;

import java.io.BufferedReader; 

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException; 
import player.Token;
import java.util.List;
import player.Token.Type;
import java.util.Map;
import java.util.HashMap;

/**
 * Tokenize an abc file and stores the tokens in the music head and music body into 2 data structures. 
 */
public class Lexer {    
    
    ArrayList<ArrayList<Token>> MusicBody;
    ArrayList<Token> MusicHeader; 
    String Key;
    ArrayList<Token> voicecounter;
    String M;
    String L;
    int size;
    int Tempo;
    ArrayList<Token> token;

    int parserPeekIndex;
    int current;
    int Tick;
    int headernum;
    ArrayList<String> bodystring;
    int bodyline;
    int totalnum;
    int checkerline;
    final String filename; 
    FileInputStream fstream; 
   
    BufferedReader br; 
    BufferedReader cr;
    
    @SuppressWarnings("serial")
    private static final Map<Character,Integer> stringNumMap = new HashMap<Character,Integer>() {{
        put('A',0); put('B',1); put('C',2);
        put('D',3); put('E',4); put('F',5);
        put('G',6); put('Z',7);
    }};

    
    /**
     * Read the abc file, count the total line number, and catch checked errors
     *  
     * @param filename the name of the abc file to be analyzed in the lexer
     * @throws IOException???
     */
    public Lexer(String filename) {
         this.filename = filename;
         fstream = null;
         br = null;
         headernum = 0;
         bodyline =0;
         try { 
             fstream = new FileInputStream(filename);
             FileInputStream fstream1 = new FileInputStream(filename); 
             br = new BufferedReader(new InputStreamReader(fstream));
             cr = new BufferedReader(new InputStreamReader(fstream1));
             while ((cr.readLine())!=null){
                 checkerline +=1;
             }
             processNextLine();
         } catch (IOException e) { 
             e.printStackTrace(); 
             throw new RuntimeException(e.getMessage()); 
         }
     } 
    /**
     * tokenizes the music header string into tokens. 
     * @throws IOException if the 1st field of header isn't X, or if the 2nd field of header isn't T,
     * 		   or if K is not that last field of the header, of if there are repeated header information,
     * 		   of if header information appears in the music body
     */
    public void processNextLine() throws IOException {         
        String str; 
        String str1;
        ArrayList<Token> headerinfo = new ArrayList<Token>();
        str = br.readLine();
        
        if (str.startsWith("X:")){
            headerinfo.add(new Token(Type.X, str, 0, 0, 0, 0,0,0,0));
            headernum++;
            str = br.readLine();          
            checkerline -=1;
        }
        else throw new IOException("the 1st field of header isn't X");
        
        if (str.startsWith("T:")){
            headerinfo.add(new Token(Type.T, str, 0, 0, 0, 0,0,0,0));
            headernum++;
            str = br.readLine();          
            checkerline -=1;
        }
        else throw new IOException("the 2nd field of header isn't T");
        
        boolean headercheck = true;
        while (headercheck==true){
            if (str.startsWith("X:")||str.startsWith("T:")) throw new IOException("repeated X or T type"); 
            else if (str.startsWith("C:")||str.startsWith("L:")||str.startsWith("Q:")||
                    str.startsWith("M:")){
                for (int i =0; i<headerinfo.size(); i++){                	
                    if (headerinfo.get(i).string.substring(0,1).equals(str.substring(0,1))){ throw new IOException("repeated C,L,Q,M types");}
                }  
                for (Type t : Type.values()) {
                    Token testToken = new Token(t, "", 0, 0, 0, 0,0,0,0);                    
                    if (testToken.pattern.matcher(str).matches()) {
                        headerinfo.add(new Token(t, str, 0, 0, 0, 0,0,0,0)); 
                        headernum++;                        
                        str = br.readLine();
                        checkerline -=1;
                        break;
                    }
                }
            }
            else if (str.startsWith("V:")){
            	for (int i =0; i<headerinfo.size(); i++){
            		if (headerinfo.get(i).string.equals(str)) throw new IOException("repeated Vi types");
            	}
            	headerinfo.add(new Token(Type.V, str, 0, 0, 0, 0,0,0,0)); 
            	headernum++;
            	str = br.readLine();
            	checkerline -=1;
            }
            else if (str.startsWith("%")){
            	
            	headerinfo.add(new Token(Type.Comment, str, 0, 0, 0, 0,0,0,0)); 
            	headernum++;
            	str = br.readLine();
            	checkerline -=1;
            }
            else if (str.startsWith("K:")){
                headerinfo.add(new Token(Type.K, str, 0, 0, 0, 0,0,0,0));
                headernum++;                
                headercheck = false;
                checkerline -=1;
                break;
            }
            else {
            	throw new IOException("K is not that last field of the header");            
            }
        }
        bodystring = new ArrayList<String>();
        while (checkerline >0){         
            str1 = br.readLine();
            if (str1.startsWith("X:")||str1.startsWith("C:")||str1.startsWith("L:")||str1.startsWith("Q:")||
                    str1.startsWith("M:")||str1.startsWith("T:")||(str1.startsWith("K:"))){
                throw new IOException("headerinfo appeared in the body");       
            }

            else{                
                bodystring.add(str1);               
                bodyline +=1;               
                checkerline -=1;                
            }
        }
            
        this.totalnum = headernum + bodyline;
        this.MusicHeader= headerinfo;
        KeyTempo(MusicHeader);
        BodyTokenize(bodystring);    
    }
    
    /**
     *Tokenize music body string into tokens. 
     * 
     * @param body string: The music body string to tokenize.
     * @throws RuntimeException if there is empty lines in music body, or if voice in the music body but didn't appear in the header,
     * 		   or if string fail to match any token types.
     */
    public void BodyTokenize(ArrayList<String> bodystring){
        ArrayList<Token> output = new ArrayList<Token>();
        
        for (int a=0; a<bodystring.size(); a++){
            String string= bodystring.get(a);
            int length = string.length();
            if (length ==0) throw new RuntimeException("Emptyline in music body");
        
            current = 0;
            while (current <length) {
                boolean anyMatchSoFar = false;            
                for (int i = length; i >current; i--) {                   
                    String currentString = string.substring(current, i);                
                    for (Type t : Type.values()) {
                        Token testToken = new Token(t, "", 0, 0, 0, 0, 0,0,0);                    
                        if (testToken.pattern.matcher(currentString).matches()) {
                            if (i< length-2&& currentString.equals("||") && string.substring(i+1,i+2).equals(":")){
                                output.add(new Token(Type.Barline, "|", 0,0,0,0,0,0,0));
                                output.add(new Token(Type.RepeatBegin, "|:", 0,0,0,0,0,0,0));
                                anyMatchSoFar = true;
                                current = i+1;
                            }
                            else {
                                anyMatchSoFar = true;  
                                current = i;
                                Token T = new Token(t, currentString, 0, 0, 0, 0, 0,0,0);
                                output.add(T);
                                if (t==Type.V){
                                    boolean c = false;
                                                              
                                    for (int b =0; b<MusicHeader.size(); b++){
                                        if (MusicHeader.get(b).string.equals(currentString)){                                   
                                            c= true;
                                            break;
                                        }
                                    }
                                    if (c==false) throw new RuntimeException("Voice didn't appear in the header");
                                }
                            }
                        }
                    }                
                }            
                if (!anyMatchSoFar) throw new RuntimeException("no right token match");
            }    
        }
        Chordcheck(output);
    }
    
    /**    
     * check if the chords are valid. 
     * @param output: the stream of tokens of music body
     * @throws RuntimeException if there are 2 or more consecutive "[", or if number of "[" not equal to the number of "]",
     *         or if there are token types other than pitch or rest within chord
     */
    //chord parameter of the 1st pitch/rest after chordsbegin    
    public void Chordcheck(ArrayList<Token> output) {
        int diff = 0; 
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.ChordsBegin) diff++;
            else if (output.get(i).type == Type.ChordsEnd) diff--;
            
            if (diff > 1) throw new RuntimeException("2 or more consecutive chordbegin");
            else if (diff < 0) throw new RuntimeException("chordend more than chordbegin");
        }
        if (diff != 0) throw new RuntimeException("invalid input pattern: should have equal number of chordbegin and chordend");
        for (int i = 0; i < output.size(); i++) {
            // checks if output tokens have types other than pitch and rest within chords
            if (output.get(i).type == Type.ChordsBegin) {
                int k = 0;
                for (int a =i+1; a< output.size(); a++){
                    if (output.get(a).type == Type.Pitch|| output.get(a).type == Type.Rest) {
                        k +=1;
                    }
                    else if (output.get(i).type != Type.Pitch 
                            && output.get(i).type != Type.Rest && output.get(a).type != Type.ChordsEnd){
                        throw new RuntimeException("there are types other than pitch and rest in chord");
                    }
                    else if (output.get(a).type == Type.ChordsEnd){
                        output.get(i+1).chord = k;
                        break;
                    }
                }
            }
        }
        NoteLength(output);
    }
    
    /**
     * stores Tempo, NoteLength, Keymeasure, meter information 
     * @param Headers: arraylist of tokens for the music header 
     */
    public void KeyTempo(ArrayList<Token> Headers) {
        ArrayList<Token> voicecounter = new ArrayList<Token>();
        int Tempochecker = 0;
        int nolenchecker = 0;
        int meterchecker = 0;
        for (int i = 0; i < Headers.size(); i++) {
            String str = Headers.get(i).string;
            if (str.startsWith("V:")) voicecounter.add(Headers.get(i));          
            else if (str.startsWith("K:")) {
                str = str.substring(2);
                if (str.startsWith(" ")) str = str.substring(1);                
                this.Key = str.trim();
            }
            else if (str.startsWith("Q:")) {
                Tempochecker = 1;            
                str = str.substring(2);
                if (str.startsWith(" ")) str = str.substring(1);
                this.Tempo = Integer.parseInt(str.trim());
            }
            else if (str.startsWith("L:")) {
                nolenchecker = 1;
                str = str.substring(2);
                if (str.startsWith(" ")) str = str.substring(1);
                this.L = str.trim();
            }
            else if (str.startsWith("M:")) {
                meterchecker = 1;
                str = str.substring(2);
                if (str.startsWith(" ")) str = str.substring(1);
                this.M = str.trim();
            }
        }
            if (Tempochecker == 0) {
                this.Tempo = 100; // default Tempo
                Tempochecker = 1;
            }
            if (nolenchecker == 0) {
                this.L = "1/8"; // default L
                nolenchecker = 1;
            }
            if (meterchecker == 0) {
                this.M = "4/4"; // default M
                meterchecker = 1;
            }
        
        
        int num = Integer.parseInt(this.L.substring(0, this.L.indexOf("/")));
        int denom = Integer.parseInt(this.L.substring(this.L.indexOf("/") + 1));
        System.out.println("num "+num + " denom "+ denom);
        this.Tempo = this.Tempo/4*(denom/num); 
        System.out.println(this.Tempo);
        this.voicecounter = voicecounter; 
    }

    /**
     * Store each Pitch and Rest's note-length, basenote, accidental, octave information in that specific token
     * @param output: ArrayList of tokens for the music body
     * @throws RuntimeException when the numerator or the denominator of the note-length is 0, or when the note-length
     *         for a pitch or a rest is noted as 0.  examples: "C0/4", "D2/0", "e0"
     */
    public void NoteLength(ArrayList<Token> output) {
        for (int i = 0; i < output.size(); i++) {
        	Token T = output.get(i);
            String str = T.string;
            if (T.type == Token.Type.Rest) {
                if (str.matches("z")) {
                    str.concat("1/1");
                    T.basenote = 7;
                    T.num = 1;
                    T.den = 1;
                } else if (str.matches("z/")) {           
                    T.basenote = 7;
                    T.num = 1;
                    T.den = 2;
                } else if (str.matches("z/[0-9]+")) {                    
                    int denom = Integer.parseInt(str.substring(2));
                    if (denom ==0) throw new RuntimeException("Invalid input: denominator of rest length can't be 0");
                    T.basenote = 7;
                    T.num = 1;
                    T.den = denom;
                } else if (str.matches("z[0-9]+/")) {                   
                    int nom = Integer.parseInt(str.substring(1,str.length()-1));
                    if (nom ==0) throw new RuntimeException("Invalid input: num of rest length can't be 0");
                    T.basenote = 7;
                    T.num = nom;
                    T.den = 2;
                } else if (str.matches("z[0-9]+")) {                    
                    int num = Integer.parseInt(str.substring(1));
                    if (num ==0) throw new RuntimeException("Invalid input: rest length can't be 0");
                    T.basenote = 7;
                    T.num = num;
                    T.den = 1;
                } else if (str.matches("z[0-9]+/[0-9]+")) {
                    for (int begin = 0; begin < str.length(); begin++) {
                        String substring = str.substring(begin);
                        if (substring.matches("[0-9]+/[0-9]+")) {
                            int num = Integer.parseInt(substring.substring(0,substring.indexOf("/")));
                            int denom = Integer.parseInt(substring.substring(substring.indexOf("/") + 1));
                            if (num ==0 || denom==0) throw new RuntimeException("Invalid input: num or denom of rest length can't be 0");
                            long gcd = gcd((long) num, (long) denom);
                            num = num / (int) gcd;
                            denom = denom / (int) gcd;
                            str.replaceAll("d+/d+", Integer.toString(num) + "/" + Integer.toString(denom));
                            T.basenote = 7;
                            T.num = num;
                            T.den = denom;
                        }
                    }
                }
            } else if (T.type == Token.Type.Pitch) {
                for (int begin = 0; begin < str.length(); begin++) {
                    int hatcount = 0;
                    if (str.substring(begin, begin + 1).matches("[A-G]")) {
                        T.basenote = stringToNumber(str.substring(begin, begin + 1));
                    } else if (str.substring(begin, begin + 1).matches("[a-g]")) {
                        T.basenote = stringToNumber(str.substring(begin, begin + 1).toUpperCase());
                        T.octave = output.get(i).octave + 1;
                    } else if (str.substring(begin, begin + 1).equals("^")) {
                        T.accid = output.get(i).accid + 1;
                        hatcount += 1;
                    } else if (str.substring(begin, begin + 1).equals("_")) {
                        T.accid = output.get(i).accid - 1;
                        hatcount -= 1;
                    } else if (str.substring(begin, begin + 1).equals("=")){
                        T.isNatural= true;
                    } else if (str.substring(begin, begin + 1).equals("'")) {
                        T.octave = output.get(i).octave + 1;
                    } else if (str.substring(begin, begin + 1).equals(",")) {
                        output.get(i).octave = output.get(i).octave - 1;
                    }
                    if (hatcount > 2) {
                        throw new RuntimeException("accidental too high for this pitch");
                    } else if (hatcount < -2) {
                        throw new RuntimeException("accidental too low for this pitch");
                    }
                }
                if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?/")) {
                    T.num = 1;
                    T.den = 2;
                } else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?/[0-9]+")) {                    
                    int denom = Integer.parseInt(str.substring(str.indexOf("/") + 1));
                    if (denom ==0) throw new RuntimeException("Invalid input: denominator of notelength can't be 0");
                    T.num = 1;
                    T.den = denom;
                } else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?[0-9]+/")) {
                    for (int begin = 0; begin < str.length(); begin++) {
                        String substring = str.substring(begin, str.length()-1);
                        if (substring.matches("[0-9]+")) {
                            int num = Integer.parseInt(substring);
                            if (num ==0) throw new RuntimeException("Invalid input: num of notelength can't be 0");
                            T.num = num;
                            T.den = 2;
                            break;}
                        }
                } else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?[0-9]+")) {
                    for (int begin = 0; begin < str.length(); begin++) {
                        String substring = str.substring(begin);
                        if (substring.matches("[0-9]+")) {
                            int num = Integer.parseInt(substring);
                            if (num ==0) throw new RuntimeException("Invalid input: notelength can't be 0");
                            T.num = num;
                            T.den = 1;
                            break;}
                    }
                } else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?")) {                    
                    T.num = 1;
                    T.den = 1;
                } else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?([0-9]+/[0-9]+)")) {
                    for (int begin = 0; begin < str.length(); begin++) {
                        String substring = str.substring(begin);
                        if (substring.matches("([0-9]+/[0-9]+)")) {
                            int num = Integer.parseInt(substring.substring(0,substring.indexOf("/")));
                            int denom = Integer.parseInt(substring.substring(substring.indexOf("/") + 1));
                            if (num ==0||denom==0) throw new RuntimeException("Invalid input: num or denom of notelength can't be 0");
                            long gcd = gcd((long) num, (long) denom);
                            num = num / (int) gcd;
                            denom = denom / (int) gcd;
                            T.num = num;
                            T.den = denom;
                        }
                    }
                }
            }
        }
        token = output;
        Tupnotelen(output);
    }

    /**
     * update and store the note-length in pitch and rest which are in tuplets and in chords
     * @param output: ArrayList of tokens for the music body
     * @throws RuntimeException when there are other elements other than pitch, rest, or chords in tuplets
     */
    public void Tupnotelen(ArrayList<Token> output) {
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Tuplets) {
                int tup = Integer.parseInt(output.get(i).string.substring(1));
                int k = 0;
                boolean chord = false;
                for (int start = i + 1; start < output.size(); start++) {
                    if (k < tup) {
                        if (output.get(start).type == Type.ChordsBegin) chord = true;                        
                        else if (output.get(start).type == Type.ChordsEnd) {
                            chord = false;
                            k++;
                        }
                        if (chord) {
                            if (output.get(start).type == Type.Pitch || output.get(start).type == Type.Rest) {                                
                                output.get(start).num *= Numfactor(tup);
                                output.get(start).den *= tup;
                            }
                        } else {
                            if (output.get(start).type == Type.Pitch || output.get(start).type == Type.Rest) {                                
                                output.get(start).num *= Numfactor(tup);
                                output.get(start).den *= tup;
                                k++;
                            } 
                            else if (output.get(start).type != Type.ChordsEnd) {
                                throw new RuntimeException("other types other than Pitch, Rest, or 'chord' in tuplets");
                            }
                        }
                    } else if (k == tup) break;
                }
            }
        }
        LCM(output);
        token = output;
    }

    /**
     * Store all the denominator of the notelengths for pitches and rests into a list
     * @param output: ArrayList of tokens for the music body
     */
    public void LCM(ArrayList<Token> output) {
        ArrayList<Integer> Denom = new ArrayList<Integer>();
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Pitch||output.get(i).type == Type.Rest) {                
                    Denom.add(output.get(i).den);
            }            
        }
        Ticker(output, Denom);
    }

    /**
     * Update note-length for pitches and rests to be TicksperQuaternote 
     * @param output: ArrayList of tokens for the music body
     */
    public void Ticker(ArrayList<Token> output, ArrayList<Integer> Denom) {
        int Tick = lcmlist(Denom);
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Pitch||output.get(i).type == Type.Rest) {
                output.get(i).noteLength = output.get(i).num * Tick/output.get(i).den;
            }
        }
        token = output;
        this.Tick = Tick;
        WCDelete(token);
    }
   
    /**
     * Delete all the whitespace type and comment type in token
     * @param output: ArrayList of tokens for the music body
     */
    public void WCDelete(ArrayList<Token> output) {
        for (int i= 0; i<output.size(); i++){
            if (output.get(i).type==Type.Whitespace ||output.get(i).type==Type.Comment){
                output.remove(output.get(i));
            }
        }
        token = output;
        MusicBody(token, voicecounter);
    }
    /**
     * Separate the music body tokens into arraylists each representing a different voice.
     * @param output: ArrayList of tokens for the music body
     * @param voicecounter: ArrayList of tokens of different voice type.
     * @throws RuntimeException when the ending of any voice isn't marked with |, |], :|, or || 
     */
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
                    if (output.get(i).type == Type.V) {
                        for (int voice = 0; voice<voicecounter.size();voice++){
                            if (voicecounter.get(voice).string.equals(output.get(i).string)){
                                counter = voice;
                                break;
                            }
                        }                     
                    }
                    else{
                    if (counter == a) {
                        VoiceArray.add(output.get(i));
                        }
                    }
                }
                String str = VoiceArray.get(VoiceArray.size()-1).string;
                if (str.matches("\\||\\|\\]|\\|\\||:\\|") == false) {
                    throw new RuntimeException(
                            "The ending of the voice isn't marked with |, |], :|, or ||");
                }
                Body.add(VoiceArray);
            }
        }
        this.MusicBody = Body;
        this.size= Body.size();
    }

    /**
     * Convert a letter to an integer.
     * @param str: a letter within the range of A-G or Z in string format
     */
    public int stringToNumber(String str) {
        return stringNumMap.get(str.charAt(0));
    }

    /**
     * peek at the first token in the token list
     * @return the current first token in the token list
     */
    public Token peek() {
        if (parserPeekIndex >= token.size()) return null;
        return token.get(parserPeekIndex);
    }

    /**
     * get the token next to the current index
     * @return the next token to the current index
     * @throws Exception
     */
    public Token next() throws Exception {
        if (parserPeekIndex >= token.size()) throw new Exception("Internal parser error");
        return token.get(parserPeekIndex++);
    }
    
    /**
     * get the correct factor for the numerator of notes in tuplets 
     * @param tup
     * @return
     */
    private static int Numfactor(int tup){
        if (tup==2) return 3;
        if (tup==3||tup==4) return tup-1;
        else return 0;
    }
    /**
     * Calculate the least common multiple of 2 integers
     * @param a: an integer
     * @param b: an integer
     * @return the least common multiple of 2 integers
     */
    private static int lcm(int a, int b) {
        long A = a;
        long B = b;
        return (int) (A * (B / gcd(A, B)));
    }

    /**
     * Calculate the least common multiple of a list of integers
     * @param input: a list of integers
     * @return the least common multiple of this list of integers
     */
    private static int lcmlist(List<Integer> input) {
        int current = input.get(0);
        for (int i = 1; i < input.size(); i++)
            current = lcm(current, input.get(i));
        return (int) current;
    }

    /**
     * Calculate the greatest common divisor of 2 longs
     * @param a: a number in long format
     * @param b: a number in long format
     * @return the greatest common divisor of 2 longs
     */
    private static long gcd(long a, long b) {
        while (b > 0) {
            long exchange = b;
            b = a % b;
            a = exchange;
        }
        return a;
    }
}