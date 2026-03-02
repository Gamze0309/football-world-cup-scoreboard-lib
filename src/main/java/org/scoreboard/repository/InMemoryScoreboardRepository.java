package org.scoreboard.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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
    private final AtomicLong insertionCounter = new AtomicLong(0);

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized List<Match> getAllMatches() {
        return List.copyOf(matches);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void startMatch(String homeTeam, String awayTeam) {
        if (hasActiveMatch(homeTeam)) {
            throw new IllegalStateException("Team " + homeTeam + " already has an active match");
        }
        if (hasActiveMatch(awayTeam)) {
            throw new IllegalStateException("Team " + awayTeam + " already has an active match");
        }

        Match match = new Match(homeTeam, awayTeam)
            .withInsertionOrder(insertionCounter.getAndIncrement());
        matches.add(match);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {  
        Optional<Match> matchOpt = findMatch(homeTeam, awayTeam);
    
        if (matchOpt.isEmpty()) {
            throw new IllegalStateException("Match between " + homeTeam + " and " + awayTeam + " not found");
        }
        
        Match existing = matchOpt.get();
        int index = matches.indexOf(existing);
        matches.set(index, existing.updateScore(homeScore, awayScore));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void finishMatch(String homeTeam, String awayTeam) {
        Optional<Match> matchOpt = findMatch(homeTeam, awayTeam);
    
        if (matchOpt.isEmpty()) {
            throw new IllegalStateException("Match between " + homeTeam + " and " + awayTeam + " not found");
        }
        
        Match existing = matchOpt.get();
        int index = matches.indexOf(existing);
        matches.remove(index);
    }

    private boolean hasActiveMatch(String teamName) {
        for (Match match : matches) {
            if (teamName.equalsIgnoreCase(match.homeTeam()) ||
                teamName.equalsIgnoreCase(match.awayTeam())) {
                return true;
            }
        }
        return false;
    }

    private Optional<Match> findMatch(String homeTeam, String awayTeam) {
        return matches.stream()
            .filter(match -> homeTeam.equalsIgnoreCase(match.homeTeam()) &&
                             awayTeam.equalsIgnoreCase(match.awayTeam()))
            .findFirst();
    }
}
