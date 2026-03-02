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
     * The match is initialized with a score of 0-0.
     * </p>
     *
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @throws IllegalArgumentException if either team name is null or empty after trimming
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
}
