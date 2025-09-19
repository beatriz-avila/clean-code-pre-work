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
            popQuestions.addLast("Pop Question " + i);
            scienceQuestions.addLast(("Science Question " + i));
            sportsQuestions.addLast(("Sports Question " + i));
            rockQuestions.addLast(createRockQuestion(i));
        }
    }

    public String createRockQuestion(int index) {
        return "Rock Question " + index;
    }

    public boolean isPlayable() {
        return (players.size() >= 2);
    }

    public boolean add(String playerName) {
        players.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public void roll(int roll) {
        System.out.println(players.get(currentPlayer).name + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (players.get(currentPlayer).inPenaltyBox) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;

                System.out.println(players.get(currentPlayer).name + " is getting out of the penalty box");
                handleRoll(roll);
            } else {
                System.out.println(players.get(currentPlayer).name + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }

        } else {
            handleRoll(roll);
        }

    }

    private void handleRoll(int roll) {
        Player currentNewPlayer = players.get(currentPlayer);
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
        if (currentCategory() == "Pop")
            System.out.println(popQuestions.removeFirst());
        if (currentCategory() == "Science")
            System.out.println(scienceQuestions.removeFirst());
        if (currentCategory() == "Sports")
            System.out.println(sportsQuestions.removeFirst());
        if (currentCategory() == "Rock")
            System.out.println(rockQuestions.removeFirst());
    }


    private String currentCategory() {
        int place = players.get(currentPlayer).place;
        if (place - 1 == 0) return "Pop";
        if (place - 1 == 4) return "Pop";
        if (place - 1 == 8) return "Pop";
        if (place - 1 == 1) return "Science";
        if (place - 1 == 5) return "Science";
        if (place - 1 == 9) return "Science";
        if (place - 1 == 2) return "Sports";
        if (place - 1 == 6) return "Sports";
        if (place - 1 == 10) return "Sports";
        return "Rock";
    }

    public boolean handleCorrectAnswer() {
        if (players.get(currentPlayer).inPenaltyBox) {
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
        Player currentNewPlayer = players.get(currentPlayer);
        currentNewPlayer.purse++;
        System.out.println(players.get(currentPlayer).name
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
        System.out.println(players.get(currentPlayer).name + " was sent to the penalty box");
        players.get(currentPlayer).inPenaltyBox = true;

        currentPlayer++;
        if (currentPlayer == players.size()) currentPlayer = 0;
        return true;
    }


    private boolean didPlayerWin() {
        return !(players.get(currentPlayer).purse == 6);
    }
}
