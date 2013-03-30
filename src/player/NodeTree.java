package player;

import java.util.ArrayList;


public class NodeTree<E> implements AST<E>{

public class NodeTree implements AST{
    final ArrayList<Token> node;
    
    public NodeTree(ArrayList<Token> node){
        this.node = node;
    }
    
    @Override
    public ArrayList<Token> toArrayList() {
        return node;
    }

}
