package at.aau.se2.gamelogic.models;

/**
 * FROST: Deal 2 damage to the unit with the lowest power on the frosted row. (at beginning of
 * controllers turn) FOG: Deal 2 damage to the unit with the highest power on the fogged row. (at
 * beginning of controllers turn) RAIN: Deal 2 damage to a random unit on the wet row. (at beginning
 * of controllers turn)
 */
public enum RowStatus {
  FROST,
  FOG,
  RAIN;
}
