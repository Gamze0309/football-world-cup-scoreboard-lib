package org.scoreboard.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MatchTest {
    @Test
    @DisplayName("should instantiate match")
    void shouldInstantiateMatch() {
        Match match = new Match("Mexico", "Canada");
        assertNotNull(match, "Match instance should have been created");
    }

    @Test
    @DisplayName("should return team names from getters")
    void shouldReturnTeamNamesFromGetters() {
        Match match = new Match("Mexico", "Canada");
        assertEquals("Mexico", match.getHomeTeam());
        assertEquals("Canada", match.getAwayTeam());
    }
    
    @Test
    @DisplayName("should create match with initial score 0-0")
    void shouldInitializeScoresToZero() {
        Match match = new Match("Mexico", "Canada");

        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
    }
}
