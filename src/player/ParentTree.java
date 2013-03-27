package player;

import java.util.ArrayList;
import java.util.List;

public class ParentTree<E> implements AST<E>{
    final ArrayList<Token> root;
    final ArrayList<Token> leftChild;
    final ArrayList<Token> rightChild;
    
    public ParentTree(ArrayList<Token> root, ArrayList<Token> leftChild, ArrayList<Token> rightChild) { 
        this.root = root;
        this.leftChild = leftChild;
        this.rightChild = rightChild;     
    }

}
