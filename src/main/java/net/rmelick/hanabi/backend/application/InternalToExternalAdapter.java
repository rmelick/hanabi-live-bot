package net.rmelick.hanabi.backend.application;

import net.rmelick.hanabi.backend.Color;
import net.rmelick.hanabi.backend.Tile;
import net.rmelick.hanabi.backend.api.*;
import net.rmelick.hanabi.backend.state.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        externalGameState.players = convertInternalPlayers(internalGameState.getPlayerStates(), playerId);
        externalGameState.drawPile = convertInternalDrawPile(internalGameState.getDrawPileState());
        externalGameState.discardPile = convertInternalDiscardPile(internalGameState.getDiscardPileState());
        externalGameState.board = convertInternalBoard(internalGameState.getBoardState());
        externalGameState.cluesRemaining = (int) internalGameState.getCluesRemaining().get();
        externalGameState.mistakesRemaining = (int) internalGameState.getMistakesRemaining().get();
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

    private static List<Player> convertInternalPlayers(List<PlayerState> internalPlayers, String playerId) {
        List<Player> externalPlayers = new ArrayList<>(internalPlayers.size());
        for (PlayerState internalPlayer : internalPlayers) {
            Player externalPlayer = new Player();
            externalPlayer.name = internalPlayer.getName();
            externalPlayer.playerIndex = internalPlayer.getPlayerIndex();

            if (internalPlayer.getId().equals(playerId)) {
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
}
