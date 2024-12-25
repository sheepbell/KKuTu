package me.sheepbell.kkutu.util;

import me.sheepbell.kkutu.web.HttpRequester;
import me.sheepbell.kkutu.web.RequestType;

public class WordValidator {
  public static boolean isValid(String word, char currentChar) {
    String response = HttpRequester.request(word, RequestType.WORD);
    return word.charAt(0) == currentChar && response != null && !response.isEmpty();
  }
}
