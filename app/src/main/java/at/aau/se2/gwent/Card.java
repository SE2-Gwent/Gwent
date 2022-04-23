package at.aau.se2.gwent;

public class Card {
    String name;
    String effect;
    int points;

    public Card(String name, String effect, int points) {
        this.name = name;
        this.effect = effect;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
