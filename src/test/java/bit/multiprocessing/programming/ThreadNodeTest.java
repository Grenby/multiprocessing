package bit.multiprocessing.programming;

import bit.multiprocessing.programming.impl.BlockedQueueThreadNode;
import bit.multiprocessing.programming.impl.ConcurrentThreadNode;
import bit.multiprocessing.programming.impl.LazySyncThreadNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Тест на пропускную способность кольца
 * Изначально создается кольцо из N нод с M сообщениями в каждой, за
 *
 */
class ThreadNodeTest {

    private final  int N = 2;
    private final int M = 10000;

    List<ThreadNode> nodes;;
    String name = "";

    @BeforeEach
    public void init(){
        nodes = new ArrayList<>();
    }

    @Test
    public void lazyTest(){
        name = "lazyTest";
        for (int i=0;i<N;i++){
            nodes.add(new LazySyncThreadNode(i));
        }
    }


    @Test
    public void blockedQueue(){
        name = "blockedQueue";
        for (int i=0;i<N;i++){
            nodes.add(new BlockedQueueThreadNode(i));
        }
    }

    @Test
    public void concurrentQueue(){
        name = "concurrentQueue";
        for (int i=0;i<N;i++){
            nodes.add(new ConcurrentThreadNode(i));
        }
    }

    @SneakyThrows
    @AfterEach
    public void test(){
        Thread.sleep(5000);
        List<ThreadNode> nodes = this.nodes;

        for (int i=0;i<N;i++){

            for (int j=0;j<M;j++){
                int finalJ = j;
                int finalI = i;
                nodes.get(i).sendMessage(()-> (finalI * M + finalJ));
            }
        }


        for (int i=0;i<N;i++){
            nodes.get(i).setNext(nodes.get((i+1) % N));
            nodes.get(i).start();
        }

        Thread.sleep(20000);




        for (ThreadNode node:nodes){
            node.setActive(false);
        }
        Thread.sleep(1000);


        List<Double> arr = new ArrayList<>();

        for (int i=0;i<N;i++){
            ThreadNode node = nodes.get(i);
            arr.add(1000000.0*node.getNumMessages() / (1.0* node.getDelta()));
        }


        double mean = arr.stream().reduce(0.0, Double::sum)/arr.size();
        double err = arr.stream().map(a -> (a-mean) * (a-mean)).reduce(0.0, Double::sum)/(arr.size()-1);
        double max = arr.stream().max(Comparator.comparingDouble(o -> o)).orElse(-1.);
        double min = arr.stream().min(Comparator.comparingDouble(o -> o)).orElse(-1.);
        err = Math.sqrt(err);
        System.out.println(name + " " +String.format("%.3f",mean) +"+-" + String.format("%.3f",err) +" операций в миллисекунду (max: " + String.format("%.3f", max)  +" min: " + String.format("%.3f",min) + ")");
    }

}