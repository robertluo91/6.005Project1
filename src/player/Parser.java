package player;

import sound.Pitch; 

import java.util.List;

public class Parser {
    final KeySignature defaultKeySignature; 
    final int tempo;     
    
    public Parser(Lexer lexer) { 
        this.tokenList = lexer.getList(); 
        //default setting
        int tempo = 100; 
        String key = "C"; 

        
        defaultKeySignature = new KeySignature(key); 
        this.tempo = tempo; 
    }
    private static int lcm(int a, int b) { 
        long A = a; 
        long B = b; 
  
        return (int) (A * (B / gcd(A, B))); 
    } 
    
    private static int lcm(List<Integer> input) { 
        int current = input.get(0); 
        for (int i = 1; i < input.size(); i++) 
            current = lcm(current, input.get(i)); 
        return (int) current; 
    }
    
    //since we are dealing with positive integers (the denominators of the a notelength is positive)
    private static long gcd(long a, long b) { 
        while (b > 0) { 
            long exchange = b; 
            b = a % b; 
            a = exchange; 
        } 
        return a; 
    } 
}
