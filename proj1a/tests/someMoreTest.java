import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class someMoreTest {

    /**
     * 同时检查内容、顺序、大小、空状态，以及两种 get。
     * 不检查非法下标，避免一个越界问题掩盖其他测试结果。
     */
    private static <T> void assertState(
            List<T> expected, LinkedListDeque61B<T> actual) {

        assertEquals(expected.size(), actual.size(), "size 不正确");
        assertEquals(expected.isEmpty(), actual.isEmpty(), "isEmpty 不正确");
        assertEquals(expected, actual.toList(), "内容或顺序不正确");

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i),
                    "get(" + i + ") 不正确");
            assertEquals(expected.get(i), actual.getRecursive(i),
                    "getRecursive(" + i + ") 不正确");
        }

        // 读取操作不应修改队列。
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual.toList());
    }

    @Test
    public void emptyDeque() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();

        assertState(List.of(), deque);

        // 重复删除空队列不应使 size 变为负数。
        for (int i = 0; i < 3; i++) {
            assertNull(deque.removeFirst());
            assertNull(deque.removeLast());
            assertState(List.of(), deque);
        }

        assertNull(deque.get(0));
        assertNull(deque.getRecursive(0));
    }

    @Test
    public void additionsAtBothEnds() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();

        deque.addFirst(2);
        assertState(List.of(2), deque);

        deque.addFirst(1);
        assertState(List.of(1, 2), deque);

        deque.addLast(3);
        assertState(List.of(1, 2, 3), deque);

        deque.addFirst(0);
        deque.addLast(4);
        assertState(List.of(0, 1, 2, 3, 4), deque);
    }

    @Test
    public void singleElementAllInsertionRemovalCombinations() {
        // 覆盖：
        // addFirst → removeFirst
        // addFirst → removeLast
        // addLast  → removeFirst
        // addLast  → removeLast
        for (boolean insertAtFront : new boolean[]{true, false}) {
            for (boolean removeAtFront : new boolean[]{true, false}) {
                LinkedListDeque61B<Integer> deque =
                        new LinkedListDeque61B<>();

                if (insertAtFront) {
                    deque.addFirst(42);
                } else {
                    deque.addLast(42);
                }

                assertState(List.of(42), deque);

                Integer removed = removeAtFront
                        ? deque.removeFirst() : deque.removeLast();

                assertEquals(Integer.valueOf(42), removed);
                assertState(List.of(), deque);

                // 删除唯一节点后仍然能重新使用。
                deque.addLast(99);
                assertState(List.of(99), deque);
            }
        }
    }

    @Test
    public void removeFirstUntilEmpty() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();

        deque.addLast(10);
        deque.addLast(20);
        deque.addLast(30);

        assertEquals(Integer.valueOf(10), deque.removeFirst());
        assertState(List.of(20, 30), deque);

        assertEquals(Integer.valueOf(20), deque.removeFirst());
        assertState(List.of(30), deque);

        assertEquals(Integer.valueOf(30), deque.removeFirst());
        assertState(List.of(), deque);

        assertNull(deque.removeFirst());
    }

    @Test
    public void removeLastUntilEmpty() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();

        deque.addLast(10);
        deque.addLast(20);
        deque.addLast(30);

        assertEquals(Integer.valueOf(30), deque.removeLast());
        assertState(List.of(10, 20), deque);

        assertEquals(Integer.valueOf(20), deque.removeLast());
        assertState(List.of(10), deque);

        assertEquals(Integer.valueOf(10), deque.removeLast());
        assertState(List.of(), deque);

        assertNull(deque.removeLast());
    }

    @Test
    public void mixedOperationsAndRepeatedReuse() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();

        for (int round = 0; round < 20; round++) {
            deque.addFirst(2);
            deque.addFirst(1);
            deque.addLast(3);
            deque.addLast(4);
            assertState(List.of(1, 2, 3, 4), deque);

            assertEquals(Integer.valueOf(1), deque.removeFirst());
            assertEquals(Integer.valueOf(4), deque.removeLast());
            assertState(List.of(2, 3), deque);

            // 删除后再次插入，检查两端链接是否仍然正确。
            deque.addFirst(0);
            deque.addLast(5);
            assertState(List.of(0, 2, 3, 5), deque);

            assertEquals(Integer.valueOf(5), deque.removeLast());
            assertEquals(Integer.valueOf(0), deque.removeFirst());
            assertEquals(Integer.valueOf(3), deque.removeLast());
            assertEquals(Integer.valueOf(2), deque.removeFirst());
            assertState(List.of(), deque);
        }
    }

    @Test
    public void validGetsBeforeAndAfterRemovals() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();
        List<Integer> expected = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            deque.addLast(i);
            expected.add(i);
        }
        assertState(expected, deque);

        deque.removeFirst();
        expected.remove(0);

        deque.removeLast();
        expected.remove(expected.size() - 1);

        assertState(expected, deque);
    }

    @Test
    public void upperOutOfBoundsIndices() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();

        assertNull(deque.get(0));
        assertNull(deque.get(Integer.MAX_VALUE));
        assertNull(deque.getRecursive(0));
        assertNull(deque.getRecursive(Integer.MAX_VALUE));

        deque.addLast(10);
        deque.addLast(20);

        for (int index : new int[]{2, 3, Integer.MAX_VALUE}) {
            assertNull(deque.get(index));
            assertNull(deque.getRecursive(index));
        }

        assertState(List.of(10, 20), deque);
    }

    @Test
    public void negativeIndicesOnEmptyDeque() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();

        assertNull(deque.getRecursive(-1));
        assertNull(deque.getRecursive(Integer.MIN_VALUE));

        assertNull(deque.get(-1));
        assertNull(deque.get(Integer.MIN_VALUE));
    }

    @Test
    public void negativeIndicesOnNonEmptyDeque() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();
        deque.addLast(10);
        deque.addLast(20);

        assertNull(deque.getRecursive(-1));
        assertNull(deque.getRecursive(Integer.MIN_VALUE));

        // 使用抢占式超时，避免错误实现进行极长时间的循环。
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            assertNull(deque.get(-1));
            assertNull(deque.get(Integer.MIN_VALUE));
        });

        assertState(List.of(10, 20), deque);
    }

    @Test
    public void duplicateValuesAndGenericType() {
        LinkedListDeque61B<String> deque = new LinkedListDeque61B<>();

        deque.addLast("same");
        deque.addFirst("same");
        deque.addLast("different");
        deque.addLast("same");

        assertState(List.of("same", "same", "different", "same"), deque);

        assertEquals("same", deque.removeFirst());
        assertEquals("same", deque.removeLast());
        assertState(List.of("same", "different"), deque);
    }

    @Test
    public void toListReturnsIndependentCopy() {
        LinkedListDeque61B<Integer> deque = new LinkedListDeque61B<>();
        deque.addLast(1);
        deque.addLast(2);

        List<Integer> firstCopy = deque.toList();
        List<Integer> secondCopy = deque.toList();

        assertNotSame(firstCopy, secondCopy);

        // 队列变化不应影响之前返回的列表。
        deque.removeFirst();
        deque.addLast(3);

        assertEquals(List.of(1, 2), firstCopy);
        assertEquals(List.of(1, 2), secondCopy);
        assertState(List.of(2, 3), deque);

        // 修改返回的 ArrayList 不应影响队列或其他副本。
        firstCopy.clear();
        firstCopy.add(99);

        assertEquals(List.of(1, 2), secondCopy);
        assertState(List.of(2, 3), deque);
    }

    @Test
    public void separateInstancesAreIndependent() {
        LinkedListDeque61B<Integer> first = new LinkedListDeque61B<>();
        LinkedListDeque61B<Integer> second = new LinkedListDeque61B<>();

        first.addLast(1);
        second.addFirst(2);
        second.addLast(3);

        assertState(List.of(1), first);
        assertState(List.of(2, 3), second);

        first.removeLast();

        assertState(List.of(), first);
        assertState(List.of(2, 3), second);
    }

    @Test
    public void randomizedComparisonWithArrayList() {
        long seed = 20261017L;
        Random random = new Random(seed);

        LinkedListDeque61B<Integer> actual = new LinkedListDeque61B<>();
        List<Integer> expected = new ArrayList<>();

        for (int step = 0; step < 5000; step++) {
            int operation = random.nextInt(6);
            int value = random.nextInt(21) - 10;

            String context = "seed=" + seed
                    + ", step=" + step
                    + ", operation=" + operation
                    + ", value=" + value;

            try {
                switch (operation) {
                    case 0:
                        actual.addFirst(value);
                        expected.add(0, value);
                        break;

                    case 1:
                        actual.addLast(value);
                        expected.add(value);
                        break;

                    case 2: {
                        Integer removed = expected.isEmpty()
                                ? null : expected.remove(0);
                        assertEquals(removed, actual.removeFirst());
                        break;
                    }

                    case 3: {
                        Integer removed = expected.isEmpty()
                                ? null : expected.remove(expected.size() - 1);
                        assertEquals(removed, actual.removeLast());
                        break;
                    }

                    case 4: {
                        // 包括合法下标以及上界越界。
                        int index = random.nextInt(expected.size() + 2);
                        Integer wanted = index < expected.size()
                                ? expected.get(index) : null;
                        assertEquals(wanted, actual.get(index));
                        break;
                    }

                    case 5: {
                        int index = random.nextInt(expected.size() + 2);
                        Integer wanted = index < expected.size()
                                ? expected.get(index) : null;
                        assertEquals(wanted, actual.getRecursive(index));
                        break;
                    }

                    default:
                        throw new AssertionError("未知操作");
                }

                assertState(expected, actual);
            } catch (AssertionError | RuntimeException failure) {
                throw new AssertionError(context, failure);
            }
        }

        // 最后交替从两端删除，验证剩余节点的链接。
        boolean removeFromFront = true;
        while (!expected.isEmpty()) {
            if (removeFromFront) {
                assertEquals(expected.remove(0), actual.removeFirst());
            } else {
                assertEquals(expected.remove(expected.size() - 1),
                        actual.removeLast());
            }
            removeFromFront = !removeFromFront;
            assertState(expected, actual);
        }

        assertNull(actual.removeFirst());
        assertNull(actual.removeLast());
        assertState(List.of(), actual);
    }
}