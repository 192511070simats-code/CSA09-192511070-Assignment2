
import java.util.*;

public class PharmacyInventory {

    private static final int LOOKUPS = 100_000;
    private static final int REPETITIONS = 5;
    private static volatile long checksum = 0;

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        System.out.println("PHARMACY INVENTORY MAP COMPARISON");
        System.out.println("Lookups per test: " + LOOKUPS);
        System.out.println("Average of " + REPETITIONS + " repetitions");
        System.out.println();

        for (int size : sizes) {
            runExperiment(size);
        }
    }

    private static void runExperiment(int size) {

        Map<String, Integer> hashMap = new HashMap<>();
        Map<String, Integer> treeMap = new TreeMap<>();
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();

        List<String> insertionOrder = new ArrayList<>();

        // Insert medicines in a deliberately mixed order.
        for (int i = 0; i < size; i++) {
            int codeNumber = (i * 7919) % size;
            String code = String.format("MED%06d", codeNumber);

            int stock = 100 + (i % 500);

            hashMap.put(code, stock);
            treeMap.put(code, stock);
            linkedHashMap.put(code, stock);

            insertionOrder.add(code);
        }

        // Prepare the same lookup workload for every map.
        String[] queries = new String[LOOKUPS];

        for (int i = 0; i < LOOKUPS; i++) {
            int index = (i * 37) % size;
            queries[i] = insertionOrder.get(index);
        }

        System.out.println("====================================");
        System.out.println("NUMBER OF MEDICINES: " + size);
        System.out.println("====================================");

        testMap("HashMap", hashMap, insertionOrder, queries);
        testMap("TreeMap", treeMap, insertionOrder, queries);
        testMap("LinkedHashMap", linkedHashMap, insertionOrder, queries);

        System.out.println();
    }

    private static void testMap(
            String name,
            Map<String, Integer> map,
            List<String> insertionOrder,
            String[] queries) {

        // Warm-up lookups.
        for (int i = 0; i < 10000; i++) {
            checksum += map.get(queries[i]);
        }

        double lookupTime = averageLookupTime(map, queries);

        double codeReportTime =
                averageCodeReportTime(map);

        double insertionReportTime =
                averageInsertionReportTime(map, insertionOrder);

        List<String> codeReport = createCodeReport(map);
        List<String> insertionReport =
                createInsertionReport(map, insertionOrder);

        boolean codeOrderCorrect = isSorted(codeReport);
        boolean insertionOrderCorrect =
                insertionReport.equals(insertionOrder);

        System.out.println("\n" + name);
        System.out.printf(
                "100,000 lookups average: %.3f ms%n",
                lookupTime);

        System.out.printf(
                "Average time per lookup: %.3f microseconds%n",
                lookupTime * 1000 / LOOKUPS);

        System.out.printf(
                "Code-order report: %.3f ms%n",
                codeReportTime);

        System.out.printf(
                "Insertion-order report: %.3f ms%n",
                insertionReportTime);

        System.out.println(
                "Code order correct: " + codeOrderCorrect);

        System.out.println(
                "Insertion order correct: " + insertionOrderCorrect);
    }

    private static double averageLookupTime(
            Map<String, Integer> map,
            String[] queries) {

        long total = 0;

        for (int r = 0; r < REPETITIONS; r++) {
            long start = System.nanoTime();

            long sum = 0;

            for (String code : queries) {
                sum += map.get(code);
            }

            long end = System.nanoTime();

            checksum += sum;
            total += end - start;
        }

        return total / (double) REPETITIONS / 1_000_000.0;
    }

    private static List<String> createCodeReport(
            Map<String, Integer> map) {

        List<String> report = new ArrayList<>(map.keySet());

        if (!(map instanceof TreeMap)) {
            Collections.sort(report);
        }

        return report;
    }

    private static List<String> createInsertionReport(
            Map<String, Integer> map,
            List<String> insertionOrder) {

        List<String> report = new ArrayList<>();

        if (map instanceof LinkedHashMap) {
            report.addAll(map.keySet());
        } else {
            for (String code : insertionOrder) {
                if (map.containsKey(code)) {
                    report.add(code);
                }
            }
        }

        return report;
    }

    private static double averageCodeReportTime(
            Map<String, Integer> map) {

        long total = 0;

        for (int r = 0; r < REPETITIONS; r++) {
            long start = System.nanoTime();

            List<String> report = createCodeReport(map);

            long end = System.nanoTime();

            checksum += report.size();
            total += end - start;
        }

        return total / (double) REPETITIONS / 1_000_000.0;
    }

    private static double averageInsertionReportTime(
            Map<String, Integer> map,
            List<String> insertionOrder) {

        long total = 0;

        for (int r = 0; r < REPETITIONS; r++) {
            long start = System.nanoTime();

            List<String> report =
                    createInsertionReport(map, insertionOrder);

            long end = System.nanoTime();

            checksum += report.size();
            total += end - start;
        }

        return total / (double) REPETITIONS / 1_000_000.0;
    }

    private static boolean isSorted(List<String> report) {

        for (int i = 1; i < report.size(); i++) {
            if (report.get(i - 1).compareTo(report.get(i)) > 0) {
                return false;
            }
        }

        return true;
    }
}
