package player;

import java.util.List; 

public interface AST<E> {
    public boolean hasChildren(); 
    
    public int numChildren(); 
      
    public E getValue(); 
      
    public AST<E> getChild(int index); 
      
    public List<AST<E>> getAllChildren(); 
    
    //recover after finishing visitor
    //public List<Note> accept(Visitor<E> visitor, int startTick); 
}
