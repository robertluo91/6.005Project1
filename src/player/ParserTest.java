package player;

import static org.junit.Assert.*;

import org.junit.Test;

import player.Token.Type;

public class ParserTest {    
    @Test
    public void TokeninTreeTest1() {
        Lexer lexer = new Lexer("piece1.abc");
        Parser parser = new Parser(lexer);
        assertEquals(parser.SequenceofVoiceForest.get(0).getNode.get(0), new Token(Type.Pitch, "C",0,4,0, 0,0));
    }
}
