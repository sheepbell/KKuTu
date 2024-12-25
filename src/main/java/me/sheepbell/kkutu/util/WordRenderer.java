package me.sheepbell.kkutu.util;

import me.sheepbell.kkutu.KKuTu;
import me.sheepbell.kkutu.game.Game;
import me.sheepbell.kkutu.web.HttpRequester;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class WordRenderer {
  private static final KKuTu PLUGIN = KKuTu.getInstance();
  private static final BukkitScheduler SCHEDULER = PLUGIN.getServer().getScheduler();
  private static final Key key = Key.key("block.note_block.pling");
  private static final Sound C = Sound.sound(key, Sound.Source.MASTER, 1, 0.707107F);
  private static final Sound F = Sound.sound(key, Sound.Source.MASTER, 1, 0.943874F);
  private static final Sound G = Sound.sound(key, Sound.Source.MASTER, 1, 1.059463F);

  public static void render(Game game, String word) {
    String definition = HttpRequester.descriptionCache.get(game.getTurnManager().getTurnCount());
    int delayPerCharacter = 20 / word.length();

    AtomicInteger index = new AtomicInteger(1);
    SCHEDULER.runTaskTimer(PLUGIN, task -> {
      if (index.get() > word.length()) {
        task.cancel();
        playFinishNotes(game);
        return;
      }

      game.getPlayers().forEach(player -> {
        Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO);
        Title title = Title.title(Component.text(word.substring(0, index.get())), Component.text(definition), times);
        player.showTitle(title);
        player.playSound(F, Sound.Emitter.self());
      });

      index.getAndIncrement();
    }, 0, delayPerCharacter);
  }

  public static void playFinishNotes(Game game) {
    AtomicInteger count = new AtomicInteger();
    SCHEDULER.runTaskTimer(PLUGIN, task -> {
      game.getPlayers().forEach(player -> {
        if (count.get() < 2) {
          player.playSound(C);
        } else {
          player.playSound(G);
          task.cancel();
        }
      });
      count.getAndIncrement();
    }, 0, 5);
  }
}
