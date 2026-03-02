package org.scoreboard.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Match")
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
        assertAll(
            () -> assertEquals("Mexico", match.getHomeTeam()),
            () -> assertEquals("Canada", match.getAwayTeam())
        );
    }
    
    @Test
    @DisplayName("should create match with initial score 0-0")
    void shouldInitializeScoresToZero() {
        Match match = new Match("Mexico", "Canada");

        assertAll(
            () -> assertEquals(0, match.getHomeScore()),
            () -> assertEquals(0, match.getAwayScore())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    @DisplayName("should reject invalid home team name")
    void shouldRejectInvalidHomeTeamName(String invalidHome) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new Match(invalidHome, "Turkey"));
        assertEquals("Team name cannot be null or empty", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    @DisplayName("should reject invalid away team name")
    void shouldRejectInvalidAwayTeamName(String invalidAway) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new Match("Germany", invalidAway));
        assertEquals("Team name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("should normalize team names by trimming whitespace")
    void shouldNormalizeTeamNamesByTrimmingWhitespace() {
        Match match = new Match("Mexico ", "Canada ");
        assertAll(
            () -> assertEquals("Mexico", match.getHomeTeam()),
            () -> assertEquals("Canada", match.getAwayTeam())
        );
    }

    @Test
    @DisplayName("should reject same home and away team")
    void shouldRejectSameTeamNames() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new Match("Canada", "Canada"));
        assertTrue(exception.getMessage().contains("Team names cannot be the same"));
    }

    @Test
    @DisplayName("should reject same team names ignoring case")
    void shouldRejectSameTeamNamesIgnoringCase() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> new Match("Canada", "canada"));
        assertTrue(exception.getMessage().contains("Team names cannot be the same"));
    }
}
