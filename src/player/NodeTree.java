package player;

import java.util.ArrayList;

public class NodeTree implements AST{
    final ArrayList<Token> root;
    final ArrayList<Token> leftChild;
    final ArrayList<Token> rightChild;
    
    /**
     * get the stored info
     * @param node the arraylist of token
     */
    public NodeTree(ArrayList<Token> root){
        this.root = root;
        leftChild = new ArrayList<Token>();
        rightChild = new ArrayList<Token>();
    }
    
    @Override
    public ArrayList<Token> toArrayList() {
        return root;
    }

}
