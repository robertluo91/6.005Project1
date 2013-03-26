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
        KeySignature KeySig = new KeySignature(key);
        
        
        //since repetition cannot be made across major section, if an already started repeating stream meets "||", 
        //then it halts, and becomes a complete repeated piece
        //Weixin: we do not deal with nested repetition at this stage, may modify later
        
        List<ArrayList<AST<ArrayList<Token>>>> SequenceofVoiceForest= new ArrayList<ArrayList<AST<ArrayList<Token>>>>();
        
        for (int u=0; u< Body.size(); u++){
            
            List<Integer> BeginIndOfSect = new ArrayList<Integer>();
            BeginIndOfSect.add(0);
            List<AST<ArrayList<Token>>> VoiceTrees = new ArrayList<AST<ArrayList<Token>>>();            
            ArrayList<Token> a = Body.get(u);
            int end = a.size();
            
            int i=0;
            while (i<end){
                //adjust temporary accidental within measure
                //by the way we perform before the next loop, we know that the ith token is the first element 
                //       within its measure
                //find the index of barline, and apply the change to all index within the measure after i
                int EndofMeasure = i;
                for (int j=i;j<end;j++){
                    if (a.get(j).type== Token.Type.Barline){
                        EndofMeasure = j;
                        break;
                    }
                }
                if (EndofMeasure == i){
                    throw new RuntimeException("measure cannot be empty");
                }
                //the originally accidented pitch should not be affected by earlier temporary accidental
                //and they should affect the later pitch without original nontrivial accidental
                List<Integer> measureaccids = new ArrayList<Integer>();
                for (int j=i;j<EndofMeasure;j++){
                    if (a.get(j).type== Token.Type.Pitch 
                            && a.get(j).accid != 0){
                        measureaccids.add(j); 
                    }
                }
                for (int j:measureaccids){
                    for (int k=j+1;k<end; k++){
                        if (a.get(k).type== Token.Type.Pitch 
                                && a.get(k).basenote==a.get(j).basenote
                                && a.get(k).octave==a.get(j).octave 
                                && !measureaccids.contains(k)){
                                a.get(k).accid = a.get(j).accid;                                                               
                        }
                        if (a.get(k).accid>2||a.get(k).accid<-2){
                            throw new RuntimeException("invalid use of accid");
                        }
                    }                        
                }
                i = EndofMeasure+1; 
            }
            
            i=0;
            while(i<end){
                //adjust accidental according to the key of header
                if (a.get(i).type == Token.Type.Pitch){
                    a.get(i).accid += KeySig.current_signature[a.get(i).basenote];  
                }
                if (a.get(i).accid>2||a.get(i).accid<-2){
                    throw new RuntimeException("invalid use of accid");
                }
                //find the sections beginning index
                //each section contains either no repeating part, one repeating part, or 
                if (a.get(i).string.equals("||")||a.get(i).string.equals("|]")||a.get(i).string.equals(":|")
                        && a.get(Math.min(i+1,a.size())).type!= Token.Type.Repeat_first 
                        && a.get(Math.min(i+1,a.size())).type!= Token.Type.Repeat_second){
                    BeginIndOfSect.add(i+1);
                }
                i++;
            }

            BeginIndOfSect.add(end+1);
            for(int j=0;j<BeginIndOfSect.size()-1;j++){
                VoiceTrees.add(Parse((ArrayList<Token>) (a.subList(BeginIndOfSect.get(j),BeginIndOfSect.get(j+1)))));
            }
            SequenceofVoiceForest.add((ArrayList<AST<ArrayList<Token>>>) VoiceTrees);             
        }
    }
   
    private AST<ArrayList<Token>> Parse(ArrayList<Token> a){
        if (NonRepeat(a)){
            return NodeTree(a);
        }
        
        else if (NoChild(a)){
            ArrayList<Token> Empty = new ArrayList<Token>();
            int indRepeat = 0;
            for (int j=0; j<a.size();j++){
                if(a.get(j).type== Token.Type.RepeatBegin){
                    indRepeat = j;
                    break;
                }
            }    
            return NodeTree(((ArrayList<Token>) a).addAll(a.subList(indRepeat, a.size())));
        }
            
        else if (SingleChild(a)){            
            throw new RuntimeException("cannot have [1 without [2");
        }
        else {
            int indChildOne = 0;
            int indChildTwo = 0;
            for (int j=0; j<a.size();j++){
                if(a.get(j).type== Token.Type.Repeat_first){
                    indChildOne = j;
                    break;
                }
                if(a.get(j).type== Token.Type.Repeat_second){
                    indChildTwo = j;
                    break;
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
