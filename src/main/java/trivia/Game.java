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
            this.place = 1;
            this.inPenaltyBox = false;
        }
    }

    int currentPlayer = -1;

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
        advancePlayer();
        System.out.println(currentPlayer().name + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer().inPenaltyBox) {
            tryToExitPenaltyBox(roll);
        }
        if (!currentPlayer().inPenaltyBox) {
            handleRoll(roll);
        }

    }

    private void tryToExitPenaltyBox(int roll) {
        if (roll % 2 != 0) {
            currentPlayer().inPenaltyBox = false;
            System.out.println(currentPlayer().name + " is getting out of the penalty box");
        } else {
            System.out.println(currentPlayer().name + " is not getting out of the penalty box");
        }
    }

    private void handleRoll(int roll) {
        Player currentNewPlayer = currentPlayer();
        currentNewPlayer.place = currentNewPlayer.place + roll;

        if (currentNewPlayer.place > 12) {
            currentNewPlayer.place = currentNewPlayer.place % 12;
        }

        System.out.println(currentNewPlayer.name
                + "'s new location is "
                + currentNewPlayer.place);
        System.out.println("The category is " + currentCategory());
        askQuestion();
    }

    private void askQuestion() {
        String currentCategory = currentCategory();
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
            case 0 -> ROCK;
            case 1 -> POP;
            case 2 -> SCIENCE;
            default -> SPORTS;

        };
    }

    public boolean handleCorrectAnswer() {
        if (!currentPlayer().inPenaltyBox) {
            System.out.println("Answer was correct!!!!");
            currentPlayer().purse++;
            System.out.println(currentPlayer().name
                    + " now has "
                    + currentPlayer().purse
                    + " Gold Coins.");
        }
        return gameShouldContinue();
    }

    private void advancePlayer() {
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
    }


    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().name + " was sent to the penalty box");
        currentPlayer().inPenaltyBox = true;
        return true;
    }

    private boolean gameShouldContinue() {
        return !(currentPlayer().purse == 6);
    }

    private Player currentPlayer() {
        return players.get(currentPlayer);
    }
}
