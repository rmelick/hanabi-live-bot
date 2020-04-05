package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import com.fossgalaxy.games.fireworks.state.events.*;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Clue;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Init;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Notify;

import java.util.*;
import java.util.stream.Collectors;

public class LiveGameRunner {
    private static final int[] HAND_SIZE = {-1, -1, 5, 5, 4, 4};

    private final Player _myPlayer;
    private final String _hanabiLivePlayerName;
    private int _hanabiLivePlayerId;
    private GameState _gameState;
    private int _numPlayers;
    private Map<Integer, HandMapping> _playerHands;

    public LiveGameRunner(String hanabiLivePlayerName) {
        _hanabiLivePlayerName = hanabiLivePlayerName;
        _myPlayer = new AgentPlayer(_hanabiLivePlayerName, SampleAgents.buildCompRandom());
    }

    public void init(Init initMessage) {
        List<String> playerNames = initMessage.getNames();
        _numPlayers = playerNames.size();
        _hanabiLivePlayerId = playerNames.indexOf(_hanabiLivePlayerName);

        _playerHands = new HashMap<>(_numPlayers);
        for (int playerID = 0; playerID < _numPlayers; playerID++) {
            _playerHands.put(playerID, new HandMapping(HAND_SIZE[_numPlayers]));
        }

        _gameState = new StateBackedByHanabiLive(_numPlayers);
        _gameState.init();

        _myPlayer.setID(_hanabiLivePlayerId, _numPlayers, playerNames.toArray(new String[0]));
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
        if (clue.getType() == 0) {
            CardInfoValue info = new CardInfoValue(
                    event.getWho().intValue(),
                    event.getTarget().intValue(),
                    (int) event.getClue().getValue(),
                    _playerHands.get(event.getWho().intValue()).findSlotsOfHanabi(event.getList()),
                    event.getTurn().intValue()
            );
            return Collections.singletonList(info);
        } else if (clue.getType() == 1) {
            CardInfoColour info = new CardInfoColour(
                    event.getWho().intValue(),
                    event.getTarget().intValue(),
                    CardColors.getFromLiveId(clue.getValue()),
                    _playerHands.get(event.getWho().intValue()).findSlotsOfHanabi(event.getList()),
                    event.getTurn().intValue()
                    );
            return Collections.singletonList(info);
        } else {
            throw new IllegalArgumentException("Invalid clue type " + clue.getType());
        }
    }

    private Action convertClue(Notify event) {
        Clue clue = event.getClue();
        if (clue.getType() == 0) {
            return new TellValue(event.getTarget().intValue(), (int) clue.getValue());
        } else {
            return new TellColour(event.getTarget().intValue(), CardColors.getFromLiveId(clue.getValue()));
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

    /**
     * IEEE Agents draw to replace, and Hanabi Live doesn't really keep track of slots, it uses the overal
     * deck index as the card id
     */
    private static class HandMapping {
        /**
         * Return the slot the card was put into
         * @param hanabiLiveOrder
         * @return
         */
        private final List<Integer> _ieeeToHanabi;
        private Queue<Integer> _nextDrawSpot;

        public HandMapping(int numCardsForHand) {
            _ieeeToHanabi = new ArrayList<>(numCardsForHand);
            _nextDrawSpot = new LinkedList<>();
            for (int i = 0; i < numCardsForHand; i++) {
                _nextDrawSpot.add(i);
                _ieeeToHanabi.add(-1);
            }
        }

        public int recordHanabiDraw(int hanabiLiveOrder) {
            int slotTaken = _nextDrawSpot.remove();
            _ieeeToHanabi.set(slotTaken, hanabiLiveOrder);
            return slotTaken;
        }


        /**
         *
         * @param hanabiLiveOrder
         * @return the slot that hanabi card is in
         */
        public int findSlotOfHanabi(Long hanabiLiveOrder) {
            return _ieeeToHanabi.indexOf(hanabiLiveOrder.intValue());
        }

        public List<Integer> findSlotsOfHanabi(List<Long> hanabiLiveOrders) {
            return hanabiLiveOrders.stream().map(this::findSlotOfHanabi).collect(Collectors.toList());
        }

        /**
         *
         * @param hanabiLiveOrder
         * @return the slot that was freed up by the play
         */
        public int recordHanabiPlay(int hanabiLiveOrder) {
            int index = _ieeeToHanabi.indexOf(hanabiLiveOrder);
            _ieeeToHanabi.remove(index);
            return index;
        }
    }
}
