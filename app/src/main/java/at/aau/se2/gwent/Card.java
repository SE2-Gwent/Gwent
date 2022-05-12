package at.aau.se2.gwent;

public class Card {
  int id;
  int resource;

  public Card(int id, int resource) {
    this.id = id;
    this.resource = resource;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
