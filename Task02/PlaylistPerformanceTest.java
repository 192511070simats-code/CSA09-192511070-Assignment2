
import java.util.*;

public class PlaylistPerformanceTest {

    static final int REPETITIONS = 10;
    static final int OPERATIONS = 1000;
    static volatile long result = 0;

    static List<String> createSongs(int size, boolean linked) {
        List<String> songs = linked
                ? new LinkedList<>()
                : new ArrayList<>();

        for (int i = 0; i < size; i++) {
            songs.add("Song" + i);
        }

        return songs;
    }

    static long measure(Runnable operation) {
        long start = System.nanoTime();
        operation.run();
        return System.nanoTime() - start;
    }

    static void testList(String name, int size,
                         boolean linked, boolean display) {

        if (display) {
            System.out.println("\n--- " + name + " ---");
        }

        long addTime = 0;
        long frontInsertTime = 0;
        long frontRemoveTime = 0;
        long middleInsertTime = 0;
        long middleRemoveTime = 0;
        long accessTime = 0;
        long iterationTime = 0;

        for (int r = 0; r < REPETITIONS; r++) {

            List<String> songs = createSongs(size, linked);
            final List<String> addEnd = songs;

            addTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    addEnd.add("NewSong" + i);
                }
            });

            songs = createSongs(size, linked);
            final List<String> frontInsert = songs;

            frontInsertTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    frontInsert.add(0, "NewSong");
                }
            });

            songs = createSongs(size + OPERATIONS, linked);
            final List<String> frontRemove = songs;

            frontRemoveTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    frontRemove.remove(0);
                }
            });

            songs = createSongs(size, linked);
            final List<String> middleInsert = songs;

            middleInsertTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    middleInsert.add(
                            middleInsert.size() / 2, "NewSong");
                }
            });

            songs = createSongs(size + OPERATIONS, linked);
            final List<String> middleRemove = songs;

            middleRemoveTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    middleRemove.remove(middleRemove.size() / 2);
                }
            });

            songs = createSongs(size, linked);
            final List<String> access = songs;

            accessTime += measure(() -> {
                long count = 0;
                for (int i = 0; i < OPERATIONS; i++) {
                    count += access.get(size / 2).length();
                }
                result += count;
            });

            songs = createSongs(size, linked);
            final List<String> iteration = songs;

            iterationTime += measure(() -> {
                long count = 0;
                for (String song : iteration) {
                    count += song.length();
                }
                result += count;
            });
        }

        if (display) {
            printResults(addTime, frontInsertTime, frontRemoveTime,
                    middleInsertTime, middleRemoveTime,
                    accessTime, iterationTime);
        }
    }

    static void testArrayDeque(int size, boolean display) {

        if (display) {
            System.out.println("\n--- ArrayDeque ---");
        }

        long addLastTime = 0;
        long addFirstTime = 0;
        long removeFirstTime = 0;
        long removeLastTime = 0;
        long iterationTime = 0;

        for (int r = 0; r < REPETITIONS; r++) {

            Deque<String> songs = new ArrayDeque<>();

            for (int i = 0; i < size; i++) {
                songs.addLast("Song" + i);
            }

            final Deque<String> addLast = songs;

            addLastTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    addLast.addLast("NewSong" + i);
                }
            });

            songs = new ArrayDeque<>();

            for (int i = 0; i < size; i++) {
                songs.addLast("Song" + i);
            }

            final Deque<String> addFirst = songs;

            addFirstTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    addFirst.addFirst("NewSong");
                }
            });

            songs = new ArrayDeque<>();

            for (int i = 0; i < size + OPERATIONS; i++) {
                songs.addLast("Song" + i);
            }

            final Deque<String> removeFirst = songs;

            removeFirstTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    removeFirst.removeFirst();
                }
            });

            songs = new ArrayDeque<>();

            for (int i = 0; i < size + OPERATIONS; i++) {
                songs.addLast("Song" + i);
            }

            final Deque<String> removeLast = songs;

            removeLastTime += measure(() -> {
                for (int i = 0; i < OPERATIONS; i++) {
                    removeLast.removeLast();
                }
            });

            songs = new ArrayDeque<>();

            for (int i = 0; i < size; i++) {
                songs.addLast("Song" + i);
            }

            final Deque<String> iteration = songs;

            iterationTime += measure(() -> {
                long count = 0;
                for (String song : iteration) {
                    count += song.length();
                }
                result += count;
            });
        }

        if (display) {
            System.out.println("Add at end average (ns): "
                    + addLastTime / REPETITIONS);
            System.out.println("Add at front average (ns): "
                    + addFirstTime / REPETITIONS);
            System.out.println("Remove at front average (ns): "
                    + removeFirstTime / REPETITIONS);
            System.out.println("Remove at end average (ns): "
                    + removeLastTime / REPETITIONS);
            System.out.println("Iteration average (ns): "
                    + iterationTime / REPETITIONS);

            System.out.println(
                    "Middle insertion: Not supported directly");
            System.out.println(
                    "Middle removal: Not supported directly");
            System.out.println("Indexed access: Not supported");
        }
    }

    static void printResults(long add, long frontInsert,
            long frontRemove, long middleInsert,
            long middleRemove, long access, long iteration) {

        System.out.println("Add at end average (ns): "
                + add / REPETITIONS);
        System.out.println("Insert at front average (ns): "
                + frontInsert / REPETITIONS);
        System.out.println("Remove at front average (ns): "
                + frontRemove / REPETITIONS);
        System.out.println("Insert in middle average (ns): "
                + middleInsert / REPETITIONS);
        System.out.println("Remove in middle average (ns): "
                + middleRemove / REPETITIONS);
        System.out.println("Access middle average (ns): "
                + access / REPETITIONS);
        System.out.println("Iteration average (ns): "
                + iteration / REPETITIONS);
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 5000, 10000};

        System.out.println("PLAYLIST PERFORMANCE COMPARISON");
        System.out.println("Warm-up runs: " + REPETITIONS);
        System.out.println("Measured repetitions: " + REPETITIONS);
        System.out.println("Operations per test: " + OPERATIONS);

        System.out.println("\nPerforming JVM warm-up...");

        // Warm up all operations before collecting measurements.
        for (int size : sizes) {
            testList("ArrayList", size, false, false);
            testList("LinkedList", size, true, false);
            testArrayDeque(size, false);
        }

        System.out.println("Warm-up completed.");
        System.out.println("\nFINAL MEASURED RESULTS");

        for (int size : sizes) {

            System.out.println("\n================================");
            System.out.println("Playlist size: " + size + " songs");
            System.out.println("================================");

            testList("ArrayList", size, false, true);
            testList("LinkedList", size, true, true);
            testArrayDeque(size, true);
        }

        System.out.println("\nExperiment completed.");
        System.out.println("Verification checksum: " + result);
    }
}
