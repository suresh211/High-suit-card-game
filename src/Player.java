import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private List<Card> hand;
    private int totalScore;
    private String bonusSuit;

    private List<String> replayLog = new ArrayList<>();
    

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.totalScore = 0;
    }

    public String getName() { return name; }
    public List<Card> getHand() { return hand; }

    public void clearHand() { hand.clear(); }
    public void addCard(Card c) { hand.add(c); }
    public void replaceCard(int index, Card c) { hand.set(index, c); }

    public void setBonusSuit(String suit) { this.bonusSuit = suit; }
    public String getBonusSuit() { return bonusSuit; }

    public void addRoundScore(int score) { totalScore += score; }
    public int getTotalScore() { return totalScore; }

    // Scoring
    public int calculateSuitScore(String suit) {
        int score = 0;
        for (Card c : hand) {
            if (c.getSuit().equalsIgnoreCase(suit)) {
                score += c.getValue();
            }
        }
        return score;
    }

    public String bestSuit() {
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        String best = suits[0];
        int max = 0;

        for (String s : suits) {
            int suitScore = calculateSuitScore(s);
            if (suitScore > max) {
                max = suitScore;
                best = s;
            }
        }
        return best;
    }

    public int calculateFinalScore() {
        String best = bestSuit();
        int base = calculateSuitScore(best);

        if (best.equalsIgnoreCase(bonusSuit)) {
            base += 5;
        }

        return base;
    }

 
    public void addReplay(String line) { replayLog.add(line); }
    public List<String> getReplay() { return replayLog; }

   
}
