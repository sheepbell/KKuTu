package me.sheepbell.kkutu.util;

public class TimerDuration {
  public static int getTurnDuration(int roundDuration) {
    return 15 - getSpeed(roundDuration);
  }

  private static int getSpeed(int roundDuration) {
    if (roundDuration < 5) {
      return 10;
    } else if (roundDuration < 11) {
      return 9;
    } else if (roundDuration < 18) {
      return 8;
    } else if (roundDuration < 26) {
      return 7;
    } else if (roundDuration < 35) {
      return 6;
    } else if (roundDuration < 45) {
      return 5;
    } else if (roundDuration < 56) {
      return 4;
    } else if (roundDuration < 68) {
      return 3;
    } else if (roundDuration < 81) {
      return 2;
    } else if (roundDuration < 95) {
      return 1;
    }
    return 0;
  }
}
