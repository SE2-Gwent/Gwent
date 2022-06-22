package at.aau.se2.gamelogic.models;

import java.util.ArrayList;

import androidx.annotation.Keep;
import at.aau.se2.gamelogic.models.cardactions.triggers.DeployTrigger;
import at.aau.se2.gamelogic.models.cardactions.triggers.OrderTrigger;

public class Card {
  private int id;
  private String name;
  private ArrayList<CardType> types;
  private int power;
  private int powerDiff;
  private String cardText;
  private DeployTrigger deployTrigger;
  private OrderTrigger orderTrigger;
  private String imgResourceBasic;
  private String imgResourceDetail;

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
      DeployTrigger deployTrigger,
      OrderTrigger orderTrigger,
      String imgResourceBasic,
      String imgResourceDetail) {
    this.id = id;
    this.name = name;
    this.types = types;
    this.power = power;
    this.powerDiff = powerDiff;
    this.cardText = cardText;
    this.deployTrigger = deployTrigger;
    this.orderTrigger = orderTrigger;
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

  public int getCurrentAttackPoints() {
    return power + powerDiff;
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

  public DeployTrigger getDeployTrigger() {
    return deployTrigger;
  }

  public OrderTrigger getOrderTrigger() {
    return orderTrigger;
  }

  public String getFirebaseId() {
    return id + "_card";
  }

  public void setDeployTrigger(DeployTrigger deployTrigger) {
    this.deployTrigger = deployTrigger;
  }

  public void setOrderTrigger(OrderTrigger orderTrigger) {
    this.orderTrigger = orderTrigger;
  }
}
