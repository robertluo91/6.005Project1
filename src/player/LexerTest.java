package player;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import player.Lexer;
import player.Token;
import player.Token.Type;

/**
 * Test some complicated stuff
 * @category no_didit
 */
public class LexerTest {    
	//define token equality: both type and string must match
    public static void assertTokenEquals(Token x, Token y) {    	
        assertEquals(x.type, y.type);
        assertEquals(x.string, y.string);
        assertEquals(x.basenote, y.basenote);
        assertEquals(x.noteLength, y.noteLength);
        assertEquals(x.octave, y.octave);
        assertEquals(x.accid, y.accid);
        assertEquals(x.chord, y.chord);
        assertEquals(x.num, y.num);
        assertEquals(x.den, y.den);        
    }
    
    @Test
    //Test number of lines for header, body, and total
    public void ReaderTest1() {        
        Lexer lexer = new Lexer("sample_abc/piece1.abc");
        assertEquals(lexer.totalnum, 7);
        assertEquals(lexer.headernum, 6);
        assertEquals(lexer.bodyline, 1);       
    }
   
    @Test
    //Test number of lines for header, body, and total, case for more than two voices and comments
    public void ReaderTest2() {        
        Lexer lexer = new Lexer("sample_abc/invention.abc");
        assertEquals(lexer.totalnum, 63);
        assertEquals(lexer.headernum, 9);
        assertEquals(lexer.bodyline, 54);
    }
   
    @Test
    //Test number of lines for header, body, and total, case for one default voice with no L input
    public void ReaderTest3() {        
        Lexer lexer = new Lexer("sample_abc/paddy.abc");
        assertEquals(lexer.totalnum, 12);
        assertEquals(lexer.headernum, 6);
        assertEquals(lexer.bodyline, 6);
    }
    
    @Test
    //Test number of lines for header, body, and total, case for one default voice with L input
    public void ReaderTest4() {        
        Lexer lexer = new Lexer("sample_abc/little_night_music.abc");
        assertEquals(lexer.totalnum, 24);
        assertEquals(lexer.headernum, 7);
        assertEquals(lexer.bodyline, 17);
    }
    
    @Test
    //Test info in the header with voices
    public void HeaderTest1() {        
        Lexer lexer = new Lexer("sample_abc/fur_elise.abc");
        assertEquals(lexer.MusicBody.size(),2);
        ArrayList<Token> resultTokens = lexer.MusicHeader; 

        Token[] expected = { new Token(Type.X, "X: 1",0,0,0, 0,0,0,0),   
                new Token(Type.T, "T:Bagatelle No.25 in A, WoO.59",0,0,0, 0,0,0,0), 
                new Token(Type.C, "C:Ludwig van Beethoven",0,0,0, 0,0,0,0),                
                new Token(Type.V, "V:1",0,0,0, 0,0,0,0), new Token(Type.V, "V:2",0,0,0, 0,0,0,0),
                new Token(Type.M, "M:3/8",0,0,0, 0,0,0,0),
                new Token(Type.L, "L:1/16",0,0,0, 0,0,0,0),new Token(Type.Q, "Q:240",0,0,0, 0,0,0,0),
                new Token(Type.K, "K:Am",0,0,0, 0,0,0,0)};   

        for (int i=0;i<resultTokens.size();i++){
        	assertTokenEquals(resultTokens.get(i), expected[i]);
        }
    }
       
    @Test
    //Test the final token ArrayList before dividing into voices in MusicBody 
    public void TokenTest1() {        
        Lexer lexer = new Lexer("sample_abc/piece2.abc");
        ArrayList<Token> resultTokens = lexer.token; 

        Token[] expected = { new Token(Type.ChordsBegin, "[", 0,0,0, 0,0,0,0),   
                new Token(Type.Pitch, "^F/",5,1,2, 3,1,0,2), new Token(Type.Pitch, "e/",4,1,2, 3,0,1,0),
                new Token(Type.ChordsEnd, "]",0,0,0, 0,0,0,0), new Token(Type.ChordsBegin, "[",0,0,0, 0,0,0,0),
                new Token(Type.Pitch, "F/",5,1,2, 3,0,0,2) };           
        for (int i=0;i<expected.length;i++){
        	assertTokenEquals(resultTokens.get(i), expected[i]);
        }
    }
    
    @Test
    //Test the token ArrayList backwards for music have more than 1 voice
    public void TokenTest2() {        
        Lexer lexer = new Lexer("sample_abc/prelude.abc");
        ArrayList<Token> resultTokens = lexer.token; 
        Token[] expected = { new Token(Type.Barline, "|]", 0,0,0, 0,0,0,0),   
                new Token(Type.Pitch, "C,,16",2,16,1, 16,0,-2,0), new Token(Type.Barline, "|",0,0,0, 0,0,0,0),
                new Token(Type.Pitch, "C,,16",2,16,1, 16,0,-2,0), new Token(Type.V, "V:3",0,0,0, 0,0,0,0),
                new Token(Type.Barline, "|]", 0,0,0, 0,0,0,0),new Token(Type.Pitch, "C,16",2,16,1, 16,0,-1,0),
                new Token(Type.Barline, "|",0,0,0, 0,0,0,0),new Token(Type.Pitch, "B,,15",1,15,1, 15,0,-2,0), 
                new Token(Type.Rest, "z",7,1,1, 1,0,0,0) };           
        for (int i=0;i<expected.length;i++){
        	assertTokenEquals(resultTokens.get(resultTokens.size()-1-i), expected[i]);
        }
    }
    
    @Test
    //Test the token ArrayList forward for music have more than 1 voice
      public void TokenTest3() {
          
          Lexer lexer = new Lexer("sample_abc/prelude.abc");
          ArrayList<Token> resultTokens = lexer.token; 
          Token[] expected = { new Token(Type.V, "V:1", 0,0,0, 0,0,0,0),   
                  new Token(Type.Rest, "z2",7,2,1, 2,0,0,0), new Token(Type.Pitch, "G",6,1,1, 1,0,0,0),
                  new Token(Type.Pitch, "c",2,1,1, 1,0,1,0), new Token(Type.Pitch, "e",4,1,1, 1,0,1,0),
                  new Token(Type.Pitch, "G",6,1,1, 1,0,0,0) };           
          for (int i=0;i<expected.length;i++){
          	assertTokenEquals(resultTokens.get(i), expected[i]);
          }
      }
    
    @Test
    //Test the voicecounter for music more than 1 voice
      public void voicecounter1() {          
          Lexer lexer = new Lexer("sample_abc/prelude.abc");
          assertEquals(lexer.voicecounter.size(), 3);         
      }
   
    @Test
    //Test the voicecounter for music with more than 1 voice
      public void voicecounter2() {          
          Lexer lexer = new Lexer("sample_abc/invention.abc");
          assertEquals(lexer.voicecounter.size(), 2);         
      }
    
    @Test
    //Test the voicecounter for music with 1 voice
      public void voicecounter3() {          
          Lexer lexer = new Lexer("sample_abc/little_night_music.abc");
          assertEquals(lexer.voicecounter.size(), 0);         
      }
    
    @Test
    //Test the size of MusicBody ArrayList for music with 1 voice
      public void Musicbody1() {          
          Lexer lexer = new Lexer("sample_abc/little_night_music.abc");
          assertEquals(lexer.size, 1);         
      }
    
    @Test
    //Test the size of MusicBody ArrayList for music with more than 1 voice
      public void Musicbody2() {          
          Lexer lexer = new Lexer("sample_abc/invention.abc");
          assertEquals(lexer.size, 2);         
      }
    
    @Test
    //Test the token voice arraylists in musicbody forward for music with more than 1 voices
      public void MusicBody3() {          
          Lexer lexer = new Lexer("sample_abc/prelude.abc");
          ArrayList<ArrayList<Token>> result = lexer.MusicBody; 
          Token[] expected1 = {   
                  new Token(Type.Rest, "z2",7,2,1, 2,0,0,0), new Token(Type.Pitch, "G",6,1,1, 1,0,0,0),
                  new Token(Type.Pitch, "c",2,1,1, 1,0,1,0), new Token(Type.Pitch, "e",4,1,1, 1,0,1,0),
                  new Token(Type.Pitch, "G",6,1,1, 1,0,0,0) };
          Token[] expected2 = {   
                  new Token(Type.Rest, "z",7,1,1, 1,0,0,0), new Token(Type.Pitch, "E7",4,7,1, 7,0,0,0),
                  new Token(Type.Rest, "z",7,1,1, 1,0,0,0), new Token(Type.Pitch, "E7",4,7,1, 7,0,0,0),
                  new Token(Type.Barline, "|",0,0,0, 0,0,0,0) };
          Token[] expected3 = { 
                  new Token(Type.Pitch, "C8",2,8,1, 8,0,0,0), new Token(Type.Pitch, "C8",2,8,1, 8,0,0,0),
                  new Token(Type.Barline, "|",0,0,0, 0,0,0,0), new Token(Type.Pitch, "C8",2,8,1, 8,0,0,0),
                  new Token(Type.Pitch, "C8",2,8,1, 8,0,0,0) };
          Token[][] expected ={expected1,expected2,expected3};
          for (int i=0;i<result.size();i++){
        	  for (int a=0; a< expected[i].length; a++){
        		  assertTokenEquals(result.get(i).get(a), expected[i][a]);}
          	}
      }
    
    @Test
    //Test the token voice arraylists in musicbody backwards for music with more than 1 voices
      public void MusicBody4() {          
          Lexer lexer = new Lexer("sample_abc/fur_elise.abc");
          ArrayList<ArrayList<Token>> result = lexer.MusicBody; 
          Token[] expected1 = {  
                  new Token(Type.Barline, "|]",0,0,0, 0,0,0,0), new Token(Type.ChordsEnd, "]",0,0,0, 0,0,0,0),
                  new Token(Type.Pitch, "A4",0,4,1, 24,0,0,0), new Token(Type.Pitch,  "C4",2,4,1, 24,0,0,2),
                  new Token(Type.ChordsBegin, "[",0,0,0, 0,0,0,0),new Token(Type.Barline, "|",0,0,0, 0,0,0,0) };
          Token[] expected2 = {   
                  new Token(Type.Barline, "|]",0,0,0, 0,0,0,0), new Token(Type.ChordsEnd, "]",0,0,0, 0,0,0,0),
                  new Token(Type.Pitch, "A,,4",0,4,1, 24,0,-2,0), new Token(Type.Pitch, "A,,,4",0,4,1, 24,0,-3,2),
                  new Token(Type.ChordsBegin, "[",0,0,0, 0,0,0,0),new Token(Type.Barline, "|",0,0,0, 0,0,0,0) };
          Token[][] expected ={expected1,expected2};
          for (int i=0;i<result.size();i++){
        	  for (int a=0; a< expected[i].length; a++){
        		  assertTokenEquals(result.get(i).get(result.get(i).size()-1-a), expected[i][a]);}
          	}
      }
   
    @Test
    //Test if the lexer is able to default Q=100, L=1/8, M=4/4 when these fields are absent 
    public void Defaultest(){        
        Lexer lexer = new Lexer("our_test/lilli2.abc");   
        assertEquals(lexer.L, "1/8");
        assertEquals(lexer.Tempo, 100);
        assertEquals(lexer.M, "4/4");
    }
    @Test
    //Test if the lexer is able to default Q=100, L=1/8, M=4/4 when these fields are absent 
    public void Defaultest2(){        
        Lexer lexer = new Lexer("our_test/amazing_grace2.abc");   
        assertEquals(lexer.L, "1/8");
        assertEquals(lexer.Tempo, 100);
        assertEquals(lexer.M, "4/4");
    }
    
    @Test(expected = RuntimeException.class)
    //the 2nd field of header isn't T
    public void Wrongheader1(){        
        new Lexer("our_test/wrongheader1.abc");   
    }
    @Test(expected = RuntimeException.class)
    //the 1st field of header isn't X
    public void Wrongheader2(){        
        new Lexer("our_test/wrongheader2.abc");   
    }
    @Test(expected = RuntimeException.class)
    //K is not that last field of the header
    public void Wrongheader3(){        
        new Lexer("our_test/wrongheader3.abc");   
    }
    @Test(expected = RuntimeException.class)
    //"headerinfo appeared in the body"
    public void Wrongheader4(){        
        new Lexer("our_test/wrongheader4.abc");   
    }
    @Test(expected = RuntimeException.class)
    //repeated header info
    public void Wrongheader5(){        
        new Lexer("our_test/wrongheader5.abc");   
    }
    @Test(expected = RuntimeException.class)
    //"Emptyline in music body"
    public void Wrongbody1(){        
        new Lexer("our_test/wrongbody1.abc");   
    }
    @Test(expected = RuntimeException.class)
    //"Voice in the music body didn't appear in the header"
    public void Wrongbody2(){        
        new Lexer("our_test/wrongbody2.abc");   
    }
    @Test(expected = RuntimeException.class)
    //"2 or more consecutive chordbegin"
    public void Wrongbody3(){        
        new Lexer("our_test/wrongbody3.abc");   
    }
    @Test(expected = RuntimeException.class)
    //different numbers of chordbegin and chordend
    public void Wrongbody4(){        
        new Lexer("our_test/wrongbody4.abc");   
    }
    @Test(expected = RuntimeException.class)
    //"there are types other than pitch and rest in chord"
    public void Wrongbody5(){        
        new Lexer("our_test/wrongbody5.abc");   
    }
    @Test(expected = RuntimeException.class)
    //"other types other than Pitch, Rest, or 'chord' in tuplets"
    public void Wrongbody6(){        
        new Lexer("our_test/wrongtuplet.abc");   
    }
    @Test(expected = RuntimeException.class)
    //string can't match any tokens (wrong notes)
    public void Wrongbody7(){        
        new Lexer("our_test/wrongnote.abc");   
    }
    @Test(expected = RuntimeException.class)
    //"The ending of the voice isn't marked with |, |], :|, or ||"
    public void Wrongbody8(){        
        new Lexer("our_test/wrongbody6.abc");   
    }

}
