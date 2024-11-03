package me.sheepbell.kkutu.game;

import org.bukkit.entity.Player;

public class GameManager {
  private static Game game;
  private static boolean isPlaying = false;

  public static Game getGame() {
    return game;
  }

  public static boolean isPlaying() {
    return isPlaying;
  }

  public static void startGame(Player player1, Player player2) {
    if (isPlaying) {
      return;
    }
    isPlaying = true;
    game = new Game("참나", player1, player2);
  }

  public static void endGame() {
    game.end();
    game = null;
    isPlaying = false;
  }
}
