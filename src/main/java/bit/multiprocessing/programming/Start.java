package bit.multiprocessing.programming;

import bit.multiprocessing.programming.impl.LazySyncThreadNode;

import java.util.ArrayList;
import java.util.List;

public class Start {

    public static void main(String[] args) throws InterruptedException {
        int n = 6;
        int m = 1000;
        List<ThreadNode> nodes = new ArrayList<>();

        for (int i=0;i<n;i++){
            ThreadNode node = new LazySyncThreadNode(i);
            for (int j=0;j<500;j++){
                int finalJ = j;
                int finalI = i;
                node.sendMessage(()-> (finalI *m + finalJ) );
            }
            nodes.add(node);
        }


        for (int i=0;i<n;i++){
            nodes.get(i).setNext(nodes.get((i+1) % n));
            nodes.get(i).start();
        }

        Thread.sleep(10000);


        ThreadNode.isActive = false;
        Thread.sleep(1000);


        List<Double> arr = new ArrayList<>();

        for (int i=0;i<n;i++){
            ThreadNode node = nodes.get(i);
            arr.add(1000000.0*node.getNumMessages() / (1.0* node.getDelta()));
        }


        double mean = arr.stream().reduce(0.0, Double::sum)/arr.size();
        double err = arr.stream().map(a -> (a-mean) * (a-mean)).reduce(0.0, Double::sum)/(arr.size()-1);
        err = Math.sqrt(err);
        System.out.println(mean +"+-" + err +" операций в милисекунду");
    }

}
