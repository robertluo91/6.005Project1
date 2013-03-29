package player;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import player.Lexer;
import player.Token;
import player.Token.Type;

public class LexerTest {
    
	// step0: define token equality: both type and string must match
    public static void assertTokenEquals(Token x, Token y) {
    	
        assertEquals(x.type, y.type);
        assertEquals(x.string, y.string);
        assertEquals(x.basenote, y.basenote);
        assertEquals(x.noteLength, y.noteLength, x.noteLength);
        assertEquals(x.octave, y.octave);
        assertEquals(x.accid, y.accid);
        assertEquals(x.chord, y.chord);
        

    }
    
    @Test
    //Test number of lines
    public void ReaderTest1() {
        
        Lexer lexer = new Lexer("piece1copy.abc");
        assertEquals(lexer.totalnum, 7);
        assertEquals(lexer.headernum, 6);
        assertEquals(lexer.bodyline, 1);
       
    }

    
    @Test
    //Test number of lines
    public void ReaderTest2() {
        
        Lexer lexer = new Lexer("scale copy.abc");
        assertEquals(lexer.totalnum, 8);
        assertEquals(lexer.headernum, 7);
        assertEquals(lexer.bodyline, 1);

    }
    @Test
    //Test number of lines
    public void ReaderTest3() {
        
        Lexer lexer = new Lexer("fur_elise copy.abc");
        assertEquals(lexer.totalnum, 118);
        assertEquals(lexer.headernum, 9);
        assertEquals(lexer.bodyline, 109);
    }
    
    @Test
    //Test info in the header
    public void HeaderTest1() {
        
        Lexer lexer = new Lexer("fur_elise copy.abc");
        assertEquals(lexer.MusicBody.size(),2);
        ArrayList<Token> resultTokens = lexer.MusicHeader; 
        Token[] expected = { new Token(Type.X, "X: 1",0,0,0, 0,0,0,0),   
                new Token(Type.T, "T:Bagatelle No.25 in A, WoO.59",0,0,0, 0,0,0,0), new Token(Type.C, "C:Ludwig van Beethoven",0,0,0, 0,0,0,0),                
                new Token(Type.V, "V:1",0,0,0, 0,0,0,0), new Token(Type.V, "V:2",0,0,0, 0,0,0,0),new Token(Type.M, "M:3/8",0,0,0, 0,0,0,0),new Token(Type.L, "L:1/16",0,0,0, 0,0,0,0),new Token(Type.Q, "Q:240",0,0,0, 0,0,0,0),
                new Token(Type.K, "K:Am",0,0,0, 0,0,0,0)};   
        for (int i=0;i<resultTokens.size();i++){
        	assertTokenEquals(resultTokens.get(i), expected[i]);
        }

    }
    
    
    @Test
    //Test Tokenize method
    public void TokenizeTest1() {
        
        Lexer lexer = new Lexer("piece2copy.abc");
        ArrayList<Token> resultTokens = lexer.check1; 
        Token[] expected = { new Token(Type.ChordsBegin, "[",0,0,0, 0,0,0,0),   
                new Token(Type.Pitch, "^F/2",0,0,0, 0,0,0,0), new Token(Type.Pitch, "e/2",0,0,0, 0,0,0,0),
                new Token(Type.ChordsEnd, "]",0,0,0, 0,0,0,0), new Token(Type.ChordsBegin, "[",0,0,0, 0,0,0,0),
                new Token(Type.Pitch, "F/2",0,0,0, 0,0,0,0) };   
        for (int i=0;i<expected.length;i++){
        	assertTokenEquals(resultTokens.get(i), expected[i]);
        }

    }
    
    @Test(expected = IOException.class)
    //Test info in the header
    public void HeaderTest3() {        
        Lexer lexer = new Lexer("testpiece.abc");

    }
    

}
    