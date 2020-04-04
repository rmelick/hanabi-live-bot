package net.rmelick.hanabi.bot.webapp;

import net.rmelick.hanabi.bot.ieee.Bots;
import net.rmelick.hanabi.bot.live.connector.HanabiLiveClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Controller
public class TablesController {
	private final HanabiLiveClient _client = new HanabiLiveClient();

	@PostConstruct
	public void init() {
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
		model.addAttribute("bots", Bots.SUPPORTED_BOTS.keySet());
		return "tables";
	}

	@PostMapping(value = "/joinTable")
	public String join(@RequestParam String tableID, @RequestParam String password, @RequestParam String bot) {
		System.out.println(String.format("Attempting to join game %s with bot %s password %s", tableID, password, bot));
		return "redirect:/tables";
	}
}
