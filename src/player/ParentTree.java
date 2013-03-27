package player;

import java.util.List;

public class ParentTree<E> implements AST<E>{
    final NodeTree middle;
    final NodeTree left;
    final NodeTree right;
        
    public ParentTree(NodeTree middle, NodeTree left, NodeTree right) throws RuntimeException{
        NodeTree currentMiddle = null;
        NodeTree currentLeft = null;
        NodeTree currentRight = null;
        this.middle = current;
        this.left = left;
        this.right = right;
    }
 
    public <R> R accept(Visitor<R> v) {
        return v.on(this);
    }
    

}

