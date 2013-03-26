package player; 
  
import java.util.HashMap; 
  
public class KeySignature { 
  
    // Need to design hash map to map 
    // key signatures to the respective signature.\ 
    // integer array [A, B, C, D, E, F, G] 
    // 0 is neutral 
    // 1 is sharp 
    // -1 is flat 
    int[] C = { 0, 0, 0, 0, 0, 0, 0 }; 
    int[] G = { 0, 0, 0, 0, 0, 1, 0 }; 
    int[] D = { 0, 0, 1, 0, 0, 1, 0 }; 
    int[] A = { 0, 0, 1, 0, 0, 1, 1 }; 
    int[] E = { 0, 0, 1, 1, 0, 1, 1 }; 
    int[] B = { 1, 0, 1, 1, 0, 1, 1 }; 
    int[] Fsharp = { 1, 0, 1, 1, 1, 1, 1 }; 
    int[] Csharp = { 1, 1, 1, 1, 1, 1, 1 }; 
  
    int[] F = { 0, -1, 0, 0, 0, 0, 0 }; 
    int[] Bflat = { 0, -1, 0, 0, -1, 0, 0 }; 
    int[] Eflat = { -1, -1, 0, 0, -1, 0, 0 }; 
    int[] Aflat = { -1, -1, 0, -1, -1, 0, 0 }; 
    int[] Dflat = { -1, -1, 0, -1, -1, 0, -1 }; 
    int[] Gflat = { -1, -1, -1, -1, -1, 0, -1 }; 
    int[] Cflat = { -1, -1, -1, -1, -1, -1, -1 }; 
  
    // hash map mapping string key signatures to int[] 
    // always capitalized, also utilizing ^ for sharp and _ for flat 
  
    HashMap<String, int[]> key_signatures = new HashMap<String, int[]>(); 
  
    int[] current_signature; 
  
    /** 
     */
    public KeySignature(String basenote) { 
        assignKeySignatures(); 
        String key = keyParse(basenote); 
        current_signature = key_signatures.get(key); 
    } 
  
    /** 
     *  
     */
    public int[] getSignature() { 
        return current_signature.clone(); 
    } 
  
    /** 
     *  
     */
    private String keyParse(String basenote) { 
        String answer = basenote.trim(); 
        return answer; 
    } 
  
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