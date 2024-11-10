package bit.multiprocessing.programming;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class QuicksortMultiThreadTest {

    private final QuicksortMultiThread quicksortMultiThread = new QuicksortMultiThread(4);
    @Test
    void quickSort() {
        val a = getData();
        var my =  new int[a.length];
        var correct =  new int[a.length];
        System.arraycopy(a, 0, my, 0, a.length);
        System.arraycopy(a, 0, correct, 0, a.length);

        quicksortMultiThread.quickSort(my);
        Arrays.sort(correct);
        assertArrayEquals(my,correct);
    }

    int[] getData(){
        final int l =10_000;
        int[] array = new int[l];
        Random random = new Random();
        for (int i = 0; i < l; i++) {
            array[i] = random.nextInt();
        }
        return array;
    }

}