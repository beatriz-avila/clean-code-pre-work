package trivia;

import java.util.*;

// REFACTOR ME
public class Game implements IGame {
    public static final int SQUARE_NUMBER = 12;
    public static final int DECK_NUMBER = 4;
    public static final int MAX_NUMBER_OF_QUESTIONS = 50;
    public static final String POP = "Pop";
    public static final String SCIENCE = "Science";
    public static final String SPORTS = "Sports";
    public static final String ROCK = "Rock";

    List<Player> players = new ArrayList<>();

    Deque<String> popQuestions = new ArrayDeque<>();
    Deque<String> scienceQuestions = new ArrayDeque<>();
    Deque<String> sportsQuestions = new ArrayDeque<>();
    Deque<String> rockQuestions = new ArrayDeque<>();

    private static class Player {
        final String name;
        int place;
        int purse;
        boolean inPenaltyBox;

        public Player(String name) {
            this.name = name;
            this.place = 0;
            this.inPenaltyBox = false;
        }

        public void moveForward(int roll) {
            place = (place + roll) % SQUARE_NUMBER;
        }

        public void displayLocation() {
            System.out.println(this.name
                    + "'s new location is "
                    + (this.place + 1));
        }
    }
    Player currentPlayer = null;

    public Game() {
        for (int i = 0; i < MAX_NUMBER_OF_QUESTIONS; i++) {
            popQuestions.addLast(createQuestion(POP, i));
            scienceQuestions.addLast(createQuestion(SCIENCE, i));
            sportsQuestions.addLast(createQuestion(SPORTS, i));
            rockQuestions.addLast(createQuestion(ROCK, i));
        }
    }
    public String createQuestion(String type, int index) {
        return type + " Question " + index;
    }

    public boolean add(String playerName) {
        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }
    public void roll(int roll) {
        nextPlayer();
        System.out.println(currentPlayer.name + " is the current player");
        System.out.println("They have rolled a " + roll);
        updatePenaltyStatus(roll);
        if (!currentPlayer.inPenaltyBox) {
            handleRoll(roll);
        }

    }
    private void updatePenaltyStatus(int roll) {
        if (currentPlayer.inPenaltyBox) {
            if (roll % 2 != 0) {
                currentPlayer.inPenaltyBox = false;
                System.out.println(currentPlayer.name + " is getting out of the penalty box");
            } else {
                System.out.println(currentPlayer.name + " is not getting out of the penalty box");
            }
        }
    }

    private void handleRoll(int roll) {
        Player currentNewPlayer = currentPlayer;
        currentNewPlayer.moveForward(roll);
        currentNewPlayer.displayLocation();
        askQuestion();
    }

    private void askQuestion() {
        String category = currentCategory();
        System.out.println("The category is " + category);
        switch (category) {
            case POP -> System.out.println(popQuestions.removeFirst());
            case SCIENCE -> System.out.println(scienceQuestions.removeFirst());
            case SPORTS -> System.out.println(sportsQuestions.removeFirst());
            case ROCK -> System.out.println(rockQuestions.removeFirst());
        }
    }
    private String currentCategory() {
        return switch (currentPlayer.place % DECK_NUMBER) {
            case 0 -> POP;
            case 1 -> SCIENCE;
            case 2 -> SPORTS;
            default -> ROCK;
        };
    }
    public boolean handleCorrectAnswer() {
        if (!currentPlayer.inPenaltyBox) {
            updatePlayerPurse();
        }
        return gameShouldContinue();
    }
    private void nextPlayer() {
           currentPlayer=players.get((players.indexOf(currentPlayer)+1)%players.size());
    }
    private void updatePlayerPurse() {
        System.out.println("Answer was correct!!!!");
        currentPlayer.purse++;
        System.out.println(currentPlayer.name
                + " now has "
                + currentPlayer.purse
                + " Gold Coins.");
    }
    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer.name + " was sent to the penalty box");
        currentPlayer.inPenaltyBox = true;
        return gameShouldContinue();
    }

    private boolean gameShouldContinue() {
        return !(currentPlayer.purse == 6);
    }
}
