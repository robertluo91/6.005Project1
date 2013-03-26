package player;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import player.Lexer;
import player.Token;

public class LexerTest {
    /**
     * Helper method for tests on good inputs
     */
    public void testHelper(Lexer l, List<Token> expected) {
        List<Token> result = new ArrayList<Token>();
        while (l.peek() != null) {
            try {
              result.add(l.next());
          } catch (Exception e) {
              e.printStackTrace();
          }
          }
          assertEquals(expected.size(), result.size());
          for (int i = 0; i < expected.size(); i++) {
              assertEquals(expected.get(i),result.get(i));
          }
    }
    
    @Test
    public void HeaderTest(){
        String Expression = "M:4/4";
        
    }
    
}
