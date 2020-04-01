package net.rmelick.hanabi.live.bot;

import org.junit.Test;

/**
 *
 */
public class GameManagerImplTest {

  @Test
  public void testInitialSetup() {
    GameManager gameManager = new GameManagerImpl();
    gameManager.startNewGame(4);
    System.out.println(gameManager.getDebugGameState());
  }
}