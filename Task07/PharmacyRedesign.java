
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class Medicine {
    final int code;
    final String name;

    Medicine(int code, String name) {
        this.code = code;
        this.name = name;
    }
}

public class PharmacyRedesign {

    // Prevents the report calculation from being discarded.
    private static volatile long resultChecksum = 0;

    // Original design: HashMap only.
    static class OriginalInventory {
        HashMap<Integer, Medicine> medicines = new HashMap<>();

        void add(Medicine medicine) {
            medicines.put(medicine.code, medicine);
        }

        long generateCodeReport() {
            ArrayList<Integer> codes =
                    new ArrayList<>(medicines.keySet());

            // Re-sorting is repeated every time a report is requested.
            Collections.sort(codes);

            long checksum = 0;
            for (int code : codes) {
                checksum += code;
                checksum += medicines.get(code).name.length();
            }
            return checksum;
        }
    }

    // Redesigned: HashMap for lookup + TreeMap for sorted reporting.
    static class RedesignedInventory {
        HashMap<Integer, Medicine> lookupIndex =
                new HashMap<>();

        TreeMap<Integer, Medicine> sortedIndex =
                new TreeMap<>();

        void add(Medicine medicine) {
            lookupIndex.put(medicine.code, medicine);
            sortedIndex.put(medicine.code, medicine);
        }

        long generateCodeReport() {
            // TreeMap already maintains keys in sorted order.
            long checksum = 0;

            for (Map.Entry<Integer, Medicine> entry
                    : sortedIndex.entrySet()) {
                checksum += entry.getKey();
                checksum += entry.getValue().name.length();
            }
            return checksum;
        }
    }

    private static void runExperiment(int size) {

        OriginalInventory original = new OriginalInventory();
        RedesignedInventory redesigned =
                new RedesignedInventory();

        Medicine[] data = new Medicine[size];

        // Prepare identical data before timing.
        for (int i = 0; i < size; i++) {
            int code = (i * 7919) % size;
            data[i] = new Medicine(code, "Medicine" + code);
        }

        long originalAddStart = System.nanoTime();

        for (Medicine medicine : data) {
            original.add(medicine);
        }

        long originalAddTime =
                System.nanoTime() - originalAddStart;

        long redesignedAddStart = System.nanoTime();

        for (Medicine medicine : data) {
            redesigned.add(medicine);
        }

        long redesignedAddTime =
                System.nanoTime() - redesignedAddStart;

        int repetitions = 5;
        int reportsPerRun = 100;

        // Warm-up both report methods.
        for (int i = 0; i < 5; i++) {
            resultChecksum = original.generateCodeReport();
            resultChecksum = redesigned.generateCodeReport();
        }

        long originalReportTotal = 0;
        long redesignedReportTotal = 0;

        long originalChecksum = 0;
        long redesignedChecksum = 0;

        for (int run = 0; run < repetitions; run++) {

            long startOriginal = System.nanoTime();

            for (int j = 0; j < reportsPerRun; j++) {
                originalChecksum =
                        original.generateCodeReport();
            }

            originalReportTotal +=
                    System.nanoTime() - startOriginal;

            long startRedesigned = System.nanoTime();

            for (int j = 0; j < reportsPerRun; j++) {
                redesignedChecksum =
                        redesigned.generateCodeReport();
            }

            redesignedReportTotal +=
                    System.nanoTime() - startRedesigned;
        }

        resultChecksum =
                originalChecksum + redesignedChecksum;

        double originalReportMs =
                originalReportTotal / 1_000_000.0;

        double redesignedReportMs =
                redesignedReportTotal / 1_000_000.0;

        double originalAverage =
                originalReportMs / (repetitions * reportsPerRun);

        double redesignedAverage =
                redesignedReportMs / (repetitions * reportsPerRun);

        System.out.println("\nMedicine count: " + size);

        System.out.printf(
                "Original HashMap insertion: %.3f ms%n",
                originalAddTime / 1_000_000.0);

        System.out.printf(
                "Redesigned dual-index insertion: %.3f ms%n",
                redesignedAddTime / 1_000_000.0);

        System.out.printf(
                "Original report average: %.6f ms/report%n",
                originalAverage);

        System.out.printf(
                "Redesigned report average: %.6f ms/report%n",
                redesignedAverage);

        System.out.println(
                "Report results match: "
                + (originalChecksum == redesignedChecksum));

        System.out.println(
                "Sorted index size correct: "
                + (redesigned.sortedIndex.size() == size));
    }

    public static void main(String[] args) {

        System.out.println(
                "TASK 07: ORIGINAL VS REDESIGNED PHARMACY INVENTORY");

        int[] sizes = {1000, 10000, 50000};

        for (int size : sizes) {
            runExperiment(size);
        }

        System.out.println("\nAll redesign experiments completed.");
    }
}
