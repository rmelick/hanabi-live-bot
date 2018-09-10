package net.rmelick.hanabi.backend;

/**
 *
 */
public class PlayerInfo {
  private String _name;
  private String _id;

  public PlayerInfo(String name, String id) {
    _name = name;
    _id = id;
  }

  public String getName() {
    return _name;
  }

  public String getId() {
    return _id;
  }
}
