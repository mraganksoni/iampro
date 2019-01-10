package com.mssinfotech.iampro.co.events;

public class NewLoginEvent {
  public final boolean isLoggedIn;

  public NewLoginEvent(boolean isLoggedIn) {
    this.isLoggedIn = isLoggedIn;
  }
}
