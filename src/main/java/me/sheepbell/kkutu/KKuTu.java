package me.sheepbell.kkutu;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import me.sheepbell.kkutu.command.KKuTuCommand;
import me.sheepbell.kkutu.listener.AsyncChatListener;
import me.sheepbell.kkutu.listener.PlayerJoinListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class KKuTu extends JavaPlugin {
  private static KKuTu instance;

  public static KKuTu getInstance() {
    return instance;
  }

  @Override
  public void onEnable() {
    instance = this;
    registerCommands();
    registerEvents();
  }

  private void registerCommands() {
    LifecycleEventManager<Plugin> manager = getLifecycleManager();
    KKuTuCommand.register(manager);
  }

  private void registerEvents() {
    List<Listener> listeners = List.of(
      new AsyncChatListener(),
      new PlayerJoinListener()
    );
    listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
  }
}
