package net.rmelick.hanabi.live.bot;

import java.util.Objects;

/**
 *
 */
public class PlayerInfo implements Comparable<PlayerInfo>{
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerInfo that = (PlayerInfo) o;
    return Objects.equals(_name, that._name) &&
        Objects.equals(_id, that._id) &&
        _type == that._type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_name, _id, _type);
  }

  @Override
  public int compareTo(PlayerInfo o) {
    return this.getId().compareTo(o.getId());
  }
}
