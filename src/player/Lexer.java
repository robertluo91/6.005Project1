package player;
import java.util.ArrayList;
import player.Token.Type;

/**
 * A lexer takes a string and splits it into tokens that are meaningful to a
 * parser. 12 token types: Rest, Pitch, Dupspec, Trispec, Quadspec, Barline, Nrepeat
 * For detailed description of token types, see Token.java
 */
public class Lexer {
    /**
     * Creates the lexer over the passed string.
     * @param string: The string to tokenize.
     */        
   
    	ArrayList<Token> tokens;
    	ArrayList<Token> Header;  //you can call Header  from outside to access the arraylist of all the header tokens
    	

    	public Lexer(String string) {

    		// iterate through the input string and identify tokens;
    		// append tokens to the global output list
    		String Expression = new String(string);
    		
    		
    		ArrayList<Token> output = new ArrayList<Token>();
    		//create an arraylist "output" to put all the tokens generated inside
    		int length = Expression.length();
    		int currentlen = 0;
    		while (currentlen < length) {
    			boolean anyMatchSoFar = false;
    			for (int i = currentlen+1; i <length; i++) {
    				String currentString = Expression.substring(currentlen, i);
    				for (Type t : Token.Type.values())
    				
    				{
    					Token testToken = new Token(t, "");
    					if (testToken.pattern.matcher(currentString).matches()) {
    						// a token has been identified because its Matcher matches method == True
    						anyMatchSoFar = true;
    						currentlen = i;
    						output.add(new Token(t, currentString));
    					}

    				}
    			}
    			if (!anyMatchSoFar) {
    				// indicates a blank space: skip to the next position
    				currentlen++;
    			}
    		}
    		ArrayList<Token> Headers = new ArrayList<Token>();
    		for (int i =0; i< output.size(); i++){
    			String str = output.get(i).string;
    			if (str.startsWith("C:") || str.startsWith("K:") 
    	                || str.startsWith("L:") || str.startsWith("M:") 
    	                || str.startsWith("Q:") || str.startsWith("T:") 
    	                || str.startsWith("X:")) { 
    	            Headers.add(output.get(i));	
    	            output.remove(i);
    			}
    			if (str.startsWith("V:")){
    				if (Headers.contains(output.get(i))==false) {
    					Headers.add(output.get(i));
    					output.remove(i);
    				}
    			}
    		}
    		//above method adds all the header info into Headers which is an arraylist of tokens, and remove them from output
    		//make sure V1, V2, etc is only added to header once if they exist 
    		int voicecounter = 0;
    		for (int i =0; i< Headers.size(); i++){
    			String str = output.get(i).string;
    			if (str.startsWith("V:")){
    				voicecounter = voicecounter + 1;
    			}
    		}
    		
    		ArrayList<String> Voicechecker = new ArrayList<String>();
    		for (int i =0; i< voicecounter; i++){
    			ArrayList<Token> i = new ArrayList<Token>();
    		}
    		for (int i =0; i< output.size(); i++){
    			String str = output.get(i).string;
    			if (str.startsWith("V:")) {
    				if (Headers.contains(output.get(i))==false) {
    				Voicechecker.add(str);
    				VoiceString[Headers.indexOf(str)].add(output.get(i));
    				}
    			}
    		}
    		
    		
    		
    		
    		this.Header = Headers;
    		this.tokens = output;  
    	}
    }