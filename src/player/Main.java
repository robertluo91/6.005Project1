package player;

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
            Lexer lexer = new Lexer(file);
            Parser parser = new Parser(lexer);
            new ASTtoPlayer(parser);
    }

    public static void main(String[] args) {
        for (String i:args){
            play(i);
        }
    }
}
