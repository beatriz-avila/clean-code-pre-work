package trivia;

import java.util.*;

// REFACTOR ME
public class Game implements IGame {
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
            place = (place+roll)%12;
        }

        public  void displayLocation() {
            System.out.println(this.name
                    + "'s new location is "
                    + (this.place+1));
        }
    }

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

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
        System.out.println(currentPlayer().name + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().inPenaltyBox) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;
                currentPlayer().inPenaltyBox = false;
                System.out.println(currentPlayer().name + " is getting out of the penalty box");
                handleRoll(roll);
            } else {
                System.out.println(currentPlayer().name + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {
            handleRoll(roll);
        }

    }

    private void handleRoll(int roll) {
        Player currentNewPlayer = currentPlayer();
        currentNewPlayer.moveForward(roll);
        currentNewPlayer.displayLocation();
        String category = currentCategory();
        System.out.println("The category is " + category);
        askQuestion(category);
    }


    private void askQuestion(String currentCategory) {
        switch (currentCategory) {
            case POP -> System.out.println(popQuestions.removeFirst());
            case SCIENCE -> System.out.println(scienceQuestions.removeFirst());
            case SPORTS -> System.out.println(sportsQuestions.removeFirst());
            case ROCK -> System.out.println(rockQuestions.removeFirst());
        }
    }

    private String currentCategory() {
        int index = currentPlayer().place;
        return switch (index % 4) {

            case 0 -> POP;
            case 1 -> SCIENCE;
            case 2 -> SPORTS;
            default -> ROCK;
        };
    }

    public boolean handleCorrectAnswer() {
        if (currentPlayer().inPenaltyBox) {
            nextPlayer();
            return true;
        } else {
            return handleRound();
        }
    }

    private void nextPlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }

    private boolean handleRound() {
        System.out.println("Answer was correct!!!!");
        Player currentNewPlayer = currentPlayer();
        currentNewPlayer.purse++;
        System.out.println(currentPlayer().name
                + " now has "
                + currentNewPlayer.purse
                + " Gold Coins.");
        boolean winner = checkWinCondition();
        nextPlayer();
        return !winner;
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().name + " was sent to the penalty box");
        currentPlayer().inPenaltyBox = true;
        nextPlayer();
        return true;
    }

    private boolean checkWinCondition() {
        return (currentPlayer().purse == 6);
    }

    private Player currentPlayer() {
        return players.get(currentPlayer);
    }
}
