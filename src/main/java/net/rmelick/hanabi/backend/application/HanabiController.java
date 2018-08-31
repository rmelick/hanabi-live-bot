package net.rmelick.hanabi.backend.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.backend.GameManager;
import net.rmelick.hanabi.backend.Rank;
import net.rmelick.hanabi.backend.api.*;
import net.rmelick.hanabi.backend.state.FullGameState;
import net.rmelick.hanabi.backend.state.PlayerState;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Takes requests over the public api and maps them to internal calls to the GameManager
 *  - extracts the user information from the session
 */
@RestController
@CrossOrigin
public class HanabiController {
  private final GameManager _gameManager = new GameManager();

  @RequestMapping(value = "/newGame", method = RequestMethod.POST)
  public Map<String, String> newGame(@RequestParam int numPlayers) {
    String gameId = _gameManager.createGame(numPlayers);
    Map<String, String> response = new HashMap<>();
    response.put("game_id", gameId);
    return response;
  }

  @RequestMapping(value = "/gameState/{gameId}", method = RequestMethod.GET)
  public ViewableGameState getGameState(@PathVariable String gameId) throws JsonProcessingException {
    FullGameState fullGameState = _gameManager.getGame(gameId);
    String currentPlayer = fullGameState.getCurrentPlayerState().getId();
    return InternalToExternalAdapter.convertInternalGameState(fullGameState, currentPlayer);
  }

  private FullGameState newRandomGameState() {
    String gameId = _gameManager.createGame(4);
    FullGameState gameState = _gameManager.getGame(gameId);
    boolean played = false;
    for (PlayerState player : gameState.getPlayerStates()) {
      int tilePosition = 0;
      for (net.rmelick.hanabi.backend.Tile tile : player.getTiles()) {
        if (Rank.ONE.equals(tile.getRank())) {
          gameState.unsafePlayOffTurn(player.getId(), tilePosition);
          played = true;
          break;
        }
        tilePosition++;
      }
      if (played) {
        break;
      }
    }
    for (PlayerState player : gameState.getPlayerStates()) {
      gameState.discard(player.getId(), 0);
    }
    return gameState;
  }
}
