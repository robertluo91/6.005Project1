package player;

import java.util.ArrayList;
import java.util.List;

import player.Token.Type;

/**
 * A lexer takes a string and splits it into tokens that are meaningful to a
 * parser. 17 token types: Rest, Pitch, Dupspec, Trispec, Quadspec, Barline,
 * Nrepeat For detailed description of token types, see Token.java
 */
public class Lexer {
	/**
	 * Creates the lexer over the passed string.
	 * 
	 * @param string
	 *            : The string to tokenize.
	 */

	ArrayList<ArrayList<Token>> MusicBody;
	ArrayList<Token> MusicHeader; // you can call Header from outside to access
									// the arraylist of all the header tokens
	String Key;
	ArrayList<Token> voicecounter;
	int Tempo;
	ArrayList<Token> token;
	int currentlen;
	int parserPeekIndex;

	public Lexer(String string) {

		// iterate through the input string and identify tokens;
		// append tokens to the global output list
		String Expression = new String(string);
		ArrayList<Token> output = new ArrayList<Token>();
		// create an arraylist "output" to put all the tokens generated inside
		int length = Expression.length();
		currentlen = 0;
		parserPeekIndex = 0;

		while (currentlen < length) {
			boolean anyMatchSoFar = false;
			for (int i = currentlen + 1; i < length; i++) {
				String currentString = Expression.substring(currentlen, i);
				for (Type t : Token.Type.values()) {
					Token testToken = new Token(t, "", "", 0.0, 0, 0);
					if (testToken.pattern.matcher(currentString).matches()) {
						// a token has been identified because its Matcher
						// matches method == True
						anyMatchSoFar = true;
						currentlen = i;
						output.add(new Token(t, currentString, "0", 0.0, 0, 0));
					}
				}
			}
			if (!anyMatchSoFar) {
				// indicates a blank space: skip to the next position
				currentlen++;
			}
		}
		Header(output);

	}

	public void Header(ArrayList<Token> output) {

		ArrayList<Token> Headers = new ArrayList<Token>();
		for (int i = 0; i < output.size(); i++) {
			String str = output.get(i).string;
			if (str.startsWith("C:") || str.startsWith("K:")
					|| str.startsWith("L:") || str.startsWith("M:")
					|| str.startsWith("Q:") || str.startsWith("T:")
					|| str.startsWith("X:")) {
				Headers.add(output.get(i));
				output.remove(i);
			}
			if (str.startsWith("V:")) {
				if (Headers.contains(output.get(i)) == false) {
					Headers.add(output.get(i));
					output.remove(i);
				}
			}
		}
		this.MusicHeader = Headers;

		KeyTempo(Headers);
		Ticker(output);
		// above method adds all the header info into Headers which is an
		// arraylist of tokens, and remove them from output
		// make sure V1, V2, etc is only added to header once if they exist
	}

	public void KeyTempo(ArrayList<Token> Headers) {
		ArrayList<Token> voicecounter = new ArrayList<Token>();
		for (int i = 0; i < Headers.size(); i++) {
			int keychecker = 0;
			int Tempochecker = 0;
			String str = Headers.get(i).string;
			if (str.startsWith("V:")) {
				voicecounter.add(Headers.get(i));
			}
			if (str.startsWith("K:")) {
				keychecker = 1;
				str = str.substring(2);
				if (str.startsWith(" ")) {
					str = str.substring(1);
				}
				this.Key = str;
			}
			if (str.startsWith("Q:")) {
				Tempochecker = 1;
				str = str.substring(2);
				if (str.startsWith(" ")) {
					str = str.substring(1);
				}
				this.Tempo = Integer.parseInt(str);
			}
			if (keychecker == 0) {
				this.Key = "C"; // default Key
			}
			if (Tempochecker == 0) {
				this.Tempo = 100; // default Tempo
			}

		}
		this.voicecounter = voicecounter;
	}

	// Ticker method changes all the note-length into the actual tick time. also
	// changes the noteLength, basenote, octave, accid fields for Rest and Pitch
	// types
	public void Ticker(ArrayList<Token> output) {

		ArrayList<Integer> Denom = new ArrayList<Integer>();
		for (int i = 0; i < output.size(); i++) {
			String str = output.get(i).string;
			if (str.matches("z")) {
				str.concat("1/1");
				output.get(i).basenote = "z";
				output.get(i).noteLength = 1.0;
			} else if (str.matches("z /")) {
				str.replaceAll("/", "1/2");
				output.get(i).basenote = "z";
				output.get(i).noteLength = 0.5;
				Denom.add(2);
			} else if (str.matches("z /d+")) {

				str.replaceAll("/", "1/");
				int denom = Integer.parseInt(str.substring(str.indexOf("/") + 1));
				output.get(i).basenote = "z";
				output.get(i).noteLength = 1.0 / denom;
				Denom.add(denom);
			} else if (str.matches("z d+/d+")) {
				for (int begin = 0; begin < str.length(); begin++) {
					String substring = str.substring(begin);
					if (substring.matches("d+/d+")) {
						int num = Integer.parseInt(substring.substring(0,substring.indexOf("/")));
						int denom = Integer.parseInt(substring.substring(substring.indexOf("/")) + 1);
						long gcd = gcd((long) num, (long) denom);
						num = num / (int) gcd;
						denom = denom / (int) gcd;
						str.replaceAll("d+/d+", Integer.toString(num) + "/"+ Integer.toString(denom));
						output.get(i).basenote = "z";
						output.get(i).noteLength = num * 1.0 / denom;
						Denom.add(denom);
					}
				}
			} else if (output.get(i).type == Token.Type.Pitch) {
				for (int begin = 0; begin < str.length(); begin++) {

					if (str.substring(begin, begin + 1).matches("[A-G]")) {
						output.get(i).basenote = str.substring(begin, begin + 1);
					}
					if (str.substring(begin, begin + 1).matches("[a-g]")) {
						output.get(i).basenote = str.substring(begin, begin + 1).toUpperCase();
						output.get(i).octave = output.get(i).octave + 1;
					}
					if (str.substring(begin, begin + 1) == "^") {
						output.get(i).accid = output.get(i).accid + 1;
					}
					if (str.substring(begin, begin + 1) == "_") {
						output.get(i).accid = output.get(i).accid - 1;
					}

					if (str.substring(begin, begin + 1) == "'") {
						output.get(i).octave = output.get(i).octave + 1;
					}
					if (str.substring(begin, begin + 1) == ",") {
						output.get(i).octave = output.get(i).octave - 1;
					}
				}
				if (str.matches("[\\^ | \\^\\^ | _ | __ | =]? [A-Ga-g] ['+ ,+]? /")) {
					str.replaceAll("/", "1/2");
					output.get(i).noteLength = 0.5;
					Denom.add(2);
				} else if (str.matches("[\\^ | \\^\\^ | _ | __ | =]? [A-Ga-g] ['+ ,+]? /d+")) {
					str.replaceAll("/", "1/");
					int denom = Integer.parseInt(str.substring(str.indexOf("/") + 1));
					output.get(i).noteLength = 1.0 / denom;
					Denom.add(denom);
				} else if (str.matches("[\\^ | \\^\\^ | _ | __ | =]? [A-Ga-g] ['+ ,+]?")) {
					str.concat("1/1");
					output.get(i).noteLength = 1.0;
					Denom.add(1);
				} else if (str.matches("[\\^ | \\^\\^ | _ | __ | =]? [A-Ga-g] ['+ ,+]? d+/d+")) {
					for (int begin = 0; begin < str.length(); begin++) {

						String substring = str.substring(begin);
						if (substring.matches("d+/d+")) {
							int num = Integer.parseInt(substring.substring(0,substring.indexOf("/")));
							int denom = Integer.parseInt(substring.substring(substring.indexOf("/")) + 1);
							long gcd = gcd((long) num, (long) denom);
							num = num / (int) gcd;
							denom = denom / (int) gcd;
							str.replaceAll("d+/d+", Integer.toString(num) + "/"
									+ Integer.toString(denom));
							output.get(i).noteLength = num * 1.0 / denom;
							Denom.add(denom);
						}
					}
				}
			}
		}
		int Tick = lcmlist(Denom);
		for (int i2 = 0; i2 < output.size(); i2++) {
			if (output.get(i2).type==Token.Type.Pitch) {
				output.get(i2).noteLength = output.get(i2).noteLength*Tick;
			} else if (output.get(i2).type==Token.Type.Rest) {
				output.get(i2).noteLength = output.get(i2).noteLength*Tick;
			}
		}
		this.token = output;
		MusicBody(token, voicecounter);
	}


	public void MusicBody(ArrayList<Token> output, ArrayList<Token> voicecounter) {
		ArrayList<ArrayList<Token>> Body = new ArrayList<ArrayList<Token>>();

		if (voicecounter.size() == 0) { // only has 1 voice
			ArrayList<Token> VoiceArray = new ArrayList<Token>();
			for (int i = 0; i < output.size(); i++) {
				VoiceArray.add(output.get(i));
			}
			Body.add(VoiceArray);
		} else { // >1 voice

			for (int a = 0; a < voicecounter.size(); a++) {
				ArrayList<Token> VoiceArray = new ArrayList<Token>();

				int counter = -1;
				for (int i = 0; i < output.size(); i++) {
					String str = output.get(i).string;
					if (str.startsWith("V:")) {
						if (voicecounter.contains(output.get(i))) {
							counter = voicecounter.indexOf(output.get(i));
							continue;
						} else {
							throw new RuntimeException(
									"Voice in the music didn't appear in the header"); 
						} //1st error check to see if V in the body also appeared in the header
					}

					if (counter == a) {
						VoiceArray.add(output.get(i));
					}
				}
				String str = VoiceArray.get(-1).string;
				if (str.matches("\\| | \\|\\] | \\|\\| | :\\|") == false) {
					throw new RuntimeException(
							"The ending of the voice isn't marked with |, |], :|, or ||"); 
				} //2nd error check for ending
				Body.add(VoiceArray);
			}
		}
		this.MusicBody = Body;

	}

	private static int lcm(int a, int b) {
		long A = a;
		long B = b;
		return (int) (A * (B / gcd(A, B)));
	}

	private static int lcmlist(List<Integer> input) {
		int current = input.get(0);
		for (int i = 1; i < input.size(); i++)
			current = lcm(current, input.get(i));
		return (int) current;
	}

	// since we are dealing with positive integers (the denominators of the a
	// notelength is positive)
	private static long gcd(long a, long b) {
		while (b > 0) {
			long exchange = b;
			b = a % b;
			a = exchange;
		}
		return a;
	}

	// helper methods for test
	/**
	 * peek at the first token in the token list
	 * 
	 * @return the current first token in the token list
	 */
	public Token peek() {
		if (parserPeekIndex >= token.size()) {
			return null;
		}
		return token.get(parserPeekIndex);
	}

	/**
	 * get the token next to the current index
	 * 
	 * @return the next token to the current index
	 * @throws Exception
	 */
	public Token next() throws Exception {
		if (parserPeekIndex >= token.size()) {
			throw new Exception("Internal parser error");
		}
		return token.get(parserPeekIndex++);
	}

	/**
	 * check if the type of the token at parserPeekIndex equals to the expected
	 * token type
	 * 
	 * @param t
	 *            the expected token type
	 * @throws Exception
	 */
	public void expect(Type t) throws Exception {
		if (parserPeekIndex >= token.size()) {
			throw new Exception("Internal parser error");
		}
		Token peeked = token.get(parserPeekIndex);
		if (peeked.type.equals(t)) {
			parserPeekIndex++;
		} else {
			throw new Exception("Syntax error at token " + peeked
					+ ", expecting " + t);
		}
	}
}