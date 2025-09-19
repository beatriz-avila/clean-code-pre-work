package trivia;

import java.util.*;

// REFACTOR ME
public class Game implements IGame {
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

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public Game() {
        for (int i = 0; i < 50; i++) {
            popQuestions.addLast(createQuestion("Pop", i));
            scienceQuestions.addLast(createQuestion("Science", i));
            sportsQuestions.addLast(createQuestion("Sports", i));
            rockQuestions.addLast(createQuestion("Rock", i));
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
        currentNewPlayer.place = currentNewPlayer.place + roll;

        if (currentNewPlayer.place > 12) {
            currentNewPlayer.place = currentNewPlayer.place - 12;
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
            case "Pop" -> System.out.println(popQuestions.removeFirst());
            case "Science" -> System.out.println(scienceQuestions.removeFirst());
            case "Sports" -> System.out.println(sportsQuestions.removeFirst());
            case "Rock" -> System.out.println(rockQuestions.removeFirst());
        }
    }

    private String currentCategory() {
        int index = currentPlayer().place - 1;
        return switch (index % 4) {
            case 0 -> "Pop";
            case 1 -> "Science";
            case 2 -> "Sports";
            default -> "Rock";
        };
    }

    public boolean handleCorrectAnswer() {
        if (currentPlayer().inPenaltyBox) {
            if (isGettingOutOfPenaltyBox) {
                return handleWinner2();
            } else {
                currentPlayer++;
                if (currentPlayer == players.size()) currentPlayer = 0;
                return true;
            }
        } else {
            return handleWinner2();
        }
    }

    private boolean handleWinner2() {
        System.out.println("Answer was correct!!!!");
        Player currentNewPlayer = currentPlayer();
        currentNewPlayer.purse++;
        System.out.println(currentPlayer().name
                + " now has "
                + currentNewPlayer.purse
                + " Gold Coins.");

        boolean winner = didPlayerWin();
        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;

        return winner;
    }

    public boolean wrongAnswer() {
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer().name + " was sent to the penalty box");
        currentPlayer().inPenaltyBox = true;

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }

    private boolean didPlayerWin() {
        return !(currentPlayer().purse == 6);
    }

    private Player currentPlayer() {
        return players.get(currentPlayer);
    }
}
