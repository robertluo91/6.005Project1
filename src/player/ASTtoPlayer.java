package player;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.SequencePlayer;


public class ASTtoPlayer {
    
    private int beatsPerMinute;
    private int ticksPerQuarterNote;
    private int time;
    private Node currentNode;
    private List<Node> nodes;

    
    public ASTtoPlayer(AST t){
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
    
    public List<Node> traverse(AST T, SequencePlayer sp){
        nodes = new ArrayList<Node>();
        currentNode = t.root; 
        addNotesInNode(currentNode);
        for(int i = 0; i < t.numChildren-1; i++){
            traverse(T.getChild(i),sp);
        }
        traverse(T.getChild(t.numChildren),sp);
    }
    
    public void addNotesInNode(Node node,SequencePlayer sp){
        for(int i =0; i < node.size; i++){
            //If the note is not a chord, the numNotesInChord should be zero
            for(int j = 0; j < node.get(i).numNotesInChord; j++){
                sp.addNote(node.get(i).baseNode,time,node.get(i).duration);
            }
            time += node.get(i).duration;
        }
        

    }
    
    public void addEachField(SequencePlayer sp, Node n){
        sp.addNode(n.accidental,n.baseNote,n.octave,n.noteLength);
    }
    
        
}