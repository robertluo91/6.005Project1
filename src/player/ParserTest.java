package player;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import player.Token.Type;

public class ParserTest {    
    @Test
    public void TokeninTreeTest1() {
        Lexer lexer = new Lexer("sample_abc\\fur_elise.abc");
        Parser parser = new Parser(lexer);
        System.out.println(parser.size);
        //ArrayList<AST> Voice = parser.SequenceofVoiceForest.get(0);
        //AST Tree = Voice.get(0);
        //ArrayList<Token> tokens = Tree.toArrayList();        
        //assertEquals(tokens.get(0), new Token(Type.Pitch, "E",0,4,0, 0,0,0,0));
    }
}
