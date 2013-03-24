package player;

import sound.Pitch;
import sound.SequencePlayer;

import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
/**
 * Main entry point of your application.
 */
public class Main {

    /**
     * Plays the input file using Java MIDI API and displays
     * header information to the standard output stream.
     * 
     * (Your code should not exit the application abnormally using
     * System.exit().)
     * 
     * @param file the name of input abc file
     */
    public static void play(String file) {
        //try {
            //Lexer lexer = new Lexer(file);
            //Parser parser = new Parser(lexer);
            //Tree<Note> tree = parser.getAST();
            //List<Note> notes = tree.accept(new Visitor(), 0);
            //SequencePlayer player = new SequencePlayer(parser.tempo,parser.ticks_per_beat);
            //           for (Note note : notes) {
            //int startTick = note.getStartTick();
            //int numTicks = note.getNumTicks();
            //List<Pitch> pitches = note.getPitch();
            //for (Pitch pitch : pitches) {
                //if (pitch != null) {
                    //player.addNote(pitch.toMidiNote(), startTick, numTicks);
                //}
            //}
        /*
         * may catch more things if needed
         */
        //catch (MidiUnavailableException e) {
            //e.printStackTrace();
        //}
        //catch (InvalidMidiDataException e) {
            //e.printStackTrace();
        //} 
    }

    public static void main(String[] args) {
        for (String i:args){
            play(i);
        }
    }

}
