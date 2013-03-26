package player;

import java.util.List;

public class NodeTree<E> implements AST<E>{
    private final E value; 
    public NodeTree(E value) { 
        this.value = value; 
    }
    public boolean hasChildren(){ 
        return false; 
    }
    public int numChildren(){ 
        return 0; 
    } 
    public E getValue(){ 
        return value; 
    } 
    public NodeTree<E> getChild(int index){ 
        throw new UnsupportedOperationException(); 
    } 
    public List<AST<E>> getAllChildren(){ 
        throw new UnsupportedOperationException(); 
    } 
    //recover after finishing visitor
    //@Override
    //public List<Note> accept(Visitor<E> visitor, int startTick){ 
    //    return visitor.playLeaf(this, startTick); 
    //} 
}
