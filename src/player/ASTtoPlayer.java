package player;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sound.Pitch;
import sound.SequencePlayer;


public class ASTtoPlayer {
    
    private int beatsPerMinute;
    private int ticksPerQuarterNote;
    private int time;
    private ArrayList<Token> currentNode;
    
    public ASTtoPlayer(ParentTree<ArrayList<Token>> t){
        try {
            SequencePlayer sp = new SequencePlayer(beatsPerMinute,ticksPerQuarterNote);
            time = 0;
            traverse(t,sp);           
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }


    public void traverse(ParentTree<ArrayList<Token>> t, SequencePlayer sp){
        new ArrayList<ArrayList<Token>>();
        currentNode = t.root; 
        addNotesInNode(currentNode,sp);
        addNotesInNode(t.leftChild,sp);
        addNotesInNode(currentNode,sp);
        addNotesInNode(t.rightChild,sp);
    }
    
    public void addNotesInNode(ArrayList<Token> node,SequencePlayer sp){
        for(int i =0; i < node.size(); i++){
            //If the note is not a chord, the numNotesInChord should be zero
            for(int j = 0; j < node.get(i).chord; j++){
                Token note = node.get(i);
                sp.addNote(new Pitch(intkey[note.basenote]).octaveTranspose(note.octave).transpose(note.accid).toMidiNote(),time,(int)note.noteLength);
            }
            time += node.get(i).noteLength;
        }
        

    }

    public static final char[] intkey = {'A','B','C','D','E','F','G','z'};
    
        
}