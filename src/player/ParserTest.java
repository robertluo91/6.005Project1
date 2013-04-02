package player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import player.Token.Type;

public class ParserTest {    
    @Test
    public void TokeninTreeTest1() {
        Lexer lexer = new Lexer("fur_elise copy.abc");
        Parser parser = new Parser(lexer);
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = Voice.get(0);
        ArrayList<Token> tokens = Tree.toArrayList();        
        assertEquals(new Token (Type.Pitch, "e",4,0,0, 0,0,0,0), tokens.get(0));
    }
}
