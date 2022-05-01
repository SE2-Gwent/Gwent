package at.aau.se2.gamelogic.models;

public class Card {
  private int id;
  private String name;
  private int power;
  private int powerDiff;
  private String cardText;

  public Card(int id, String name, int power, int powerDiff, String cardText) {
    this.id = id;
    this.name = name;
    this.power = power;
    this.powerDiff = powerDiff;
    this.cardText = cardText;
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
}
