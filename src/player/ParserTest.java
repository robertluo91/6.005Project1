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
        //we don't care about the num, den and chord number
        assertEquals(a.type,b.type);
        assertEquals(a.basenote,b.basenote);
        assertEquals(a.noteLength,b.noteLength);
        assertEquals(a.accid,b.accid);
        assertEquals(a.octave,b.octave);
        }
    @Test
    // check single notes in 
    public void TokeninTreeTest1() {
        Lexer lexer = new Lexer("sample_abc/fur_elise.abc");
        Parser parser = new Parser(lexer);
        int tick = parser.tpb;
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = Voice.get(0);
        ArrayList<Token> tokens = Tree.toArrayList();
        //since the original notelength of the first note is the default notelength, 
        //hence multiplying with tpb, it is tick = tpb 
        Token testtoken1 = new Token (Type.Pitch, "E",4,0,0,tick,0,1,0);
        Token testtoken2 = new Token (Type.Pitch, "D",3,0,0,tick,1,1,0);
        Token testtoken3 = new Token (Type.Pitch, "D",3,0,0,tick,0,1,0);
        assertTokenEquals(testtoken1, tokens.get(0));
        assertTokenEquals(testtoken2, tokens.get(1));
        assertTokenEquals(testtoken3, tokens.get(6));
    }
    @Test
    //check repetition with two endings
    //Since we add-on the repeated part and removed all the barline, we check if the repetition part is added at the right place.
    public void TokeninTreeTest2() {
        Lexer lexer = new Lexer("sample_abc/paddy.abc");
        Parser parser = new Parser(lexer);
        int tick = parser.tpb;
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = Voice.get(0);
        ArrayList<Token> tokens = Tree.toArrayList();
        //since the original notelength of the first note is the default notelength, 
        //hence multiplying with tpb, it is tick = tpb 
        Token testtoken1 = new Token (Type.Pitch, "D",3,0,0,tick,0,1,0);
        Token testtoken2 = new Token (Type.Pitch, "D",3,0,0,tick,0,1,0);
        assertTokenEquals(testtoken1, tokens.get(42));
        assertTokenEquals(testtoken2, tokens.get(90));
    }
    
    @Test
    //check if chords are separate to single notes with a field "chord" to record the number of notes in chord.
    public void TokeninTree3(){
        Lexer lexer = new Lexer("sample_abc/piece2.abc");
        Parser parser = new Parser(lexer);
        int tick = parser.tpb;
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = Voice.get(0);
        ArrayList<Token> tokens = Tree.toArrayList();
        //since the original notelength of the first note is the default notelength, 
        //hence multiplying with tpb, it is tick = tpb 
        Token testtoken1 = new Token (Type.Pitch, "F",5,0,0,tick/2,1,0,1);
        Token testtoken2 = new Token (Type.Pitch, "E",4,0,0,tick/2,0,1,1);
        assertTokenEquals(testtoken1, tokens.get(0));
        assertTokenEquals(testtoken2, tokens.get(1));  
    }
    
    @Test
    //check if rest can be parsed correctly
    public void TokeninTree4(){
        Lexer lexer = new Lexer("sample_abc/piece2.abc");
        Parser parser = new Parser(lexer);
        int tick = parser.tpb;
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = Voice.get(0);
        ArrayList<Token> tokens = Tree.toArrayList();
        //since the original notelength of the first note is the default notelength, 
        //hence multiplying with tpb, it is tick = tpb 
        Token testtoken1 = new Token (Type.Rest, "z",7,0,0,tick/2,0,0,0);
        Token testtoken2 = new Token (Type.Rest, "z",7,0,0,tick/2,0,0,0);
        assertTokenEquals(testtoken1, tokens.get(4));
        assertTokenEquals(testtoken2, tokens.get(7));  
    }
    
    public void assertEqualsTree(){
        
    }
    
    @Test
    // check if a file without repetition can correctly build a AST without children
    public void TokeninTree5(){
        Lexer lexer = new Lexer("sample_abc/piece1.abc");
        Parser parser = new Parser(lexer);
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = (NodeTree) Voice.get(0);
        //ArrayList<Token> t = Tree.root;
        assertEquals(Tree.leftChild,null);
        assertEquals(Tree.rightChild,null);
        }
    
    @Test
    // check if a file with a regular (single-ending) repetition can correctly build a AST without children
    // since the repeated part should be add-on to the root of AST.
    public void TokeninTree6(){
        Lexer lexer = new Lexer("our_test/repeattest.abc");
        Parser parser = new Parser(lexer);
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = (NodeTree) Voice.get(0);
        assertEquals(Tree.leftChild,null);
        assertEquals(Tree.rightChild,null);
        }
    
    //@Test
    // check if a file with a two-ending repetition can correctly build a AST with both left and right children
    public void TokeninTree7(){
        Lexer lexer = new Lexer("our_test/repeattest2.abc");
        Parser parser = new Parser(lexer);
        List<AST> Voice = parser.SequenceofVoiceForest.get(0);
        AST Tree = (NodeTree) Voice.get(0);
        assertEquals((Tree.leftChild == null),false);
        assertEquals((Tree.rightChild == null),false);
        }
    
   
    
}


