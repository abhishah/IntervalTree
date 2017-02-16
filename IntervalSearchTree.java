public class IntervalSearchTree {

    private IntervalNode root;

    private class IntervalNode {
        IntervalNode left; 
        int start;
        int end;
        int maxEnd;
        IntervalNode right;

        public IntervalNode(int start,int end){
            this.start = start;
            this.end = end;
        }

        public IntervalNode(IntervalNode left, int start, int end, int maxEnd, IntervalNode right) {
            this.left = left;
            this.start = start;
            this.end = end;
            this.maxEnd = maxEnd;
            this.right = right;
        }
    }

    /**
     * Adds an interval to the the calendar
     * 
     * @param start the start of interval
     * @param end   the end of the interval.
     */
    public void add (int start, int end) {
        if (start >= end) throw new IllegalArgumentException("The end " + end + " should be greater than start " + start);

        IntervalNode inode = root;
        while (inode != null) {
            inode.maxEnd = (end > inode.maxEnd) ? end : inode.maxEnd;
            if (start < inode.start) {
                if (inode.left == null) {
                    inode.left = new IntervalNode(null, start, end, end, null);
                    return;
                }
                inode = inode.left;
            } else {
                if (inode.right == null) {
                    inode.right = new IntervalNode(null, start, end, end, null);
                    return;
                }
                inode = inode.right;
            }
        }
        root =  new IntervalNode(null, start, end, end, null);
    }

    /**
     * Returns if there is an overlap in the two intervals
     * Two intervals such that one of the points coincide:
     * eg: [10, 20] and [20, 40] are NOT considered as overlapping.
     */    
    public IntervalNode overlapSearch(int start, int end) {
        if (start >= end) throw new IllegalArgumentException("The end " + end + " should be greater than start " + start);

        IntervalNode intervalNode = root;

        while (intervalNode != null) {
            if (intersection(start, end, intervalNode.start, intervalNode.end)) return intervalNode;

            if (goLeft(start, end, intervalNode.left)) {
                intervalNode = intervalNode.left;
            } else {
                intervalNode = intervalNode.right;
            }
        }
        return null;
    }


    public void delete(int start,int end){
        IntervalNode node = new IntervalNode(start,end);
        deleteNode(root,node);
    }


     public IntervalNode deleteNode(IntervalNode roots,IntervalNode node) {  
    
      if (roots == null)  
       return null;  
      if (roots.start > node.start) {  
       roots.left = deleteNode(roots.left,node);  
      } else if (roots.start < node.start) {  
       roots.right = deleteNode(roots.right, node);  
      
      } else {  
       // if nodeToBeDeleted have both children  
       if (roots.left != null && roots.right != null) {  
        IntervalNode temp = roots;  
        // Finding minimum element from right  
        IntervalNode minNodeForRight = minimumNode(temp.right);  
        // Replacing current node with minimum node from right subtree  
        roots.start = minNodeForRight.start;
        roots.end = minNodeForRight.end;  
        // Deleting minimum node from right now  
        roots.right = deleteNode(roots.right, new IntervalNode(minNodeForRight.start,minNodeForRight.end));  
       }  
       // if nodeToBeDeleted has only left child  
       else if (roots.left != null) {  
        roots = roots.left;  
       }  
       // if nodeToBeDeleted has only right child  
       else if (roots.right != null) { 
        roots = roots.right;  
       }  
       // if nodeToBeDeleted do not have child (Leaf node)  
       else  
        roots = null;  
      }
      if(roots!= null && roots.left == null && roots.right == null) roots.maxEnd = roots.end;
      if(roots!= null && roots.left != null) roots.maxEnd = Math.max(roots.end,roots.left.maxEnd);
      if(roots !=null && roots.right != null) roots.maxEnd = Math.max(roots.end,roots.right.maxEnd);
      
      return roots;  
     }  

    /**
     * Outputs Preorder of the Interval Tree
     */
    public void preorder(IntervalNode node){
        if(node == null) return;
        System.out.println("["+node.start+" ,"+node.end+"] "+ node.maxEnd);
        preorder(node.left);
        preorder(node.right);
    }

    /**
     * Find the minimum start value node in the tree
     */
    private IntervalNode minimumNode(IntervalNode n) {

        while(n.left != null){
            n = n.left;
        }

        return n;
    }


    /**
     * Returns if there is an intersection in the two intervals
     * Two intervals such that one of the points coincide:
     * eg: [10, 20] and [20, 40] are NOT considered as intersecting.
     */
    private boolean intersection (int start, int end, int intervalStart, int intervalEnd) {
        return start < intervalEnd && end > intervalStart;
    }

    /**
     * Returns true is interval lies in left
     */
    private boolean goLeft(int start, int end, IntervalNode intervalLeftSubtree) {
        return intervalLeftSubtree != null && intervalLeftSubtree.maxEnd > start;
    }


    public static void main(String args[]){
        IntervalSearchTree intervalSearchTree = new IntervalSearchTree();
        intervalSearchTree.add(15, 20);
        intervalSearchTree.add(10, 30);
        intervalSearchTree.add(17, 19);
        intervalSearchTree.add(5, 20);
        intervalSearchTree.add(12, 15);
        intervalSearchTree.add(30, 40);

        System.out.println("Preorder:");
        intervalSearchTree.preorder(intervalSearchTree.root);
        
        IntervalNode n = intervalSearchTree.overlapSearch(14,16);
        System.out.println("Overlap Match "+n.start+", "+n.end);

        n = intervalSearchTree.overlapSearch(21,23);
        System.out.println("Overlap Match "+n.start+", "+n.end);

        intervalSearchTree.delete(30,40);
        intervalSearchTree.delete(17,19);
        System.out.println("Preorder:");
        intervalSearchTree.preorder(intervalSearchTree.root); 
    }

}