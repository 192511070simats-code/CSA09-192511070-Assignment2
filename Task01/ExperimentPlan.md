
# Task 01 – Experiment Plan

## Topic
Building Text Under Load – String, StringBuilder and StringBuffer

## Objective
To compare the performance and memory behaviour of String, StringBuilder and StringBuffer when building ticket text.

## Workloads

| Workload | Number of Tickets | Fields per Ticket |
|---|---:|---:|
| Small | 1 | 6 |
| Large | 10,000 | 50 |
| Custom | 5,000 | 100 |

## Methods to Compare
1. String concatenation using +
2. StringBuilder using append()
3. StringBuffer using append()

## Measurements
- Execution time using System.nanoTime()
- Average execution time across repeated runs
- Estimated intermediate String objects
- Approximate memory usage before and after execution

## Predictions (Before Execution)


### 1. Which method do I predict will be fastest?

I predict StringBuilder will be the fastest because it is mutable and doesn't create new objects for every concatenation. It also avoids synchronization overhead compared to StringBuffer.



### 2. Which method do I predict will be slowest?

I predict String (+) will be the slowest because String is immutable. Every concatenation creates a new String object and copies the existing content, making it inefficient for large workloads.



### 3. What will happen when the number of tickets and fields increases?

As the number of tickets and fields increases, the execution time of all three methods will increase.

- String (+): Execution time increases significantly because it creates new objects repeatedly.
- StringBuilder: Execution time increases gradually and is expected to remain the fastest.
- StringBuffer: Execution time also increases, but synchronization may make it slightly slower than StringBuilder.

Conclusion: Larger workloads require more time, but StringBuilder will generally perform best.



### 4. Which method is suitable for building individual ticket text?

I would choose StringBuilder because it is mutable, faster and provides better performance when building individual ticket text.

### 5. Which method is suitable for a shared text buffer accessed by multiple threads?

I would choose StringBuffer because its methods are synchronized and thread-safe, making it suitable for shared text operations accessed by multiple threads.

