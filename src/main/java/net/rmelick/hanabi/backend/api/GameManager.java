package net.rmelick.hanabi.backend.api;

import net.rmelick.hanabi.backend.Clue;

/**
 *
 */
public interface GameManager {

  // starts and deals
  void startNewGame(int numPlayers);

  //
  void currentPlayerGivesClue(int toPlayer, Clue clue);

  void currentPlayerPlays(int positionToPlay);

  void currentPlayerDiscards(int positionToDiscard);

  String getDebugGameState();
}
