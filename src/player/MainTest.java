package player;

import org.junit.Test;
/**
 * Test some complicated stuff
 * @category no_didit
 */
public class MainTest {
    //complicatedtest1, two voices
    @Test
    public void fur_elise() { 
        Main.play("sample_abc/fur_elise.abc"); 
    } 
    
    //complicatedtest2, one voice
    @Test
    public void invention(){ 
        Main.play("sample_abc/invention.abc"); 
    }
    
    //without repeat with chord
    @Test
    public void little_night_music(){ 
        Main.play("sample_abc/little_night_music.abc"); 
    }
    
    //with repeat without chord
    @Test
    public void paddy(){ 
        Main.play("sample_abc/paddy.abc"); 
    } 
    
    //start with repeat
    @Test
    public void lilly(){ 
        Main.play("our_test/lilli.abc"); 
    } 
    
    //easypiece1, without repeat, chord, or rest, with no voice input
    @Test
    public void piece1(){ 
        Main.play("sample_abc/piece1.abc"); 
    } 
    
    //easypiece2, with chord, and rest
    @Test
    public void piece2(){ 
        Main.play("sample_abc/piece2.abc"); 
    }     
    
    //more than two voices
    @Test
    public void prelude(){ 
        Main.play("sample_abc/prelude.abc"); 
    } 
    
    //all basenotes
    @Test
    public void scale(){ 
        Main.play("sample_abc/scale.abc"); 
    }
    
    //valid header but fields in different order from other abc files 
    @Test
    public void les_3_marins_de_groix(){ 
        Main.play("our_test/les_3_marins_de_groix.abc"); 
    }
    
   
}
