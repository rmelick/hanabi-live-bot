package net.rmelick.hanabi.backend;

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
