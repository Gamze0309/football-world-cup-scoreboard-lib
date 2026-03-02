package org.scoreboard.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.scoreboard.model.Match;
import org.scoreboard.repository.InMemoryScoreboardRepository;
import org.scoreboard.repository.ScoreboardRepository;

@DisplayName("ScoreBoardService")
public class ScoreboardServiceTest {

    private ScoreboardService scoreboardService;

    @BeforeEach
    void setUp() {
        ScoreboardRepository repository = new InMemoryScoreboardRepository();
        scoreboardService = new ScoreboardService(repository);
    }

    @Nested
    @DisplayName("Start Match")
    class StartMatch {

        @Test
        @DisplayName("should instantiate scoreboard service")
        void shouldInstantiateService() {
            assertNotNull(scoreboardService, "ScoreBoardService instance should have been created");
        }

        @Test
        @DisplayName("should add match when startMatch called")
        void shouldAddMatch() {
            scoreboardService.startMatch("Brazil", "Argentina");

            List<Match> matches = scoreboardService.getAllMatchesSummary();
            assertEquals(1, matches.size(), "Matches list should contain one match after addition");
        }

        @Test
        @DisplayName("should start match with correct team names")
        void shouldStartMatchWithCorrectTeamNames() {
            scoreboardService.startMatch("Brazil", "Argentina");

            List<Match> matches = scoreboardService.getAllMatchesSummary();
            Match match = matches.get(0);

            assertAll(
                () -> assertEquals("Brazil", match.homeTeam(), "Home team name should be 'Brazil'"),
                () -> assertEquals("Argentina", match.awayTeam(), "Away team name should be 'Argentina'")
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = " ")
        @DisplayName("should reject invalid home team name")
        void shouldRejectInvalidHomeTeamName(String invalidHome) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreboardService.startMatch(invalidHome, "Canada"));
            assertEquals("Team name cannot be null or empty", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = " ")
        @DisplayName("should reject invalid away team name")
        void shouldRejectInvalidAwayTeamName(String invalidAway) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreboardService.startMatch("Canada", invalidAway));
            assertEquals("Team name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("should reject when home team already has an active match")
        void shouldRejectWhenHomeTeamAlreadyActive() {
            scoreboardService.startMatch("Brazil", "Argentina");

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scoreboardService.startMatch("Brazil", "Canada"));
            assertTrue(exception.getMessage().contains("already has an active match"));
        }

        @Test
        @DisplayName("should reject when away team already has an active match")
        void shouldRejectWhenAwayTeamAlreadyActive() {
            scoreboardService.startMatch("Turkey", "Canada");

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scoreboardService.startMatch("Brazil", "Canada"));
            assertTrue(exception.getMessage().contains("already has an active match"));
        }

        @Test
        @DisplayName("should reject duplicate match")
        void shouldRejectDuplicateMatch() {
            scoreboardService.startMatch("Brazil", "Canada");

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scoreboardService.startMatch("Brazil", "Canada"));
            assertTrue(exception.getMessage().contains("already has an active match"));
        }

        @Test
        @DisplayName("should detect active teams ignoring case")
        void shouldDetectActiveTeamsIgnoringCase() {
            scoreboardService.startMatch("Turkey", "brazil");

            assertAll(
                () -> assertThrows(IllegalStateException.class,
                    () -> scoreboardService.startMatch("Brazil", "Canada")),
                () -> assertThrows(IllegalStateException.class,
                    () -> scoreboardService.startMatch("Canada", "TURKEY"))
            );
        }

        @Test
        @DisplayName("should detect duplicate team when home team has trailing whitespace")
        void shouldDetectDuplicateTeamWithHomeTeamWhitespace() {
            scoreboardService.startMatch("Brazil", "Argentina");

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scoreboardService.startMatch("Brazil ", "Canada"));
            assertTrue(exception.getMessage().contains("already has an active match"));
        }

        @Test
        @DisplayName("should detect duplicate team when away team has trailing whitespace")
        void shouldDetectDuplicateTeamWithAwayTeamWhitespace() {
            scoreboardService.startMatch("Brazil", "Argentina");

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scoreboardService.startMatch("Canada", "Argentina "));
            assertTrue(exception.getMessage().contains("already has an active match"));
        }
    }

    @Nested
    @DisplayName("Update Score")
    class UpdateScore {

        @Test
        @DisplayName("should update match scores")
        void shouldUpdateScores() {
            scoreboardService.startMatch("Brazil", "Argentina");
            scoreboardService.updateScore("Brazil", "Argentina", 2, 3);

            Match match = scoreboardService.getAllMatchesSummary().get(0);
            assertAll(
                () -> assertEquals(2, match.homeScore()),
                () -> assertEquals(3, match.awayScore())
            );
        }

        @Test
        @DisplayName("should update scores multiple times on same match")
        void shouldUpdateScoresMultipleTimes() {
            scoreboardService.startMatch("Brazil", "Argentina");
            scoreboardService.updateScore("Brazil", "Argentina", 1, 0);
            scoreboardService.updateScore("Brazil", "Argentina", 2, 3);

            Match match = scoreboardService.getAllMatchesSummary().get(0);
            assertAll(
                () -> assertEquals(2, match.homeScore()),
                () -> assertEquals(3, match.awayScore())
            );
        }

        @Test
        @DisplayName("should update scores ignoring case")
        void shouldIgnoreCase() {
            scoreboardService.startMatch("Brazil", "Argentina");
            scoreboardService.updateScore("brazil", "argentina", 2, 3);

            Match match = scoreboardService.getAllMatchesSummary().get(0);
            assertAll(
                () -> assertEquals(2, match.homeScore()),
                () -> assertEquals(3, match.awayScore())
            );
        }

        @Test
        @DisplayName("should update scores with trimmed team names")
        void shouldTrimTeamNames() {
            scoreboardService.startMatch("Mexico", "Germany");
            scoreboardService.updateScore("Mexico ", " Germany", 3, 3);

            assertEquals(3, scoreboardService.getAllMatchesSummary().get(0).homeScore());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = " ")
        @DisplayName("should reject invalid home team name on update score")
        void shouldRejectInvalidHomeTeamName(String invalidHome) {
            scoreboardService.startMatch("Canada", "Brazil");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreboardService.updateScore(invalidHome, "Brazil", 1, 1));
            assertEquals("Team name cannot be null or empty", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = " ")
        @DisplayName("should reject invalid away team name on update score")
        void shouldRejectInvalidAwayTeamName(String invalidAway) {
            scoreboardService.startMatch("Canada", "Brazil");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreboardService.updateScore("Canada", invalidAway, 1, 1));
            assertEquals("Team name cannot be null or empty", exception.getMessage());
        }

        @Test
        @DisplayName("should not update non existing match")
        void shouldThrowWhenMatchNotFound() {
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scoreboardService.updateScore("Canada", "Brazil", 1, 1));
            assertTrue(exception.getMessage().contains("not found"));
        }

        @Test
        @DisplayName("should not update with negative home score")
        void shouldRejectNegativeHomeScore() {
            scoreboardService.startMatch("Canada", "Brazil");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreboardService.updateScore("Canada", "Brazil", -1, 1));
            assertTrue(exception.getMessage().contains("negative"));
        }

        @Test
        @DisplayName("should not update with negative away score")
        void shouldRejectNegativeAwayScore() {
            scoreboardService.startMatch("Canada", "Brazil");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreboardService.updateScore("Canada", "Brazil", 1, -1));
            assertTrue(exception.getMessage().contains("negative"));
        }
    }

    @Nested
    @DisplayName("Finish Match")
    class FinishMatch {

        @Test
        @DisplayName("should remove match from scoreboard")
        void shouldRemoveMatch() {
            scoreboardService.startMatch("Brazil", "Argentina");
            scoreboardService.finishMatch("Brazil", "Argentina");

            assertEquals(0, scoreboardService.getAllMatchesSummary().size());
        }

        @Test
        @DisplayName("should keep other matches when one is finished")
        void shouldKeepOtherMatches() {
            scoreboardService.startMatch("Brazil", "Argentina");
            scoreboardService.startMatch("Turkey", "Canada");
            scoreboardService.finishMatch("Turkey", "Canada");

            List<Match> matches = scoreboardService.getAllMatchesSummary();
            assertAll(
                () -> assertEquals(1, matches.size()),
                () -> assertEquals("Brazil", matches.get(0).homeTeam()),
                () -> assertEquals("Argentina", matches.get(0).awayTeam())
            );
        }

        @Test
        @DisplayName("should finish match ignoring case")
        void shouldIgnoreCase() {
            scoreboardService.startMatch("Brazil", "Argentina");
            scoreboardService.finishMatch("brazil", "argentina");

            assertEquals(0, scoreboardService.getAllMatchesSummary().size());
        }

        @Test
        @DisplayName("should finish match with trimmed team names")
        void shouldTrimTeamNames() {
            scoreboardService.startMatch("Brazil", "Argentina");
            scoreboardService.finishMatch(" Brazil ", "  Argentina");

            assertEquals(0, scoreboardService.getAllMatchesSummary().size());
        }

        @Test
        @DisplayName("should throw when match does not exist")
        void shouldThrowWhenMatchNotFound() {
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scoreboardService.finishMatch("Brazil", "Turkey"));
            assertTrue(exception.getMessage().contains("not found"));
        }

        @Test
        @DisplayName("should throw when finishing already finished match")
        void shouldThrowWhenFinishingTwice() {
            scoreboardService.startMatch("Mexico", "Canada");
            scoreboardService.finishMatch("Mexico", "Canada");

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> scoreboardService.finishMatch("Mexico", "Canada"));
            assertTrue(exception.getMessage().contains("not found"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = " ")
        @DisplayName("should reject invalid home team name")
        void shouldRejectInvalidHomeTeamName(String invalidHome) {
            scoreboardService.startMatch("Brazil", "Argentina");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreboardService.finishMatch(invalidHome, "Argentina"));
            assertEquals("Team name cannot be null or empty", exception.getMessage());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = " ")
        @DisplayName("should reject invalid away team name")
        void shouldRejectInvalidAwayTeamName(String invalidAway) {
            scoreboardService.startMatch("Brazil", "Argentina");

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> scoreboardService.finishMatch("Brazil", invalidAway));
            assertEquals("Team name cannot be null or empty", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Get Summary")
    class GetSummary {

        @Test
        @DisplayName("should return unmodifiable list")
        void shouldReturnUnmodifiableList() {
            List<Match> result = scoreboardService.getAllMatchesSummary();

            assertThrows(UnsupportedOperationException.class,
                () -> result.add(new Match("Brazil", "Argentina")));
        }

        @Test
        @DisplayName("should return empty matches list initially")
        void shouldReturnEmptyMatchesListInitially() {
            List<Match> matches = scoreboardService.getAllMatchesSummary();
            assertEquals(0, matches.size(), "Initial matches list should be empty");
        }

        @Test
        @DisplayName("should return matches sorted by total score descending")
        void shouldSortByTotalScoreDescending() {
            scoreboardService.startMatch("Germany", "France");
            scoreboardService.startMatch("Mexico", "Canada");
            scoreboardService.startMatch("Spain", "Brazil");

            scoreboardService.updateScore("Mexico", "Canada", 0, 5);
            scoreboardService.updateScore("Spain", "Brazil", 10, 2);
            scoreboardService.updateScore("Germany", "France", 2, 2);

            List<Match> summary = scoreboardService.getAllMatchesSummary();

            assertAll(
                () -> assertEquals("Spain", summary.get(0).homeTeam()),
                () -> assertEquals("Mexico", summary.get(1).homeTeam()),
                () -> assertEquals("Germany", summary.get(2).homeTeam())
            );
        }

        @Test
        @DisplayName("should return matches sorted by total score")
        void shouldGetSortedSummaryByTotalScore() {
            scoreboardService.startMatch("Mexico", "Canada");
            scoreboardService.startMatch("Spain", "Brazil");
            scoreboardService.startMatch("Germany", "France");
            
            scoreboardService.updateScore("Mexico", "Canada", 2, 5);
            scoreboardService.updateScore("Spain", "Brazil", 10, 3);
            scoreboardService.updateScore("Germany", "France", 2, 4);

            List<Match> summary = scoreboardService.getAllMatchesSummary();
            assertAll(
                () -> assertEquals("Spain", summary.get(0).homeTeam()),
                () -> assertEquals("Mexico", summary.get(1).homeTeam()),
                () -> assertEquals("Germany", summary.get(2).homeTeam())
            );
        }
    }
}
