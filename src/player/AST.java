package player;

import java.util.ArrayList;

/**
 * abstract syntax tree, implemented by NodeTree and ParentTree
 */
public interface AST {
    public ArrayList<Token> toArrayList();
}
