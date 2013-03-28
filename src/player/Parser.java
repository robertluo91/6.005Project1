package player;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public final ArrayList<ArrayList<AST>> SequenceofVoiceForest  = new ArrayList<ArrayList<AST>>();
    public final int tpb;
    public final int tempo;
    public Parser(Lexer lexer) {                
        //ArrayList<Token> Headers = lexer.MusicHeader;
        ArrayList<ArrayList<Token>> Body = lexer.MusicBody;
        String key = lexer.Key;
        tempo = lexer.Tempo;
        tpb = lexer.Tick; 
        KeySignature KeySig = new KeySignature(key);
        
        //we don't deal with nested repetition
        //since repetition cannot be made across major section, if an already started repeating stream meets "||", 
        //then it halts, and becomes a complete repeated piece
        //Weixin: we do not deal with nested repetition at this stage, may modify later
        
        for (int u=0; u< Body.size(); u++){
            
            List<Integer> EndIndOfMajorSect = new ArrayList<Integer>();
            List<AST> VoiceTrees = new ArrayList<AST>();            
            ArrayList<Token> a = Body.get(u);
            int end = a.size();
            EndIndOfMajorSect.add(end);
            
            //check valid ending
            if (!ValidEnding(a)) throw new RuntimeException("invalid ending type");
            int i=0;
            while (i<end){
                //adjust temporary accidental within measure
                //by the way we perform before the next loop, we know that the ith token is the first element 
;                //       within its measure
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
                
                //find the major sections beginning index
                if (a.get(i).string.equals("||")||a.get(i).string.equals("|]")){
                    EndIndOfMajorSect.add(i+1);
                }
                i++;
            }
            
            VoiceTrees.add(Parse((ArrayList<Token>) (a.subList(0,EndIndOfMajorSect.get(0)))));
            for(int j=0;j<EndIndOfMajorSect.size()-1;j++){
                VoiceTrees.add(Parse((ArrayList<Token>) (a.subList(EndIndOfMajorSect.get(j),EndIndOfMajorSect.get(j+1)))));
            }
            SequenceofVoiceForest.add((ArrayList<AST>) VoiceTrees);             
        }
    }
   
    private AST Parse(ArrayList<Token> a){        
        if (NoChild(a)){
            ArrayList<Token> r = ParseRepeat(a);  
            return new NodeTree(r);
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
            return new ParentTree(ParseRepeat(a), 
                    ParseRepeat((ArrayList<Token>) a.subList(indChildOne, indChildTwo-1)), 
                    ParseRepeat((ArrayList<Token>) a.subList(indChildTwo, a.size())));      
        }
    }

    //private boolean NonRepeat(ArrayList<Token> list){
    //    for (int i=0;i<list.size();i++){
    //        if (list.get(i).type== Token.Type.RepeatBegin
    //                || list.get(i).type== Token.Type.RepeatEnd
    //                || list.get(i).type== Token.Type.Repeat_first){
    //            return false;    
    //        }
    //    }
    //    return true;
    //}    

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
    
    private boolean ValidEnding(ArrayList<Token> list){
        int end = list.size()-1;
        if (list.get(end).string.equals("|")
                ||list.get(end).string.equals("||")
                ||list.get(end).string.equals("|]")
                ||list.get(end).string.equals(":|"))
        {  return true; }
        return false;
    }
    
    //"Parse" a string without variants
    private ArrayList<Token> ParseRepeat(ArrayList<Token> list){
        //first we find the first appeared "|:"or ":|"
        List<Integer> BeginRepeat = new ArrayList<Integer>();
        List<Integer> EndRepeat = new ArrayList<Integer>();
        int i =0;
        while (i<list.size()){
            if (list.get(i).type== Token.Type.RepeatBegin
                    || list.get(i).type== Token.Type.RepeatEnd){
                break;
            }
            i++;
        }
        
        if (list.get(i).type== Token.Type.RepeatEnd){
            BeginRepeat.add(-1);
        }
        
        for (int j=0;j<list.size();j++){
            if (list.get(j).type== Token.Type.RepeatBegin){
                BeginRepeat.add(j);
            }
            else if (list.get(j).type== Token.Type.RepeatEnd){
                EndRepeat.add(j);
            }
        }
        
        int l= BeginRepeat.get(BeginRepeat.size()-1);
        while (l<list.size()){
            if (list.get(l).type == Token.Type.RepeatEnd){
                break;
            }
            l++;
        }
        
        if (l>list.size()-1){
            EndRepeat.add(list.size());
        }
        
        if (EndRepeat.size()!=BeginRepeat.size()){
            throw new RuntimeException("invalid repetition type");
        }
        
        ArrayList<Token> Parsedlist= new ArrayList<Token>();
        if (BeginRepeat.get(0)!=-1||BeginRepeat.get(0)!=0){
            Parsedlist.addAll(list.subList(0, BeginRepeat.get(0)));
            Parsedlist.addAll(list.subList(BeginRepeat.get(0), EndRepeat.get(0)));
            Parsedlist.addAll(list.subList(BeginRepeat.get(0), EndRepeat.get(0)));
        }
        else if (BeginRepeat.get(0)==-1){
            Parsedlist.addAll(list.subList(0, EndRepeat.get(0)));
            Parsedlist.addAll(list.subList(0, EndRepeat.get(0)));
        }
        else if (BeginRepeat.get(0)==0){
            Parsedlist.addAll(list.subList(1, EndRepeat.get(0)));
            Parsedlist.addAll(list.subList(1, EndRepeat.get(0)));
        }
        for (int j=1; j<EndRepeat.size();j++){            
            Parsedlist.addAll(list.subList(EndRepeat.get(j-1)+1, BeginRepeat.get(j)));
            Parsedlist.addAll(list.subList(BeginRepeat.get(j)+1, EndRepeat.get(j)));
            Parsedlist.addAll(list.subList(BeginRepeat.get(j)+1, EndRepeat.get(j)));
        } 
        if (EndRepeat.get(EndRepeat.size()-1)!=list.size()-1){
            Parsedlist.addAll(list.subList(EndRepeat.get(EndRepeat.size()-1)+1,list.size()-1));
        }
        
        if(BeginRepeat.size()==0){
            Parsedlist = list;
        }
        
        int m = Parsedlist.size()-1;
        while (m>-1){
            if (Parsedlist.get(m).type!=Token.Type.Pitch && Parsedlist.get(m).type!= Token.Type.Rest){
                Parsedlist.remove(m);
            }
            m--;
        }
        return Parsedlist;
    }
}
