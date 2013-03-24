package player;

import java.util.ArrayList;
import java.util.List;

public class ParentTree<E> implements AST<E>{
    private final E value; 
    final List<AST<E>> list;
    public ParentTree(E _value, List<AST<E>> _list) { 
        this.value = _value; 
        this.list = _list; 
    }
    public boolean hasChildren(){ 
        return (list.size() > 0); 
    } 
      
    public int numChildren(){ 
        return list.size(); 
    } 
      
    public void addChild(AST<E> abc){ 
        this.list.add(abc); 
    } 
      
    public void addAllChildren(List<AST<E>> l){ 
        this.list.addAll(l); 
    } 
           
    public E getValue(){ 
        return value; 
    } 
    
    public AST<E> getChild(int index){ 
        return list.get(index); 
    } 
      
    public List<AST<E>> getAllChildren(){ 
        return new ArrayList<AST<E>>(list); 
    }
    //recover after finishing visitor
    //@Override
    //public List<Note> accept(Visitor<E> visitor, int startTick){ 
    //    throw new RuntimeException("cannot be implemented"); 
    //}
}
