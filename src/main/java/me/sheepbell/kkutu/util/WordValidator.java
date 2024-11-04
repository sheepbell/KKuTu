package me.sheepbell.kkutu.util;

public class WordValidator {
  public static boolean isValid(String word, char currentChar) {
    String response = HttpRequester.request(word);
    return word.charAt(0) == currentChar && response != null && !response.isEmpty();
  }
}
