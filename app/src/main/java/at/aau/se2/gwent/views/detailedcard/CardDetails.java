package at.aau.se2.gwent.views.detailedcard;

public class CardDetails {
  private String name;
  private String types;
  private int power;
  private int powerDiff;
  private String cardText;
  private String imgResourceName;

  CardDetails(
      String name,
      String types,
      int power,
      int powerDiff,
      String cardText,
      String imgResourceName) {
    this.name = name;
    this.types = types;
    this.power = power;
    this.powerDiff = powerDiff;
    this.cardText = cardText;
    this.imgResourceName = imgResourceName;
  }

  enum ViewState {
    INITIAL,
    LOADED
  }

  String getName() {
    return name;
  }

  String getTypes() {
    return types;
  }

  int getPower() {
    return power;
  }

  int getPowerDiff() {
    return powerDiff;
  }

  String getCardText() {
    return cardText;
  }

  String getImgResourceName() {
    return imgResourceName;
  }
}
