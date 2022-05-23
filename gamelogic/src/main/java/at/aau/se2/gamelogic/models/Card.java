package at.aau.se2.gamelogic.models;

import java.util.ArrayList;

import at.aau.se2.gamelogic.models.cardactions.ActionParams;

public class Card {
  private int id;
  private String name;
  private ArrayList<CardType> types;
  private int power;
  private int powerDiff;
  private String cardText;
  private ArrayList<ActionParams> cardActions;
  private String imgResourceBasic;
  private String imgResourceDetail;

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
      ArrayList<ActionParams> cardActions,
      String imgResourceBasic,
      String imgResourceDetail) {
    this.id = id;
    this.name = name;
    this.types = types;
    this.power = power;
    this.powerDiff = powerDiff;
    this.cardText = cardText;
    this.cardActions = cardActions;
    this.imgResourceBasic = imgResourceBasic;
    this.imgResourceDetail = imgResourceDetail;
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

  public String getImgResourceBasic() {
    return imgResourceBasic;
  }

  public void setImgResourceBasic(String imgResourceBasic) {
    this.imgResourceBasic = imgResourceBasic;
  }

  public String getImgResourceDetail() {
    return imgResourceDetail;
  }

  public void setImgResourceDetail(String imgResourceDetail) {
    this.imgResourceDetail = imgResourceDetail;
  }
}
