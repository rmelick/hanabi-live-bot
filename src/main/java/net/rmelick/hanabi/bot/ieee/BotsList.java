package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.utils.agentbuilder.AgentFinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class BotsList {
    private final List<String> _supportedBots = new ArrayList<>();
    private final AgentFinder _agentFinder = new AgentFinder();

    public void init() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<AgentFinder> clazz = AgentFinder.class;
        Method scanMethod = clazz.getDeclaredMethod("scanForAgents", null);
        scanMethod.setAccessible(true);
        scanMethod.invoke(_agentFinder);
    }

    public Collection<String> getAvailableBots() {
        return _agentFinder.getFactories().keySet();
    }
}
