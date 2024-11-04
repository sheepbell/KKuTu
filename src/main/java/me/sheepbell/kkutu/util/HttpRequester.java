package me.sheepbell.kkutu.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class HttpRequester {
  public static final HashMap<String, String> descriptionCache = new HashMap<>();
  private static final String API_URL = "https://stdict.korean.go.kr/api/search.do?key=";
  private static final String API_PARAMETER = "&type_search=search&req_type=json&q=";

  public static String request(String word) {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(API_URL + PrivateConstant.API_KEY + API_PARAMETER + word))
      .build();

    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      Bukkit.broadcast(Component.text("API 요청 중 오류가 발생했습니다.", NamedTextColor.RED));
    }

    cache(word, response);

    return response.body();
  }

  private static void cache(String word, HttpResponse<String> response) {
    if (response != null && !response.body().isEmpty()) {
      descriptionCache.put(word, getDefinition(response.body()));
    }
  }

  private static String getDefinition(String body) {
    try {
      JSONParser parser = new JSONParser();
      JSONObject jsonObject = (JSONObject) parser.parse(body);
      JSONObject channel = (JSONObject) jsonObject.get("channel");
      JSONArray items = (JSONArray) channel.get("item");
      JSONObject item = (JSONObject) items.getFirst();
      JSONObject sense = (JSONObject) item.get("sense");
      return sense.get("definition").toString();
    } catch (ParseException e) {
      Bukkit.broadcast(Component.text("API 응답을 파싱하는 중 오류가 발생했습니다.", NamedTextColor.RED));
      return null;
    }
  }
}
