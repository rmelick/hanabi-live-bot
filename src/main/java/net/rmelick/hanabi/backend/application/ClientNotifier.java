package net.rmelick.hanabi.backend.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Controller
public class ClientNotifier {
  private final Logger logger = LoggerFactory.getLogger(ClientNotifier.class);
  private final SimpMessagingTemplate _outgoingSocketMessages;

  @Autowired
  public ClientNotifier(SimpMessagingTemplate template) {
    _outgoingSocketMessages = template;
  }


  public void notifyGameStateSummaryChange(String gameId) {
    logger.info("notifying game state change for " + gameId);
    Map<String, String> message = new HashMap<>();
    message.put("messageType", "GAME_STATE_CHANGED");
    message.put("game_id", gameId);
    _outgoingSocketMessages.convertAndSend("/topic/all", message);
    _outgoingSocketMessages.convertAndSend("/topic/gamestatesummary/" + gameId, message);
  }

  public void notifyGameStateChange(String gameId) {
    logger.info("notifying game state change for " + gameId);
    Map<String, String> message = new HashMap<>();
    message.put("messageType", "GAME_STATE_CHANGED");
    message.put("game_id", gameId);
    _outgoingSocketMessages.convertAndSend("/topic/all", message);
    _outgoingSocketMessages.convertAndSend("/topic/gamestate/" + gameId, message);
  }
}
