import org.apache.log4j.Logger;

/**
 * @Author: 孟庆浩
 * @Description:
 * @Date: Created in 18-1-4 下午9:47
 * @Modified By:
 */
public class Test {


    private Logger logger = Logger.getLogger(Test.class);


    public static void main(String[] args) {
        //nodeTest();
        Double d = 0.8d;
        Double d2 = 0.1d;
        System.out.println(d+d2);
    }

    /**
     * 单链表翻转
     */
    static void nodeTest() {
        Node node = null;
        for (int i = 10; i > 0; i--) {
            node = new Node(i, node);
        }
        //单链表翻转
        int num = 0;
        Node newNode = null;
        Node itemNode;
        do {
            if (num == 0) {
                newNode = node;
                node = node.next;
                newNode.next = null;
                num++;
            } else {
                itemNode = node;
                node = node.next;
                itemNode.next = newNode;
                newNode = itemNode;
            }
        } while (node.next != null);


    }

}

class Node {
    int item;
    Node next;

    Node(int i, Node next) {
        this.item = i;
        this.next = next;
    }
}
