package me.sheepbell.kkutu.bot;

import me.sheepbell.kkutu.game.GameManager;
import me.sheepbell.kkutu.web.HttpRequester;
import me.sheepbell.kkutu.web.RequestType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Random;

public class Bot {
  private static final Random RANDOM = new Random();

  public static void autoPlay(String word) {
    String startingChar = String.valueOf(word.charAt(word.length() - 1));
    String response = HttpRequester.request(startingChar, RequestType.START);
    String wordToPlay = getWord(response);
    Bukkit.broadcast(Component.text(wordToPlay));
    GameManager.getGame().getTurnManager().submit(wordToPlay);
  }

  private static String getWord(String body) {
    try {
      JSONParser parser = new JSONParser();
      JSONObject jsonObject = (JSONObject) parser.parse(body);
      JSONObject channel = (JSONObject) jsonObject.get("channel");
      JSONArray items = (JSONArray) channel.get("item");
      JSONObject item = (JSONObject) items.get(RANDOM.nextInt(items.size()));
      return item.get("word").toString().replace("-", "").replace("^", "");
    } catch (ParseException e) {
      Bukkit.broadcast(Component.text("API 응답을 파싱하는 중 오류가 발생했습니다.", NamedTextColor.RED));
      return null;
    }
  }
}
