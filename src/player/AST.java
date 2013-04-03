package player;

import java.util.ArrayList;

/**
 * abstract syntax tree, implemented by NodeTree and ParentTree
 */
public interface AST {

    ArrayList<Token> root = null;
    ArrayList<Token>leftChild = null;
    ArrayList<Token> rightChild = null;
            
    public ArrayList<Token> toArrayList();
}
