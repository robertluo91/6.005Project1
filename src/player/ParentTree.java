package player;

import java.util.ArrayList;
/**
 * 
 *
 */
public class ParentTree implements AST{
    final ArrayList<Token> root;
    final ArrayList<Token> leftChild;
    final ArrayList<Token> rightChild;
    /**
     * 
     * @param root
     * @param leftChild
     * @param rightChild
     */
    public ParentTree(ArrayList<Token> root, ArrayList<Token> leftChild, ArrayList<Token> rightChild) { 
        this.root = root;
        this.leftChild = leftChild;
        this.rightChild = rightChild;     
    }
    
    @Override
    public ArrayList<Token> toArrayList() {
        ArrayList<Token> PTREE= new ArrayList<Token>();
        PTREE.addAll(root);
        PTREE.addAll(leftChild);
        PTREE.addAll(root);
        PTREE.addAll(rightChild);
        return PTREE;
    }
}

