package net.rmelick.hanabi.live.bot.application;

import net.rmelick.hanabi.live.bot.Color;
import net.rmelick.hanabi.live.bot.Hint;
import net.rmelick.hanabi.live.bot.Rank;
import net.rmelick.hanabi.live.bot.api.game.HintMove;

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
