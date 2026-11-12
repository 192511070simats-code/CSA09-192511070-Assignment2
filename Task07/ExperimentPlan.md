
# Task 07 - Diagnose, Redesign and Validate

## A. Diagnose the Bottleneck

The original Q4 pharmacy inventory used HashMap for medicine lookup. HashMap provides average O(1) lookup, but it does not maintain medicine codes in sorted order.

For every code-order report, the program copied the keys and sorted them again. This repeated sorting creates unnecessary work.

In the previous Q4 experiment with 50,000 medicines:
- HashMap code-order report: 51.566 ms.
- TreeMap code-order report: 4.321 ms.

The limitation is repeated sorting, not the HashMap lookup operation.

## B. Design Objective

The redesign aims to:
1. Preserve fast average medicine lookup.
2. Avoid sorting all medicine codes every time a code-order report is requested.
3. Produce the same correct sorted report.
4. Measure the additional insertion cost and report improvement.

## C. Revised Design

Use a dual-index inventory:

- HashMap<Integer, Medicine> supports average O(1) lookup.
- TreeMap<Integer, Medicine> maintains medicine codes in sorted order.

When a medicine is admitted, insert it into both indexes. For a code-order report, iterate through the TreeMap directly.

Pseudocode:

    addMedicine(medicine):
        lookupIndex.put(code, medicine)
        sortedIndex.put(code, medicine)

    findMedicine(code):
        return lookupIndex.get(code)

    generateCodeReport():
        for each medicine in sortedIndex:
            print medicine

The redesign avoids copying and sorting the keys for every report.

Trade-off: It uses additional memory and makes insertion more expensive because both indexes must be updated.

## D. Prediction Before Implementation

Predictions:
- Original report generation: O(n log n), because keys are sorted for every report.
- Redesigned report generation: O(n), because TreeMap maintains sorted order.
- Original insertion: average O(1).
- Redesigned insertion: average O(1) for HashMap plus O(log n) for TreeMap.
- Redesigned storage: O(n) additional index storage.

The redesigned solution is expected to perform better when many code-order reports are requested after medicines are added.

## E. Implementation and Validation

The program compares both designs using identical medicine data at:
- 1,000 medicines.
- 10,000 medicines.
- 50,000 medicines.

Both designs generate 100 reports per run, with five measured repetitions. Insertion time is also recorded.

Report checksums are compared to verify that both designs process the same medicine records. The sorted index size is also checked.

## F. Final Evaluation

Compare measured insertion and report times. Determine whether the reduction in repeated report work justifies the extra memory and insertion cost.

The original HashMap-only design may still be preferable when code-order reports are rare and lookup speed and low memory usage are the main priorities.

The dual-index design is preferable when code-order reports are requested frequently.
