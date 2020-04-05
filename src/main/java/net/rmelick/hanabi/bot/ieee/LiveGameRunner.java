package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.DiscardCard;
import com.fossgalaxy.games.fireworks.state.actions.PlayCard;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.CardInfoColour;
import com.fossgalaxy.games.fireworks.state.events.CardInfoValue;
import com.fossgalaxy.games.fireworks.state.events.CardReceived;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;
import com.fossgalaxy.games.fireworks.state.events.GameInformation;
import net.rmelick.hanabi.bot.live.connector.schemas.java.ActionType;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Clue;
import net.rmelick.hanabi.bot.live.connector.schemas.java.ClueType;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Init;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Notify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveGameRunner {
    private static final int[] HAND_SIZE = {-1, -1, 5, 5, 4, 4};

    private final Player _myPlayer;
    private final String _myHanabiLivePlayerName;
    private int _myHanabiLivePlayerID;
    private GameState _gameState;
    private int _numPlayers;
    private Map<Integer, HandMapping> _playerHands;

    public LiveGameRunner(String hanabiLivePlayerName) {
        _myHanabiLivePlayerName = hanabiLivePlayerName;
        _myPlayer = new AgentPlayer(_myHanabiLivePlayerName, SampleAgents.buildCompRandom());
    }

    public void init(Init initMessage) {
        List<String> playerNames = initMessage.getNames();
        _numPlayers = playerNames.size();
        _myHanabiLivePlayerID = playerNames.indexOf(_myHanabiLivePlayerName);

        _playerHands = new HashMap<>(_numPlayers);
        for (int playerID = 0; playerID < _numPlayers; playerID++) {
            _playerHands.put(playerID, new HandMapping(HAND_SIZE[_numPlayers]));
        }

        _gameState = new StateBackedByHanabiLive(_numPlayers);
        _gameState.init();

        _myPlayer.setID(_myHanabiLivePlayerID, _numPlayers, playerNames.toArray(new String[0]));
    }

    public void initialEvents(List<Notify> initials) {
        GameEvent gameInfo = new GameInformation(
                _numPlayers,
                HAND_SIZE[_numPlayers],
                _gameState.getInfomation(),
                _gameState.getLives());
        List<GameEvent> gameEvents = new ArrayList<>();
        gameEvents.add(gameInfo);
        for (Notify notify: initials) {
            gameEvents.addAll(convertInitialEvent(notify));
        }
        // constants taken from com.fossgalaxy.games.fireworks.GameRunner.init
        _myPlayer.resolveTurn(-2, null, gameEvents);
    }

    public List<GameEvent> convertInitialEvent(Notify event) {
        switch (event.getType()) {
            case "draw":
                return convertInitialDraw(event);
            case "clue":
                return convertInitialClue(event);
            case "status":
            case "text":
            case "turn": // not sure about this, we may want it
                return Collections.emptyList(); // don't care
            default:
                throw new IllegalArgumentException("Unknown initial event " + event.getType());
        }
    }

    private List<GameEvent> convertInitialDraw(Notify event) {
        // TODO: figure out how to integrate this into the deck instead of directly (so it takes care of removing the cards from the draw pile, etc.)
        // that might let us avoid making these?  not sure
        // also need to figure out how we pass other draw events into the DeckBackedByHanabi
        int playerIndex = event.getWho().intValue();
        int handIndex = _playerHands.get(playerIndex).recordHanabiDraw(event.getOrder().intValue());
        CardDrawn cardDrawn = new CardDrawn(
                playerIndex,
                handIndex,
                CardColors.getFromLiveId(event.getSuit()),
                event.getRank().intValue(),
                0);
        CardReceived cardReceived = new CardReceived(
                playerIndex,
                handIndex,
                true, //TODO handle the final draw correctly saying there are no more cards left
                0);
        return List.of(cardDrawn, cardReceived);
    }

    private List<GameEvent> convertInitialClue(Notify event) {
        Clue clue = event.getClue();
        ClueType type = ClueType.valueOfHanabiID((int) clue.getType())
        switch (type) {
            case RANK:
                CardInfoValue rankInfo = new CardInfoValue(
                        event.getWho().intValue(),
                        event.getTarget().intValue(),
                        (int) event.getClue().getValue(),
                        _playerHands.get(event.getWho().intValue()).findSlotsOfHanabi(event.getList()),
                        event.getTurn().intValue()
                );
                return Collections.singletonList(rankInfo);
            case COLOR:
                CardInfoColour colorInfo = new CardInfoColour(
                        event.getWho().intValue(),
                        event.getTarget().intValue(),
                        CardColors.getFromLiveId(clue.getValue()),
                        _playerHands.get(event.getWho().intValue()).findSlotsOfHanabi(event.getList()),
                        event.getTurn().intValue()
                );
                return Collections.singletonList(colorInfo);
            default:
                throw new IllegalArgumentException("Unknown clue type " + type);
        }

    }

    private Action convertClue(Notify event) {
        Clue clue = event.getClue();
        ClueType type = ClueType.valueOfHanabiID((int) clue.getType())
        switch (type) {
            case RANK:
                return new TellValue(event.getTarget().intValue(), (int) clue.getValue());
            case COLOR:
                return new TellColour(event.getTarget().intValue(), CardColors.getFromLiveId(clue.getValue()));
            default:
                throw new IllegalArgumentException("Unknown clue type " + type);
        }
    }

    public boolean recordClue(Notify event) {
        Action clueAction = convertClue(event);
        Long giverID = event.getGiver();
        clueAction.apply(giverID.intValue(), _gameState);
        return true;
    }

    public boolean recordPlay(Notify event) {
        return false;
    }

    public net.rmelick.hanabi.bot.live.connector.schemas.java.Action getNextPlayerMove() {
        Action nextAction = _myPlayer.getAction();
        return convertIEEEActionToHanabiLive(nextAction);
    }

    private net.rmelick.hanabi.bot.live.connector.schemas.java.Action convertIEEEActionToHanabiLive(Action action) {
        net.rmelick.hanabi.bot.live.connector.schemas.java.Action liveAction = new net.rmelick.hanabi.bot.live.connector.schemas.java.Action();
        if (action instanceof DiscardCard) {
            DiscardCard discard = (DiscardCard) action;
            int hanabiIDToDiscard = _playerHands.get(_myHanabiLivePlayerID).getHanabiInSlot(discard.slot);
            liveAction.setTarget(hanabiIDToDiscard);
            liveAction.setType(ActionType.DISCARD.getHanabiLiveID());
        } else if (action instanceof PlayCard) {
            PlayCard play = (PlayCard) action;
            int hanabiIDToPlay = _playerHands.get(_myHanabiLivePlayerID).getHanabiInSlot(play.slot);
            liveAction.setTarget(hanabiIDToPlay);
            liveAction.setType(ActionType.PLAY.getHanabiLiveID());
        } else if (action instanceof TellValue) {

        } else if (action instanceof TellColour) {

        } else {
            throw new IllegalArgumentException("Unknown action " + action);
        }
    }

}
