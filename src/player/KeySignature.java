package player; 
  
import java.util.HashMap; 
  
public class KeySignature { 
    // hash map mapping string key signature to int[]   
    HashMap<String, int[]> key_signatures = new HashMap<String, int[]>(); 
  
    int[] current_signature; 
    
    /**
     * hashmap from key to int[] showing the accidental changes due to key
     * @param key String in K field in header
     */
    public KeySignature(String key) { 
        assignKeySignatures(); 
        current_signature = key_signatures.get(key); 
    }
    
    // hash map
    // key signatures to the respective signature.
    // integer array [A, B, C, D, E, F, G, z] 
    // 0 is neutral, 1 is sharp, -1 is flat    
    int[] C = { 0, 0, 0, 0, 0, 0, 0, 0}; 
    int[] G = { 0, 0, 0, 0, 0, 1, 0, 0}; 
    int[] D = { 0, 0, 1, 0, 0, 1, 0, 0}; 
    int[] A = { 0, 0, 1, 0, 0, 1, 1, 0}; 
    int[] E = { 0, 0, 1, 1, 0, 1, 1, 0}; 
    int[] B = { 1, 0, 1, 1, 0, 1, 1, 0}; 
    int[] Fsharp = { 1, 0, 1, 1, 1, 1, 1, 0}; 
    int[] Csharp = { 1, 1, 1, 1, 1, 1, 1, 0}; 
  
    int[] F = { 0, -1, 0, 0, 0, 0, 0, 0}; 
    int[] Bflat = { 0, -1, 0, 0, -1, 0, 0, 0}; 
    int[] Eflat = { -1, -1, 0, 0, -1, 0, 0, 0}; 
    int[] Aflat = { -1, -1, 0, -1, -1, 0, 0, 0}; 
    int[] Dflat = { -1, -1, 0, -1, -1, 0, -1, 0}; 
    int[] Gflat = { -1, -1, -1, -1, -1, 0, -1, 0}; 
    int[] Cflat = { -1, -1, -1, -1, -1, -1, -1, 0}; 
    
    /**
     * map from key to its equivalence key class (key signature)
     */
    private void assignKeySignatures() { 
        // sharp signatures 
        key_signatures.put("C", C); 
        key_signatures.put("Am", C); 
        key_signatures.put("G", G); 
        key_signatures.put("Em", G); 
        key_signatures.put("D", D); 
        key_signatures.put("Bm", D); 
        key_signatures.put("A", A); 
        key_signatures.put("^Fm", A); 
        key_signatures.put("E", E); 
        key_signatures.put("^Cm", E); 
        key_signatures.put("B", B); 
        key_signatures.put("^Gm", B); 
        key_signatures.put("^F", Fsharp); 
        key_signatures.put("^Dm", Fsharp); 
  
        // flat signatures 
        key_signatures.put("F", F); 
        key_signatures.put("Dm", F); 
        key_signatures.put("_B", Bflat); 
        key_signatures.put("Gm", Bflat); 
        key_signatures.put("_E", Eflat); 
        key_signatures.put("Cm", Eflat); 
        key_signatures.put("_A", Aflat); 
        key_signatures.put("Fm", Aflat); 
        key_signatures.put("_D", Dflat); 
        key_signatures.put("_Bm", Dflat); 
        key_signatures.put("_G", Gflat); 
        key_signatures.put("_Em", Gflat); 
        key_signatures.put("_C", Cflat); 
  
    } 
  
}