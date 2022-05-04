package at.aau.se2.gamelogic.models;

import at.aau.se2.gamelogic.models.heroactions.HeroActionParams;

public class Hero {
  private int id;
  private String name;
  private HeroActionParams heroAction;
  private int heroActionCoolDown;

  public Hero(int id, String name, HeroActionParams heroAction) {
    this.id = id;
    this.name = name;
    this.heroAction = heroAction;
    this.heroActionCoolDown = 0;
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

  public HeroActionParams getHeroAction() {
    return heroAction;
  }

  public void setHeroAction(HeroActionParams heroAction) {
    this.heroAction = heroAction;
  }

  public int getHeroActionCoolDown() {
    return heroActionCoolDown;
  }

  public void setHeroActionCoolDown(int heroActionCoolDown) {
    this.heroActionCoolDown = heroActionCoolDown;
  }
}
