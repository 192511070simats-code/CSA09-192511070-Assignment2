
public class StringPerformanceTest {

    static volatile String[] retainedOutput;

    public static void main(String[] args) throws InterruptedException {

        int repetitions = 10;

        System.out.println("STRING PERFORMANCE COMPARISON");
        System.out.println("Repetitions: " + repetitions);

        runExperiment("Small", 1, 6, repetitions);
        runExperiment("Large", 10000, 50, repetitions);
        runExperiment("Custom", 5000, 100, repetitions);
    }

    // Run one complete workload experiment
    public static void runExperiment(
            String workload, int tickets, int fields, int repetitions)
            throws InterruptedException {

        String[][] data = createData(tickets, fields);

        System.out.println("\n==================================");
        System.out.println("Workload: " + workload);
        System.out.println("Number of tickets: " + tickets);
        System.out.println("Fields per ticket: " + fields);
        System.out.println("==================================");

        // Warm-up runs
        buildUsingString(data);
        buildUsingStringBuilder(data);
        buildUsingStringBuffer(data);

        // String measurement
        long stringTotalTime = 0;
        int stringResult = 0;

        for (int i = 0; i < repetitions; i++) {
            long startTime = System.nanoTime();

            String[] output = buildUsingString(data);
            stringResult = calculateTotalLength(output);

            long endTime = System.nanoTime();
            stringTotalTime += endTime - startTime;
        }

        long stringAverageTime = stringTotalTime / repetitions;

        // StringBuilder measurement
        long builderTotalTime = 0;
        int builderResult = 0;

        for (int i = 0; i < repetitions; i++) {
            long startTime = System.nanoTime();

            String[] output = buildUsingStringBuilder(data);
            builderResult = calculateTotalLength(output);

            long endTime = System.nanoTime();
            builderTotalTime += endTime - startTime;
        }

        long builderAverageTime = builderTotalTime / repetitions;

        // StringBuffer measurement
        long bufferTotalTime = 0;
        int bufferResult = 0;

        for (int i = 0; i < repetitions; i++) {
            long startTime = System.nanoTime();

            String[] output = buildUsingStringBuffer(data);
            bufferResult = calculateTotalLength(output);

            long endTime = System.nanoTime();
            bufferTotalTime += endTime - startTime;
        }

        long bufferAverageTime = bufferTotalTime / repetitions;

        // Display execution-time results
        System.out.println("\n--- Execution Time Results ---");

        System.out.println("String total length: " + stringResult);
        System.out.println("String average time: " + stringAverageTime + " ns");

        System.out.println("StringBuilder total length: " + builderResult);
        System.out.println("StringBuilder average time: " + builderAverageTime + " ns");

        System.out.println("StringBuffer total length: " + bufferResult);
        System.out.println("StringBuffer average time: " + bufferAverageTime + " ns");

        // Approximate memory measurement
        System.out.println("\n--- Approximate Memory Usage ---");

        System.gc();
        Thread.sleep(100);

        long stringBefore = getUsedMemory();
        retainedOutput = buildUsingString(data);
        long stringAfter = getUsedMemory();

        System.out.println("String memory before: " + stringBefore / 1024 + " KB");
        System.out.println("String memory after: " + stringAfter / 1024 + " KB");
        System.out.println("String memory difference: "
                + (stringAfter - stringBefore) / 1024 + " KB");

        retainedOutput = null;
        System.gc();
        Thread.sleep(100);

        long builderBefore = getUsedMemory();
        retainedOutput = buildUsingStringBuilder(data);
        long builderAfter = getUsedMemory();

        System.out.println("StringBuilder memory before: " + builderBefore / 1024 + " KB");
        System.out.println("StringBuilder memory after: " + builderAfter / 1024 + " KB");
        System.out.println("StringBuilder memory difference: "
                + (builderAfter - builderBefore) / 1024 + " KB");

        retainedOutput = null;
        System.gc();
        Thread.sleep(100);

        long bufferBefore = getUsedMemory();
        retainedOutput = buildUsingStringBuffer(data);
        long bufferAfter = getUsedMemory();

        System.out.println("StringBuffer memory before: " + bufferBefore / 1024 + " KB");
        System.out.println("StringBuffer memory after: " + bufferAfter / 1024 + " KB");
        System.out.println("StringBuffer memory difference: "
                + (bufferAfter - bufferBefore) / 1024 + " KB");

        // Verify output lengths
        System.out.println("\nAll three methods generated equal output lengths: "
                + (stringResult == builderResult && builderResult == bufferResult));

        retainedOutput = null;
    }

    // Get currently used heap memory
    public static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    // Calculate total length of generated tickets
    public static int calculateTotalLength(String[] output) {
        int totalLength = 0;

        for (String ticket : output) {
            totalLength += ticket.length();
        }

        return totalLength;
    }

    // Generate ticket data
    public static String[][] createData(int tickets, int fields) {
        String[][] data = new String[tickets][fields];

        for (int i = 0; i < tickets; i++) {
            for (int j = 0; j < fields; j++) {
                data[i][j] = "FIELD-" + i + "-" + j;
            }
        }

        return data;
    }

    // String method
    public static String[] buildUsingString(String[][] data) {
        String[] output = new String[data.length];

        for (int i = 0; i < data.length; i++) {
            String ticket = "";

            for (int j = 0; j < data[i].length; j++) {
                ticket = ticket + data[i][j];
            }

            output[i] = ticket;
        }

        return output;
    }

    // StringBuilder method
    public static String[] buildUsingStringBuilder(String[][] data) {
        String[] output = new String[data.length];

        for (int i = 0; i < data.length; i++) {
            StringBuilder ticket = new StringBuilder();

            for (int j = 0; j < data[i].length; j++) {
                ticket.append(data[i][j]);
            }

            output[i] = ticket.toString();
        }

        return output;
    }

    // StringBuffer method
    public static String[] buildUsingStringBuffer(String[][] data) {
        String[] output = new String[data.length];

        for (int i = 0; i < data.length; i++) {
            StringBuffer ticket = new StringBuffer();

            for (int j = 0; j < data[i].length; j++) {
                ticket.append(data[i][j]);
            }

            output[i] = ticket.toString();
        }

        return output;
    }
}
