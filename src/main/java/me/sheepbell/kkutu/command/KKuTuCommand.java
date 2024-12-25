package me.sheepbell.kkutu.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.sheepbell.kkutu.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class KKuTuCommand {
  public static void register(LifecycleEventManager<Plugin> manager) {
    manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
      Commands commands = event.registrar();
      commands.register(
        literal("kkutu")
          .then(literal("start")
            .then(argument("isSolo", BoolArgumentType.bool())
              .executes(context -> {
                boolean isSolo = BoolArgumentType.getBool(context, "isSolo");
                GameManager.startGame(Bukkit.getWorld("world").getPlayers().get(0), Bukkit.getWorld("world").getPlayers().get(1), isSolo);
                return Command.SINGLE_SUCCESS;
              })))
          .build()
      );
    });
  }
}
