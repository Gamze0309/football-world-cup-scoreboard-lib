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

    /**
     * Creates a new match between two teams.
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws IllegalArgumentException if either team name is null or empty after trimming
     */
    public Match(String homeTeam, String awayTeam) {
        String normalizedHomeTeam = validateAndNormalizeTeamName(homeTeam);
        String normalizedAwayTeam = validateAndNormalizeTeamName(awayTeam);

        this.homeTeam = normalizedHomeTeam;
        this.awayTeam = normalizedAwayTeam;
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
        return 0;
    }

    /**
     * Returns the current score of the away team.
     *
     * @return the away team's score
     */
    public int getAwayScore() {
        return 0;
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
    private static String validateAndNormalizeTeamName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }

        String normalized = name.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be null or empty");
        }

        return normalized;
    }
}
