
# Task 05 - Generic Ranking Component

## 1. Objective
Design a reusable generic Leaderboard<T> using Java generics and Comparator.

## 2. Design Decision
Use a separate Comparator instead of implementing Comparable<Team>.
This separates ranking rules from the Team class and allows the same generic leaderboard to rank different types.

## 3. Ranking Rules
1. Points: highest first.
2. Goal difference: highest first.
3. Team name: alphabetical order (A-Z).

## 4. Predictions
- Comparator.comparingInt() will compare integer fields safely.
- thenComparing() will apply tie-breakers in order.
- List.sort() will sort the ranking.
- The original list should remain unchanged when ranking() returns a sorted copy.
- The comparator should satisfy transitivity and equality consistency.

## 5. Test Datasets
Dataset 1:
- Multiple teams with equal points.
- Multiple teams with equal goal difference.
- Different team names.

Dataset 2:
- Negative and positive point values.
- Extreme integer values to test subtraction overflow.
- Teams with equal ranking fields.

## 6. Alternative Approaches
Compare:
- Comparator chain.
- Comparable<Team>.
- Three stable sorts: name, goal difference, then points.

## 7. Complexity Analysis
Analyse time and extra space for each approach.

## 8. Final Evaluation
Explain why the chosen design is reusable and why its comparator satisfies the ranking principle.
