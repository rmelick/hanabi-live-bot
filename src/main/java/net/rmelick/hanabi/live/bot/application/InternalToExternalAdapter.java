package net.rmelick.hanabi.live.bot.application;

import net.rmelick.hanabi.live.bot.Color;
import net.rmelick.hanabi.live.bot.CompletedGame;
import net.rmelick.hanabi.live.bot.GameWaitingToBegin;
import net.rmelick.hanabi.live.bot.Hint;
import net.rmelick.hanabi.live.bot.Tile;
import net.rmelick.hanabi.live.bot.api.game.*;
import net.rmelick.hanabi.live.bot.api.game.HintInformation;
import net.rmelick.hanabi.live.bot.api.game.PlayMove;
import net.rmelick.hanabi.live.bot.api.game.Players;
import net.rmelick.hanabi.live.bot.api.game.ViewableGameState;
import net.rmelick.hanabi.live.bot.api.lobby.GameStateSummary;
import net.rmelick.hanabi.live.bot.api.lobby.GameStatus;
import net.rmelick.hanabi.live.bot.api.lobby.PlayerInfo;
import net.rmelick.hanabi.backend.state.*;
import net.rmelick.hanabi.live.bot.state.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class InternalToExternalAdapter {
    /**
     * Convert the full internal game state to a subset of the state viewable by the user
     * @param internalGameState full state
     * @param playerId user viewing the state
     * @return
     */
    static ViewableGameState convertInternalGameState(FullGameState internalGameState, String playerId) {
        ViewableGameState externalGameState = new ViewableGameState();
        externalGameState.gameId = internalGameState.getGameId();
        externalGameState.players = convertInternalPlayers(internalGameState, playerId);
        externalGameState.drawPile = convertInternalDrawPile(internalGameState.getDrawPileState());
        externalGameState.discardPile = convertInternalDiscardPile(internalGameState.getDiscardPileState());
        externalGameState.board = convertInternalBoard(internalGameState.getBoardState());
        externalGameState.cluesRemaining = (int) internalGameState.getCluesRemaining().get();
        externalGameState.mistakesRemaining = (int) internalGameState.getMistakesRemaining().get();
        externalGameState.availableMoves = computeAvailableMoves(playerId, internalGameState);
        externalGameState.isCompleted = internalGameState.isGameIsCompleted();
        return externalGameState;
    }

  private static Board convertInternalBoard(BoardState internalBoardState) {
        Board externalBoard = new Board();
        externalBoard.playedTiles = new HashMap<>();
        for (Map.Entry<Color, List<Tile>> entry : internalBoardState.getPlayedTiles().entrySet()) {
            externalBoard.playedTiles.put(entry.getKey().getPrettyName(), convertInternalTilesFullInfo(entry.getValue()));
        }
        externalBoard.scoredPoints = internalBoardState.getCurrentPoints();
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
        thisPlayer.tiles = convertInternalTilesInHandCurrentPlayer(playerState.getTilesInHand());
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
            externalPlayer.tiles = convertInternalTilesInHandFullInfo(internalPlayer.getTilesInHand());
            externalPlayers.add(externalPlayer);
        }
        return externalPlayers;
    }

    private static List<Tile> convertInternalTilesInHandFullInfo(List<TileInHand> internalTiles) {
        List<Tile> externalTiles = new ArrayList<>(internalTiles.size());
        for (TileInHand tileInHand : internalTiles) {
            Tile internalTile = tileInHand.getTile();
            Tile externalTile = new Tile();
            externalTile.color = internalTile.getColor().getPrettyName();
            externalTile.id = internalTile.getId();
            externalTile.publicId = internalTile.getPublicId();
            externalTile.rank = internalTile.getRank().getValue();
            externalTile.hintInformation = convertInternalHints(tileInHand);
            externalTiles.add(externalTile);
        }
        return externalTiles;
    }

    private static List<Tile> convertInternalTilesFullInfo(List<Tile> internalTiles) {
      List<Tile> externalTiles = new ArrayList<>(internalTiles.size());
      for (Tile internalTile : internalTiles) {
        Tile externalTile = new Tile();
        externalTile.color = internalTile.getColor().getPrettyName();
        externalTile.id = internalTile.getId();
        externalTile.publicId = internalTile.getPublicId();
        externalTile.rank = internalTile.getRank().getValue();
        externalTiles.add(externalTile);
      }
      return externalTiles;
    }

    private static List<Tile> convertInternalTilesInHandCurrentPlayer(List<TileInHand> internalTiles) {
        List<Tile> externalTiles = new ArrayList<>(internalTiles.size());
        for (TileInHand tileInHand : internalTiles) {
            Tile externalTile = new Tile();
            externalTile.publicId = tileInHand.getTile().getPublicId();
            externalTile.hintInformation = convertInternalHints(tileInHand);
            externalTiles.add(externalTile);
        }
        return externalTiles;
    }

    private static HintInformation convertInternalHints(TileInHand tileInHand) {
      HintInformation hintInformation = new HintInformation();
      hintInformation.positiveHintsGiven = convertInternalHints(tileInHand.getMatchingHints());
      hintInformation.negativeHintsGiven = convertInternalHints(tileInHand.getNonMatchingHints());
      return hintInformation;
    }

    private static List<HintMove> convertInternalHints(List<Hint> internalHints) {
      List<HintMove> externalHints = new ArrayList<>(internalHints.size());
      for (Hint internalHint: internalHints) {
        HintMove externalHint = new HintMove();
        switch (internalHint.getHintType()) {
        case COLOR:
          externalHint.color = internalHint.getColorHint().getPrettyName();
          break;
        case RANK:
          externalHint.rank = internalHint.getRankHint().getValue();
          break;
        }
        externalHints.add(externalHint);
      }
      return externalHints;
    }

    private static AvailableMoves computeAvailableMoves(String playerId, FullGameState internalGameState) {
      // discards and plays are always available for all of your tiles
      AvailableMoves availableMoves = new AvailableMoves();
      List<TileInHand> playerTiles = internalGameState.getPlayer(playerId).getTilesInHand();
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
      Set<String> availableColors = playerState.getTilesInHand().stream()
          .map(tile -> tile.getTile().getColor().getPrettyName())
          .collect(Collectors.toSet());
      Set<Integer> availableRanks = playerState.getTilesInHand().stream()
          .map(tile -> tile.getTile().getRank().getValue())
          .collect(Collectors.toSet());
      HintsForPlayer hints = new HintsForPlayer();
      hints.colorHints = new ArrayList<>(availableColors);
      hints.rankHints = new ArrayList<>(availableRanks);
      return hints;
    }

  static GameStateSummary getGameStateSummary(GameWaitingToBegin gameWaitingToBegin) {
    GameStateSummary summary = new GameStateSummary();
    summary.gameId = gameWaitingToBegin.getGameId();
    summary.status = GameStatus.WAITING_TO_BEGIN;
    summary.numPlayers = gameWaitingToBegin.getPlayers().size();
    summary.players = convertPlayerInfos(gameWaitingToBegin.getPlayers());
    return summary;
  }

  static GameStateSummary getGameStateSummary(FullGameState game) {
    GameStateSummary summary = new GameStateSummary();
    summary.gameId = game.getGameId();
    summary.status = GameStatus.IN_PROGRESS;
    summary.numPlayers = game.getPlayerStates().size();
    summary.players = convertPlayerStates(game.getPlayerStates());
    return summary;
  }

  static GameStateSummary getGameStateSummary(CompletedGame game) {
    GameStateSummary summary = new GameStateSummary();
    summary.gameId = game.getGameState().getGameId();
    summary.status = GameStatus.COMPLETED;
    summary.numPlayers = game.getGameState().getPlayerStates().size();
    summary.players = convertPlayerStates(game.getGameState().getPlayerStates());
    return summary;
  }

  private static List<PlayerInfo> convertPlayerInfos(List<net.rmelick.hanabi.live.bot.PlayerInfo> internalPlayerInfos) {
      return internalPlayerInfos.stream().map(internalPlayer -> {
        PlayerInfo externalPlayer = new PlayerInfo();
        externalPlayer.id = internalPlayer.getId();
        externalPlayer.name = internalPlayer.getName();
        externalPlayer.type = internalPlayer.getType().toString();
        return externalPlayer;
      }).collect(Collectors.toList());
  }

  private static List<PlayerInfo> convertPlayerStates(List<PlayerState> internalPlayerStates) {
    return internalPlayerStates.stream().map(internalPlayer -> {
      PlayerInfo externalPlayer = new PlayerInfo();
      externalPlayer.id = internalPlayer.getId();
      externalPlayer.name = internalPlayer.getName();
      externalPlayer.type = internalPlayer.getType().toString();
      return externalPlayer;
    }).collect(Collectors.toList());
  }
}
