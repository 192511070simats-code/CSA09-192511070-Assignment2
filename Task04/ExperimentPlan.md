# Task 04 - Choosing a Map for Pharmacy Inventory

## 1. Objective
To compare HashMap, TreeMap and LinkedHashMap for pharmacy inventory stock lookup and report generation.

## 2. Predictions Before Implementation

| Map | put() | get() | Iteration Order |
|---|---|---|---|
| HashMap | O(1) average | O(1) average | No guaranteed order |
| TreeMap | O(log n) | O(log n) | Sorted by key |
| LinkedHashMap | O(1) average | O(1) average | Insertion order |

## 3. Experimental Workloads
Test three inventory sizes:
- 1,000 medicines
- 10,000 medicines
- 50,000 medicines

Measure:
- Time for 100,000 stock lookups.
- Time to generate the medicine-code sorted report.
- Time to generate the insertion-order report.
- Whether each report has the required order.

## 4. Expected Report Behaviour
- TreeMap naturally produces the code-order report.
- LinkedHashMap naturally produces the insertion-order report.
- HashMap requires sorting for the code-order report and extra ordering information for the insertion-order report.

## 5. Scalability Criterion
Define an acceptable single stock lookup response time of 1 millisecond or less. Compare measured results against this criterion.

## 6. Alternative Approach
Use an array sorted by medicine code and Arrays.binarySearch() for lookup. Analyse lookup, insertion, reporting and space complexity.

## 7. Final Evaluation
Use experimental results to recommend a suitable map for the hospital pharmacy and explain why Hashtable is not preferred in modern Java applications.