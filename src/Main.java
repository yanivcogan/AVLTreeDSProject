import java.util.ArrayList;

public class Main {


    private static void TestBasicInsertionAndLL() {
        AVLTree tree = new AVLTree();
        assert tree.empty();
        tree.insert(3, "miao");
        assert tree.empty() == false;
        tree.insert(2, "miao");
        tree.insert(1, "miao");
        AVLTree.AVLNode node = tree.root;
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

    private static void TestInsert() {
        AVLTree tree = new AVLTree();
 //       int items[] = {15,10,22,4,11,20,24,2,7,12,18,1,6,8,5};
 //       for(int i = 0; i < items.length; ++i) {
 //           tree.insert(items[i], "miao");
 //       }
        int items[] = {1, 3, 2, 4};
        for(int i = 0; i < items.length; ++i) {
            tree.insert(items[i], "miao");
        }
        print(tree);
    }
        public static void main(String[] args)
    {
        TestBasicInsertionAndLL();
        TestLR();
        TestRR();
        TestRL();
        TestInsert();
    }
}
