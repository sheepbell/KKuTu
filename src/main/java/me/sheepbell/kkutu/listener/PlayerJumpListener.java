package me.sheepbell.kkutu.listener;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PlayerJumpListener implements Listener {
  @EventHandler
  public void onPlayerJump(PlayerJumpEvent event) {
    String API_URL = "https://stdict.korean.go.kr/api/search.do?certkey_no=7027&key=44A243F05A85A1D66F450A364BFCED8B&type_search=search&req_type=json&q=";
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(API_URL + "나무"))
      .build();

    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      Bukkit.broadcast(Component.text("API 요청 중 오류가 발생했습니다.", NamedTextColor.RED));
    }

    if (response == null) {
      Bukkit.broadcast(Component.text("null"));
    }

    System.out.println(response.body());
  }
}
