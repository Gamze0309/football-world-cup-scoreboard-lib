# Football World Cup Score Board

A simple in-memory library for tracking live football match scores during the World Cup.

## Features

- Start a game (initial score 0-0)
- Finish a game (removes from score board)
- Update score (set both home and away team scores)
- Get summary sorted by total score (ties ordered by most recently added)

## Running Tests

```bash
# Prerequisites:
# - JDK 17 or newer installed and `java` on PATH
# - Apache Maven installed (Maven 3.8+ recommended) and `mvn` on PATH

mvn test
```

## Architecture

The library follows a layered architecture with clear separation of concerns:

- **Model (`Match`)** -- Immutable Java record representing a match. Self-validates at construction time (team names, scores), making invalid state unrepresentable. Score updates return a new instance.
- **Repository (`ScoreBoardRepository` / `InMemoryScoreBoardRepository`)** -- Interface-based storage layer. The in-memory implementation uses a synchronized `ArrayList` (chosen to keep it simple) with defensive copies for thread safety. Enforces storage-level constraints (one active match per team) atomically.
- **Service (`ScoreBoardService`)** -- Entry point for library consumers. Normalizes input, delegates to the repository, and handles summary sorting.

## Design Decisions

- **Immutable model:** `Match` is a Java `record`. Score updates create new instances rather than mutating state. The compact constructor enforces all invariants at construction time.
- **Self-validating model:** The `Match` record rejects invalid data (null/empty names, negative scores, same team) during construction. This makes invalid state unrepresentable, regardless of which layer creates instances.
- **Repository interface:** Storage is abstracted behind `ScoreBoardRepository`, enabling the in-memory implementation to be swapped (e.g., for a database) without modifying the service layer.
- **Thread safety:** All repository methods are `synchronized`. The duplicate-team check and match insertion happen atomically within a single synchronized block, preventing check-then-act race conditions. Returned lists are immutable snapshots via `List.copyOf()`.
- **Sorting strategy:** Summary sorting leverages Java's stable sort guarantee. The match list is reversed (most-recent-first) then stable-sorted by total score descending. Ties naturally preserve the reversed order, giving priority to matches started later.
- **Defensive copies:** `List.copyOf()` is used throughout to prevent callers from modifying internal repository state.

## Assumptions

- Team names are case-insensitive ("Mexico" equals "mexico")
- A team can only play in one active match at a time
- Scores cannot be negative
- Team names cannot be null, empty, same, or whitespace-only
- Leading and trailing whitespace in team names is trimmed
