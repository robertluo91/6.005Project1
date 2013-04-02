package player;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.Pitch;
import sound.SequencePlayer;

public class ASTtoPlayer {    
    private int beatsPerMinute;
    private int ticksPerBeat;
    private int clock;
    
    /**
     * play music passed from parser
     * @param parser
     */
    public ASTtoPlayer(Parser parser){
        List<List<AST>> SequenceofVoiceForest = parser.SequenceofVoiceForest;
        ticksPerBeat = parser.tpb;
        beatsPerMinute = parser.tempo;
        SequencePlayer sp;
        try {
            sp = new SequencePlayer(beatsPerMinute,ticksPerBeat);
            for (int voice=0; voice<SequenceofVoiceForest.size(); voice++){
                traverse(SequenceofVoiceForest.get(voice),sp);
            }
            sp.play();
            
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }  
    
    /**
     * traverse the arraylist of ASTs using sp tree by tree
     * @param VoiceForest an arraylist of AST
     * @param sp SequencePlayer
     */
    public void traverse(List<AST> VoiceForest,SequencePlayer sp){
        clock = 0;
        for (int index=0; index<VoiceForest.size();index++){
            traverse(VoiceForest.get(index), sp);
        }
    }
    
    /**
     * traverse an AST by traversing its expanding arraylist
     * @param t an AST
     * @param sp SequencePlayer
     */
    public void traverse(AST t, SequencePlayer sp){
        addNotesInNode(t.toArrayList(),sp);
    }
    
    /**
     * traverse an arraylist of token, adding notes to play
     * @param node an arraylist of token
     * @param sp SequencePlayer
     */
    public void addNotesInNode(ArrayList<Token> node,SequencePlayer sp){
        int i = 0;
        //System.out.println(node);
        while(i < node.size()){
            clock += node.get(i).noteLength;
            //If the note is not a chord, the numNotesInChord should be zero
            int j;
            for(j=i; j < i+node.get(i).chord+1 && j < node.size(); j++){
                Token note = node.get(j);
                //skip "rest"
                if (note.basenote!=7){
                    Pitch newPitch = new Pitch(intkey[note.basenote]).octaveTranspose(note.octave).transpose(note.accid);
                    sp.addNote(newPitch.toMidiNote(),clock,(int) note.noteLength);
                }
            }
            i=j;
        }
    }
    
    //map between 0-7 and basenote (rest included) 
    public static final char[] intkey = {'A','B','C','D','E','F','G','z'};       
}