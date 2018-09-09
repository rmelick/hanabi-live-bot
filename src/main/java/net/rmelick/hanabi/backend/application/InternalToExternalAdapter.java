package net.rmelick.hanabi.backend.application;

import net.rmelick.hanabi.backend.Color;
import net.rmelick.hanabi.backend.Tile;
import net.rmelick.hanabi.backend.api.*;
import net.rmelick.hanabi.backend.state.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InternalToExternalAdapter {
    /**
     * Convert the full internal game state to a subset of the state viewable by the user
     * @param internalGameState full state
     * @param playerId user viewing the state
     * @return
     */
    public static ViewableGameState convertInternalGameState(FullGameState internalGameState, String playerId) {
        ViewableGameState externalGameState = new ViewableGameState();
        externalGameState.gameId = internalGameState.getGameId();
        externalGameState.players = convertInternalPlayers(internalGameState, playerId);
        externalGameState.drawPile = convertInternalDrawPile(internalGameState.getDrawPileState());
        externalGameState.discardPile = convertInternalDiscardPile(internalGameState.getDiscardPileState());
        externalGameState.board = convertInternalBoard(internalGameState.getBoardState());
        externalGameState.cluesRemaining = (int) internalGameState.getCluesRemaining().get();
        externalGameState.mistakesRemaining = (int) internalGameState.getMistakesRemaining().get();
        externalGameState.availableMoves = computeAvailableMoves(playerId, internalGameState);
        return externalGameState;
    }

  private static Board convertInternalBoard(BoardState internalBoardState) {
        Board externalBoard = new Board();
        externalBoard.playedTiles = new HashMap<>();
        for (Map.Entry<Color, List<Tile>> entry : internalBoardState.getPlayedTiles().entrySet()) {
            externalBoard.playedTiles.put(entry.getKey().getPrettyName(), convertInternalTilesFullInfo(entry.getValue()));
        }
        return externalBoard;
    }

    private static DiscardPile convertInternalDiscardPile(DiscardPileState internalDiscardPileState) {
        DiscardPile externalDiscardPile = new DiscardPile();
        externalDiscardPile.tiles = convertInternalTilesFullInfo(internalDiscardPileState.getTiles());
        return externalDiscardPile;
    }

    private static DrawPile convertInternalDrawPile(DrawPileState internalDrawPileState) {
        DrawPile externalDrawPile = new DrawPile();
        externalDrawPile.tilesRemaining = internalDrawPileState.tilesRemaining();
        return externalDrawPile;
    }

    private static Players convertInternalPlayers(FullGameState internalGameState, String thisPlayerId) {
        String currentPlayerId = internalGameState.getCurrentPlayerState().getId();
        List<PlayerState> internalPlayers = new ArrayList<>(internalGameState.getPlayerStates());
        int thisPlayerIndex = internalGameState.getPlayer(thisPlayerId).getPlayerIndex();
        // rotate 'this player' to front of array
        Collections.rotate(internalPlayers, -thisPlayerIndex);
        PlayerState thisPlayer = internalPlayers.remove(0);

        Players players = new Players();
        players.thisPlayer = convertInternalThisPlayer(thisPlayer, currentPlayerId);
        players.otherPlayers = convertInternalOtherPlayers(internalPlayers, currentPlayerId);
        return players;
    }

    private static Player convertInternalThisPlayer(PlayerState playerState, String currentPlayerId) {
        Player thisPlayer = new Player();
        thisPlayer.name = playerState.getName();
        thisPlayer.id = playerState.getId();
        thisPlayer.playerIndex = playerState.getPlayerIndex();
        thisPlayer.isCurrentPlayer = playerState.getId().equals(currentPlayerId);
        thisPlayer.tiles = convertInternalTilesCurrentPlayer(playerState.getTiles());
        return thisPlayer;
    }

    private static List<Player> convertInternalOtherPlayers(List<PlayerState> otherPlayers, String currentPlayerId) {
        List<Player> externalPlayers = new ArrayList<>(otherPlayers.size());
        for (PlayerState internalPlayer : otherPlayers) {
            Player externalPlayer = new Player();
            externalPlayer.name = internalPlayer.getName();
            externalPlayer.id = internalPlayer.getId();
            externalPlayer.playerIndex = internalPlayer.getPlayerIndex();
            externalPlayer.isCurrentPlayer = internalPlayer.getId().equals(currentPlayerId);
            externalPlayer.tiles = convertInternalTilesFullInfo(internalPlayer.getTiles());
            externalPlayers.add(externalPlayer);
        }
        return externalPlayers;
    }

    private static List<net.rmelick.hanabi.backend.api.Tile> convertInternalTilesFullInfo(List<net.rmelick.hanabi.backend.Tile> internalTiles) {
        List<net.rmelick.hanabi.backend.api.Tile> externalTiles = new ArrayList<>(internalTiles.size());
        for (net.rmelick.hanabi.backend.Tile internalTile : internalTiles) {
            net.rmelick.hanabi.backend.api.Tile externalTile = new net.rmelick.hanabi.backend.api.Tile();
            externalTile.color = internalTile.getColor().getPrettyName();
            externalTile.id = internalTile.getId();
            externalTile.publicId = internalTile.getPublicId();
            externalTile.rank = internalTile.getRank().getValue();
            externalTiles.add(externalTile);
        }
        return externalTiles;
    }

    private static List<net.rmelick.hanabi.backend.api.Tile> convertInternalTilesCurrentPlayer(List<net.rmelick.hanabi.backend.Tile> internalTiles) {
        List<net.rmelick.hanabi.backend.api.Tile> externalTiles = new ArrayList<>(internalTiles.size());
        for (net.rmelick.hanabi.backend.Tile internalTile : internalTiles) {
            net.rmelick.hanabi.backend.api.Tile externalTile = new net.rmelick.hanabi.backend.api.Tile();
            externalTile.publicId = internalTile.getPublicId();
            externalTiles.add(externalTile);
        }
        return externalTiles;
    }

    private static AvailableMoves computeAvailableMoves(String playerId, FullGameState internalGameState) {
      if (!playerId.equals(internalGameState.getCurrentPlayerState().getId())) {
        return emptyAvailableMoves();
      }

      // discards and plays are always available for all of your tiles
      AvailableMoves availableMoves = new AvailableMoves();
      List<Tile> playerTiles = internalGameState.getPlayer(playerId).getTiles();
      availableMoves.discards = new ArrayList<>(playerTiles.size());
      availableMoves.plays = new ArrayList<>(playerTiles.size());
      for (int tileIndex = 0; tileIndex < playerTiles.size(); tileIndex++) {
        DiscardMove dm = new DiscardMove();
        dm.position = tileIndex;
        availableMoves.discards.add(dm);
        PlayMove pm = new PlayMove();
        pm.position = tileIndex;
        availableMoves.plays.add(pm);
      }

      // hints available depend on the other players hand (you can't hint something that isn't there)
      availableMoves.hints = new HashMap<>();
      for (PlayerState playerState : internalGameState.getPlayerStates()) {
        if (!playerState.getId().equals(playerId)) {
          availableMoves.hints.put(playerState.getId(), computeAvailableHints(playerState));
        }
      }

      return availableMoves;
    }

    private static AvailableMoves emptyAvailableMoves() {
      AvailableMoves availableMoves = new AvailableMoves();
      availableMoves.discards = Collections.emptyList();
      availableMoves.plays = Collections.emptyList();
      availableMoves.hints = Collections.emptyMap();
      return availableMoves;
    }

    private static HintsForPlayer computeAvailableHints(PlayerState playerState) {
      Set<String> availableColors = playerState.getTiles().stream()
          .map(tile -> tile.getColor().getPrettyName())
          .collect(Collectors.toSet());
      Set<Integer> availableRanks = playerState.getTiles().stream()
          .map(tile -> tile.getRank().getValue())
          .collect(Collectors.toSet());
      HintsForPlayer hints = new HintsForPlayer();
      hints.colorHints = new ArrayList<>(availableColors);
      hints.rankHints = new ArrayList<>(availableRanks);
      return hints;
    }

}
