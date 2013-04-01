package player;

import org.junit.Test;

public class MainTest {

    @Test
    public void fur_elise() { 
        Main.play("sample_abc/fur_elise.abc"); 
    } 
    @Test
    public void prelude(){ 
        Main.play("sample_abc/prelude.abc"); 
    } 
    @Test
    public void piece1(){ 
        Main.play("sample_abc/piece1.abc"); 
    } 
    @Test
    public void piece2(){ 
        Main.play("sample_abc/piece2.abc"); 
    } 
    @Test
    public void little_night_music(){ 
        Main.play("sample_abc/little_night_music.abc"); 
    } 
    @Test
    public void scale(){ 
        Main.play("sample_abc/scale.abc"); 
    } 
    @Test
    public void invention(){ 
        Main.play("sample_abc/invention.abc"); 
    } 
    @Test
    public void paddy(){ 
        Main.play("sample_abc/paddy.abc"); 
    } 
}
