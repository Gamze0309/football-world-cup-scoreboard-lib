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
     */
    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
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
}
