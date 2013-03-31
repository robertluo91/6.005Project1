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
 * A lexer takes a string and splits it into tokens that are meaningful to a
 * parser. For detailed description of token types, see Token.java
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

    //constructor takes in the name of the file, and BufferedReader reads the
    //content in the abc file, and store it into br? 
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
             //System.out.println(checkerline);
             processNextLine();
         } catch (IOException e) { 
             e.printStackTrace(); 
             throw new RuntimeException(e.getMessage()); 
             
         }
         
     } 
    //creates the header token arraylist and music body in string
    public void processNextLine() throws IOException { 
        
        String str; 
        String str1;
        ArrayList<Token> headerinfo = new ArrayList<Token>();
        str = br.readLine();
        if (str.startsWith("X:")){
            //System.out.println(str);
            headerinfo.add(new Token(Type.X, str, 0, 0, 0, 0,0,0,0));
            headernum++;
            str = br.readLine();
            checkerline -=1;
        }else{
            throw new IOException("the 1st field of header isn't X");
        }
        if (str.startsWith("T:")){
            //System.out.println(str);
            headerinfo.add(new Token(Type.T, str, 0, 0, 0, 0,0,0,0));
            headernum++;
            str = br.readLine();
            checkerline -=1;
        }else{
            throw new IOException("the 2nd field of header isn't T");
        }
        boolean headercheck = true;
        while (headercheck==true){
            if (str.startsWith("X:")||str.startsWith("T:")){
                throw new IOException("repeated X or T type");
        }
            else if (str.startsWith("C:")||str.startsWith("L:")||str.startsWith("Q:")||
                    str.startsWith("M:")||str.startsWith("V:")){
                for (int i =0; i<headerinfo.size(); i++){
                    if (headerinfo.get(i).string == str){
                        throw new IOException("repeated C,L,Q,M,Vi types");}}  
                for (Type t : Type.values()) {
                    Token testToken = new Token(t, "", 0, 0, 0, 0,0,0,0);                    
                    if (testToken.pattern.matcher(str).matches()) {
                        
                        headerinfo.add(new Token(t, str, 0, 0, 0, 0,0,0,0)); 
                        //System.out.println(str);
                        headernum++;
                        str = br.readLine();
                        checkerline -=1;}
                    }
            }
            else if (str.startsWith("K:")){
                headerinfo.add(new Token(Type.K, str, 0, 0, 0, 0,0,0,0));
                //System.out.println(str);
                headernum++;
                
                headercheck = false;
                checkerline -=1;
                //System.out.println(checkerline);
                }
            else{
                throw new IOException("K is not that last field of the header"); 
            }
        }
        bodystring = new ArrayList<String>();
        while (checkerline >0){         
            str1 = br.readLine();
            //
            //System.out.println(str1);
            
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
     * Creates the lexer over the passed string.
     * 
     * @param string
     *            : The string to tokenize.
     */
    public void BodyTokenize(ArrayList<String> bodystring){

        
        ArrayList<Token> output = new ArrayList<Token>();
        // create an arraylist "output" to put all the tokens generated inside
        for (int a=0; a<bodystring.size(); a++){
            String string= bodystring.get(a);
            int length = string.length();
            if (length ==0){
                throw new RuntimeException("Emptyline in music body");
            }
            //System.out.println(length);

        current = 0;
        //parserPeekIndex = 0;

        while (current <length) {
            boolean anyMatchSoFar = false;            
            for (int i = length; i >current; i--) {
                //find the longeset possible valid token
                String currentString = string.substring(current, i);                
                for (Type t : Type.values()) {
                    Token testToken = new Token(t, "", 0, 0, 0, 0, 0,0,0);                    
                    if (testToken.pattern.matcher(currentString).matches()) {
                        anyMatchSoFar = true;  
                        current = i;
                        Token T = new Token(t, currentString, 0, 0, 0, 0, 0,0,0);
                        output.add(T);
                        if (t==Type.V){
                            //System.out.println(currentString+" "+currentString.length());
                            boolean c = false;
                            //string is immutable type!!!!!
                            
                        for (int b =0; b<MusicHeader.size(); b++){
                            //System.out.println(MusicHeader.get(b).string +" "+ MusicHeader.get(b).string.length());
                            if (MusicHeader.get(b).string.equals(currentString)){                                   
                                c= true;
                                break;}
                        }
                        if (c==false){      
                                throw new RuntimeException("Voice didn't appear in the header");}}
                        }
                   }
                }
            if (!anyMatchSoFar) {
                //??not necessary? indicates a blank space in the beginning of the string:
                // skip to the next position
                current++;
                }
            }
        }
        
        /*System.out.println(output.get(0).string);
        System.out.println(output.get(1).string);
        System.out.println(output.get(2).string); 
        System.out.println(output.get(11).string);  
        System.out.println(output.get(output.size()-1).string);*/

        Chordcheck(output);

    }

    //check if the chords are valid, also stores the # of pitches/rests in chords into the 
    //chord parameter of the 1st pitch/rest after chordsbegin
    public void Chordcheck(ArrayList<Token> output) {
        int diff = 0; // check if output tokens have equal chordsbegin and
                        // chordsend
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.ChordsBegin)
                diff++;
            else if (output.get(i).type == Type.ChordsEnd)
                diff--;
            if (diff > 1)
                throw new RuntimeException("2 or more consecutive chordbegin");
            else if (diff < 0)
                throw new RuntimeException("chordend more than chordbegin");
        }
        if (diff != 0){
            throw new RuntimeException(
                    "invalid input pattern: should have equal number of chordbegin and chordend");}
        for (int i = 0; i < output.size(); i++) { // checks if output tokens
                                                    // have types other than pitch and rest within
                                                    // chords
            if (output.get(i).type == Type.ChordsBegin) {
                
                int k = 0;
                for (int a =i+1; a< output.size(); a++){
                    if (output.get(a).type == Type.Pitch
                            || output.get(a).type == Type.Rest) {
                        k +=1;
                    }
                    else if (output.get(i).type != Type.Pitch
                        && output.get(i).type != Type.Rest && output.get(a).type != Type.ChordsEnd){
                        throw new RuntimeException(
                                "there are types other than pitch and rest in chord");
                    }
                    else if (output.get(a).type == Type.ChordsEnd){
                        output.get(i+1).chord = k;
                        //System.out.println(i+1 + " k equals" + k+" " +output.get(i+1).chord);
                        break;
                    }
                }
            
            }
        }
        //token = output;
        NoteLength(output);
    }
    
    //get the Tempo and Key of the music from header and get all the voices into voicecounter
    public void KeyTempo(ArrayList<Token> Headers) {
        ArrayList<Token> voicecounter = new ArrayList<Token>();
        for (int i = 0; i < Headers.size(); i++) {
            int Tempochecker = 0;
            int nolenchecker = 0;
            int meterchecker = 0;
            String str = Headers.get(i).string;
            if (str.startsWith("V:")) {
                voicecounter.add(Headers.get(i));
            }
            else if (str.startsWith("K:")) {
                str = str.substring(2);
                if (str.startsWith(" ")) {
                    str = str.substring(1);
                }
                this.Key = str;
            }
            else if (str.startsWith("Q:")) {
                Tempochecker = 1;
                str = str.substring(2);
                if (str.startsWith(" ")) {
                    str = str.substring(1);
                }
                this.Tempo = Integer.parseInt(str);
            }
            else if (str.startsWith("L:")) {
                nolenchecker = 1;
                str = str.substring(2);
                if (str.startsWith(" ")) {
                    str = str.substring(1);
                }
                this.L = str;
            }
            else if (str.startsWith("M:")) {
                meterchecker = 1;
                str = str.substring(2);
                if (str.startsWith(" ")) {
                    str = str.substring(1);
                }
                this.M = str;
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

        }
        int num = Integer.parseInt(this.L.substring(0, this.L.indexOf("/")));
        int denom = Integer.parseInt(this.L.substring(this.L.indexOf("/") + 1));
        this.Tempo = this.Tempo/4*(denom/num);
        this.voicecounter = voicecounter; // voicecounter is not used later
        //System.out.println("# of voicecounter"+" "+voicecounter.size());
    }

    // NoteLength method updates all the note-lengths for pitch and rest tokens
    // also changes basenote, octave, accid fields for Rest, Pitch types
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
                    //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den);
                } else if (str.matches("z/")) {
                   
                    T.basenote = 7;
                    T.num = 1;
                    T.den = 2;
                    //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den);
                } else if (str.matches("z/[0-9]+")) {
                    
                    int denom = Integer
                            .parseInt(str.substring(2));
                    if (denom ==0) throw new RuntimeException("Invalid input: denominator of rest length can't be 0");
                    T.basenote = 7;
                    T.num = 1;
                    T.den = denom;
                    //System.out.println(output.get(i).string+" "+output.get(i).basenote+""+output.get(i).num+" "+output.get(i).den);
                }  else if (str.matches("z[0-9]+/")) {
                    
                    int nom = Integer.parseInt(str.substring(1,str.length()-1));
                    if (nom ==0) throw new RuntimeException("Invalid input: num of rest length can't be 0");
                    T.basenote = 7;
                    T.num = nom;
                    T.den = 2;
                    //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den);
                }   else if (str.matches("z[0-9]+")) {
                    
                    int num = Integer.parseInt(str.substring(1));
                    if (num ==0) throw new RuntimeException("Invalid input: rest length can't be 0");
                    T.basenote = 7;
                    T.num = num;
                    T.den = 1;
                    //System.out.println(output.get(i).string+" "+output.get(i).basenote+""+output.get(i).num+" "+output.get(i).den);
                }else if (str.matches("z[0-9]+/[0-9]+")) {
                    for (int begin = 0; begin < str.length(); begin++) {
                        String substring = str.substring(begin);
                        if (substring.matches("[0-9]+/[0-9]+")) {
                            int num = Integer.parseInt(substring.substring(0,substring.indexOf("/")));
                            int denom = Integer.parseInt(substring
                                    .substring(substring.indexOf("/") + 1));
                            if (num ==0 || denom==0) throw new RuntimeException("Invalid input: num or denom of rest length can't be 0");
                            long gcd = gcd((long) num, (long) denom);
                            num = num / (int) gcd;
                            denom = denom / (int) gcd;
                            str.replaceAll("d+/d+", Integer.toString(num) + "/"
                                    + Integer.toString(denom));
                            T.basenote = 7;
                            T.num = num;
                            T.den = denom;
                            //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den);
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
                    }else if (str.substring(begin, begin + 1).equals("'")) {
                        T.octave = output.get(i).octave + 1;
                    } else if (str.substring(begin, begin + 1).equals(",")) {
                        output.get(i).octave = output.get(i).octave - 1;
                    }
                    if (hatcount > 2) {
                        throw new RuntimeException(
                                "accidental too high for this pitch");
                    } else if (hatcount < -2) {
                        throw new RuntimeException(
                                "accidental too low for this pitch");
                    }
                }
                if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?/")) {
                    
                    T.num = 1;
                    T.den = 2;
                    //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den+" "+output.get(i).accid+" "+output.get(i).octave);
                } else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?/[0-9]+")) {
                    
                    int denom = Integer
                            .parseInt(str.substring(str.indexOf("/") + 1));
                    if (denom ==0) throw new RuntimeException("Invalid input: denominator of notelength can't be 0");
                    T.num = 1;
                    T.den = denom;
                    //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den+" "+output.get(i).accid+" "+output.get(i).octave);
                } else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?[0-9]+/")) {
                    for (int begin = 0; begin < str.length(); begin++) {

                        String substring = str.substring(begin, str.length()-1);
                        if (substring.matches("[0-9]+")) {
                            int num = Integer.parseInt(substring);
                            if (num ==0) throw new RuntimeException("Invalid input: num of notelength can't be 0");
                            T.num = num;
                            T.den = 2;
                            //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den+" "+output.get(i).accid+" "+output.get(i).octave);
                            break;}
                        }
                }else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?[0-9]+")) {
                    for (int begin = 0; begin < str.length(); begin++) {

                        String substring = str.substring(begin);
                        if (substring.matches("[0-9]+")) {
                            int num = Integer.parseInt(substring);
                            if (num ==0) throw new RuntimeException("Invalid input: notelength can't be 0");
                            T.num = num;
                            T.den = 1;
                            //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den+" "+output.get(i).accid+" "+output.get(i).octave);
                            break;}
                        }
                }else if (str.matches("(((\\^){1,2})|((\\_){1,2})|(\\=))?[A-Ga-g]((,+)|('+))?")) {
                    
                    T.num = 1;
                    T.den = 1;
                    //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den+" "+output.get(i).accid+" "+output.get(i).octave);
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
                            //System.out.println(output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den+" "+output.get(i).accid+" "+output.get(i).octave);
                        }
                    }
                }
            }
        }
        /**
        for (int i=0; i<output.size(); i++){
        	if (output.get(i).accid!=0){
        		Token Tok = output.get(i);
        		for (int a=i+1; a<output.size();a++){
        			if (output.get(a).basenote==Tok.basenote&&output.get(a).num==Tok.num&&
        					output.get(a).den==Tok.den&&output.get(a).basenote==Tok.basenote&& output.get(a).accid==0){
        				output.get(a).accid = Tok.accid;
        			}else if (output.get(a).basenote==Tok.basenote&&output.get(a).num==Tok.num &&
        					output.get(a).den==Tok.den&&output.get(a).basenote==Tok.basenote&& output.get(a).accid!=0){
        				Tok = output.get(a);
        			}else if (output.get(a).type ==Type.Barline){
        				i = a+1;
        				break;
        			}
        		}
        	}
        }
        */
        token = output;
        Tupnotelen(output);
      
    }

    // updates notelength for all pitches and rests under chords and tuplets,
    // return exception if there are types other than Pitch, Rest, or 'chord' in tuplets
    public void Tupnotelen(ArrayList<Token> output) {
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Tuplets) {
                int tup = Integer.parseInt(output.get(i).string.substring(1));
                //System.out.println("havetup"+" "+i+" "+output.get(i).string+" "+tup);
                int k = 0;
                boolean chord = false;
                for (int start = i + 1; start < output.size(); start++) {
                    if (k < tup) {
                        if (output.get(start).type == Type.ChordsBegin) {
                            chord = true;
                        }
                        else if (output.get(start).type == Type.ChordsEnd) {
                            chord = false;
                            k++;
                        }
                        if (chord) {
                            if (output.get(start).type == Type.Pitch || output.get(start).type == Type.Rest) {
                                
                                output.get(start).num = output.get(start).num* (tup - 1);
                                output.get(start).den = output.get(start).den* tup;
                                //System.out.println("tupletschord:position at"+" "+start+" "+output.get(start).string+" "+output.get(start).basenote+" "+output.get(start).num+" "+output.get(start).den+" "+output.get(start).accid+" "+output.get(start).octave);
                            }
                        } else {
                            if (output.get(start).type == Type.Pitch || output.get(start).type == Type.Rest) {
                                
                                output.get(start).num = output.get(start).num* (tup - 1);
                                output.get(start).den = output.get(start).den* tup;
                                //System.out.println("tuplets:position at"+" "+start+" "+output.get(start).string+" "+output.get(start).basenote+" "+output.get(start).num+" "+output.get(start).den+" "+output.get(start).accid+" "+output.get(start).octave);
                                k++;
                            } 
                            else if (output.get(start).type != Type.ChordsEnd) {
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

    // have an arraylist of all the denominators for the
    // note length for pitch, rest
    public void LCM(ArrayList<Token> output) {
        ArrayList<Integer> Denom = new ArrayList<Integer>();
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Pitch
                    || output.get(i).type == Type.Rest) {                
                    Denom.add(output.get(i).den);}            
        }
        Ticker(output, Denom);
        
    }

    // Ticker method changes all the note-length into the actual tick time.
    public void Ticker(ArrayList<Token> output, ArrayList<Integer> Denom) {
        int Tick = lcmlist(Denom);
        System.out.println("Tick="+Tick);
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).type == Type.Pitch
                    || output.get(i).type == Type.Rest) {
                output.get(i).noteLength = output.get(i).num * Tick/output.get(i).den;
                //System.out.println("Tickchange"+" "+output.get(i).string+" "+output.get(i).basenote+" "+output.get(i).num+" "+output.get(i).den+" "+output.get(i).noteLength);
            }
        }
        token = output;
        this.Tick = Tick;
        WCDelete(token);
    }
    
    //delete all the whitespace type and comment type in token
    public void WCDelete(ArrayList<Token> output) {
        for (int i= 0; i<output.size(); i++){
            if (output.get(i).type==Type.Whitespace ||output.get(i).type==Type.Comment){
                //System.out.println(output.get(i).string+" whitespace or comment");
                output.remove(output.get(i));
                            
            }
        }
        token = output;
        MusicBody(token, voicecounter);
        
        /**
        //delete the blow for block after test
        for (int i=0; i<output.size();i++){
        	System.out.println(output.get(i).string + " accid is "+output.get(i).accid+" octave is "+output.get(i).octave);
        }//delete above for block after test
        */
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
                } // 2nd error check for ending
                /*System.out.println("voice "+ a + " "+ VoiceArray.get(0).string);
                System.out.println("voice "+ a +" "+ VoiceArray.get(1).string);
                System.out.println("voice "+ a +" "+ VoiceArray.get(2).string);
                System.out.println("voice "+ a +" "+ VoiceArray.get(3).string);
                System.out.println("voice "+ a +" "+ VoiceArray.get(VoiceArray.size()-3).string);
                System.out.println("voice "+ a +" "+ VoiceArray.get(VoiceArray.size()-2).string);
                System.out.println("voice "+ a +" "+ VoiceArray.get(VoiceArray.size()-1).string);*/
                Body.add(VoiceArray);
            }
        }
        this.MusicBody = Body;
        this.size= Body.size();
        //System.out.println("# of arraylist in musicbody "+ MusicBody.size());

    }

    //converts a letter in string format into an int [0-7]
    public int stringToNumber(String str) {
        return stringNumMap.get(str.charAt(0));
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

    //the below method is not used
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
