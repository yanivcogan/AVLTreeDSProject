import java.util.ArrayList;
import java.util.Arrays;

public class Main {


    private static void TestBasicInsertionAndLL() {
        AVLTree tree = new AVLTree();
        assert tree.empty();
        tree.insert(3, "miao");
        assert tree.empty() == false;
        tree.insert(2, "miao");
        tree.insert(1, "miao");
        AVLTree.AVLNode node = tree.root;
        System.out.println(tree);
        assert node.key == 2;
        assert node.left.key == 1;
        assert node.right.key == 3;
    }

    private static void TestLR() {
        AVLTree tree = new AVLTree();
        assert tree.empty();
        tree.insert(3, "miao");
        assert tree.empty() == false;
        tree.insert(1, "miao");
        tree.insert(2, "miao");
        AVLTree.AVLNode node = tree.root;
        assert node.key == 2;
        assert node.left.key == 1;
        assert node.right.key == 3;
    }

    private static void TestRR() {
        AVLTree tree = new AVLTree();
        tree.insert(1, "miao");
        tree.insert(2, "miao");
        tree.insert(3, "miao");
        AVLTree.AVLNode node = tree.root;
        assert node.key == 2;
        assert node.left.key == 1;
        assert node.right.key == 3;
    }

    private static void TestRL() {
        AVLTree tree = new AVLTree();
        tree.insert(1, "miao");
        tree.insert(3, "miao");
        tree.insert(2, "miao");
        AVLTree.AVLNode node = tree.root;
        assert node.key == 2;
        assert node.left.key == 1;
        assert node.right.key == 3;
    }

    private static String tostring(AVLTree.AVLNode node)
    {
        String res;
        String parent = node.parent == null? "null" : "" + node.parent.key;
        String rkey = node.right.isRealNode()? "" + node.right.key : "virt";
        String lkey = node.left.isRealNode()? "" + node.left.key : "virt";
        res = "k-" + node.key + ", p-" + parent + ", r-" + rkey
                + ", l-" + lkey + ", h-" + node.height + ", s-" + node.size;
        return res;
    }

    static private void print(AVLTree tree)
    {
        ArrayList<AVLTree.AVLNode> lst = tree.getArray();

        for(AVLTree.AVLNode node : lst)
        {
            System.out.println(tostring(node));
        }
    }

    //checks that heights are consistent [assuming the height in root is correct], |BF| is always <= 1,
    // and node's parent pointer refers their actual parent

    private static void TestConsistency(AVLTree tree) {
        if(tree == null || tree.root == null)
        {
            return;
        }
        ArrayList<AVLTree.AVLNode> lst = tree.getArray();
        for(int i = 0; i < lst.size(); ++i)
        {
            AVLTree.AVLNode node = lst.get(i);
            if(node.isRealNode())
            {
                if(node.left.parent != node)
                {
                    System.out.println("parent inconsistency between keys " +  node.key + "," + node.left.key);
                }
                if(node.right.parent != node)
                {
                    System.out.println("parent inconsistency between keys " +  node.key + "," + node.right.key);
                }
                if(Math.abs(tree.calcBF(node)) > 1)
                {
                    System.out.println("bad bf on key " + node.key);
                }
                if(node.height != Math.max(node.right.height, node.left.height) + 1)
                {
                    System.out.println("bad height on key " + node.key);
                }
            }
        }
    }
    private static void TestInsert() {
        AVLTree tree = new AVLTree();
        int items[] = {15,10,22,4,11,20,24,2,7,12,18,1,6,8,5};
        for(int i = 0; i < items.length; ++i) {
            tree.insert(items[i], "miao");
        }
        TestConsistency(tree);
        //System.out.println(tree.root.print());
        //print(tree);
    }
    private static void TestMinMax() {
        AVLTree tree = new AVLTree();
        int items[] = {1, 3, 2, 4};
        for(int i = 0; i < items.length; ++i) {
            tree.insert(items[i], "miao " + i);
        }
        String min = tree.min();
        assert tree.max().equals("miao 3");
        assert tree.min().equals("miao 0");
    }
    private static void testInOrder(){
        AVLTree tree = new AVLTree();
        int items[] = {1, 3, 2, 4};
        for(int i = 0; i < items.length; ++i) {
            tree.insert(items[i], "miao " + i);
        }
        int [] keysInOrder = {1, 2, 3, 4};
        assert Arrays.equals(tree.keysToArray(), keysInOrder);
        String [] valuesInOrder = {"miao 0", "miao 2", "miao 1", "miao 3"};
        assert Arrays.equals(tree.infoToArray(), valuesInOrder);
        System.out.println(Arrays.equals(tree.infoToArray(), valuesInOrder));
        System.out.println(Arrays.toString(tree.keysToArray()));
        System.out.println(Arrays.toString(tree.infoToArray()));
    }
    public static void main(String[] args)
    {
        TestBasicInsertionAndLL();
        TestLR();
        TestRR();
        TestRL();
        TestInsert();
        TestMinMax();
        testInOrder();
    }
}
