package player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import player.Token.Type;

/**
 * Test some complicated stuff
 * @category no_didit
 */
public class ParserTest {
    public void assertTokenEquals(Token a, Token b){
        assertEquals(a.type,b.type);
        assertEquals(a.basenote,b.basenote);
        assertEquals(a.noteLength,b.noteLength);
        assertEquals(a.accid,b.accid);
        assertEquals(a.octave,b.octave);
        //we don't care about the num, den and chord number
    }
    @Test
    public void TokeninTreeTest1() {
        Lexer lexer = new Lexer("sample_abc/fur_elise.abc");
        Parser parser = new Parser(lexer);
        int tick = parser.tpb;
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = Voice.get(0);
        ArrayList<Token> tokens = Tree.toArrayList();
        //since the original notelength of the first note is the default notelength, 
        //hence multiplying with tpb, it is tick = tpb 
        Token testtoken = new Token (Type.Pitch, "E",4,0,0,tick,0,1,0);
        assertTokenEquals(testtoken, tokens.get(0));
    }
}
