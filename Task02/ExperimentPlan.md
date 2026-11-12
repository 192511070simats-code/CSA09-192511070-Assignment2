
# Task 02 - Playlist Performance Experiment Plan

## 1. Objective
To compare the performance of ArrayList, LinkedList and ArrayDeque when performing different playlist operations.

## 2. Experimental Setup
- Java Version: JDK 21.0.13 (Temurin)
- Measurement Method: System.nanoTime()
- Warm-up runs: 10
- Measured repetitions: 10
- Operations per test: 1000

## 3. Workloads

| Workload | Initial Playlist Size |
|---|---:|
| Small | 1,000 songs |
| Medium | 5,000 songs |
| Large | 10,000 songs |

## 4. Operations Tested

### ArrayList and LinkedList
- Add at end
- Insert at front
- Remove at front
- Insert in middle
- Remove in middle
- Access middle element by index
- Iterate through all elements

### ArrayDeque
- Add at end
- Add at front
- Remove at front
- Remove at end
- Iterate through all elements

ArrayDeque does not directly support indexed access or insertion and removal by index.

## 5. Predictions

1. ArrayList will provide fast indexed access because it uses a resizable array.
2. LinkedList will perform better for insertion and removal at the front because it uses linked nodes.
3. ArrayList may be slower for front insertion and removal because elements must be shifted.
4. LinkedList will be slower for indexed access because it must traverse nodes.
5. ArrayDeque will be efficient for adding and removing elements at both ends.
6. ArrayList is expected to be suitable for a general playlist, while ArrayDeque is expected to be suitable for an Up Next queue.

## 6. Measurement Method
Each operation is measured using System.nanoTime(). Ten warm-up runs are performed before the measured tests. The average time of ten measured repetitions is recorded for each operation.

## 7. Verification
The program completes all experiments and displays a verification checksum to confirm that the measured access and iteration operations were executed.
