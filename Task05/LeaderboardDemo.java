
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

interface Rankable {
}

class Team implements Rankable {
    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T extends Rankable> {
    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    void add(T item) {
        items.add(item);
    }

    // Accepts a list of T or any subtype of T.
    void addAll(List<? extends T> newItems) {
        items.addAll(newItems);
    }

    List<T> ranking() {
        List<T> sortedCopy = new ArrayList<>(items);
        sortedCopy.sort(order);
        return sortedCopy;
    }
}

public class LeaderboardDemo {

    public static void main(String[] args) {

        // Points: high to low, goal difference: high to low,
        // name: alphabetical order.
        Comparator<Team> byRank =
                Comparator.comparingInt((Team t) -> t.points)
                        .reversed()
                        .thenComparing(
                                Comparator.comparingInt(
                                        (Team t) -> t.goalDiff)
                                        .reversed())
                        .thenComparing(t -> t.name);

        System.out.println("DATASET 1: MULTI-LEVEL TIE BREAKING");

        Leaderboard<Team> board = new Leaderboard<>(byRank);

        List<Team> teams = new ArrayList<>();
        teams.add(new Team("Panthers", 9, 4));
        teams.add(new Team("Eagles", 9, 6));
        teams.add(new Team("Cobras", 6, 1));
        teams.add(new Team("Bisons", 9, 4));
        teams.add(new Team("Falcons", 6, 1));
        teams.add(new Team("Dolphins", 9, 6));

        board.addAll(teams);

        System.out.println("Ranking: " + board.ranking());

        System.out.println("\nDATASET 2: EXTREME INTEGER VALUES");

        Leaderboard<Team> extremeBoard =
                new Leaderboard<>(byRank);

        extremeBoard.add(new Team(
                "Maximum", Integer.MAX_VALUE, 5));
        extremeBoard.add(new Team("Middle", 0, 3));
        extremeBoard.add(new Team(
                "Minimum", Integer.MIN_VALUE, 8));
        extremeBoard.add(new Team("Alpha", 0, 3));

        System.out.println("Ranking: " + extremeBoard.ranking());

        System.out.println("\nFAULTY COMPARATOR TEST");

        // Subtraction can overflow and produce incorrect ordering.
        Comparator<Team> faulty =
                (a, b) -> b.points - a.points;

        Team maximum =
                new Team("Maximum", Integer.MAX_VALUE, 5);
        Team minimum =
                new Team("Minimum", Integer.MIN_VALUE, 8);
        Team middle =
                new Team("Middle", 0, 3);

        int maxVsMiddle =
                Integer.signum(faulty.compare(maximum, middle));
        int middleVsMin =
                Integer.signum(faulty.compare(middle, minimum));
        int maxVsMin =
                Integer.signum(faulty.compare(maximum, minimum));

        System.out.println(
                "Maximum vs Middle: " + maxVsMiddle);
        System.out.println(
                "Middle vs Minimum: " + middleVsMin);
        System.out.println(
                "Maximum vs Minimum: " + maxVsMin);

        System.out.println(
                "Faulty comparator transitivity check: "
                + (maxVsMiddle <= 0
                && middleVsMin <= 0
                && maxVsMin <= 0));

        System.out.println("\nAll experiments completed.");
    }
}
