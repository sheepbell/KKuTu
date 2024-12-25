package me.sheepbell.kkutu.web;

import me.sheepbell.kkutu.game.GameManager;
import me.sheepbell.kkutu.util.PrivateConstant;
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
  public static final HashMap<Integer, String> descriptionCache = new HashMap<>();
  private static final String API_URL = "https://stdict.korean.go.kr/api/search.do?key=";
  private static final String WORD_PARAMETER = "&type_search=search&req_type=json&q=";
  private static final String START_PARAMETER = "&req_type=json&method=start&num=100&advanced=y&type2=native,chinese&letter_s=2&letter_e=5&q=";

  public static String request(String word, RequestType type) {
    String parameter = type == RequestType.WORD ? WORD_PARAMETER : START_PARAMETER;
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(API_URL + PrivateConstant.API_KEY + parameter + word))
      .build();

    HttpResponse<String> response = null;
    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      Bukkit.broadcast(Component.text("API 요청 중 오류가 발생했습니다.", NamedTextColor.RED));
    }

    cache(response);

    return response.body();
  }

  private static void cache(HttpResponse<String> response) {
    if (response != null && !response.body().isEmpty()) {
      descriptionCache.put(GameManager.getGame().getTurnManager().getTurnCount(), getDefinition(response.body()));
    }
  }

  private static String getWord(String body) {
    try {
      JSONParser parser = new JSONParser();
      JSONObject jsonObject = (JSONObject) parser.parse(body);
      JSONObject channel = (JSONObject) jsonObject.get("channel");
      JSONArray items = (JSONArray) channel.get("item");
      JSONObject item = (JSONObject) items.getFirst();
      return item.get("word").toString().replace("-", "").replace("^", "");
    } catch (ParseException e) {
      Bukkit.broadcast(Component.text("API 응답을 파싱하는 중 오류가 발생했습니다.", NamedTextColor.RED));
      return null;
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

  public static boolean isCached(String word) {
    return descriptionCache.containsKey(word);
  }
}
