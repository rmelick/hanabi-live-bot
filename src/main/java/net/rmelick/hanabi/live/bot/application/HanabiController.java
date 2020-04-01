package net.rmelick.hanabi.live.bot.application;

import net.rmelick.hanabi.live.bot.GameManager;
import net.rmelick.hanabi.live.bot.GameWaitingToBegin;
import net.rmelick.hanabi.live.bot.PlayerInfo;
import net.rmelick.hanabi.live.bot.PlayerType;
import net.rmelick.hanabi.live.bot.api.game.DiscardMove;
import net.rmelick.hanabi.live.bot.api.game.HintMove;
import net.rmelick.hanabi.live.bot.api.game.PlayMove;
import net.rmelick.hanabi.live.bot.api.game.ViewableGameState;
import net.rmelick.hanabi.live.bot.api.lobby.GameStateSummary;
import net.rmelick.hanabi.live.bot.state.FullGameState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Takes requests over the public api and maps them to internal calls to the GameManager
 *  - extracts the user information from the session
 */
@RestController
@CrossOrigin
public class HanabiController {
  private final GameManager _gameManager = new GameManager();
  private final ClientNotifier _clientNotifier;

  @Autowired
  public HanabiController(ClientNotifier clientNotifier) {
    _clientNotifier = clientNotifier;
  }

  @RequestMapping(value = "/newGame", method = RequestMethod.POST)
  public void newGame() {
    _gameManager.createGame();
  }

  @RequestMapping(value = "/games", method = RequestMethod.GET)
  public List<GameStateSummary> getAllGames() {
    List<GameStateSummary> allGames = new ArrayList<>();
    allGames.addAll(_gameManager.getGamesWaitingToBegin().values().
        stream().map(InternalToExternalAdapter::getGameStateSummary)
        .collect(Collectors.toList()));

    allGames.addAll(_gameManager.getInProgressGames().values().
        stream().map(InternalToExternalAdapter::getGameStateSummary)
        .collect(Collectors.toList()));

    allGames.addAll(_gameManager.getCompletedGames().values().
        stream().map(InternalToExternalAdapter::getGameStateSummary)
        .collect(Collectors.toList()));
    return allGames;
  }

  @RequestMapping(value = "/games/{gameId}/summary", method = RequestMethod.GET)
  public GameStateSummary getGameStateSummary(@PathVariable String gameId) {
    if (_gameManager.getGameWaitingToBegin(gameId) != null) {
      return InternalToExternalAdapter.getGameStateSummary(_gameManager.getGameWaitingToBegin(gameId));
    } else if (_gameManager.getInProgressGame(gameId) != null) {
      return InternalToExternalAdapter.getGameStateSummary(_gameManager.getInProgressGame(gameId));
    } else if (_gameManager.getCompletedGame(gameId) != null) {
      return InternalToExternalAdapter.getGameStateSummary(_gameManager.getCompletedGame(gameId));
    } else {
      return null;
    }
  }

  @RequestMapping(value = "/games/{gameId}/join", method = RequestMethod.POST)
  public void joinGame(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId, @RequestParam String playerName) {
    GameWaitingToBegin gameWaitingToBegin = _gameManager.getGameWaitingToBegin(gameId);
    PlayerInfo playerInfo = new PlayerInfo(playerName, playerId, PlayerType.HUMAN);
    gameWaitingToBegin.addPlayer(playerInfo);
    _clientNotifier.notifyGameStateSummaryChange(gameId);
  }

  @RequestMapping(value = "/games/{gameId}/state", method = RequestMethod.GET)
  public ViewableGameState getGameState(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId) {
    FullGameState fullGameState = _gameManager.getInProgressGame(gameId);
    return InternalToExternalAdapter.convertInternalGameState(fullGameState, playerId);
  }

  @RequestMapping(value = "/games/{gameId}/start", method = RequestMethod.POST)
  public void startGame(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId) {
    _gameManager.startGame(gameId);
    _clientNotifier.notifyGameStateSummaryChange(gameId);
  }

  @RequestMapping(value = "/games/{gameId}/move/play", method = RequestMethod.POST)
  public void makeMove(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId, @RequestBody PlayMove playMove) {
    _gameManager.getInProgressGame(gameId).play(playerId, playMove.position);
    _clientNotifier.notifyGameStateChange(gameId);
  }

  @RequestMapping(value = "/games/{gameId}/move/discard", method = RequestMethod.POST)
  public void makeMove(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId, @RequestBody DiscardMove discardMove) {
    _gameManager.getInProgressGame(gameId).discard(playerId, discardMove.position);
    _clientNotifier.notifyGameStateChange(gameId);
  }

  @RequestMapping(value = "/games/{gameId}/move/hint", method = RequestMethod.POST)
  public void makeMove(@PathVariable String gameId, @RequestHeader("X-Player-Id") String playerId, @RequestBody HintMove hintMove) {
    _gameManager.getInProgressGame(gameId).hint(playerId, hintMove.playerId, ExternalToInternalAdapter.convertHintMove(hintMove));
    _clientNotifier.notifyGameStateChange(gameId);
  }

}
