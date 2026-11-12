
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;

class Student {
    private final String regNo;
    private final String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return regNo.equals(((Student) o).regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }

    @Override
    public String toString() {
        return regNo + " " + name;
    }
}

public class Attendance {
    public static void main(String[] args) {

        // Original faculty example
        System.out.println("ORIGINAL ATTENDANCE TEST");

        Set<Student> present = new HashSet<>();

        present.add(new Student("192311001", "Arun"));
        present.add(new Student("192311002", "Divya"));
        present.add(new Student("192311001", "Arun"));

        System.out.println("Unique students: " + present.size());


        // TEST 1: Same object scanned twice
        System.out.println("\nTEST 1: Same object scanned twice");

        Set<Student> test1 = new HashSet<>();
        Student student1 = new Student("192311001", "Arun");

        test1.add(student1);
        test1.add(student1);

        System.out.println("Predicted set size: 1");
        System.out.println("Actual set size: " + test1.size());


        // TEST 2: Separate objects with the same registration number
        System.out.println("\nTEST 2: Duplicate student objects");

        Set<Student> test2 = new HashSet<>();

        test2.add(new Student("192311001", "Arun"));
        test2.add(new Student("192311001", "Arun"));

        System.out.println("Predicted set size: 1");
        System.out.println("Actual set size: " + test2.size());


        // TEST 3: TreeSet comparator inconsistent with equals
        System.out.println("\nTEST 3: TreeSet comparator inconsistency");

        Set<Student> test3 = new TreeSet<>(
                Comparator.comparing(student -> student.toString().split(" ")[1])
        );

        test3.add(new Student("192311001", "Arun"));
        test3.add(new Student("192311001", "Kumar"));

        System.out.println("Predicted set size: 2");
        System.out.println("Actual set size: " + test3.size());

        System.out.println("\nAll experiments completed.");
    }
}
