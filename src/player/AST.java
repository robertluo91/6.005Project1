package player;

import java.util.List; 

public interface AST<E>{
    public interface Visitor<R> {
        public R on(ParentTree p);
 
        public R on(NodeTree n);
    }

    //public String toString();
 
    public <R> R accept(Visitor<R> v);
    
    //recover after finishing visitor
    //public List<Note> accept(Visitor<E> visitor, int startTick); 
}
