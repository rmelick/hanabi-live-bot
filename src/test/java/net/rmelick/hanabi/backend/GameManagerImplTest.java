package net.rmelick.hanabi.backend;

import org.junit.Test;

import static org.junit.Assert.*;

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