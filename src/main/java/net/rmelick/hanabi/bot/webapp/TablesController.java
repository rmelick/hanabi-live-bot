package net.rmelick.hanabi.bot.webapp;

import net.rmelick.hanabi.bot.live.connector.HanabiLiveClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.http.WebSocket;

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
	public String greeting(Model model) {
		model.addAttribute("tables", _client.getWorldState().getCurrentTables());
		return "tables";
	}

}
