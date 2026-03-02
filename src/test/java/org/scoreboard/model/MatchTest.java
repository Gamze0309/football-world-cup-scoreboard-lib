package org.scoreboard.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Match")
public class MatchTest {

    @Nested
    @DisplayName("Creation")
    class Creation {

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
                () -> assertEquals("Mexico", match.homeTeam()),
                () -> assertEquals("Canada", match.awayTeam())
            );
        }
        
        @Test
        @DisplayName("should create match with initial score 0-0")
        void shouldInitializeScoresToZero() {
            Match match = new Match("Mexico", "Canada");

            assertAll(
                () -> assertEquals(0, match.homeScore()),
                () -> assertEquals(0, match.awayScore())
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
                () -> assertEquals("Mexico", match.homeTeam()),
                () -> assertEquals("Canada", match.awayTeam())
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

    @Nested
    @DisplayName("Score Update")
    class ScoreUpdate {

        @Test
        @DisplayName("should return new match with updated scores")
        void shouldReturnNewMatchWithUpdatedScores() {
            Match original = new Match("Mexico", "Canada");
            Match updated = original.updateScore(2, 3);

            assertAll(
                () -> assertEquals(2, updated.homeScore()),
                () -> assertEquals(3, updated.awayScore()),
                () -> assertEquals("Mexico", updated.homeTeam()),
                () -> assertEquals("Canada", updated.awayTeam())
            );
        }

        @Test
        @DisplayName("should preserve original match scores after update")
        void shouldPreserveOriginalMatchScores() {
            Match original = new Match("Mexico", "Canada");
            original.updateScore(2, 3);

            assertAll(
                () -> assertEquals(0, original.homeScore()),
                () -> assertEquals(0, original.awayScore())
            );
        }

        @ParameterizedTest
        @CsvSource({"-1, 0", "0, -1", "-1, -1", "-5, -10"})
        @DisplayName("should reject negative scores")
        void shouldRejectNegativeScores(int homeScore, int awayScore) {
            Match match = new Match("Mexico", "Canada");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> match.updateScore(homeScore, awayScore));
            assertEquals("Scores cannot be negative", exception.getMessage());
        }
    }
}
