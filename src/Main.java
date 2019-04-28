import java.util.*;

public class Main {


    private static void TestBasicInsertionAndLL() {
        AVLTree tree = new AVLTree();
        assert tree.empty();
        tree.insert(3, "miao");
        assert tree.empty() == false;
        tree.insert(2, "miao");
        tree.insert(1, "miao");
        AVLTree.AVLNode node = tree.root;
        //System.out.println(tree);
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

    private static boolean TestConsistency(AVLTree tree) {
        if(tree == null || tree.root == null)
        {
            return true;
        }
        int realNodes = 0;
        ArrayList<AVLTree.AVLNode> lst = tree.getArray();
        for(int i = 0; i < lst.size(); ++i)
        {
            AVLTree.AVLNode node = lst.get(i);
            if(node.isRealNode())
            ++realNodes;
            {
                if(node.left.parent != node)
                {
                    System.out.println("parent inconsistency between keys " +  node.key + "," + node.left.key);
                    return false;
                }
                if(node.right.parent != node)
                {
                    System.out.println("parent inconsistency between keys " +  node.key + "," + node.right.key);
                    return false;
                }
                if(Math.abs(tree.calcBF(node)) > 1)
                {
                    System.out.println("bad bf on key " + node.key);
                    return false;
                }
                if(node.height != Math.max(node.right.height, node.left.height) + 1)
                {
                    System.out.println("bad height on key " + node.key);
                    return false;
                }
                if(node.size != node.right.size + node.left.size + 1)
                {
                    System.out.println("bad size on key " + node.key);
                    return false;
                }
            }
        }
        if(realNodes != lst.size())
        {
            System.out.println("did not check all nodes!");
            return false;
        }
        return true;
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
        assert Arrays.equals(tree.keysToArray(), new int[0]);
        int items[] = {1, 3, 2, 4};
        for(int i = 0; i < items.length; ++i) {
            tree.insert(items[i], "miao " + i);
        }
        int [] keysInOrder = {1, 2, 3, 4};
        assert Arrays.equals(tree.keysToArray(), keysInOrder);
        String [] valuesInOrder = {"miao 0", "miao 2", "miao 1", "miao 3"};
        assert Arrays.equals(tree.infoToArray(), valuesInOrder);
    }

    private static void testSuccessorPredecessor(){
        AVLTree tree = new AVLTree();
        int items[] = {3, 11, 4, 16, 5, 19, 13, 1, 15, 10, 18, 12, 20, 14, 6, 9, 8, 2, 17, 7};
        for(int i = 0; i < items.length; ++i) {
            tree.insert(items[i], "test " + items[i]);
        }
        for(int i = 1; i <= items.length - 1; ++i) {
            assert AVLTree.successor(tree.searchNode(i)).getKey() == i+1;
        }
        for(int i = 2; i <= items.length; ++i) {
            assert AVLTree.predecesoor(tree.searchNode(i)).getKey() == i-1;
        }
    }
    private static void testDeletion(){
        AVLTree tree = new AVLTree();
        int items[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for(int i = 0; i < items.length; ++i) {
            tree.insert(items[i], "test " + items[i]);
        }
        //root deletion
        tree.delete(4);
        //leaf deletion
        tree.delete(3);
        //single child deletion
        tree.delete(9);
        //double child deletion
        tree.delete(8);
        TestConsistency(tree);
    }

    private static void testRootDeletion(){
        AVLTree tree = new AVLTree();
        tree.delete(10);
        tree.insert(1, "test1");
        TestConsistency(tree);
        tree.delete(1);
        tree.insert(1, "test1");
        tree.insert(2, "test1");
        tree.delete(1);
        TestConsistency(tree);
        tree.delete(2);
        TestConsistency(tree);
        tree.insert(2, "test1");
        tree.insert(1, "test1");
        tree.delete(2);
        TestConsistency(tree);
        tree.delete(1);
        TestConsistency(tree);
    }

    private static void testDeletionLoop(int size, int iters){
        boolean problem = false;
        for(int i = 0; i < iters && problem == false; ++i)
        {
            problem = testDeletion2(size);
        }
    }
    private static boolean testDeletion2(int size){
        AVLTree tree = new AVLTree();
        for(int i = 0; i < size; ++i) {
            tree.insert(i, "test " + i);
        }
        TestConsistency(tree);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            list.add(i);
        }
        Collections.shuffle(list);
        boolean wasException = false;
        boolean problem = false;
        int i;
        for (i = 0; i < size && problem == false; ++i) {
            try {
                tree.delete(list.get(i));
                if (TestConsistency(tree) == false) {
                    problem = true;
                }
            } catch (Exception e) {
                wasException = true;
                problem = true;
                e.printStackTrace();
            }
        }
        if(problem == true) {
            String removed = "";
            for (int j = 0; j <= i; ++j) {
                removed += list.get(j) + " ";
            }
            System.out.println("order of removal: " + removed + ",");
            try {
                System.out.println(tree);
            } catch (Exception E){
                System.out.println("could not print tree");
            }
            return true;
        }
        return false;
        }

    private static void testOverallConsistency(int len) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < len; ++i) {
            list.add(i);
        }

        Collections.shuffle(list);
        AVLTree tree = new AVLTree();
        for (int i = 0; i < len; ++i) {
            tree.insert(list.get(i), "miao");
            if (TestConsistency(tree) == false) {
                System.out.println(tree);
                return;
            }
        }
        String prev = "";

        Collections.shuffle(list);
        for (int i = 0; i < len; ++i) {
            prev = tree.toString();
            tree.delete(list.get(i));
            if (TestConsistency(tree) == false) {
                System.out.println(prev);
                System.out.println(tree);
                return;
            }
        }
    }

    private static AVLTree createTree(int[] items)
    {
        AVLTree tree = new AVLTree();
        for(int i = 0; i < items.length; ++i) {
            tree.insert(items[i], "test " + items[i]);
        }
        return tree;
    }

    private static AVLTree createTree(int size)
    {
        int[] items = new int[size];
        for(int i = 0; i < items.length; ++i) {
            items[i] = i;
        }
        return createTree(items);
    }

    static void curr()
    {
        AVLTree tree = createTree(10);
        int[] rem = {5, 1}; //2 7
        for(int i = 0; i < rem.length; ++i)
        {
            tree.delete(rem[i]);
        }
        System.out.println(tree);
        tree.delete(2);
        TestConsistency(tree);
        System.out.println(tree);
    }
    private static void testLess(int size) {
        AVLTree tree = createTree(20);
        tree.delete(7);
        tree.delete(15);
        int sum = 0;
        for(int i = 0; i < 20; ++i)
        {
            if(i != 7 && i != 15)
                sum += i;
            if(tree.less(i) != sum)
            {
                System.out.println("for " + i + " got "+ tree.less(i) + "expected " + sum);
            }
        }

        tree = createTree(size);
        AVLTree tree2 = new AVLTree();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            list.add(i);
        }
        Collections.shuffle(list);
        sum = 0;
        int[] exp = new int[size];
        for(int i = 1; i < size; ++i) {
            exp[i] = exp[i - 1] + i;
        }
        for(int i = 0; i < size; ++i) {
            int num = list.get(i);
            tree.delete(num);
            tree2.insert(num,"");
            int a = 0;
            int b = 0;
            for(int j = 0; j < size; ++j){
                try {
                    a = tree.less(j);
                }catch (Exception e) {
                    System.out.println("error for key " + j + "in tree1");
                    e.printStackTrace();
                    System.out.println(tree);
                    a = tree.less(j);
                    return;
                }
                try {
                    b = tree2.less(j);
                }catch (Exception e) {
                    System.out.println("error for key " + j + "in tree2");
                    e.printStackTrace();
                    System.out.println(tree2);
                }
                if(a + b != exp[j]){
                    System.out.println("error for key " + j);
                    return;
                }
            }
        }
    }

    private static void testRank(int size) {
        AVLTree tree = new AVLTree();
        for(int i = 1; i < size; ++i)
        {
            tree.insert(i * 2, "" + i);
        }
//        System.out.println(tree);
/**        for(int i = 1; i < size; ++i)
 {
 System.out.println("for " + i*2 + " " + tree.subtreeRoot(i * 2).key);
 }
 **/    for(int i = 1; i < size; ++i)
        {
            int res = Integer.parseInt(tree.select(i));
            if(res != i)
            {
                System.out.println(i + "retruened " + res);
            }
        }
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
        testSuccessorPredecessor();
        testDeletion();
        testRootDeletion();
        testDeletionLoop(100, 1000);
        testRank(10);
        testLess(50);
    }
}