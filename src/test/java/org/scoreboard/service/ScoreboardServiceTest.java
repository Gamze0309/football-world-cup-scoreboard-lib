package org.scoreboard.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.scoreboard.model.Match;

public class ScoreboardServiceTest {
    @Test
    @DisplayName("should instantiate scoreboard service")
    void shouldInstantiateScoreBoardService() {
        ScoreboardService scoreboardService = new ScoreboardService();
        assertNotNull(scoreboardService, "ScoreBoardService instance should have been created");
    }

    @Test
    @DisplayName("should return empty matches list initially")
    void shouldReturnEmptyMatchesListInitially() {
        ScoreboardService scoreboardService = new ScoreboardService();
        List<Match> matches = scoreboardService.getAllMatches();
        assertEquals(0, matches.size(), "Initial matches list should be empty");
    }

    @Test
    @DisplayName("should add match when startMatch called")
    void shouldAddMatch() {
        ScoreboardService scoreboardService = new ScoreboardService();
        scoreboardService.startMatch("Brazil", "Argentina");

        List<Match> matches = scoreboardService.getAllMatches();
        assertEquals(1, matches.size(), "Matches list should contain one match after addition");
    }

    @Test
    @DisplayName("should start match with correct team names")
    void shouldStartMatchWithCorrectTeamNames() {
        ScoreboardService scoreboardService = new ScoreboardService();
        scoreboardService.startMatch("Brazil", "Argentina");

        List<Match> matches = scoreboardService.getAllMatches();
        Match match = matches.get(0);

        assertAll(
            () -> assertEquals("Brazil", match.getHomeTeam(), "Home team name should be 'Brazil'"),
            () -> assertEquals("Argentina", match.getAwayTeam(), "Away team name should be 'Argentina'")
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    @DisplayName("should reject invalid home team name")
    void shouldRejectInvalidHomeTeamName(String invalidHome) {
        ScoreboardService scoreboardService = new ScoreboardService();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scoreboardService.startMatch(invalidHome, "Canada"));
        assertEquals("Team name cannot be null or empty", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    @DisplayName("should reject invalid away team name")
    void shouldRejectInvalidAwayTeamName(String invalidAway) {
        ScoreboardService scoreboardService = new ScoreboardService();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scoreboardService.startMatch("Canada", invalidAway));
        assertEquals("Team name cannot be null or empty", exception.getMessage());
    }
}
