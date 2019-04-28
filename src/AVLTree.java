import jdk.nashorn.internal.runtime.ECMAException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {
    AVLNode root;
    AVLNode max;
    AVLNode min;

    static final int KEY_EXISTS = -1;

    AVLTree() {
        root = new AVLNode(null);
        root.setSubtreeSize(0);
        max = root;
        min = root;
    }

    /**
     * TODO Don't forget to delete this!
     * Prints the tree, starting with the root.
     */
    public String toString()
    {
        if (empty())
            return "<empty tree>";
        else
            return root.print();
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return root.key == AVLNode.VIRTUAL_NODE;
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        IAVLNode itr = find(k);
        return itr.getKey() == k ? itr.getValue() : null;
    }
    //TODO this is a temporary function used for testing - delete it later
    public AVLNode searchNode(int k){
        return (AVLNode)find(k);
    };
    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */

    /**assumes tree is not empty. returns node with key if exists or its would-be parent**/
    private IAVLNode find(int key) {
        return find(key, root);
    }

    private IAVLNode find(int key, AVLNode root) {
        IAVLNode itr = root;
        IAVLNode prev = null;
        while (itr.isRealNode()) {
            if (itr.getKey() == key) {
                return itr;
            }
            prev = itr;
            itr = itr.getKey() > key ? itr.getLeft() : itr.getRight();
        }
        return prev;
    }

    public int insert(int k, String i) {
        if (empty()) {
            root = new AVLNode(k, i, null);
            max = root;
            min = root;
            return 0;
        }
        AVLNode itr = (AVLNode) find(k);
        if (itr.getKey() == k) {
            return KEY_EXISTS;
        }
        AVLNode newNode = new AVLNode(k, i, itr);
        insertChild(itr, newNode);
        if(k < this.min.key)
            this.min = newNode;
        if(k > this.max.key)
            this.max = newNode;
        return fixUpward(newNode);
    }

    private void insertChild(AVLNode parent, AVLNode newNode)
    {
        if (parent.getKey() > newNode.getKey()) {
            setLeftChild(newNode, parent.left);
            setLeftChild(parent, newNode);
        }else{
            setRightChild(newNode, parent.right);
            setRightChild(parent, newNode);
        }
        newNode.update();
    }

    private void setRightChild(AVLNode node, AVLNode right)
    {
        node.right = right;
        right.parent = node;
    }

    private void setLeftChild(AVLNode node, AVLNode left)
    {
        node.left = left;
        left.parent = node;
    }

    private void giveParent(AVLNode from, AVLNode to) {
        to.parent = from.parent;
        if (to.parent != null) {
            if (to.parent.left == from) {
                to.parent.left = to;
            } else {
                to.parent.right = to;
            }
        }
    }

    private void rotateRight(AVLNode node)
    {
        AVLNode middle = node.left;
        root = root == node ? middle : root;
        setLeftChild(node, middle.right);
        giveParent(node, middle);
        setRightChild(middle, node);
        node.update();
        middle.update();
    }

    private void rotateLeft(AVLNode node)
    {
        AVLNode middle = node.right;
        root = root == node? middle : root;
        setRightChild(node, middle.left);
        giveParent(node, middle);
        setLeftChild(middle, node);
        node.update();
        middle.update();
    }

    private void rotateLL(AVLNode node)
    {
        rotateRight(node);
    }

    private void rotateLR(AVLNode node)
    {
        rotateLeft(node.left);
        rotateRight(node);
    }

    private void rotateRL(AVLNode node)
    {
        rotateRight(node.right);
        rotateLeft(node);
    }

    private void rotateRR(AVLNode node)
    {
        rotateLeft(node);
    }
    /**return value is redundant right now.**/
    private int Balance(AVLNode node) {
        int bf = calcBF(node);
        if(bf == 2)
        {
            if(calcBF(node.left) == -1)
            {
                rotateLR(node);
            }else {
                rotateLL(node);
            }
        }else{
            if(calcBF(node.right) == 1)
            {
                rotateRL(node);
            }else {
                rotateRR(node);
            }
        }
        return 1;
    }

    public int calcBF(AVLNode node)
    {
        if(!node.isRealNode())
        {
            return 0;
        }
        return node.left.height - node.right.height;
    }

    private int fixUpward(AVLNode newNode) {
        AVLNode itr = newNode;
        int bf;
        int rebalanceCount = 0;
        while(itr != null)
        {
            AVLNode temp = itr.parent;
            itr.update();
            bf = calcBF(itr);
            if(bf > 1 || bf < -1)
            {
                rebalanceCount++;
                Balance(itr);
            }
            itr = temp;
        }
        return rebalanceCount;
    }

    private void updateMinMax(boolean changeMin)
    {
        AVLNode itr = root;
        AVLNode next;
        do
        {
            next = (changeMin)? itr.left : itr.right;
        }while(next.isRealNode());
        if(changeMin)
            min = itr;
        else
            max = itr;
    }
    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        if(size() == 0)
        {
            return -1;
        }
        boolean isMin = false;
        boolean minOrMax = false;
        AVLNode nodeToDelete;
        if(k == min.key || k == max.key)
        {
            isMin = k == min.key;
            nodeToDelete = (isMin)? min : max;
            minOrMax = true;
        }else{
            nodeToDelete = (AVLNode)find(k);
        }
        if(nodeToDelete.getKey() != k)
            return -1;
        int res = delete(nodeToDelete);
        if(minOrMax)
            updateMinMax(isMin);
        return res;
    }

    private int delete(AVLNode node){
        AVLNode rebalanceFrom;
        if(node.right.key == AVLNode.VIRTUAL_NODE && node.left.key == AVLNode.VIRTUAL_NODE) {
            rebalanceFrom = deleteLeaf(node);
        }else if(node.right.key == AVLNode.VIRTUAL_NODE || node.left.key == AVLNode.VIRTUAL_NODE) {
            if(node == root)
            {
                root = node.right.isRealNode()? node.right : node.left;
                root.parent = null;
                return 0;
            }
            rebalanceFrom = deleteSingleChildNode(node);
        } else {
            rebalanceFrom = deleteDoubleChildNode(node);
        }
        return fixUpward(rebalanceFrom);
    }

    /**
     * deletes a node, under the assumption that it is a leaf
     * @param node
     * @return the parent of the node that was deleted
     * NOTE: this function could be static, and in fact should be, but for that to be the case, AVLNode has to be static as well.
     * AVLNode should be static (it never uses an instance function of AVLTree), but we wanted to stick with the skeleton file.
     */
    private AVLNode deleteLeaf(AVLNode node)
    {
        AVLNode parentNode = node.parent;
        //if case of attempted deletion from an empty tree, return null
        if(parentNode == null)
            return null;
        if(parentNode.left == node)
            parentNode.left = new AVLNode(parentNode);
        else
            parentNode.right = new AVLNode(parentNode);
        return  parentNode;
    }

    /**
     * deletes a node, under the assumption that it only has a single child
     * @param node
     * @return the parent of the node that was deleted
     * NOTE: this function could be static, and in fact should be, but for that to be the case, AVLNode has to be static as well.
     * AVLNode should be static (it never uses an instance function of AVLTree), but we wanted to stick with the skeleton file.
     */
    private AVLNode deleteSingleChildNode(AVLNode node)
    {
        AVLNode parentNode = node.parent;
        AVLNode childNode = node.left.key != AVLNode.VIRTUAL_NODE ? node.left : node.right;
        if(parentNode.left == node)
            parentNode.left = childNode;
        else
            parentNode.right = childNode;
        childNode.parent = parentNode;
        return  parentNode;
    }

    /**
     * deletes a node, under the assumption that it has two children
     * @param node
     * @return the parent of the node that was deleted
     * NOTE: this function could be static, and in fact should be, but for that to be the case, AVLNode has to be static as well.
     * AVLNode should be static (it never uses an instance function of AVLTree), but we wanted to stick with the skeleton file.
     */
    private AVLNode deleteDoubleChildNode(AVLNode node)
    {
        AVLNode successor = successor(node);
        delete(successor);
        node.key = successor.key;
        node.value = successor.value;
        return successor.parent;
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root AVL node, or null if the tree is empty
     * <p>
     * precondition: none
     * postcondition: none
     */
    public IAVLNode getRoot() {
        return root;
    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        return min.value;
    }
    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        return max.value;
    }

    /**
     * @param node - the root of the subtree
     * @return the node with the minimal key in the subtree originating from the input node
     * returns the node if given a virtual node
     */
    private static AVLNode minInSubtree(AVLNode node){
        if(node == null)
            return null;
        if(node.getKey() == AVLNode.VIRTUAL_NODE)
            return node;
        AVLNode curr = node;
        while(curr.left.getKey() != AVLNode.VIRTUAL_NODE)
            curr = curr.left;
        return curr;
    }
    /**
     * @param node - the root of the subtree
     * @return the node with the maximal key in the subtree originating from the input node
     * returns the node if given a virtual node
     */
    private static AVLNode maxInSubtree(AVLNode node){
        if(node == null)
            return null;
        if(node.getKey() == AVLNode.VIRTUAL_NODE)
            return node;
        AVLNode curr = node;
        while(curr.right.getKey() != AVLNode.VIRTUAL_NODE)
            curr = curr.right;
        return curr;
    }
    public static AVLNode successor(AVLNode node) {
        if(node.right.getKey() != AVLNode.VIRTUAL_NODE)
            return AVLTree.minInSubtree(node.right);
        //if the node doesn't have right child, search upwards, using two nodes
        //one node would suffice in theory, the decision to use two has to do with code clarity
        AVLNode tempNode = node;
        AVLNode tempParent = tempNode.parent;
        //ascend left-wards
        while(tempParent != null && tempParent.right == tempNode){
            tempNode = tempParent;
            tempParent = tempNode.parent;
        }
        return tempParent;

    }
    public static AVLNode predecesoor(AVLNode node) {
        if(node.left.getKey() != AVLNode.VIRTUAL_NODE)
            return AVLTree.maxInSubtree(node.left);
        //if the node doesn't have right child, search upwards, using two nodes
        //one node would suffice in theory, the decision to use two has to do with code clarity
        AVLNode tempNode = node;
        AVLNode tempParent = tempNode.parent;
        //ascend left-wards
        while(tempParent != null && tempParent.left == tempNode){
            tempNode = tempParent;
            tempParent = tempNode.parent;
        }
        return tempParent;

    }

    /**
     * adds an object to an array in the first available position
     * @param arr - a non-full array
     * @param o - the object to be added
     */
    private void addToArrayAtFirstNull(Object[] arr, Object o) {
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == null) {
                arr[i] = o;
                return;
            }
        }
        //throw new Exception("the array is already full");
    }
    private AVLNode[] inOrderScan(){
        return inOrderScanRec(root, new AVLNode[this.size()]);
    }

    /**
     * recuresibely iterates over the nodes in the subtree (int-order) beginning in the supplied node, and adding them to an array
     * @param node - the node to scan from
     * @param scannedSoFar - an array of all nodes scanned so far
     * @return
     */
    private AVLNode[] inOrderScanRec(AVLNode node, AVLNode[] scannedSoFar){
        if(node.key == AVLNode.VIRTUAL_NODE)
            return scannedSoFar;
        inOrderScanRec(node.left, scannedSoFar);
        addToArrayAtFirstNull(scannedSoFar, node);
        inOrderScanRec(node.right, scannedSoFar);
        return scannedSoFar;
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        //this is the correct way to extract a single field from an array of objects in Java, but we weren't sure if it would be allowed.
        //return Arrays.stream(this.inOrderScan()).mapToInt(AVLNode::getKey).toArray();
        AVLNode[] inOrder = this.inOrderScan();
        int[] keys = new int[inOrder.length];
        for(int i = 0; i < inOrder.length; i++)
            keys[i] = inOrder[i].getKey();
        return keys;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        //this is the correct way to extract a single field from an array of objects in Java, but we weren't sure if it would be allowed.
        //return Arrays.stream(this.inOrderScan()).map(AVLNode::getValue).toArray(String[]::new);
        AVLNode[] inOrder = this.inOrderScan();
        String[] info = new String[inOrder.length];
        for(int i = 0; i < inOrder.length; i++)
            info[i] = inOrder[i].getValue();
        return info;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * <p>
     * precondition: none
     * postcondition: none
     */
    public int size() {
        return root.size;
    }

    //finds the i-th smallest item in the given subtree
    public String select(int i, AVLNode node) {
        if(i == node.lSize() + 1)
            return node.value;
        return (i <= node.lSize())? select(i, node.left) : select(i - node.lSize() - 1, node.right);
    }

    /**
     * public string select(int i)
     * <p>
     * Returns the value of the i'th smallest key (return null if tree is empty)
     * Example 1: select(1) returns the value of the node with minimal key
     * Example 2: select(size()) returns the value of the node with maximal key
     * Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor
     * <p>
     * precondition: size() >= i > 0
     * postcondition: none
     */

    public String select(int i) {
        if (size() == 0)
            return null;
        if(i > root.lSize())
        {
            return select(i, root);
        }
        AVLNode itr = min;
        while(i > itr.lSize() + 1)
        {
            itr = itr.parent;
        }
        return select(i, itr);
    }

    public AVLNode subtreeRoot(int k) {
        AVLNode itr;
        if (k >= root.key) {
            itr = root;
            while (itr.key < k) {
                itr = itr.right;
            }
        } else {
            itr = min;
            while (itr.key < k) {
                itr = itr.parent;
            }
            itr = (itr.key > k)? itr.left : itr;
        }
        return itr;
    }

/**    public String select(int i) {
        if (size() == 0)
            return null;
        int curRank = 0;
        AVLNode itr = min;
        if(i < root.rSize() + 1)
        {
            while(i < itr.rSize() + 1)
            {
                itr = itr.parent;
            }
        }else{
            itr = root;
        }
        curRank = itr.rSize() + 1;
        while(true)
        {
            if(curRank + itr.rSize() < i) {
                itr = itr.right;
            }else{
                curRank += itr.rSize();
                if(curRank == i)
                {
                    return itr.value;
                }
                itr = itr.left;
            }
        }
    }
**/

    /**
     * public int less(int i)
     * <p>
     * Returns the sum of all keys which are less or equal to i
     * i is not neccessarily a key in the tree
     * <p>
     * precondition: none
     * postcondition: none
     */
    public int less(int i) {
        if (size() == 0 || i < min.key) {
            return 0;
        }
        if(i == min.key)
        {
            return i;
        }
        if (i >= max.key) {
            return root.sumSubtreeKeys;
        }
        AVLNode subRoot = subtreeRoot(i);
        int sum = i >= root.key ? root.sumSubtreeKeys - subRoot.sumSubtreeKeys : 0;
        AVLNode itr = subRoot;
        while((i > itr.key && itr.right.isRealNode()) || (i < itr.key && itr.left.isRealNode()))
        {
            if(itr.key < i)
            {
                sum += itr.left.sumSubtreeKeys;
                sum = itr.key < i? sum + itr.key : sum;
                itr = itr.right;
            }else{
                itr = itr.left;
            }
        }
        if(itr.key <= i) {
            return sum + itr.sumSubtreeKeys - itr.right.sumSubtreeKeys;
        }else{
            return sum + itr.left.sumSubtreeKeys;
        }
    }
  /**        boolean added = insert(i, "") != -1;
        int sum = 0;
        AVLNode itr = root;
        while(itr.key != i)
        {
            if(itr.key > i)
            {
                sum += itr.right.sumSubtreeKeys;
                itr = itr.left;
            }else{
                itr = itr.right;
            }
        }
        sum += itr.right.sumSubtreeKeys + i;
        if(added)
        {
            delete(itr);
        }
        return sum;
    }**/

    /**used in getArray**/
    public void getArrayRec(AVLNode node, ArrayList<AVLNode> lst)
    {
        if(!node.isRealNode())
        {
            return;
        }
        getArrayRec(node.left, lst);
        lst.add(node);
        getArrayRec(node.right, lst);
    }

    /**get array with all nodes, in order. for debugging.**/
    public ArrayList<AVLNode> getArray()
    {
        ArrayList<AVLNode> arr = new ArrayList<>();
        getArrayRec(root, arr);
        return arr;
    }

    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode {
        public int getKey(); //returns node's key (for virtuval node return -1)

        public String getValue(); //returns node's value [info] (for virtuval node return null)

        public void setLeft(IAVLNode node); //sets left child

        public IAVLNode getLeft(); //returns left child (if there is no left child return null)

        public void setRight(IAVLNode node); //sets right child

        public IAVLNode getRight(); //returns right child (if there is no right child return null)

        public void setParent(IAVLNode node); //sets parent

        public IAVLNode getParent(); //returns the parent (if there is no parent return null)

        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

        public void setSubtreeSize(int size); // sets the number of real nodes in this node's subtree

        public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))

        public void setHeight(int height); // sets the height of the node

        public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    }

    /**
     * public class AVLNode
     * <p>
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in
     * another file.
     * This class can and must be modified.
     * (It must implement IAVLNode)
     */
    public class AVLNode implements IAVLNode {
        static final int VIRTUAL_NODE = -1;
        static final int VIRTUAL_HEIGHT = -1;
        static final int LEAF_HEIGHT = 0;


        int key;
        String value;
        int height; //need to update parents
        int size; //need to update parents
        AVLNode right;
        AVLNode left;
        AVLNode parent;
        int sumSubtreeKeys; //need to update parents

        public AVLNode(AVLNode parent) {
            key = VIRTUAL_NODE;
            height = VIRTUAL_HEIGHT;
            this.parent = parent;
        }

        public AVLNode(int key, String value, AVLNode parent) {
            this.key = key;
            this.value = value;
            height = LEAF_HEIGHT;
            this.parent = parent;
            left = new AVLNode(this);
            right = new AVLNode(this);
            size = 1;
            sumSubtreeKeys = key;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setLeft(IAVLNode node) {
            left = (AVLNode) node;
        }

        public IAVLNode getLeft() {
            return left;
        }

        public void setRight(IAVLNode node) {
            right = (AVLNode) node;
        }

        public IAVLNode getRight() {
            return right;
        }

        public void setParent(IAVLNode node) {
            this.parent = (AVLNode) node;
        }

        public IAVLNode getParent() {
            return parent;
        }

        // Returns True if this is a non-virtual AVL node
        public boolean isRealNode() {
            return key != VIRTUAL_NODE;
        }

        public void setSubtreeSize(int size) {
            this.size = size;
        }

        public int getSubtreeSize() {
            return size;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getHeight() {
            return height;
        }
        public int rSize() { return size - left.size - 1; }
        public int lSize() {
            return size - right.size - 1;
        }
        public int calcHeight()
        {
            if(key == VIRTUAL_NODE){
                return -1;
            }
            return 1 + Math.max(this.left.getHeight(), this.right.getHeight());
        }

        //used for recalculating height and size (and later sum of keys) after changes in the node's children.
        public void update()
        {
            this.setHeight(calcHeight());
            size = right.size + left.size + 1;
            sumSubtreeKeys = Math.max(left.sumSubtreeKeys, 0) + Math.max(right.sumSubtreeKeys, 0) + key;
        }

        /**
         * TODO Don't forget to delete this!
         * <p>
         * Returns the text that represents this node in short (a few characters)
         */
        private String getPrintText()
        {
            if (!isRealNode())
                return "Ø";
            //return ""+key + ":" + value + ":"+rank;
            return "" + key + "(" + value + ")";
        }

        /**
         * TODO Don't forget to delete this!
         * <p>
         * Prints the node's subtree in a magnificent fashion
         */
        public String print()
        {
            StringBuilder representation = new StringBuilder();
            String newLine = System.lineSeparator();
            List<List<String>> lines = new ArrayList<List<String>>();
            List<AVLNode> level = new ArrayList<AVLNode>();
            List<AVLNode> next = new ArrayList<AVLNode>();

            level.add(this);
            int nn = 1;

            int widest = 0;
            int m1 = 0, m2 = 0, m3 = 0;
            while (nn != 0)
            {
                List<String> line = new ArrayList<String>();

                nn = 0;

                for (AVLNode n : level)
                {
                    if (n == null)
                    {
                        line.add(null);

                        next.add(null);
                        next.add(null);
                    } else
                    {
                        String aa = n.getPrintText();
                        line.add(aa);
                        if (aa.length() > widest)
                            widest = aa.length();

                        next.add(n.left);
                        next.add(n.right);

                        if (n.getLeft() != null)
                            nn++;
                        if (n.getRight() != null)
                            nn++;
                    }
                }

                if (widest % 2 == 1)
                    widest++;

                lines.add(line);

                List<AVLNode> tmp = level;
                level = next;
                next = tmp;
                next.clear();
            }

            int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
            for (int i = 0; i < lines.size(); i++)
            {
                List<String> line = lines.get(i);
                int hpw = (int) Math.floor(perpiece / 2f) - 1;

                if (i > 0)
                {
                    for (int j = 0; j < line.size(); j++)
                    {

                        // split node
                        char c = ' ';
                        if (j % 2 == 1)
                        {
                            if (line.get(j - 1) != null)
                            {
                                c = (line.get(j) != null) ? '┴' : '┘';
                            } else
                            {
                                if (line.get(j) != null)
                                    c = '└';
                            }
                        }
                        representation.append(c);

                        // lines and spaces
                        if (line.get(j) == null)
                        {
                            for (int k = 0; k < perpiece - 1; k++)
                            {
                                representation.append(" ");
                            }
                        } else
                        {

                            for (int k = 0; k < hpw; k++)
                            {
                                representation.append(j % 2 == 0 ? " " : "─");
                            }
                            representation.append(j % 2 == 0 ? "┌" : "┐");
                            for (int k = 0; k < hpw; k++)
                            {
                                representation.append(j % 2 == 0 ? "─" : " ");
                            }
                        }
                    }
                    representation.append(newLine);
                }

                // print line of numbers
                for (int j = 0; j < line.size(); j++)
                {

                    String f = line.get(j);
                    if (f == null)
                        f = "";
                    int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                    int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                    // a number
                    for (int k = 0; k < gap1; k++)
                    {
                        representation.append(" ");
                    }
                    representation.append(f);
                    for (int k = 0; k < gap2; k++)
                    {
                        representation.append(" ");
                    }
                }
                representation.append(newLine);

                perpiece /= 2;
            }
            return representation.toString();
        }

    }
}

