package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.utils.agentbuilder.AgentFactory;
import com.fossgalaxy.games.fireworks.utils.agentbuilder.AgentFinder;
import com.fossgalaxy.games.fireworks.utils.agentbuilder.ConstructorFactory;
import com.fossgalaxy.games.fireworks.utils.agentbuilder.MethodFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.logging.Logger;

public class BotsList {
    private static final java.util.logging.Logger LOG = Logger.getLogger(BotsList.class.getName());

    private final SortedSet<String> _supportedBots = new TreeSet<>();
    private final AgentFinder _agentFinder = new AgentFinder();

    public void init() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        Class<AgentFinder> clazz = AgentFinder.class;
        Method scanMethod = clazz.getDeclaredMethod("scanForAgents", null);
        scanMethod.setAccessible(true);
        scanMethod.invoke(_agentFinder);
        _supportedBots.addAll(filterForNoArgAgents(_agentFinder.getFactories().values()));
    }

    private Collection<String> filterForNoArgAgents(Collection<AgentFactory> factories) throws NoSuchFieldException, IllegalAccessException {
        Field constructorConverters = ConstructorFactory.class.getDeclaredField("converters");
        constructorConverters.setAccessible(true);
        Field methodConverters = MethodFactory.class.getDeclaredField("converters");
        methodConverters.setAccessible(true);

        List<String> validNames = new ArrayList<>();
        for (AgentFactory factory : factories) {
            if (factory instanceof ConstructorFactory) {
                Function<String, ?>[] converters = (Function<String, ?>[]) constructorConverters.get(factory);
                if (converters == null || converters.length == 0) {
                    validNames.add(factory.name());
                }
            } else if (factory instanceof MethodFactory) {
                Function<String, ?>[] converters = (Function<String, ?>[]) methodConverters.get(factory);
                if (converters == null || converters.length == 0 ) {
                    validNames.add(factory.name());
                }
            } else {
                LOG.warning("Unknown factory type " + factory);
            }
        }
        return validNames;

    }

    public Collection<String> getAvailableBots() {
        return _supportedBots;
    }
}
