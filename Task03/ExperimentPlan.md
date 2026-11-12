
# Task 03 - HashSet and hashCode-equals Contract

## 1. Objective
To investigate how equals() and hashCode() affect duplicate removal in HashSet and compare the behaviour with TreeSet.

## 2. Experimental Setup
- Java Version: JDK 21.0.13 (Temurin)
- Data Structure: HashSet<Student> and TreeSet<Student>
- Equality Rule: Students are equal when their registration numbers match.
- Measurement: Compare predicted and actual set sizes.

## 3. Experiments

### Test 1 - Same Object Scanned Twice
Insert the same Student object twice into a HashSet.

### Test 2 - Duplicate Student Objects
Insert two different Student objects with the same registration number.

### Test 3 - TreeSet Comparator Inconsistency
Use a TreeSet comparator based on student names, while equals() compares registration numbers.

## 4. Predictions
- Test 1: Expected set size is 1.
- Test 2: Expected set size is 1 after implementing hashCode().
- Test 3: Expected set size is 2 because the comparator considers the different names unequal.

## 5. Verification
The program displays the predicted and actual set sizes for all three experiments.

## 6. Correction
The Student class overrides hashCode() using regNo.hashCode(), ensuring consistency with equals().
