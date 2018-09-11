package net.rmelick.hanabi.backend;

/**
 *
 */
public class PlayerInfo {
  private String _name;
  private String _id;
  private PlayerType _type;

  public PlayerInfo(String name, String id, PlayerType type) {
    _name = name;
    _id = id;
    _type = type;
  }

  public String getName() {
    return _name;
  }

  public String getId() {
    return _id;
  }

  public PlayerType getType() {
    return _type;
  }
}
