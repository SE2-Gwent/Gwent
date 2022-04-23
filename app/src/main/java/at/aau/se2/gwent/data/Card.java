package at.aau.se2.gwent.data;

import java.util.Set;

public class Card {
    enum CardType {
        Human,
        Knight,
        Elf
    }

    private int id;
    private String name;
    private Set <CardType> types;
    private int power;
    private String text;
    private String flavorText;
    private int powerDiff;

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

    public Set<CardType> getTypes() {
        return types;
    }

    public void setTypes(Set<CardType> types) {
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
