package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.ai.AgentPlayer;
import com.fossgalaxy.games.fireworks.players.Player;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.state.GameState;
import com.fossgalaxy.games.fireworks.state.actions.Action;
import com.fossgalaxy.games.fireworks.state.actions.TellColour;
import com.fossgalaxy.games.fireworks.state.actions.TellValue;
import com.fossgalaxy.games.fireworks.state.events.CardDrawn;
import com.fossgalaxy.games.fireworks.state.events.CardReceived;
import com.fossgalaxy.games.fireworks.state.events.GameEvent;
import com.fossgalaxy.games.fireworks.state.events.GameInformation;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Clue;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Init;
import net.rmelick.hanabi.bot.live.connector.schemas.java.Notify;

import java.util.ArrayList;
import java.util.List;

public class LiveGameRunner {
    private static final int[] HAND_SIZE = {-1, -1, 5, 5, 4, 4};
    public static final int MY_ID = 0;

    private final Player _myPlayer = new AgentPlayer("localPlayer", SampleAgents.buildCompRandom());
    private GameState _gameState;
    private int _numPlayers;


    public void init(Init initMessage) {
        List<String> playerNames = initMessage.getNames();
        _numPlayers = playerNames.size();
        _gameState = new StateBackedByHanabiLive(_numPlayers);
        _gameState.init();
        // TODO figure out how to know which player position I am in
        _myPlayer.setID(MY_ID, _numPlayers, playerNames.toArray(new String[0]));

    }

    public void initialEvents(List<Notify> initials) {
        GameEvent gameInfo = new GameInformation(_numPlayers, HAND_SIZE[_numPlayers], _gameState.getInfomation(), _gameState.getLives());
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
            default:
                throw new IllegalArgumentException("Unknown initial event " + event);
        }
    }

    private List<GameEvent> convertInitialDraw(Notify event) {
        CardDrawn cardDrawn = new CardDrawn(
                event.getWho().intValue(),
                event.getOrder().intValue(),
                CardColors.getFromLiveId(event.getSuit()),
                event.getRank().intValue(),
                0);
        CardReceived cardReceived = new CardReceived(
                event.getWho().intValue(),
                event.getOrder().intValue(),
                true, //TODO handle the final draw correctly saying there are no more cards left
                0);
        return List.of(cardDrawn, cardReceived);
    }

    private Action convertClue(Notify event) {
        Clue clue = event.getClue();
        if (clue.getType() == 0) {
            return new TellValue(event.getTarget().intValue(), (int) clue.getValue());
        } else {
            return new TellColour(event.getTarget().intValue(), CardColors.getFromLiveId(clue.getValue()));
        }
    }
}
