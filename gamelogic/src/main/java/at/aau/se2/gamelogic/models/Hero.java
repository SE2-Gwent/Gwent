package at.aau.se2.gamelogic.models;

import androidx.annotation.Keep;

public class Hero {
  public enum Action {
    ATTACK,
    HEAL
  }

  private int id;
  private Action heroAction;
  private int heroActionCoolDown; // Cooldown in Zuegen

  @Keep
  public Hero() {}

  public Hero(int id, Action heroAction, int heroActionCoolDown) {
    this.id = id;
    this.heroAction = heroAction;
    this.heroActionCoolDown = heroActionCoolDown;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getHeroActionCoolDown() {
    return heroActionCoolDown;
  }

  public Action getHeroAction() {
    return heroAction;
  }

  public void setHeroActionCoolDown(int heroActionCoolDown) {
    this.heroActionCoolDown = heroActionCoolDown;
  }
}
