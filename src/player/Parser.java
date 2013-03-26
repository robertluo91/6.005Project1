package player;

import sound.Pitch; 
import player.Token.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Parser {
    
    public Parser(Lexer lexer) {                
        ArrayList<Token> Headers = lexer.MusicHeader;
        ArrayList<ArrayList<Token>> Body = lexer.MusicBody;
        String key = lexer.Key;
        int tempo = lexer.Tempo;
        int tpb = lexer.Tick; 
        KeySignature defaultKeySignature = new KeySignature(key);
        
        //hashmap keychange Body;
        //since repetition cannot be made across major section, if an already started repeating stream meets "||", 
        //then it halts, and becomes a complete repeated piece
        //Weixin: we do not deal with nested repetition at this stage, may modify later
        
        //final AST<ArrayList<Token>> tree;
        List<ArrayList<AST<ArrayList<Token>>>> SequenceofVoiceForest= new ArrayList<ArrayList<AST<ArrayList<Token>>>>();
        for (ArrayList<Token> a:Body){            
            List<Integer> BeginIndOfSect = new ArrayList<Integer>();
            BeginIndOfSect.add(0);
            List<AST<ArrayList<Token>>> VoiceTrees = new ArrayList<AST<ArrayList<Token>>>();            
            //find the sections beginning index
            //each section contains either no repeating part, one repeating part, or 
            for(int i=0;i<a.size(); i++){
                if (a.get(i).string.equals("||")||a.get(i).string.equals("|]")||a.get(i).string.equals(":|")
                        && a.get(Math.min(i+1,a.size())).type!= Token.Type.Repeat_first 
                        && a.get(Math.min(i+1,a.size())).type!= Token.Type.Repeat_second){
                    BeginIndOfSect.add(i+1);
                }
            }
            BeginIndOfSect.add(Body.size()+1);
            for(int j=0;j<BeginIndOfSect.size()-1;j++){
                VoiceTrees.add(Parser(Body.subList(BeginIndOfSect.get(j),BeginIndOfSect.get(j+1))));
            }
        }
    }
   
    public Parser(ArrayList<Token> a){
        if (NonRepeat(a)){
            return NodeTree(a);
        }
        
        else if (NoChild(a)){
            ArrayList<Token> Empty = new ArrayList<Token>();
            int indRepeat = 0;
            for (int j=0; j<a.size();j++){
                if(a.get(j).type== Token.Type.RepeatBegin){
                    indRepeat = j;
                }
            }    
            return ParentTree(a, a.subList(indRepeat, a.size()), Empty);
        }
            
        else if (SingleChild(a)){            
            //ArrayList<Token> Empty = new ArrayList<Token>();
            //int indChild = 0;
            //for (int j=0; j<a.size();j++){
                //if(a.get(j).type== Token.Type.Repeat_first){
                    //indChild = j;
                //}
            //}
            //return ParentTree(a, a.subList(indChild, a.size()), Empty);
            throw new RuntimeException("cannot have [1 without [2");
        }
        else {
            int indChildOne = 0;
            int indChildTwo = 0;
            for (int j=0; j<a.size();j++){
                if(a.get(j).type== Token.Type.Repeat_first){
                    indChildOne = j;
                }
                if(a.get(j).type== Token.Type.Repeat_second){
                    indChildTwo = j;
                }
            }
            return ParentTree(a, a.subList(indChildOne, indChildTwo), a.subList(indChildTwo, a.size()));      
        }
    }

    private boolean NonRepeat(ArrayList<Token> list){
        for (int i=0;i<list.size();i++){
            if (list.get(i).type== Token.Type.RepeatBegin
                    || list.get(i).type== Token.Type.RepeatEnd
                    || list.get(i).type== Token.Type.Repeat_first){
                return false;    
            }
        }
        return true;
    }
    
    private boolean NoChild(ArrayList<Token> list){
        for (int i=0;i<list.size();i++){
            if (list.get(i).type== Token.Type.Repeat_first){
                return false;    
            }
        }
        return true;
    }
    
    private boolean SingleChild(ArrayList<Token> list){
        for (int i=0;i<list.size();i++){
            if (list.get(i).type== Token.Type.Repeat_first){
                return false;    
            }
        }
        return true;
    } 
}
