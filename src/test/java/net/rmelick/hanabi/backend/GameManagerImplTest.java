package net.rmelick.hanabi.backend;

import net.rmelick.hanabi.backend.api.GameManager;
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