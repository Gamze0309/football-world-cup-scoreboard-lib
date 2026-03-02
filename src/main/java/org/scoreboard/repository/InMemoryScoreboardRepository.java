package org.scoreboard.repository;

import java.util.ArrayList;
import java.util.List;

import org.scoreboard.model.Match;

/**
 * In-memory implementation of the ScoreboardRepository interface.
 * <p>
 * This implementation stores match data in memory using an ArrayList,
 * providing fast access but no persistence across application restarts.
 * </p>
 */
public class InMemoryScoreboardRepository implements ScoreboardRepository{
    private final List<Match> matches = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Match> getAllMatches() {
        return matches;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        
        for (Match match : matches) {
            if (homeTeam.equalsIgnoreCase(match.getHomeTeam()) || homeTeam.equalsIgnoreCase(match.getAwayTeam())) {
                throw new IllegalStateException("Team " + homeTeam  + " already has an active match");
            }

             if (awayTeam.equalsIgnoreCase(match.getHomeTeam()) || awayTeam.equalsIgnoreCase(match.getAwayTeam())) {
                throw new IllegalStateException("Team " + awayTeam  + " already has an active match");
            }
        }

        Match match = new Match(homeTeam, awayTeam);
        matches.add(match);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {  
        for (int i = 0; i < matches.size(); i++) {
            if (homeTeam.equalsIgnoreCase(matches.get(i).getHomeTeam()) &&
                awayTeam.equalsIgnoreCase(matches.get(i).getAwayTeam())) {
                    matches.set(i, new Match(homeTeam, awayTeam, homeScore, awayScore));
                return;
            }
        }

        throw new IllegalStateException("Match between " + homeTeam + " and " + awayTeam + " not found");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        for (int i = 0; i < matches.size(); i++) {
            if (homeTeam.equalsIgnoreCase(matches.get(i).getHomeTeam()) &&
                awayTeam.equalsIgnoreCase(matches.get(i).getAwayTeam())) {
                    matches.remove(i);
                    return;
            }
        }

        throw new IllegalStateException("Match between " + homeTeam + " and " + awayTeam + " not found");
    }
}
