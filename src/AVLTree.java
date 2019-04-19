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
            newNode.left = parent.left;
            parent.left = newNode;
        }else{
            setRightChild(newNode, parent.right);
            setRightChild(parent, parent.right);
            newNode.right = parent.right;
            parent.right = newNode;
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
            if(calcBF(node.left) == 1)
            {
                rotateLL(node);
            }else {
                rotateLR(node);
            }
        }else{
            if(calcBF(node.right) == -1)
            {
                rotateRR(node);
            }else {
                rotateRL(node);
            }
        }
        return 1;
    }

    private int calcBF(AVLNode node)
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
        boolean rebalance = false;
        while(itr != null)
        {
            AVLNode temp = itr.parent;
            itr.update();
            bf = calcBF(itr);
            if(bf > 1 || bf < -1)
            {
                rebalance = true;
                Balance(itr);
            }
            itr = temp;
        }
        return rebalance ? 1 : 0;
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
        return 42;    // to be replaced by student code
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

    private AVLNode[] inOrderScan(){
        return inOrderScanRec(root, new ArrayList<AVLNode>()).toArray(new AVLNode[0]);
    }
    private List<AVLNode> inOrderScanRec(AVLNode node, List<AVLNode> scannedSoFar){
        if(node.key == AVLNode.VIRTUAL_NODE)
            return scannedSoFar;
        inOrderScanRec(node.left, scannedSoFar);
        scannedSoFar.add(node);
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
        return Arrays.stream(this.inOrderScan()).mapToInt(AVLNode::getKey).toArray();
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        return Arrays.stream(this.inOrderScan()).map(AVLNode::getValue).toArray(String[]::new);
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
        return null;
    }


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
        return 0;
    }

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
        public int calcHeight()
        {
            if(key == VIRTUAL_NODE){
                return -1;
            }
            return 1 + Math.max(this.left.calcHeight(), this.right.calcHeight());
        }

        //used for recalculating height and size (and later sum of keys) after changes in the node's children.
        public void update()
        {
            this.setHeight(calcHeight());
            size = right.size + left.size + 1;
        }

        //calculates node's height by checking its children's heights. if changed is not null,
        // writes in it if the height changed or not. maybe this parameter is not necessary as we always need to go
        // all the way to the root after insert\delete to update size. kept it for now.
        public int calcHeight(Boolean changed)
        {
            if(isRealNode())
            {
                int oldHeight = height;
                height = Math.max(left.height, right.height) + 1;
                if (changed != null) {
                    changed = height != oldHeight;
                }
            }
            return height;
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

