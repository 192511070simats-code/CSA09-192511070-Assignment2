
# Task 06 - Hospital Emergency Triage

## 1. Objective
Design a hospital triage queue that treats patients with the highest medical priority first and preserves arrival order among patients with equal severity.

## 2. Design and Predictions
Severity 1 is the highest priority and severity 5 is the lowest.

A PriorityQueue with a Comparator is used:
- Compare severity in ascending order.
- If severity is equal, compare arrival counter in ascending order.

Predictions:
- Adding a patient: O(log n).
- Removing the next patient: O(log n).
- Examining the next patient using peek(): O(1).
- Space: O(n).

A plain FIFO Queue cannot enforce severity priority. A PriorityQueue is suitable because it keeps the highest-priority element at its head.

## 3. Correctness Test
Eight patients are admitted with different severity levels and arrival orders. The expected treatment order is calculated manually and compared with the program output.

## 4. Growth Investigation
Queue sizes:
- 1,000 patients
- 10,000 patients
- 50,000 patients

Five repetitions are used for each size. Average time per add and remove operation is recorded.

## 5. Waiting List Display
Printing a PriorityQueue directly does not guarantee treatment order. A copy of the queue is repeatedly polled to display patients in correct treatment order without changing the original queue.

## 6. Starvation and Fairness
A patient with severity 5 may wait indefinitely if higher-severity patients continue arriving. This is called starvation.

A possible fair rule is priority aging: increase a patient's priority after a defined waiting period, while never allowing a minor case to bypass a critical emergency without a clinical safety rule. The system should periodically recalculate effective priority and preserve arrival order for equal effective priorities.

## 7. Alternative Designs
### Sorted LinkedList
- Admission: O(n), because the correct insertion position must be found.
- Removal from the front: O(1).
- Space: O(n).

### Five FIFO Queues
Use one FIFO queue for each severity level.
- Admission: O(1).
- Removal: O(1), by checking severity queues from 1 to 5.
- Space: O(n), plus five queue objects.

### PriorityQueue
- Admission: O(log n).
- Removal: O(log n).
- Space: O(n).

## 8. Deployment Decision
A PriorityQueue can handle 5,000 daily admissions because its insertion and removal operations scale logarithmically. However, a real hospital system also needs clinical validation, persistent records, concurrent access control, monitoring, and a safe priority-aging policy. The benchmark results support the data-structure choice but do not alone prove production readiness.

## 9. Final Evaluation
Compare predicted and measured costs, verify the eight-patient treatment order, explain starvation, and justify the selected queue design.
