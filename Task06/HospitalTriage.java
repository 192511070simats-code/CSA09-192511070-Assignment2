
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

class Patient {
    final String name;
    final int severity;
    final long arrival;

    Patient(String name, int severity, long arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}

public class HospitalTriage {

    private long counter = 0;

    // Lower severity number means higher priority.
    // Earlier arrival wins when severity is equal.
    private final Comparator<Patient> triageOrder =
            Comparator.comparingInt((Patient p) -> p.severity)
                    .thenComparingLong(p -> p.arrival);

    private final PriorityQueue<Patient> queue =
            new PriorityQueue<>(triageOrder);

    void admit(String name, int severity) {
        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException(
                    "Severity must be between 1 and 5.");
        }

        queue.add(new Patient(name, severity, counter++));
    }

    Patient next() {
        return queue.poll();
    }

    void displayWaitingList() {
        // A PriorityQueue iterator does not guarantee treatment order.
        PriorityQueue<Patient> copy =
                new PriorityQueue<>(queue);

        System.out.print("Waiting list in treatment order: ");

        while (!copy.isEmpty()) {
            System.out.print(copy.poll() + " ");
        }

        System.out.println();
    }

    private static void correctnessTest() {
        System.out.println("EIGHT-PATIENT CORRECTNESS TEST");

        HospitalTriage er = new HospitalTriage();

        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);
        er.admit("Priya", 3);
        er.admit("Dev", 2);

        er.displayWaitingList();

        StringBuilder actual = new StringBuilder();

        Patient patient;
        while ((patient = er.next()) != null) {
            actual.append(patient).append(" ");
        }

        String expected =
                "Meena(S1) Kumar(S1) Lata(S2) Dev(S2) "
                + "Ravi(S3) John(S3) Priya(S3) Asha(S5)";

        System.out.println("Actual treatment order: " + actual.toString().trim());
        System.out.println("Expected treatment order: " + expected);
        System.out.println("Correctness check: "
                + expected.equals(actual.toString().trim()));
    }

    private static void growthTest() {
        System.out.println("\nGROWTH INVESTIGATION");
        System.out.println("Average time per operation over 5 repetitions");

        int[] sizes = {1000, 10000, 50000};
        int repetitions = 5;

        for (int size : sizes) {
            long totalAddTime = 0;
            long totalRemoveTime = 0;

            for (int run = 0; run < repetitions; run++) {

                List<Patient> patients = new ArrayList<>();

                for (int i = 0; i < size; i++) {
                    patients.add(new Patient(
                            "Patient" + i,
                            (i % 5) + 1,
                            i));
                }

                PriorityQueue<Patient> addQueue =
                        new PriorityQueue<>(
                                Comparator.comparingInt(
                                        (Patient p) -> p.severity)
                                        .thenComparingLong(p -> p.arrival));

                long startAdd = System.nanoTime();

                for (Patient p : patients) {
                    addQueue.add(p);
                }

                totalAddTime += System.nanoTime() - startAdd;

                // Prepare a separate full queue before measuring removal.
                PriorityQueue<Patient> removeQueue =
                        new PriorityQueue<>(addQueue);

                long startRemove = System.nanoTime();

                while (!removeQueue.isEmpty()) {
                    removeQueue.poll();
                }

                totalRemoveTime += System.nanoTime() - startRemove;
            }

            double averageAddNs =
                    (double) totalAddTime / (repetitions * size);

            double averageRemoveNs =
                    (double) totalRemoveTime / (repetitions * size);

            System.out.printf(
                    "Size: %d | Add: %.2f ns/patient | Remove: %.2f ns/patient%n",
                    size, averageAddNs, averageRemoveNs);
        }
    }

    public static void main(String[] args) {
        correctnessTest();
        growthTest();
        System.out.println("\nAll experiments completed.");
    }
}
