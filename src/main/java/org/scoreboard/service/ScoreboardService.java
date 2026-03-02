package org.scoreboard.service;

import java.util.Comparator;
import java.util.List;

import org.scoreboard.model.Match;
import org.scoreboard.repository.ScoreboardRepository;

/**
 * Service class for managing a football world cup scoreboard.
 * <p>
 * This service maintains a collection of ongoing matches and provides
 * operations to start matches and retrieve match information.
 * </p>
 */
public class ScoreboardService {
    private final ScoreboardRepository scoreboardRepository;

    /**
     * Constructs a new ScoreboardService with the specified repository.
     *
     * @param scoreboardRepository the repository to use for storing match data
     */
    public ScoreboardService(ScoreboardRepository scoreboardRepository) {
        this.scoreboardRepository = scoreboardRepository;
    }
    
    /**
     * Returns a summary of all matches currently tracked by the scoreboard.
     * <p>
     * The matches are sorted by their total score (home score + away score)
     * in descending order, with the highest-scoring matches appearing first.
     * </p>
     *
     * @return a list of all matches sorted by total score in descending order
     */
    public List<Match> getAllMatchesSummary() {
        Comparator<Match> sortOrder = Comparator
            .comparingInt((Match match) -> match.homeScore() + match.awayScore())
            .reversed();

        return scoreboardRepository.getAllMatches().stream()
            .sorted(sortOrder)
            .toList();
    }

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
    public void startMatch(String homeTeam, String awayTeam) {
        String normalizedHome = Match.validateAndNormalizeTeamName(homeTeam);
        String normalizedAway = Match.validateAndNormalizeTeamName(awayTeam);
        scoreboardRepository.startMatch(normalizedHome, normalizedAway);
    }

    /**
     * Updates the score of an existing match on the scoreboard.
     * <p>
     * Finds the match by team names (case-insensitive) and replaces it with a new match
     * object containing the updated scores. Team names are normalized by trimming whitespace.
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
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        String normalizedHome = Match.validateAndNormalizeTeamName(homeTeam);
        String normalizedAway = Match.validateAndNormalizeTeamName(awayTeam);
        scoreboardRepository.updateScore(normalizedHome, normalizedAway, homeScore, awayScore);
    }

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
    public void finishMatch(String homeTeam, String awayTeam) {
        String normalizedHome = Match.validateAndNormalizeTeamName(homeTeam);
        String normalizedAway = Match.validateAndNormalizeTeamName(awayTeam);
        scoreboardRepository.finishMatch(normalizedHome, normalizedAway);
    }
}
