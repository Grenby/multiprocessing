package bit.multiprocessing.programming;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class QuicksortSeqTest {

    private final Quicksort quicksort = new QuicksortSeq();

    public static Stream<Arguments> testArrays() {
        return Stream.of(
                Arguments.of(new int[]{5,4,3,2,1}),
                Arguments.of(new int[]{1,2,3,2,1})
                );
    }

    @ParameterizedTest
    @MethodSource("testArrays")
    public void testSort(int[] a){
        var my =  new int[a.length];
        var correct =  new int[a.length];
        System.arraycopy(a, 0, my, 0, a.length);
        System.arraycopy(a, 0, correct, 0, a.length);

        quicksort.quickSort(my);
        Arrays.sort(correct);
        assertArrayEquals(my,correct);
    }

}