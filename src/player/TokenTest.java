package player;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import player.Token.Type;


public class TokenTest {
    @Test
    //M
    public void testMeter(){
        Token t = new Token(Type.M, "4/4");
        assertEquals(t.type,Type.M);
        assertEquals(t.string, "4/4");
    }
    
    @Test
    //X
    public void testIndex(){
        Token t = new Token(Type.X, "1");
        assertEquals(t.type,Type.X);
        assertEquals(t.string,"1");  
    }
    
    @Test
    //K
    public void testKeyType(){
        Token t = new Token(Type.K, "1");
        assertEquals(t.type,Type.K);
        assertEquals(t.string,"1");  
    }
    
    @Test
    //C
    public void testComposer(){
        Token t = new Token(Type.C, "W. Mozart");
        assertEquals(t.type,Type.C);
        assertEquals(t.string,"W. Mozart");  
    }
    @Test
    //T
    public void testTitle(){
        Token t = new Token(Type.X, "1");
        assertEquals(t.type,Type.X);
        assertEquals(t.string,"1");  
    }
    @Test
    //L
    public void tesNoteLength(){
        Token t = new Token(Type.L, "1/8");
        assertEquals(t.type,Type.L);
        assertEquals(t.string,"1/8");  
    }
    @Test
    //Q
    public void testTempo(){
        Token t = new Token(Type.Q, "100");
        assertEquals(t.type,Type.Q);
        assertEquals(t.string,"100");  
    }
    
    @Test
    //V
    public void testVoice(){
        Token t = new Token(Type.V, "3");
        assertEquals(t.type,Type.V);
        assertEquals(t.string,"3");  
    }
    
    @Test
    //Rest
    public void testRest(){
        Token t = new Token(Type.Rest, "z");
        assertEquals(t.type,Type.Rest);
        assertEquals(t.string,"z");  
    }
    
    @Test
    //Pitch
    public void testPitch(){
        Token t = new Token(Type.Pitch, "^D'1/4");
        assertEquals(t.type,Type.Pitch);
        assertEquals(t.string,"^D'1/4");  
    }
    
    @Test
    //Tuplets
    public void testTuplets(){
        Token t = new Token(Type.Tuplets, "(3");
        assertEquals(t.type,Type.Tuplets);
        assertEquals(t.string,"(3");  
    }
    
    @Test
    //ChordsBegin
    public void testChordsBegin(){
        Token t = new Token(Type.ChordsBegin, "[");
        assertEquals(t.type,Type.ChordsBegin);
        assertEquals(t.string,"[");  
    }
    
    @Test
    //ChordsEnd
    public void testChordsEnd(){
        Token t = new Token(Type.ChordsEnd, "]");
        assertEquals(t.type,Type.ChordsEnd);
        assertEquals(t.string,"]");  
    }
    
    @Test
    //Barline
    public void testBarline(){
        Token t = new Token(Type.Barline, "|");
        assertEquals(t.type,Type.Barline);
        assertEquals(t.string,"|");  
    }
    
    @Test
    //RepeatBegin
    public void testRepeatBegin(){
        Token t = new Token(Type.RepeatBegin, "|:");
        assertEquals(t.type,Type.RepeatBegin);
        assertEquals(t.string,"|:");  
    }
    @Test
    //RepeatEnd
    public void testRepeatEnd(){
        Token t = new Token(Type.RepeatEnd, ":|");
        assertEquals(t.type,Type.RepeatEnd);
        assertEquals(t.string,":|");  
    }
    
    @Test
    //Nrepeat
    public void testNrepeat(){
        Token t = new Token(Type.Nrepeat, "[1");
        assertEquals(t.type,Type.Nrepeat);
        assertEquals(t.string,"[1");  
    }
}
