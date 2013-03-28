package player;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    public void LineTest1() {
        
        Lexer lexer = new Lexer("piece1copy.abc");
        assertEquals(lexer.totalnum, 7);

    }

    
    @Test
    //Test number of lines
    public void LineTest2() {
        
        Lexer lexer = new Lexer("scale copy.abc");
        assertEquals(lexer.totalnum, 8);

    }
   /** 
    @Test
    //Test number of lines
    public void LineTest3() {
        
        Lexer lexer = new Lexer("fur_elise copy.abc");
        assertEquals(lexer.linenum, 118);

    }
    */
    
    @Test
    //Test Tokenize method
    public void TokenizeTest1() {
        
        Lexer lexer = new Lexer("piece2copy.abc");
        ArrayList<Token> resultTokens = lexer.check1; 
        Token[] expected = { new Token(Type.X, "X: 1",0,0.0,0, 0,0),   
                new Token(Type.T, "T: Piece No.1",0,0.0,0, 0,0), new Token(Type.M, "M:4/4",0,0.0,0, 0,0),
                new Token(Type.L, "L:1/4",0,0.0,0, 0,0), new Token(Type.Q, "Q:140",0,0.0,0, 0,0),
                new Token(Type.K, "K:C",0,0.0,0, 0,0) };   
        for (int i=0;i<expected.length;i++){
        	assertTokenEquals(resultTokens.get(i), expected[i]);
        }

    }
    
/**
    @Test
    //Test Header
    public void HeaderTest1() {
        
        Lexer lexer = new Lexer("piece1copy.abc");
        ArrayList<Token> resultTokens = lexer.MusicHeader; 
        Token[] expected = { new Token(Type.X, "X:1",0,0.0,0, 0,0),   
                new Token(Type.T, "T: Piece No.1",0,0.0,0, 0,0), new Token(Type.M, "M:4/4",0,0.0,0, 0,0),
                new Token(Type.L, "L:1/4",0,0.0,0, 0,0), new Token(Type.Q, "Q:140",0,0.0,0, 0,0),
                new Token(Type.K, "K:C",0,0.0,0, 0,0) };   
        for (int i=0;i<expected.length;i++){
        	assertTokenEquals(resultTokens.get(i), expected[i]);
        }

    }
    */
}
    