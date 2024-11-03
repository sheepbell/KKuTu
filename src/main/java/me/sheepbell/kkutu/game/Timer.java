package me.sheepbell.kkutu.game;

import me.sheepbell.kkutu.KKuTu;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.empty;

public class Timer {
  private static final KKuTu PLUGIN = KKuTu.getInstance();
  private static final BukkitScheduler SCHEDULER = PLUGIN.getServer().getScheduler();
  private final Game game;
  private final BossBar timer;
  private final AtomicInteger leftDuration;
  private int duration;
  private boolean isRunning = false;

  public Timer(Game game, int duration, BossBar.Color color) {
    this.game = game;
    this.timer = BossBar.bossBar(empty(), 1, color, BossBar.Overlay.PROGRESS);
    this.duration = duration;
    this.leftDuration = new AtomicInteger(duration);
    showToPlayers(game);
  }

  private void showToPlayers(Game game) {
    game.getPlayers().forEach(player -> player.showBossBar(timer));
  }

  public void start() {
    isRunning = true;
    SCHEDULER.runTaskTimer(PLUGIN, task -> {
      if (!isRunning) {
        task.cancel();
      }

      if (leftDuration.getAndDecrement() <= 0) {
        end();
        task.cancel();
        return;
      }

      timer.progress((float) leftDuration.get() / duration);
    }, 20L, 20L);
  }

  public void pause() {
    isRunning = false;
  }

  public void end() {
    GameManager.endGame();
  }

  public void reset() {
    pause();
    timer.progress(1);
    leftDuration.set(duration);
  }

  public int getLeftDuration() {
    return leftDuration.get();
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public void delete() {
    pause();
    game.getPlayers().forEach(player -> player.hideBossBar(timer));
  }
}
