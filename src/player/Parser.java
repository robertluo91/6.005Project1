package player;

import sound.Pitch; 
import player.Token.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Parser {
    final KeySignature defaultKeySignature; 
    final int tempo;     
    final int tpb;
    final String key;
    
    public Parser(Lexer lexer) {                
        ArrayList<Token> Headers = lexer.MusicHeader;
        ArrayList<ArrayList<Token>> Body = lexer.MusicBody;
        this.key = Headers.key;
        this.tempo = Headers.tempo;
        this.tpb = Headers.tick; 
        defaultKeySignature = new KeySignature(key);
        
        for (int i = 0;i<Body.size();i++){
            Body.get(i).
        }
        
        final AST<ArrayList<Token>> tree;
        for (ArrayList<Token> a:Body){
            List<AST<ArrayList<Token>>> VoiceTrees = new ArrayList<AST<ArrayList<Token>>>;
            int repeat_start_index = 0;
            int repeat_end_index = a.size();
            for(int i=0;i<a.size(); i++){
                if (a.get(i).equals(":|") && a.get(i+1).type == Type.Nrepeat){
                    //generate a new tree
                    for (int j=0; int j < i; i++){
                        
                    }
                    VoiceTrees.add(currenttree)
                }
            }
        }
    }
   

}
