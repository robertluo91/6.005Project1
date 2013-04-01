package player;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.Pitch;
import sound.SequencePlayer;

/**
 * 
 *
 */
public class ASTtoPlayer {
    
    private int beatsPerMinute;
    private int ticksPerBeat;
    private int clock;
    /**
     * 
     * @param parser
     */
    public ASTtoPlayer(Parser parser){
        ArrayList<ArrayList<AST>> SequenceofVoiceForest = parser.SequenceofVoiceForest;
        ticksPerBeat = parser.tpb;
        beatsPerMinute = parser.tempo;
        SequencePlayer sp;
        try {
            sp = new SequencePlayer(beatsPerMinute,ticksPerBeat);
            for (int voice=0; voice<SequenceofVoiceForest.size(); voice++){
                traverse(SequenceofVoiceForest.get(voice),sp);
            }
            sp.play();
            //System.out.println(sp);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }  
    
    /**
     * 
     * @param VoiceForest
     * @param sp
     */
    public void traverse(ArrayList<AST> VoiceForest,SequencePlayer sp){
        clock = 0;
        for (int index=0; index<VoiceForest.size();index++){
            traverse(VoiceForest.get(index), sp);
        }
    }
    
    /**
     * 
     * @param t
     * @param sp
     */
    public void traverse(AST t, SequencePlayer sp){
        addNotesInNode(t.toArrayList(),sp);
    }
    
    /**
     * 
     * @param node
     * @param sp
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
                if (note.basenote!=7){
                    Pitch newPitch = new Pitch(intkey[note.basenote]).octaveTranspose(note.octave).transpose(note.accid);
                    //System.out.println(newPitch);
                    sp.addNote(newPitch.toMidiNote(),
                        clock,(int) note.noteLength);
                }
            }
            i=j;
        }
        

    }

    public static final char[] intkey = {'A','B','C','D','E','F','G','z'};
        
}