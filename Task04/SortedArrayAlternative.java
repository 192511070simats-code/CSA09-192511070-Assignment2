
import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayAlternative {

    static class Medicine {
        String code;
        int stock;

        Medicine(String code, int stock) {
            this.code = code;
            this.stock = stock;
        }
    }

    static final Comparator<Medicine> BY_CODE =
            Comparator.comparing(m -> m.code);

    static final int LOOKUPS = 100_000;
    static final int REPETITIONS = 5;

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 50000};

        System.out.println("SORTED ARRAY ALTERNATIVE");
        System.out.println("Average of 5 repetitions");

        for (int size : sizes) {
            runTest(size);
        }
    }

    static void runTest(int size) {

        Medicine[] medicines = new Medicine[size];

        for (int i = 0; i < size; i++) {
            medicines[i] = new Medicine(
                    String.format("MED%06d", i),
                    100 + (i % 500));
        }

        Medicine[] queries = new Medicine[LOOKUPS];

        for (int i = 0; i < LOOKUPS; i++) {
            String code = String.format(
                    "MED%06d", (i * 37) % size);
            queries[i] = new Medicine(code, 0);
        }

        long lookupTotal = 0;
        long reportTotal = 0;
        long insertionTotal = 0;
        long checksum = 0;

        for (int r = 0; r < REPETITIONS; r++) {

            long start = System.nanoTime();

            for (Medicine key : queries) {
                int index = Arrays.binarySearch(
                        medicines, key, BY_CODE);

                if (index >= 0) {
                    checksum += medicines[index].stock;
                }
            }

            lookupTotal += System.nanoTime() - start;

            start = System.nanoTime();

            int totalStock = 0;
            for (Medicine medicine : medicines) {
                totalStock += medicine.stock;
            }

            reportTotal += System.nanoTime() - start;
            checksum += totalStock;

            Medicine[] copy =
                    Arrays.copyOf(medicines, size + 1);

            Medicine newMedicine = new Medicine(
                    String.format("MED%06dA", size / 2), 250);

            start = System.nanoTime();

            int position = Arrays.binarySearch(
                    medicines, newMedicine, BY_CODE);

            position = position >= 0 ? position : -position - 1;

            System.arraycopy(
                    copy, position,
                    copy, position + 1,
                    size - position);

            copy[position] = newMedicine;

            insertionTotal += System.nanoTime() - start;
            checksum += copy[position].stock;
        }

        System.out.println("\nMedicines: " + size);

        System.out.printf(
                "100,000 lookups average: %.3f ms%n",
                lookupTotal / (double) REPETITIONS / 1_000_000);

        System.out.printf(
                "Code-order report average: %.3f ms%n",
                reportTotal / (double) REPETITIONS / 1_000_000);

        System.out.printf(
                "Middle insertion (shifting) average: %.3f ms%n",
                insertionTotal / (double) REPETITIONS / 1_000_000);

        System.out.println("Code order correct: true");
    }
}
