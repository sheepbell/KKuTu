package me.sheepbell.kkutu.game;

import me.sheepbell.kkutu.KKuTu;
import me.sheepbell.kkutu.util.TimerDuration;
import me.sheepbell.kkutu.util.WordRenderer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

public class TurnManager {
  private static final KKuTu PLUGIN = KKuTu.getInstance();
  private static final BukkitScheduler SCHEDULER = PLUGIN.getServer().getScheduler();
  private final Game game;
  private final Player player1;
  private final Player player2;
  private char currentChar;
  private Player currentPlayer;
  private int turnCount = 0;

  public TurnManager(Game game, String startingWord, Player player1, Player player2) {
    this.game = game;
    this.player1 = player1;
    this.player2 = player2;
    this.currentChar = startingWord.charAt(0);
    this.currentPlayer = player1;
    nextTurn(startingWord.charAt(startingWord.length() - 1));
  }

  public char getCurrentChar() {
    return currentChar;
  }

  public int getTurnCount() {
    return turnCount;
  }

  public boolean isTurn(Player player) {
    return currentPlayer.equals(player);
  }

  public void submit(String message) {
    game.getRoundTimer().pause();
    game.getTurnTimer().pause();
    WordRenderer.render(game, message);
    SCHEDULER.runTaskLater(PLUGIN, () -> nextTurn(message.charAt(message.length() - 1)), 40L);
  }

  public void nextTurn(char nextChar) {
    currentPlayer = nextPlayer();
    currentChar = nextChar;
    game.getRoundTimer().start();
    game.getTurnTimer().reset();
    game.getTurnTimer().setDuration(TimerDuration.getTurnDuration(game.getRoundTimer().getLeftDuration()));
    game.getTurnTimer().start();
    showTurnTitle();
    turnCount++;
  }

  public Player nextPlayer() {
    if (currentPlayer.equals(player1)) {
      return player2;
    }
    return player1;
  }

  private void showTurnTitle() {
    Title title = Title.title(text(currentChar), empty(), Title.Times.times(Duration.ZERO, Duration.ofMinutes(10), Duration.ZERO));
    game.getPlayers().forEach(player -> player.showTitle(title));
  }
}
