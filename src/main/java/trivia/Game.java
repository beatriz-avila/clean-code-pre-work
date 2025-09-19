package trivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// REFACTOR ME
public class Game implements IGame {
  ArrayList players = new ArrayList();
  boolean[] inPenaltyBox = new boolean[6];

  List<Player> newPlayers = new ArrayList<>();

  LinkedList<String> popQuestions = new LinkedList();
  LinkedList<String> scienceQuestions = new LinkedList();
  LinkedList<String> sportsQuestions = new LinkedList();
  LinkedList<String> rockQuestions = new LinkedList();

  private class Player {
    final String name;
    int place;
    int purse;
    boolean inPenaltyBox;

    public Player(String name) {
      this.name = name;
      this.place = 1;
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
    return (howManyPlayers() >= 2);
  }

  public boolean add(String playerName) {
    newPlayers.add(new Player(playerName));
    inPenaltyBox[howManyPlayers()] = false;
    players.add(playerName);

    System.out.println(playerName + " was added");
    System.out.println("They are player number " + players.size());
    return true;
  }

  public int howManyPlayers() {
    return players.size();
  }

  public void roll(int roll) {
    System.out.println(players.get(currentPlayer) + " is the current player");
    System.out.println("They have rolled a " + roll);

    if (inPenaltyBox[currentPlayer]) {
      if (roll % 2 != 0) {
        isGettingOutOfPenaltyBox = true;

        System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
        handleRoll(roll);
      } else {
        System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
        isGettingOutOfPenaltyBox = false;
      }

    } else {
      handleRoll(roll);
    }

  }

  private void handleRoll(int roll) {
    Player currentNewPlayer = newPlayers.get(currentPlayer);
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
    int place = newPlayers.get(currentPlayer).place;
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
    if (inPenaltyBox[currentPlayer]) {
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
    Player currentNewPlayer = newPlayers.get(currentPlayer);
    currentNewPlayer.purse++;
    System.out.println(players.get(currentPlayer)
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
    System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
    inPenaltyBox[currentPlayer] = true;

    currentPlayer++;
    if (currentPlayer == players.size()) currentPlayer = 0;
    return true;
  }


  private boolean didPlayerWin() {
    return !(newPlayers.get(currentPlayer).purse == 6);
  }
}
