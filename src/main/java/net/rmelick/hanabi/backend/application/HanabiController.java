package net.rmelick.hanabi.backend.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.rmelick.hanabi.backend.Color;
import net.rmelick.hanabi.backend.Rank;
import net.rmelick.hanabi.backend.api.Board;
import net.rmelick.hanabi.backend.api.DiscardPile;
import net.rmelick.hanabi.backend.api.DrawPile;
import net.rmelick.hanabi.backend.api.Player;
import net.rmelick.hanabi.backend.api.Tile;
import net.rmelick.hanabi.backend.api.ViewableGameState;
import net.rmelick.hanabi.backend.state.BoardState;
import net.rmelick.hanabi.backend.state.DiscardPileState;
import net.rmelick.hanabi.backend.state.DrawPileState;
import net.rmelick.hanabi.backend.state.GameState;
import net.rmelick.hanabi.backend.state.PlayerState;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@RestController
@CrossOrigin
public class HanabiController {

  @RequestMapping("/gameState")
  public String getGameState() throws JsonProcessingException {
    ViewableGameState gameState = convertInternalGameState(newRandomGameState());
    return new ObjectMapper().writeValueAsString(gameState);

  }

  private ViewableGameState convertInternalGameState(GameState internalGameState) {
    ViewableGameState externalGameState = new ViewableGameState();
    externalGameState.players = convertInternalPlayers(internalGameState.getPlayerStates());
    externalGameState.drawPile = convertInternalDrawPile(internalGameState.getDrawPileState());
    externalGameState.discardPile = convertInternalDiscardPile(internalGameState.getDiscardPileState());
    externalGameState.board = convertInternalBoard(internalGameState.getBoardState());
    externalGameState.cluesRemaining = (int) internalGameState.getCluesRemaining().get();
    externalGameState.mistakesRemaining = (int) internalGameState.getMistakesRemaining().get();
    return externalGameState;
  }

  private Board convertInternalBoard(BoardState internalBoardState) {
    Board externalBoard = new Board();
    externalBoard.playedTiles = new HashMap<>();
    for (Map.Entry<Color, List<net.rmelick.hanabi.backend.Tile>> entry : internalBoardState.getPlayedTiles().entrySet()) {
      externalBoard.playedTiles.put(entry.getKey().getPrettyName(), convertInternalTilesFullInfo(entry.getValue()));
    }
    return externalBoard;
  }

  private DiscardPile convertInternalDiscardPile(DiscardPileState internalDiscardPileState) {
    DiscardPile externalDiscardPile = new DiscardPile();
    externalDiscardPile.tiles = convertInternalTilesFullInfo(internalDiscardPileState.getTiles());
    return externalDiscardPile;
  }

  private DrawPile convertInternalDrawPile(DrawPileState internalDrawPileState) {
    DrawPile externalDrawPile = new DrawPile();
    externalDrawPile.tilesRemaining = internalDrawPileState.tilesRemaining();
    return externalDrawPile;
  }

  private List<Player> convertInternalPlayers(List<PlayerState> internalPlayers) {
    List<Player> externalPlayers = new ArrayList<>(internalPlayers.size());
    int currentPlayerIndex = 0;
    for (PlayerState internalPlayer : internalPlayers) {
      Player externalPlayer = new Player();
      externalPlayer.name = internalPlayer.getName();
      externalPlayer.playerIndex = internalPlayer.getPlayerIndex();

      if (internalPlayer.getPlayerIndex() == currentPlayerIndex) {
        externalPlayer.isCurrentPlayer = true;
        externalPlayer.tiles = convertInternalTilesCurrentPlayer(internalPlayer.getTiles());
      } else {
        externalPlayer.isCurrentPlayer = false;
        externalPlayer.tiles = convertInternalTilesFullInfo(internalPlayer.getTiles());
      }
      externalPlayers.add(externalPlayer);
    }
    return externalPlayers;
  }

  private List<Tile> convertInternalTilesFullInfo(List<net.rmelick.hanabi.backend.Tile> internalTiles) {
    List<Tile> externalTiles = new ArrayList<>(internalTiles.size());
    for (net.rmelick.hanabi.backend.Tile internalTile : internalTiles) {
      Tile externalTile = new Tile();
      externalTile.color = internalTile.getColor().getPrettyName();
      externalTile.id = internalTile.getId();
      externalTile.publicId = internalTile.getPublicId();
      externalTile.rank = internalTile.getRank().getValue();
      externalTiles.add(externalTile);
    }
    return externalTiles;
  }

  private List<Tile> convertInternalTilesCurrentPlayer(List<net.rmelick.hanabi.backend.Tile> internalTiles) {
    List<Tile> externalTiles = new ArrayList<>(internalTiles.size());
    for (net.rmelick.hanabi.backend.Tile internalTile : internalTiles) {
      Tile externalTile = new Tile();
      externalTile.publicId = internalTile.getPublicId();
      externalTiles.add(externalTile);
    }
    return externalTiles;
  }

  private GameState newRandomGameState() {
    GameState gameState = new GameState(4);
    boolean played = false;
    for (PlayerState player : gameState.getPlayerStates()) {
      int tilePosition = 0;
      for (net.rmelick.hanabi.backend.Tile tile : player.getTiles()) {
        if (Rank.ONE.equals(tile.getRank())) {
          gameState.play(player.getPlayerIndex(), tilePosition);
          played = true;
          break;
        }
        tilePosition++;
      }
      if (played) {
        break;
      }
    }
    gameState.discard(0, 0);
    gameState.discard(1, 0);
    gameState.discard(2, 0);
    gameState.discard(3, 0);
    gameState.discard(0, 0);
    gameState.discard(1, 0);
    return gameState;
  }
}
