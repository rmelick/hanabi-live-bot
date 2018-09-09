package net.rmelick.hanabi.backend.application;

import net.rmelick.hanabi.backend.Color;
import net.rmelick.hanabi.backend.Hint;
import net.rmelick.hanabi.backend.Rank;
import net.rmelick.hanabi.backend.api.HintMove;

/**
 *
 */
public class ExternalToInternalAdapter {
  public static Hint convertHintMove(HintMove externalHint) {
    if (externalHint.color != null) {
      return new Hint(Hint.HintType.COLOR, Color.fromPrettyName(externalHint.color), null);
    } else {
     return new Hint(Hint.HintType.RANK, null, Rank.fromInt(externalHint.rank));
    }
  }
}
