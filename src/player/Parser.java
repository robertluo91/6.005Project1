package player;

import java.util.ArrayList;
import java.util.List;

import player.Token.Type;
/**
 * 
 *
 */
public class Parser {
    public final ArrayList<ArrayList<AST>> SequenceofVoiceForest  = new ArrayList<ArrayList<AST>>();
    public final int tpb;
    public final int tempo;
    public final int size;
    
    /**
     * @param lexer arraylist of voice_list, where voice_list is an arraylist of tokens for a particular voice
     * @throws RuntimeException when invalid ending, empty measure, wrong pattern of accid, confusion caused by pure nested repetition
     *         and when there is "[1" but no "[2" or the reverse, and if "[2" before "[1" in a major section 
     */
    public Parser(Lexer lexer) throws RuntimeException{                
        ArrayList<ArrayList<Token>> Body = lexer.MusicBody;
        size= lexer.size;
        String key = lexer.Key;
        tempo = lexer.Tempo;
        tpb = lexer.Tick; 
        KeySignature KeySig = new KeySignature(key);
        
        //Weixin: we deal with cases where there are either no "[1,[2" or both "[1,[2" exist; if both exist, 
        //        each variant can contain repetitions
        //we don't deal with nested repetition
        //since repetition cannot be made across major section, if an already started repeating stream meets "||", 
        //then it halts, and becomes a complete repeated piece    
   
        for (int voice=0; voice< size; voice++){
            //need to know the end of major section in order to deal with repetition
            List<Integer> EndIndOfMajorSect = new ArrayList<Integer>();
            List<AST> TreesCurrentVoice = new ArrayList<AST>();            
            ArrayList<Token> a = Body.get(voice);
            int end = a.size()-1;
            
            //check valid ending
            if (!ValidEnding(a)) throw new RuntimeException("invalid ending type");
            
            //adjust temporary accidental within measure
            int i=0;
            while (i<end+1){                
                //by the way we perform before the next loop, we know that the ith token is the first element 
                //       within its measure
                //find the index of barline, and apply the change to all index within the measure after i
                int EndofMeasure = i;
                for (int j=i;j<end+1;j++){
                    if (a.get(j).type== Token.Type.Barline
                            ||a.get(j).type == Token.Type.RepeatBegin
                            ||a.get(j).type == Token.Type.RepeatEnd){
                        EndofMeasure = j;
                        break;
                    }
                }
                //by assumption, the first token of a voice is not a barline
                if (EndofMeasure == i&&a.get(i).type!= Token.Type.RepeatBegin){
                    throw new RuntimeException("measure cannot be empty");
                }
                //the originally accidented pitch should not be affected by earlier temporary accidental
                //and they should affect the latter pitch without original nontrivial accidental
                List<Integer> measureaccids = new ArrayList<Integer>();
                for (int j=i;j<EndofMeasure;j++){
                    if (a.get(j).type== Token.Type.Pitch){
                        if (a.get(j).accid != 0 || a.get(j).isNatural){
                            measureaccids.add(j); 
                        }                        
                    }
                }
                for (int j:measureaccids){
                    for (int k=j+1;k<EndofMeasure; k++){
                        if (a.get(k).type== Token.Type.Pitch 
                                && a.get(k).basenote==a.get(j).basenote
                                && a.get(k).octave==a.get(j).octave 
                                && !measureaccids.contains(k)){
                            if (!a.get(j).isNatural){
                                a.get(k).accid = a.get(j).accid;  
                            }
                            else a.get(k).accid = 0;                                                                 
                        }
                        if (a.get(k).accid>2||a.get(k).accid<-2){
                            throw new RuntimeException("invalid use of accid");
                        }
                    }                        
                }
                i = EndofMeasure+1; 
            }
            
            //adjust accidental according to the key of header
            i=0;
            while(i<end+1){                
                if ((a.get(i).type == Token.Type.Pitch)&&(a.get(i).isNatural == false)){
                    a.get(i).accid += KeySig.current_signature[a.get(i).basenote];  
                }
                if ((a.get(i).accid>2||a.get(i).accid<-2)&&(a.get(i).isNatural == false)){
                    throw new RuntimeException("invalid use of accid");
                }
                
                //find the major sections ending index
                if (a.get(i).string.equals("||")||a.get(i).string.equals("|]")){
                    EndIndOfMajorSect.add(i);
                }
                i++;
                //since we make sure the end of the voice list of tokens is valid 
            }
            if (a.get(end).string.equals("|")||a.get(end).string.equals(":|")){
                EndIndOfMajorSect.add(end);
            }
            
            if (EndIndOfMajorSect.size()==1){
                TreesCurrentVoice.add(Parse(a));
            }            
            else{
                TreesCurrentVoice.add(Parse(SubList(a,0,EndIndOfMajorSect.get(0)+1)));
                for(int j=0;j<EndIndOfMajorSect.size()-1;j++){
                    TreesCurrentVoice.add(Parse(SubList(a,EndIndOfMajorSect.get(j)+1,EndIndOfMajorSect.get(j+1)+1)));
                }
            }
            SequenceofVoiceForest.add((ArrayList<AST>) TreesCurrentVoice);             
        }
    }
   /**
    * given an arraylist of tokens possibly with variants and repetition 
    * @param majorsection arraylist of tokens
    * @return AST equivalent to the list
    * @throws RuntimeException if have only one of [1,[2; and if [2 appears before [1 
    */
    private AST Parse(ArrayList<Token> majorsection){        
        if (NoFirstChild(majorsection)){
            if (!NoSecondChildren(majorsection)){
                throw new RuntimeException("cannot have [2 without [1");
            }
            ArrayList<Token> repeat = ParseRepeat(majorsection);  
            return new NodeTree(repeat);
        }
            
        else {
            int indChildOne = 0;
            int indChildTwo = 0;
            for (int j=0; j<majorsection.size();j++){
                if(majorsection.get(j).type== Token.Type.Repeat_first){
                    indChildOne = j;
                    break;
                }                
            }
            if (NoSecondChildren(majorsection.subList(indChildOne+1,majorsection.size()-1))){            
                throw new RuntimeException("cannot have [1 without [2");
            }
            if (!NoSecondChildren(majorsection.subList(0,indChildOne))){
                throw new RuntimeException("cannot have [2 before [1");
            }
            for (int j=indChildOne; j< majorsection.size();j++){
                if(majorsection.get(j).type== Token.Type.Repeat_second){
                    indChildTwo = j;
                    break;
                }
            }
            //check if the (indChildTwo-1)th is the symbol :|
            if (majorsection.get(indChildTwo-1).type!= Token.Type.RepeatEnd){
                throw new RuntimeException("invalid variant type: no RepeatEnd before second variant");
            }
            return new ParentTree(ParseRepeat(SubList(majorsection,0,indChildOne)),
                    ParseRepeat(Complete(SubList(majorsection,indChildOne+1, indChildTwo-1))), 
                    ParseRepeat(SubList(majorsection,indChildTwo+1, majorsection.size())));                          
        }
    }
    
    /**
     * @param list ArrayList of token
     * @return NoFirstChild boolean showing that if it has no first variant
     */
    private boolean NoFirstChild(ArrayList<Token> list){
        for (int i=0;i<list.size();i++){
            if (list.get(i).type== Token.Type.Repeat_first){
                return false;    
            }
        }
        return true;
    }
    
    /**
     * @param list List of token
     * @return NoFirstChild boolean showing that if it has no second variant
     */
    private boolean NoSecondChildren(List<Token> list){
        for (int i=0;i<list.size();i++){
            if (list.get(i).type== Token.Type.Repeat_second){
                return false;    
            }
        }
        return true;
    }
    
    /**
     * 
     * @param list
     * @return
     */
    private boolean ValidEnding(ArrayList<Token> list){
        int end = list.size()-1;
        if (list.get(end).string.equals("|")
                ||list.get(end).string.equals("||")
                ||list.get(end).string.equals("|]")
                ||list.get(end).string.equals(":|"))
        {  return true; }
        else return false;
    }
    
    /**
     * Parse a string without variants
     * @param list, arraylist of tokens without variants "[1,[2", satisfying ValidEnding 
     * @return Parsedlist, arraylist of tokens with only pitch and rest, equivalent to list when playing 
     * @throws RuntimeException when nested repetition
     */
    private ArrayList<Token> ParseRepeat(ArrayList<Token> list){
        //first we find the first appeared "|:"or ":|"
        List<Integer> BeginRepeat = new ArrayList<Integer>();
        List<Integer> EndRepeat = new ArrayList<Integer>();
        List<Integer> Repeat = new ArrayList<Integer>();
        ArrayList<Token> Parsedlist= new ArrayList<Token>();
        
        int firstsymbol =0;
        boolean omitbegin = false;
       //true when there is an omitted RepeatBegin at the beginning of the major section 
        while (firstsymbol<list.size()){
            if (list.get(firstsymbol).type== Token.Type.RepeatBegin){
                break;
            }
            else if(list.get(firstsymbol).type== Token.Type.RepeatEnd){
                omitbegin = true;
                break;
            }
            else firstsymbol++;
        }
               
        
        for (int j=0;j<list.size();j++){
            if (list.get(j).type== Token.Type.RepeatBegin){
                BeginRepeat.add(j);
                Repeat.add(j);
            }
            else if (list.get(j).type== Token.Type.RepeatEnd){
                EndRepeat.add(j);
                Repeat.add(j);
            }
        }
        
        if (Repeat.size()==0){
            Parsedlist = list;
        }
        
        else {
            boolean omitend = false;
            //true when there is an omitted RepeatEnd at the end of the major section,
            //only if there there is BeginRepeat symbol
            if (BeginRepeat.size()>0){
                int l= BeginRepeat.get(BeginRepeat.size()-1);
                while (l<list.size()){
                    if (list.get(l).type == Token.Type.RepeatEnd){
                        break;
                    }
                    else l++;
                }
                
                if (l==list.size()){
                    omitend = true;
                }
            }            
            
            
            //checked nested; when there is no nested repetition, and the repeat symbol completed,
            //                then pairs of repeat symbols have no overlap 
            int begincorrection = 0;
            if (omitbegin) begincorrection=1;
            if (omitend) EndRepeat.add(list.size()); Repeat.add(list.size());
            if ((BeginRepeat.size()+begincorrection)!=EndRepeat.size()){
                throw new RuntimeException("invalid repetition type");
            }
            int Repeatsize = Repeat.size()+ begincorrection;
            //if no throw, then Repeat has a size of even number        
            for (int repeat = 0; repeat< Repeatsize/2-begincorrection;repeat++){
                if (Repeat.get(2*repeat+begincorrection)!=BeginRepeat.get(repeat)){
                    throw new RuntimeException("nested repetition");
                }
                if (Repeat.get(2*repeat-begincorrection+1)!=EndRepeat.get(repeat)){
                    throw new RuntimeException("nested repetition");
                }
            }
           
            
            if (BeginRepeat.get(0)!=0){
                if (!omitbegin){
                Parsedlist.addAll(list.subList(0, BeginRepeat.get(0)));
                Parsedlist.addAll(list.subList(BeginRepeat.get(0)+1, EndRepeat.get(0)));
                Parsedlist.addAll(list.subList(BeginRepeat.get(0)+1, EndRepeat.get(0)));
                }
                else{
                    Parsedlist.addAll(list.subList(0, EndRepeat.get(0)));
                    Parsedlist.addAll(list.subList(0, EndRepeat.get(0)));
                }
            }        
            else {
                Parsedlist.addAll(list.subList(1, EndRepeat.get(0)));
                Parsedlist.addAll(list.subList(1, EndRepeat.get(0)));
            }
            for (int j=1; j<EndRepeat.size();j++){            
                Parsedlist.addAll(list.subList(EndRepeat.get(j-1)+1, BeginRepeat.get(j-begincorrection)));
                Parsedlist.addAll(list.subList(BeginRepeat.get(j-begincorrection)+1, EndRepeat.get(j)));
                Parsedlist.addAll(list.subList(BeginRepeat.get(j-begincorrection)+1, EndRepeat.get(j)));
            } 
            if (EndRepeat.get(EndRepeat.size()-1)!=list.size()-1){
                Parsedlist.addAll(list.subList(EndRepeat.get(EndRepeat.size()-1)+1,list.size()-1));
            }                    
        }
        //remove all symbols except pitch, and rest 
        int m = Parsedlist.size()-1;
        while (m>-1){
            if (Parsedlist.get(m).type!=Token.Type.Pitch && Parsedlist.get(m).type!= Token.Type.Rest){
                Parsedlist.remove(m);
            }
            m--;
        }
        return Parsedlist;
    }
    
    /**
     * 
     * @param list
     * @param start
     * @param end
     * @return
     */
    private ArrayList<Token> SubList(ArrayList<Token> list, int start, int end){
        ArrayList<Token> returnlist = new ArrayList<Token>();
        returnlist.addAll(list.subList(start,end));
        return returnlist;
    }
    /**
     * to complete the truncated first variant as a valid piece
     * @param list, arraylist of token without barline
     * @return returnlist, complete the list by adding a barline at end
     */
    private ArrayList<Token> Complete(ArrayList<Token> list){
        ArrayList<Token> returnlist = new ArrayList<Token>();
        returnlist.addAll(list);
        returnlist.add(new Token(Type.Barline, "|", 0, 0, 0, 0, 0, 0, 0));
        return returnlist;
    }
}
