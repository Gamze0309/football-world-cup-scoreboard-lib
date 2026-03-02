package org.scoreboard.repository;

import java.util.List;

import org.scoreboard.model.Match;

/**
 * Repository interface for managing scoreboard data operations.
 * <p>
 * This interface defines the contract for scoreboard data persistence,
 * including operations to start, update, finish matches, and retrieve match information.
 * </p>
 */
public interface ScoreboardRepository {
    /**
     * Starts a new match between two teams and adds it to the scoreboard.
     * <p>
     * The match is initialized with a score of 0-0. Team names are normalized by trimming whitespace.
     * </p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws IllegalArgumentException if either team name is null or empty after trimming,
     *                                  or if both team names are the same (case-insensitive)
     * @throws IllegalStateException if either team already has an active match
     */
    void startMatch(String homeTeam, String awayTeam);
    
    /**
     * Returns all matches currently tracked by the scoreboard.
     *
     * @return a list of all matches
     */
    List<Match> getAllMatches();
    
    /**
     * Updates the score of an existing match on the scoreboard.
     * <p>
     * Finds the match by team names (case-insensitive) and updates it with the new scores.
     * Team names are normalized by trimming whitespace.
     * </p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @param homeScore the new score for the home team
     * @param awayScore the new score for the away team
     * @throws IllegalArgumentException if either team name is null or empty after trimming,
     *                                  or if either score is negative
     * @throws IllegalStateException if the match is not found on the scoreboard
     */
    void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore);
    
    /**
     * Finishes a match and removes it from the scoreboard.
     * <p>
     * Finds the match by team names (case-insensitive) and removes it from the active matches.
     * Team names are normalized by trimming whitespace.
     * </p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws IllegalArgumentException if either team name is null or empty after trimming
     * @throws IllegalStateException if the match is not found on the scoreboard
     */
    void finishMatch(String homeTeam, String awayTeam);
}
