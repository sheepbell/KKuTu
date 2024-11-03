package me.sheepbell.kkutu.util;

public class WordValidator {
  public static boolean isValid(String word, char currentChar) {
    return word.charAt(0) == currentChar && !HttpRequester.request(word).isEmpty();
  }
}
