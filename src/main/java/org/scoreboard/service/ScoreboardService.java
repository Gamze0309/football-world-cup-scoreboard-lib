package org.scoreboard.service;

import java.util.ArrayList;
import java.util.List;

import org.scoreboard.model.Match;

/**
 * Service class for managing a football world cup scoreboard.
 * <p>
 * This service maintains a collection of ongoing matches and provides
 * operations to start matches and retrieve match information.
 * </p>
 */
public class ScoreboardService {
    private final List<Match> matches = new ArrayList<>();
    
    /**
     * Returns all matches currently tracked by the scoreboard.
     *
     * @return a list of all matches
     */
    public List<Match> getAllMatches() {
        return matches;
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

        for (Match match : matches) {
            if (normalizedHome.equalsIgnoreCase(match.getHomeTeam()) || normalizedHome.equalsIgnoreCase(match.getAwayTeam())) {
                throw new IllegalStateException("Team " + normalizedHome  + " already has an active match");
            }

            if (normalizedAway.equalsIgnoreCase(match.getHomeTeam()) || normalizedAway.equalsIgnoreCase(match.getAwayTeam())) {
                throw new IllegalStateException("Team " + normalizedAway  + " already has an active match");
            }
        }

        Match match = new Match(homeTeam, awayTeam);
        matches.add(match);
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
     * @throws IllegalArgumentException if either team name is null or empty after trimming
     */
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        String normalizedHome = Match.validateAndNormalizeTeamName(homeTeam);
        String normalizedAway = Match.validateAndNormalizeTeamName(awayTeam);

        for (int i = 0; i < matches.size(); i++) {
            if (normalizedHome.equalsIgnoreCase(matches.get(i).getHomeTeam()) &&
                normalizedAway.equalsIgnoreCase(matches.get(i).getAwayTeam())) {
                    matches.set(i, new Match(normalizedHome, normalizedAway, homeScore, awayScore));
                return;
            }
        }
    }
}
