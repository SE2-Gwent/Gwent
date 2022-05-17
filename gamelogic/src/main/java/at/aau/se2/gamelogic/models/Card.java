package at.aau.se2.gamelogic.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.models.cardactions.ActionParams;

public class Card {
  private int id;
  private String name;
  private ArrayList<CardType> types;
  private int power;
  private int powerDiff;
  private String cardText;
  private ArrayList<ActionParams> cardActions;
  private Map<ActionParams, Integer> currentActionCooldown;

  @Keep
  public Card() {}

  // TODO: tmp, please delete when boardView connected to GameLogic
  public Card(int id) {
    this.id = id;
  }

  public Card(
      int id,
      String name,
      ArrayList<CardType> types,
      int power,
      int powerDiff,
      String cardText,
      ArrayList<ActionParams> cardActions) {
    this.id = id;
    this.name = name;
    this.types = types;
    this.power = power;
    this.powerDiff = powerDiff;
    this.cardText = cardText;
    this.cardActions = cardActions;
    this.currentActionCooldown = new HashMap<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<CardType> getTypes() {
    return types;
  }

  public void setTypes(ArrayList<CardType> types) {
    this.types = types;
  }

  public int getPower() {
    return power;
  }

  public void setPower(int power) {
    this.power = power;
  }

  public int getPowerDiff() {
    return powerDiff;
  }

  public void setPowerDiff(int powerDiff) {
    this.powerDiff = powerDiff;
  }

  public String getCardText() {
    return cardText;
  }

  public void setCardText(String cardText) {
    this.cardText = cardText;
  }

  public ArrayList<ActionParams> getCardActions() {
    return cardActions;
  }

  public void setCardActions(ArrayList<ActionParams> cardActions) {
    this.cardActions = cardActions;
  }

  public Map<ActionParams, Integer> getCurrentActionCooldown() {
    return currentActionCooldown;
  }

  public void setCurrentActionCooldown(Map<ActionParams, Integer> currentActionCooldown) {
    this.currentActionCooldown = currentActionCooldown;
  }
}
