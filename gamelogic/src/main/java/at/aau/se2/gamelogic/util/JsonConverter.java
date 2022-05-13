package at.aau.se2.gamelogic.util;

import com.google.gson.Gson;

import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gamelogic.models.CardDecks;
import at.aau.se2.gamelogic.models.GameField;
import at.aau.se2.gamelogic.models.GameFieldRows;
import at.aau.se2.gamelogic.models.Hero;
import at.aau.se2.gamelogic.models.Player;
import at.aau.se2.gamelogic.models.Row;
import at.aau.se2.gamelogic.models.cardactions.AttackParams;
import at.aau.se2.gamelogic.models.cardactions.DeployParams;
import at.aau.se2.gamelogic.models.cardactions.FogParams;
import at.aau.se2.gamelogic.models.heroactions.HeroActionParams;

public class JsonConverter {
  public static String serialize(Object c) {
    return new Gson().toJson(c);
  }

  public static Card deserializeCard(String json) {
    return new Gson().fromJson(json, Card.class);
  }

  public static CardDecks deserializeCardDecks(String json) {
    return new Gson().fromJson(json, CardDecks.class);
  }

  public static GameField deserializeGameField(String json) {
    return new Gson().fromJson(json, GameField.class);
  }

  public static GameFieldRows deserializeGameFieldRows(String json) {
    return new Gson().fromJson(json, GameFieldRows.class);
  }

  public static Hero deserializeHero(String json) {
    return new Gson().fromJson(json, Hero.class);
  }

  public static Player deserializePlayer(String json) {
    return new Gson().fromJson(json, Player.class);
  }

  public static Row deserializeRow(String json) {
    return new Gson().fromJson(json, Row.class);
  }

  public static AttackParams deserializeCardAttackParams(String json) {
    return new Gson().fromJson(json, AttackParams.class);
  }

  public static DeployParams deserializeCardDeployParams(String json) {
    return new Gson().fromJson(json, DeployParams.class);
  }

  public static FogParams deserializeCardFogParams(String json) {
    return new Gson().fromJson(json, FogParams.class);
  }

  public static HeroActionParams deserializeHeroAttackParams(String json) {
    return new Gson().fromJson(json, at.aau.se2.gamelogic.models.heroactions.AttackParams.class);
  }
}
