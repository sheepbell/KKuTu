package me.sheepbell.kkutu.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.sheepbell.kkutu.game.GameManager;
import me.sheepbell.kkutu.game.TurnManager;
import me.sheepbell.kkutu.util.WordValidator;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Duration;

public class AsyncChatListener implements Listener {
  @EventHandler
  public void onAsyncChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    String message = PlainTextComponentSerializer.plainText().serialize(event.message());
    TurnManager turnManager = GameManager.getGame().getTurnManager();

    if (!GameManager.isPlaying()) {
      event.setCancelled(true);
      return;
    }

    if (!turnManager.isTurn(player)) {
      return;
    }

    if (WordValidator.isValid(message, turnManager.getCurrentChar())) {
      turnManager.submit(message);
    } else {
      notifyWrongMessage(player);
    }
  }

  private void notifyWrongMessage(Player player) {
    Title.Times times = Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO);
//    Title title = Title.title(Component.text(message, NamedTextColor.RED), Component.empty(), times);
    Sound sound = Sound.sound(Key.key("block.anvil.land"), Sound.Source.MASTER, 1, 1);
//    player.showTitle(title);
    player.playSound(sound, Sound.Emitter.self());
  }
}
