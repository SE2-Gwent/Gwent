package at.aau.se2.gwent.data;

import java.util.LinkedList;

public class Card {
  public enum CardType {
    Human,
    Soldier,
    Knight,
    Elf
  }

  private int id;
  private String name;
  private LinkedList<CardType> types;
  private int power;
  private String text;
  private String flavorText;
  private int powerDiff;

  public Card(
      int id,
      String name,
      LinkedList<CardType> types,
      int power,
      String text,
      String flavorText,
      int powerDiff) {
    this.id = id;
    this.name = name;
    this.types = types;
    this.power = power;
    this.text = text;
    this.flavorText = flavorText;
    this.powerDiff = powerDiff;
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

  public LinkedList<CardType> getTypes() {
    return types;
  }

  public void setTypes(LinkedList<CardType> types) {
    this.types = types;
  }

  public int getPower() {
    return power;
  }

  public void setPower(int power) {
    this.power = power;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getFlavorText() {
    return flavorText;
  }

  public void setFlavorText(String flavorText) {
    this.flavorText = flavorText;
  }

  public int getPowerDiff() {
    return powerDiff;
  }

  public void setPowerDiff(int powerDiff) {
    this.powerDiff = powerDiff;
  }
}
