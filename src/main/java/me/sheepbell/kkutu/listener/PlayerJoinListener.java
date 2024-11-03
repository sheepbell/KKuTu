package me.sheepbell.kkutu.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    event.getPlayer().clearTitle();
//    AtomicInteger count = new AtomicInteger();
//    KKuTu.getInstance().getServer().getScheduler().runTaskTimer(KKuTu.getInstance(), () -> {
//      if (count.getAndIncrement() >= 3) {
//        return;
//      }
//      event.getPlayer().playSound(Sound.sound(Key.key("block.note_block.chime"), Sound.Source.MASTER, 1, 0.943874F));
//    }, 20L, 1L);
  }
}
