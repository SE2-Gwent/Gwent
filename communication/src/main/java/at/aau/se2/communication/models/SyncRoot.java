package at.aau.se2.communication.models;

import java.util.Date;

import androidx.annotation.Keep;

public class SyncRoot {
  private String createdOn = new Date().toString();

  @Keep
  public SyncRoot() {}

  public String getCreatedOn() {
    return createdOn;
  }
}
