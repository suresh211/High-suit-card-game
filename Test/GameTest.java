import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

public class GameTest {

    // 1️⃣ Deck contains 52 cards (by dealing all)
    @Test
    public void deckHas52Cards() {
        Deck deck = new Deck();
        int count = 0;

        for (int i = 0; i < 52; i++) {
            assertNotNull(deck.dealCard());
            count++;
        }
        assertEquals(52, count);
    }

    // 2️⃣ Deck does not return null before empty
    @Test
    public void dealCardNotNullInitially() {
        Deck deck = new Deck();
        assertNotNull(deck.dealCard());
    }

    // 3️⃣ All cards in deck are unique
    @Test
    public void deckHasNoDuplicateCards() {
        Deck deck = new Deck();
        Set<String> cards = new HashSet<>();

        for (int i = 0; i < 52; i++) {
            Card c = deck.dealCard();
            cards.add(c.getRank() + "-" + c.getSuit());
        }
        assertEquals(52, cards.size());
    }

    // 4️⃣ Shuffle does not change deck size
    @Test
    public void shuffleKeepsDeckSize() {
        Deck deck = new Deck();
        deck.shuffle();

        int count = 0;
        for (int i = 0; i < 52; i++) {
            if (deck.dealCard() != null) count++;
        }
        assertEquals(52, count);
    }

    // 5️⃣ Card rank is stored correctly
    @Test
    public void cardRankCorrect() {
        Card card = new Card("Hearts", "A");
        assertEquals("A", card.getRank());
    }

    // 6️⃣ Card suit is stored correctly
    @Test
    public void cardSuitCorrect() {
        Card card = new Card("Spades", "10");
        assertEquals("Spades", card.getSuit());
    }

    // 7️⃣ Player is created with correct name
    @Test
    public void playerNameCorrect() {
        Player player = new Player("Alex");
        assertEquals("Alex", player.getName());
    }

    // 8️⃣ Player can hold cards
    @Test
    public void playerCanReceiveCard() {
        Player player = new Player("Sam");
        Card card = new Card("Clubs", "5");
        player.addCard(card);

        assertEquals(1, player.getHand().size());
    }

    // 9️⃣ Player hand size can be 5 cards
    @Test
    public void playerHandSizeFive() {
        Player player = new Player("Jamie");
        Deck deck = new Deck();

        for (int i = 0; i < 5; i++) {
            player.addCard(deck.dealCard());
        }
        assertEquals(5, player.getHand().size());
    }

    // 🔟 Card drawn from one deck not found again
    @Test
    public void cardNotDrawnTwice() {
        Deck deck = new Deck();
        Card first = deck.dealCard();

        boolean foundAgain = false;
        for (int i = 0; i < 51; i++) {
            Card next = deck.dealCard();
            if (first.getRank().equals(next.getRank())
                    && first.getSuit().equals(next.getSuit())) {
                foundAgain = true;
            }
        }
        assertFalse(foundAgain);
    }
}
