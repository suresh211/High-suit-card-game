
# HighSuit Card Game

HighSuit is a Java console-based card game developed as part of a university coursework assignment.  
The application allows one or two players to play multiple rounds using a standard 52-card deck, select bonus suits, swap cards, and score based on the highest-value suit in their hand.

## Features
- Supports 1–2 players
- Optional computer-controlled player
- Multi-round gameplay (1–3 rounds)
- Bonus suit selection with additional scoring
- Card swapping (up to four cards per round)
- Automatic score calculation
- High score table (top 5 average scores)
- Replay system showing full round history

## Project Structure
- `Card.java` – Represents an individual playing card
- `Deck.java` – Creates and manages a 52-card deck
- `Player.java` – Stores player hand, score, and replay data
- `Game.java` – Controls game flow, rounds, scoring, replay, and high scores
- `Main.java` – Entry point that starts the game
- `test/` – Contains JUnit test classes for validating core functionality

## How to Run
1. Open the project in NetBeans or any Java IDE.
2. Ensure Java is installed (Java 8 or later).
3. Run the `Main` class.
4. Follow the on-screen instructions to play the game.

## Testing
JUnit test classes were created to test core behaviour, including:
- Deck size and uniqueness of cards
- Correct card dealing
- Player hand size
- Scoring logic

Testing covers normal cases, boundary cases, and edge cases.

## High Scores
High scores are stored externally in a text file and represent the average score per game.  
Only the top five scores are displayed.

## Author
[Suresh Deuja]

## License
This project was developed for educational purposes only.
