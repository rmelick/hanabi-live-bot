package net.rmelick.hanabi.bot.webapp;

import net.rmelick.hanabi.bot.ieee.BotsList;
import net.rmelick.hanabi.bot.live.connector.ActiveGamesManager;
import net.rmelick.hanabi.bot.live.connector.HanabiLobbyClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.logging.Logger;

@Controller
public class TablesController {
	private static final Logger LOG = Logger.getLogger(TablesController.class.getName());
	private final ActiveGamesManager _gamesManager = new ActiveGamesManager();
	private final HanabiLobbyClient _client = new HanabiLobbyClient(_gamesManager);
	private final BotsList _botsList = new BotsList();

	@PostConstruct
	public void init() {
		_botsList.init();
		try {
			_client.connectWebsocket();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/tables")
	public String tables(Model model) {
		model.addAttribute("tables", _client.getWorldState().getCurrentTables());
		model.addAttribute("bots", BotsList.SUPPORTED_BOTS.keySet());
		return "tables";
	}

	@PostMapping(value = "/joinTable")
	public String join(@RequestParam Long tableID, @RequestParam String password, @RequestParam String bot) {
		System.out.println(String.format("Attempting to join game %s with bot %s password %s", tableID, bot, password));
		try {
			_gamesManager.joinGame(tableID, password);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return "redirect:/tables";
	}
}
