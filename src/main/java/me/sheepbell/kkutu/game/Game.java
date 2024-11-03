package me.sheepbell.kkutu.game;

import me.sheepbell.kkutu.util.HttpRequester;
import me.sheepbell.kkutu.util.TimerDuration;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;

import java.util.List;

public class Game {
  private final String startingWord;
  private final Player player1;
  private final Player player2;
  private final Timer turnTimer;
  private final Timer roundTimer;
  private final TurnManager turnManager;

  public Game(String startingWord, Player player1, Player player2) {
    this.startingWord = startingWord;
    this.player1 = player1;
    this.player2 = player2;
    this.roundTimer = new Timer(this, 90, BossBar.Color.BLUE);
    this.turnTimer = new Timer(this, TimerDuration.getTurnDuration(90), BossBar.Color.YELLOW);
    this.turnManager = new TurnManager(this, startingWord, player1, player2);
  }

  public List<Player> getPlayers() {
    return List.of(player1, player2);
  }

  public Timer getTurnTimer() {
    return turnTimer;
  }

  public Timer getRoundTimer() {
    return roundTimer;
  }

  public TurnManager getTurnManager() {
    return turnManager;
  }

  protected void end() {
    player1.clearTitle();
    player2.clearTitle();
    roundTimer.delete();
    turnTimer.delete();
    HttpRequester.descriptionCache.clear();
    Player winner = turnManager.nextPlayer();
    Bukkit.broadcast(winner.name());
  }

  private void playWinningEffect(Player winner) {
    Title title = Title.title(winner.name().color(NamedTextColor.GOLD), Component.empty());
    player1.showTitle(title);
    player2.showTitle(title);
    fireFirework(winner);
  }

  private void fireFirework(Player winner) {
    Firework firework = (Firework) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK_ROCKET);
  }
}
