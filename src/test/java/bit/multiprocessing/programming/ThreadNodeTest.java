package bit.multiprocessing.programming;

import bit.multiprocessing.programming.impl.LazyThreadNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ThreadNodeTest {

    private final  int N = 6;
    private final int M = 1000;

    List<ThreadNode> nodes = new ArrayList<>();
    String name = "";
    @Test
    public void lazyTest(){
        name = "lazyTest";
        for (int i=0;i<N;i++){
            nodes.add(new LazyThreadNode(i));
        }
    }


    @SneakyThrows
    @AfterEach
    public void test(){

        List<ThreadNode> nodes = this.nodes;

        for (int i=0;i<N;i++){

            for (int j=0;j<500;j++){
                int finalJ = j;
                int finalI = i;
                nodes.get(i).sendMessage(()-> (finalI *M + finalJ) );
            }
        }


        for (int i=0;i<N;i++){
            nodes.get(i).setNext(nodes.get((i+1) % N));
            nodes.get(i).start();
        }

        Thread.sleep(10000);


        ThreadNode.isActive = false;
        Thread.sleep(1000);


        List<Double> arr = new ArrayList<>();

        for (int i=0;i<N;i++){
            ThreadNode node = nodes.get(i);
            arr.add(1000000.0*node.getNumMessages() / (1.0* node.getDelta()));
        }


        double mean = arr.stream().reduce(0.0, Double::sum)/arr.size();
        double err = arr.stream().map(a -> (a-mean) * (a-mean)).reduce(0.0, Double::sum)/(arr.size()-1);
        err = Math.sqrt(err);
        System.out.println(name + " " + mean +"+-" + err +" операций в милисекунду");
    }

}