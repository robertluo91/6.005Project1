package player;
import java.util.ArrayList;

import player.Token;
import player.Token.Type;

/**
 * A lexer takes a string and splits it into tokens that are meaningful to a
 * parser. 17 token types: Rest, Pitch, Dupspec, Trispec, Quadspec, Barline, Nrepeat
 * For detailed description of token types, see Token.java
 */
public class Lexer {
    /**
     * Creates the lexer over the passed string.
     * @param string: The string to tokenze.
     */        
    
    
		ArrayList<ArrayList<Token>> MusicBody;
    	ArrayList<Token> MusicHeader;  //you can call Header  from outside to access the arraylist of all the header tokens
    	ArrayList<Token> output;
    	int currentlen;
    	int parserPeekIndex;
    	
    	
    	

    	public Lexer(String string) {

    		// iterate through the input string and identify tokens;
    		// append tokens to the global output list
    		String Expression = new String(string);
    		
    		//********************
    		output = new ArrayList<Token>();
    		//create an arraylist "output" to put all the tokens generated inside
    		int length = Expression.length();
    		//*******************
    		currentlen = 0;
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
    		ArrayList<Token> voicecounter = new ArrayList<Token>();
    		for (int i =0; i< Headers.size(); i++){
    			String str = output.get(i).string;
    			if (str.startsWith("V:")){
    				voicecounter.add(Headers.get(i));
    			}
    		}
    		
    		ArrayList<ArrayList<Token>> Body = new ArrayList<ArrayList<Token>>();
    		//the following checks if there is only 1 voice and voicearray doesn't have anything like V1,etc
    		if (voicecounter.size()==0){
    			ArrayList<Token> VoiceArray = new ArrayList<Token>();
    			for (int i =0; i< output.size(); i++){
    				VoiceArray.add(output.get(i));	
    			}
    			Body.add(VoiceArray);
    		}
    		else{   //when it actually has multiple voice
    		
    			for (int a=0; a <voicecounter.size(); a++){
    				ArrayList<Token> VoiceArray = new ArrayList<Token>();  
    		 		
    				int counter = -1; 
    				for (int i =0; i< output.size(); i++){
    					String str = output.get(i).string;
    					if (str.startsWith("V:")){
    						if (voicecounter.contains(output.get(i))){
    							counter = voicecounter.indexOf(output.get(i));
    							continue;
    						}
    						else {
    							throw new RuntimeException("Voice in the music didn't appear in the header");  //1st error check if the voice in the body of music has appeared in the header 
    						}
    					}
    			
    					if ( counter== a) {
    						VoiceArray.add(output.get(i));	
    					}	 
    				}
    				String str = VoiceArray.get(-1).string; 
    				if (str.matches("\\| | \\|\\] | \\|\\| | :\\|")==false){
    					throw new RuntimeException("The ending of the voice isn't marked with |, |], :|, or ||"); //2nd error check if any of the arraylist of a single voice ends with one of the 3 simbols
    				}
    				Body.add(VoiceArray);
    			}
    		}

    		this.MusicHeader = Headers;
    		this.MusicBody = Body;  
    	}
    	
    	
    	/////////////////////////////   YI   ////////////////////////////////
    	 /**
         * peek at the first token in the token list
         * @return the current first token in the token list
         */
        public Token peek() {
            if (parserPeekIndex >= output.size()) {
                return null;
            }
            return output.get(parserPeekIndex);
        }
        
        /**
         * get the token next to the current index
         * @return the next token to the current index
         * @throws Exception
         */
        public Token next() throws Exception {
            if (parserPeekIndex >= output.size()) {
                throw new Exception("Internal parser error");
            }
            return output.get(parserPeekIndex++);
        }
        
        /**
         * check if the type of the token at parserPeekIndex equals to the expected token type
         * @param t the expected token type
         * @throws Exception
         */
        public void expect(Type t) throws Exception {
            if (parserPeekIndex >= output.size()) {
                throw new Exception("Internal parser error");
            }
            Token peeked = output.get(parserPeekIndex);
            if (peeked.type.equals(t)) {
                parserPeekIndex++;
            } else {
                throw new Exception("Syntax error at token " + peeked + 
                        ", expecting " + t);
            }
        }
        
    }