import java.io.*;
import java.util.*;

public class Game {

    private Player[] players;
    private int rounds;
    private final int HAND_SIZE = 5;
    private final String HIGH_SCORE_FILE = "highscores.txt";
    private Scanner input = new Scanner(System.in);

    public void start() {
        System.out.println("***********************");
        System.out.println("*** WELCOME TO HIGHSUIT ***");
        System.out.println("***********************\n");

        setupGame();
        playRounds();
        showFinalScores();
        updateHighScores();
        showHighScores();
        replayOption();
    }

    private void setupGame() {
        System.out.print("How many players? (1-2) >>> ");
        int count = readRange(1, 2);

        System.out.print("How many rounds? (1-3) >>> ");
        rounds = readRange(1, 3);

        players = new Player[2];

        String p1;
while (true) {
    System.out.print("\nPlayer 1 name >>> ");
    p1 = input.nextLine().trim();

    if (p1.isEmpty()) {
        System.out.println("Name cannot be empty.");
    } else if (p1.matches("\\d+")) {
        System.out.println("Name cannot be only numbers.");
    } else {
        break;
    }
}
players[0] = new Player(p1);


        if (count == 1) {
            players[1] = new Player("Computer");
        } else {
           while (true) {
    System.out.print("Player 2 name >>> ");
    String p2 = input.nextLine().trim();

    if (p2.isEmpty()) {
        System.out.println("Name cannot be empty.");
    } 
    else if (p2.matches("\\d+")) {
        System.out.println("Name cannot be only numbers.");
    } 
    else if (p2.equalsIgnoreCase(p1)) {
        System.out.println("Names must be different!");
    } 
    else {
        players[1] = new Player(p2);
        break;  
    }
}

        }
    }

   private void playRounds() {
    for (int r = 1; r <= rounds; r++) {


        for (Player p : players) {
            p.addReplay("\n=== ROUND " + r + " ===");
        }

        System.out.println("\n***************");
        System.out.println("*** Round " + r + " ***");
        System.out.println("***************\n");

        Deck deck = new Deck();
        deck.shuffle();
        dealCards(deck);

        for (Player p : players) {
            if (p.getName().equalsIgnoreCase("computer"))
                computerTurn(p, deck, r);
            else
                humanTurn(p, deck, r);
        }
    }
}


    private void humanTurn(Player p, Deck deck, int round) {

        printHeader(p, round);
        logHeader(p, round);

        System.out.println("Your current hand:");
        printHand(p);
        logHand(p, "Initial hand:");

        String bonus = readBonusSuit();
        p.setBonusSuit(bonus);
        log(p, "Bonus suit: " + bonus);

        List<Integer> drops = readDropIndexes();
        if (!drops.isEmpty()) {
            log(p, "Cards dropped:");
            for (int i : drops) log(p, p.getHand().get(i).toString());
        }

        Collections.sort(drops);
        for (int i : drops) p.replaceCard(i, deck.dealCard());

        System.out.println("\nYour new hand:");
        printHand(p);
        logHand(p, "Final hand:");

        scorePlayer(p);
        pause();
    }

    private void computerTurn(Player p, Deck deck, int round) {

        printHeader(p, round);
        logHeader(p, round);

        System.out.println("Your current hand:");
        printHand(p);
        logHand(p, "Initial hand:");

        String bonus = p.bestSuit();
        p.setBonusSuit(bonus);
        System.out.println("\nComputer chooses " + bonus + " as the bonus suit");
        log(p, "Bonus suit: " + bonus);
        pause();

        List<Integer> drops = chooseComputerDrops(p);
        if (!drops.isEmpty()) {
            System.out.print("Computer drops: ");
            List<String> names = new ArrayList<>();
            for (int i : drops) {
                names.add(p.getHand().get(i).toString());
                log(p, p.getHand().get(i).toString());
            }
            System.out.println(String.join(", ", names));
        }
        pause();

        Collections.sort(drops);
        for (int i : drops) p.replaceCard(i, deck.dealCard());

        System.out.println("\nYour new hand:");
        printHand(p);
        logHand(p, "Final hand:");

        scorePlayer(p);
        pause();
    }

    private void scorePlayer(Player p) {
        String bestSuit = p.bestSuit();
        int suitScore = p.calculateSuitScore(bestSuit);
        int total = p.calculateFinalScore();

        System.out.println("Your maximum suit score is " + suitScore + " in " + bestSuit);
        if (bestSuit.equalsIgnoreCase(p.getBonusSuit()))
            System.out.println("Bonus suit matches for a 5-point bonus!");

        System.out.println("Score for this round is " + total);
        log(p, "Score: " + total);

        p.addRoundScore(total);
    }

 private void replayOption() {
    System.out.print("\nWould you like to see a replay? (Y/N) >>> ");
    if (input.nextLine().equalsIgnoreCase("Y")) {

        System.out.println("\nREPLAY");
        System.out.println("-----");

        for (Player p : players) {
            for (String line : p.getReplay()) {
                System.out.println(line);
            }
        }
    }
}


 private void updateHighScores() {
    try {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) file.createNewFile();

        Map<String, Double> bestAverages = new HashMap<>();

        Scanner r = new Scanner(file);
        while (r.hasNextLine()) {
            String line = r.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(" ", 2);
            double avg = Double.parseDouble(parts[0]); 
            String name = parts[1];

            bestAverages.put(name,
                Math.max(bestAverages.getOrDefault(name, 0.0), avg));
        }
        r.close();

        for (Player p : players) {
            double avg = (double) p.getTotalScore() / rounds;
            bestAverages.put(p.getName(),
                Math.max(bestAverages.getOrDefault(p.getName(), 0.0), avg));
        }

        PrintWriter w = new PrintWriter(file);
        for (Map.Entry<String, Double> e : bestAverages.entrySet()) {
            w.println(String.format("%.2f %s", e.getValue(), e.getKey()));
        }
        w.close();

    } catch (Exception e) {
        System.out.println("Error updating high scores.");
    }
}


 private void showHighScores() {
    System.out.println("\nHIGH SCORE TABLE");
    System.out.println("----------------");

    List<String[]> scores = new ArrayList<>();

    try (Scanner sc = new Scanner(new File(HIGH_SCORE_FILE))) {
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) {
                scores.add(line.split(" ", 2)); 
            }
        }
    } catch (Exception e) {
        System.out.println("No high scores available.");
        return;
    }

    scores.sort((a, b) ->
        Double.compare(
            Double.parseDouble(b[0]),
            Double.parseDouble(a[0])
        )
    );

    for (int i = 0; i < Math.min(5, scores.size()); i++) {
        System.out.printf(
            "%d. %.2f %s%n",
            i + 1,
            Double.parseDouble(scores.get(i)[0]),
            scores.get(i)[1]
        );
    }
}


    private void dealCards(Deck d) {
        for (Player p : players) {
            p.clearHand();
            for (int i = 0; i < HAND_SIZE; i++)
                p.addCard(d.dealCard());
        }
    }

    private void printHand(Player p) {
        char c = 'A';
        for (Card card : p.getHand())
            System.out.println(c++ + ": " + card);
    }

    private void printHeader(Player p, int r) {
        System.out.println("\n----------------------");
        System.out.println("Player: " + p.getName() + "   Round: " + r);
        System.out.println("----------------------\n");
    }

    private void logHeader(Player p, int r) {
        log(p, "----------------------");
        log(p, "Player: " + p.getName() + "   Round: " + r);
        log(p, "----------------------");
    }

    private void logHand(Player p, String title) {
        log(p, title);
        for (Card c : p.getHand()) log(p, c.toString());
    }

    private void log(Player p, String msg) {
        p.addReplay(msg);
    }

    private List<Integer> chooseComputerDrops(Player p) {
        List<Integer> list = new ArrayList<>();
        String suit = p.bestSuit();
        for (int i = 0; i < p.getHand().size(); i++)
            if (!p.getHand().get(i).getSuit().equalsIgnoreCase(suit))
                list.add(i);
        return list.size() > 2 ? list.subList(0, 2) : list;
    }

    private String readBonusSuit() {
        while (true) {
            System.out.print("\nPlease nominate your bonus suit? (C/D/H/S) >>> ");
            switch (input.nextLine().toUpperCase()) {
                case "C": return "Clubs";
                case "D": return "Diamonds";
                case "H": return "Hearts";
                case "S": return "Spades";
            }
        }
    }
private List<Integer> readDropIndexes() {
    while (true) {
        System.out.print("\nPlease nominate the cards to be dropped >>> ");
        String inputLine = input.nextLine().toUpperCase().trim();

        List<Integer> list = new ArrayList<>();

        if (inputLine.isEmpty()) {
            return list;
        }

        for (char c : inputLine.toCharArray()) {
            if (c >= 'A' && c <= 'E') {
                int index = c - 'A';
                if (!list.contains(index)) {
                    list.add(index);
                }
            } else {
                System.out.println("Invalid input. Use letters A to E only.");
                list.clear();
                break;
            }
        }

        if (list.size() > 4) {
            System.out.println("You may swap at most 4 cards.");
            continue;
        }

        if (!list.isEmpty()) {
            return list;
        }
    }
}


  private int readRange(int min, int max) {
    while (true) {
        try {
            int value = input.nextInt();
            input.nextLine(); 
            if (value >= min && value <= max) {
                return value;
            }
        } catch (Exception e) {
            input.nextLine(); 
        }
        System.out.print("Invalid input. Try again >>> ");
    }
}

    private void pause() {
        System.out.println("\nPress Enter key to continue...");
        input.nextLine();
    }

    private void showFinalScores() {
        System.out.println("\nFINAL SCORES");
        System.out.println("-------------");
        for (Player p : players)
            System.out.println(p.getName() + " : " + p.getTotalScore() + " points");
    }
}
