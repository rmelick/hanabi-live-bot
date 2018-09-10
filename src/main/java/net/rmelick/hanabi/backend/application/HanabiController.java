package net.rmelick.hanabi.backend.application;

import net.rmelick.hanabi.backend.GameManager;
import net.rmelick.hanabi.backend.GameWaitingToBegin;
import net.rmelick.hanabi.backend.api.game.DiscardMove;
import net.rmelick.hanabi.backend.api.game.HintMove;
import net.rmelick.hanabi.backend.api.game.PlayMove;
import net.rmelick.hanabi.backend.api.game.ViewableGameState;
import net.rmelick.hanabi.backend.api.lobby.GameStateSummary;
import net.rmelick.hanabi.backend.api.lobby.GameStatus;
import net.rmelick.hanabi.backend.state.FullGameState;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Takes requests over the public api and maps them to internal calls to the GameManager
 *  - extracts the user information from the session
 */
@RestController
@CrossOrigin
public class HanabiController {
  private final GameManager _gameManager = new GameManager();

  @RequestMapping(value = "/newGame", method = RequestMethod.POST)
  public void newGame(@RequestParam int numPlayers) {
    _gameManager.createGame();
  }

  @RequestMapping(value = "/games", method = RequestMethod.GET)
  public List<GameStateSummary> getAllGames() {
    List<GameStateSummary> allGames = new ArrayList<>();
    allGames.addAll(_gameManager.getGamesWaitingToBegin().values().
        stream().map(gameWaitingToBegin -> {
          GameStateSummary summary = new GameStateSummary();
          summary.gameId = gameWaitingToBegin.getGameId();
          summary.status = GameStatus.WAITING_TO_BEGIN;
          summary.numPlayers = gameWaitingToBegin.getPlayers().size();
          return summary;
        })
        .collect(Collectors.toList()));

    allGames.addAll(_gameManager.getInProgressGames().values().
        stream().map(game -> {
      GameStateSummary summary = new GameStateSummary();
      summary.gameId = game.getGameId();
      summary.status = GameStatus.IN_PROGRESS;
      summary.numPlayers = game.getPlayerStates().size();
      return summary;
    })
        .collect(Collectors.toList()));

    allGames.addAll(_gameManager.getCompletedGames().values().
        stream().map(game -> {
      GameStateSummary summary = new GameStateSummary();
      summary.gameId = game.getGameState().getGameId();
      summary.status = GameStatus.COMPLETED;
      summary.numPlayers = game.getGameState().getPlayerStates().size();
      return summary;
    })
        .collect(Collectors.toList()));
    return allGames;
  }

  @RequestMapping(value = "/games/{gameId}/join", method = RequestMethod.POST)
  public Map<String, String> joinGame(@PathVariable String gameId) {
    FullGameState fullGameState = _gameManager.getInProgressGame(gameId);
    String playerId = fullGameState.getCurrentPlayerState().getId();
    Map<String, String> response = new HashMap<>();
    response.put("player_id", playerId);
    return response;
  }

  @RequestMapping(value = "/games/{gameId}/state", method = RequestMethod.GET)
  public ViewableGameState getGameState(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId) {
    FullGameState fullGameState = _gameManager.getInProgressGame(gameId);
    return InternalToExternalAdapter.convertInternalGameState(fullGameState, playerId);
  }

  @RequestMapping(value = "/games/{gameId}/move/play", method = RequestMethod.POST)
  public void makeMove(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId, @RequestBody PlayMove playMove) {
    _gameManager.getInProgressGame(gameId).play(playerId, playMove.position);
  }

  @RequestMapping(value = "/games/{gameId}/move/discard", method = RequestMethod.POST)
  public void makeMove(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId, @RequestBody DiscardMove discardMove) {
    _gameManager.getInProgressGame(gameId).discard(playerId, discardMove.position);
  }

  @RequestMapping(value = "/games/{gameId}/move/hint", method = RequestMethod.POST)
  public void makeMove(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId, @RequestBody HintMove hintMove) {
    _gameManager.getInProgressGame(gameId).hint(playerId, hintMove.playerId, ExternalToInternalAdapter.convertHintMove(hintMove));
  }

}
