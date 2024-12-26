package me.sheepbell.kkutu.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.sheepbell.kkutu.KKuTu;
import me.sheepbell.kkutu.bot.Bot;
import me.sheepbell.kkutu.game.Game;
import me.sheepbell.kkutu.game.GameManager;
import me.sheepbell.kkutu.game.TurnManager;
import me.sheepbell.kkutu.util.WordValidator;
import me.sheepbell.kkutu.web.HttpRequester;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;

public class AsyncChatListener implements Listener {
  private static final Plugin PLUGIN = KKuTu.getInstance();
  private static final BukkitScheduler SCHEDULER = PLUGIN.getServer().getScheduler();

  @EventHandler
  public void onAsyncChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    String message = PlainTextComponentSerializer.plainText().serialize(event.message());
    Game game = GameManager.getGame();
    TurnManager turnManager = game.getTurnManager();

    if (!GameManager.isPlaying()) {
      event.setCancelled(true);
      return;
    }

    if (!turnManager.isTurn(player)) {
      return;
    }

    if (message.isBlank() || message.contains(" ")) {
      notifyWrongMessage(message, false);
      return;
    }

    if (HttpRequester.isCached(message)) {
      notifyWrongMessage(message, true);
      return;
    }

    if (WordValidator.isValid(message, turnManager.getCurrentChar())) {
      turnManager.submit(message);
      if (game.isSolo()) {
        SCHEDULER.runTaskLater(PLUGIN, () -> Bot.autoPlay(message), 80L);
      }
    } else {
      notifyWrongMessage(message, false);
    }
  }

  private void notifyWrongMessage(String message, boolean cached) {
    Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO);
    Component subtitle = cached ? Component.text("이미 사용된 단어입니다.", NamedTextColor.RED) : Component.empty();
    Title title = Title.title(Component.text(message, NamedTextColor.RED), subtitle, times);
    Sound sound = Sound.sound(Key.key("block.anvil.land"), Sound.Source.MASTER, 1, 1);
    GameManager.getGame().getPlayers().forEach(player -> {
      player.showTitle(title);
      player.playSound(sound, Sound.Emitter.self());
      SCHEDULER.runTaskLater(PLUGIN, () -> player.showTitle(GameManager.getGame().getTurnManager().getTitle()), 20L);
    });
  }
}
