package org.scoreboard.model;

/**
 * Represents a football match between two teams with their respective scores.
 * <p>
 * This class encapsulates the details of a match including the home team,
 * away team, and their current scores.
 * </p>
 */
public class Match {
    private final String homeTeam;
    private final String awayTeam;
    private final int homeScore;
    private final int awayScore;

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
        this(homeTeam, awayTeam, 0, 0);
    }

    /**
     * Private constructor for creating a match with specific scores.
     * <p>
     * This constructor validates and normalizes team names and ensures they are different.
     * It is used internally for creating new match instances with updated scores.
     * </p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @param homeScore the score of the home team
     * @param awayScore the score of the away team
     * @throws IllegalArgumentException if either team name is null or empty after trimming,
     *                                  or if both team names are the same (case-insensitive)
     */
    private Match(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        String normalizedHomeTeam = validateAndNormalizeTeamName(homeTeam);
        String normalizedAwayTeam = validateAndNormalizeTeamName(awayTeam);

        validateTeamsDifferent(normalizedHomeTeam, normalizedAwayTeam);

        this.homeTeam = normalizedHomeTeam;
        this.awayTeam = normalizedAwayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
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
        return new Match(homeTeam, awayTeam, homeScore, awayScore);
    }

    /**
     * Returns the name of the home team.
     *
     * @return the home team name
     */
    public String getHomeTeam() {
        return homeTeam;
    }

    /**
     * Returns the name of the away team.
     *
     * @return the away team name
     */
    public String getAwayTeam() {
        return awayTeam;
    }

    /**
     * Returns the current score of the home team.
     *
     * @return the home team's score
     */
    public int getHomeScore() {
        return homeScore;
    }

    /**
     * Returns the current score of the away team.
     *
     * @return the away team's score
     */
    public int getAwayScore() {
        return awayScore;
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
}
