package sound;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;

/**
 * Test some super complicated stuff.
 * @category no_didit
 */
public class SequencePlayerTest {
    @Test
    public void testPiece1(){
        try {
            // row row row your boat!
            SequencePlayer player = new SequencePlayer(140,12);
            //C C C3/4 D/4 E |
            player.addNote(new Pitch('C').toMidiNote(), 0, 12);
            player.addNote(new Pitch('C').toMidiNote(), 12, 12);
            player.addNote(new Pitch('C').toMidiNote(), 24, 9);
            player.addNote(new Pitch('D').toMidiNote(), 33, 3);
            player.addNote(new Pitch('E').toMidiNote(), 36, 12);
            //E3/4 D/4 E3/4 F/4 G2 |
            player.addNote(new Pitch('E').toMidiNote(), 48, 9);
            player.addNote(new Pitch('D').toMidiNote(), 57, 3);
            player.addNote(new Pitch('E').toMidiNote(), 60, 9);
            player.addNote(new Pitch('F').toMidiNote(), 69, 3);
            player.addNote(new Pitch('G').toMidiNote(), 72, 24);
            //(3c/2c/2c/2 (3G/2G/2G/2 (3E/2E/2E/2 (3C/2C/2C/2 |
            player.addNote(new Pitch('C').octaveTranspose(1).toMidiNote(), 96, 4);
            player.addNote(new Pitch('C').octaveTranspose(1).toMidiNote(), 100, 4);
            player.addNote(new Pitch('C').octaveTranspose(1).toMidiNote(), 104, 4);
            player.addNote(new Pitch('G').toMidiNote(), 108, 4);
            player.addNote(new Pitch('G').toMidiNote(), 112, 4);
            player.addNote(new Pitch('G').toMidiNote(), 116, 4);
            player.addNote(new Pitch('E').toMidiNote(), 120, 4);
            player.addNote(new Pitch('E').toMidiNote(), 124, 4);
            player.addNote(new Pitch('E').toMidiNote(), 128, 4);
            player.addNote(new Pitch('C').toMidiNote(), 132, 4);
            player.addNote(new Pitch('C').toMidiNote(), 136, 4);
            player.addNote(new Pitch('C').toMidiNote(), 140, 4);
            //| G3/4 F/4 E3/4 D/4 C2 |]
            player.addNote(new Pitch('G').toMidiNote(), 144, 9);
            player.addNote(new Pitch('F').toMidiNote(), 153, 3);
            player.addNote(new Pitch('E').toMidiNote(), 156, 9);
            player.addNote(new Pitch('D').toMidiNote(), 165, 3);
            player.addNote(new Pitch('C').toMidiNote(), 168, 24);
            player.play();
            System.out.println(player);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        
    }

    @Test
    public void testPiece2(){
        try {
            SequencePlayer player = new SequencePlayer(200,6);
            
            //[^F/2e/2] [F/2e/2] z/2 [F/2e/2] z/2 [F/2c/2] [Fe] |
            player.addNote(new Pitch('F').accidentalTranspose(1).toMidiNote(), 0, 3);
            player.addNote(new Pitch('E').octaveTranspose(1).toMidiNote(), 0, 3);
            player.addNote(new Pitch('F').accidentalTranspose(1).toMidiNote(), 3, 3);
            player.addNote(new Pitch('E').octaveTranspose(1).toMidiNote(), 3, 3);
            // rest 3
            player.addNote(new Pitch('F').accidentalTranspose(1).toMidiNote(), 9, 3);
            player.addNote(new Pitch('E').octaveTranspose(1).toMidiNote(), 9, 3);
            // rest 3
            player.addNote(new Pitch('F').accidentalTranspose(1).toMidiNote(), 15, 3);
            player.addNote(new Pitch('C').octaveTranspose(1).toMidiNote(), 15, 3);
            player.addNote(new Pitch('F').accidentalTranspose(1).toMidiNote(), 18, 6);
            player.addNote(new Pitch('E').octaveTranspose(1).toMidiNote(), 18, 6);
            // [GBg] z G z |
            player.addNote(new Pitch('G').toMidiNote(), 24, 6);
            player.addNote(new Pitch('G').octaveTranspose(1).toMidiNote(), 24, 6);
            player.addNote(new Pitch('B').toMidiNote(), 24, 6);
            // rest 6
            player.addNote(new Pitch('G').toMidiNote(), 36, 6);
            // rest 6
            // c3/2 G/2 z E |
            player.addNote(new Pitch('C').octaveTranspose(1).toMidiNote(), 48, 9);
            player.addNote(new Pitch('G').toMidiNote(), 57, 3);
            // rest 6
            player.addNote(new Pitch('E').toMidiNote(), 66, 6);
            // E/2 A B _B/2 A |
            player.addNote(new Pitch('E').toMidiNote(), 72, 3);
            player.addNote(new Pitch('A').toMidiNote(), 75, 6);
            player.addNote(new Pitch('B').toMidiNote(), 81, 6);
            player.addNote(new Pitch('B').accidentalTranspose(-1).toMidiNote(), 87, 3);
            player.addNote(new Pitch('A').toMidiNote(), 90, 6);
            // (3Geg a e/2 g/2 |
            player.addNote(new Pitch('G').toMidiNote(), 96, 4);
            player.addNote(new Pitch('E').octaveTranspose(1).toMidiNote(), 100, 4);
            player.addNote(new Pitch('G').octaveTranspose(1).toMidiNote(), 104, 4);
            player.addNote(new Pitch('A').octaveTranspose(1).toMidiNote(), 108, 6);
            player.addNote(new Pitch('F').octaveTranspose(1).toMidiNote(), 114, 3);
            player.addNote(new Pitch('G').octaveTranspose(1).toMidiNote(), 117, 3);
            // z/2 e c/2 d/2 b |]
            // rest 3
            player.addNote(new Pitch('E').octaveTranspose(1).toMidiNote(), 123, 6);
            player.addNote(new Pitch('C').octaveTranspose(1).toMidiNote(), 129, 3);
            player.addNote(new Pitch('D').octaveTranspose(1).toMidiNote(), 132, 3);
            player.addNote(new Pitch('B').toMidiNote(), 135, 9);
            
            player.play();
            
            System.out.println(player);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}
