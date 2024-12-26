package me.sheepbell.kkutu.game;

import org.bukkit.entity.Player;

import java.util.Random;

public class GameManager {
  private static final Random RANDOM = new Random();
  private static Game game;
  private static boolean isPlaying = false;

  public static Game getGame() {
    return game;
  }

  public static boolean isPlaying() {
    return isPlaying;
  }

  public static void startGame(Player player1, Player player2, boolean isSolo) {
    if (isPlaying) {
      return;
    }
    isPlaying = true;
    String[] startingWords = {"가", "나", "다", "라", "마"};
    String startingWord = startingWords[RANDOM.nextInt(startingWords.length)];
    game = new Game(startingWord, player1, player2, isSolo);
  }

  public static void endGame() {
    game.end();
    game = null;
    isPlaying = false;
  }
}
