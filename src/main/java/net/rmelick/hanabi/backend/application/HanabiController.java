package net.rmelick.hanabi.backend.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.backend.GameManager;
import net.rmelick.hanabi.backend.Rank;
import net.rmelick.hanabi.backend.api.*;
import net.rmelick.hanabi.backend.api.input.Move;
import net.rmelick.hanabi.backend.state.FullGameState;
import net.rmelick.hanabi.backend.state.PlayerState;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

  @RequestMapping(value = "/games/{gameId}/join", method = RequestMethod.POST)
  public Map<String, String> joinGame(@PathVariable String gameId) {
    FullGameState fullGameState = _gameManager.getGame(gameId);
    String playerId = fullGameState.getCurrentPlayerState().getId();
    Map<String, String> response = new HashMap<>();
    response.put("player_id", playerId);
    return response;
  }

  @RequestMapping(value = "/games/{gameId}/state", method = RequestMethod.GET)
  public ViewableGameState getGameState(@PathVariable String gameId, @RequestParam String playerId) {
    FullGameState fullGameState = _gameManager.getGame(gameId);
    return InternalToExternalAdapter.convertInternalGameState(fullGameState, playerId);
  }

  @RequestMapping(value = "/games/{gameId}/makeMove", method = RequestMethod.POST)
  public void makeMove(@PathVariable String gameId, @RequestBody Move move) {

  }

}
