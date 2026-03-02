package org.scoreboard.model;

/**
 * Represents an immutable football match between two teams with their respective scores.
 * <p>
 * This record encapsulates the details of a match including the home team,
 * away team, and their current scores. Team names are automatically normalized
 * by trimming whitespace during construction.
 * </p>
 */
public record Match(String homeTeam, String awayTeam, int homeScore, int awayScore, long insertionOrder) {

    /**
     * Creates a new match between two teams with initial score of 0-0.
     * <p>
     * Team names are normalized by trimming leading and trailing whitespace.
     * </p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws IllegalArgumentException if either team name is null or empty after trimming,
     *                                  or if both team names are the same (case-insensitive)
     */
    public Match(String homeTeam, String awayTeam) {
        this(homeTeam, awayTeam, 0, 0, 0);
    }

    /**
     * Canonical constructor that validates and normalizes team names.
     * <p>
     * This compact constructor validates that team names are not null or empty,
     * normalizes them by trimming whitespace, and ensures they are different.
     * </p>
     *
     * @throws IllegalArgumentException if either team name is null or empty after trimming,
     *                                  or if both team names are the same (case-insensitive)
     */
    public Match {
        homeTeam = validateAndNormalizeTeamName(homeTeam);
        awayTeam = validateAndNormalizeTeamName(awayTeam);
        validateTeamsDifferent(homeTeam, awayTeam);
        validateScores(homeScore, awayScore);
    }

    /**
     * Creates a new Match instance with updated scores.
     * <p>
     * Since Match is immutable, this method returns a new Match object
     * with the same teams but updated scores.
     * </p>
     *
     * @param homeScore the new score for the home team
     * @param awayScore the new score for the away team
     * @return a new Match instance with the updated scores
     */
    public Match updateScore(int homeScore, int awayScore) {
        return new Match(homeTeam, awayTeam, homeScore, awayScore, insertionOrder);
    }

    /**
     * Creates a new Match instance with a specified insertion order.
     * <p>
     * Since Match is immutable, this method returns a new Match object
     * with the same teams and scores but with the specified insertion order.
     * The insertion order is used to maintain the chronological order of matches
     * when they have the same total score.
     * </p>
     *
     * @param insertionOrder the insertion order value to assign to the match
     * @return a new Match instance with the specified insertion order
     */
    public Match withInsertionOrder(long insertionOrder) {
        return new Match(homeTeam, awayTeam, homeScore, awayScore, insertionOrder);
    }

    /**
     * Validates and normalizes a team name by trimming whitespace.
     * <p>
     * This method ensures that the team name is not null or empty (after trimming)
     * and returns the normalized version with leading and trailing whitespace removed.
     * </p>
     *
     * @param name the team name to validate and normalize
     * @return the normalized team name with whitespace trimmed
     * @throws IllegalArgumentException if the name is null or empty after trimming
     */
    public static String validateAndNormalizeTeamName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }

        String normalized = name.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }

        return normalized;
    }

    private static void validateTeamsDifferent(String homeTeam, String awayTeam) {
        if (homeTeam.equalsIgnoreCase(awayTeam)) {
            throw new IllegalArgumentException("Team names cannot be the same");
        }
    }

    private static void validateScores(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
    }
}
