package net.rmelick.hanabi.bot.ieee;

import com.fossgalaxy.games.fireworks.ai.Agent;
import com.fossgalaxy.games.fireworks.annotations.AgentBuilderStatic;
import com.fossgalaxy.games.fireworks.state.BasicState;
import com.fossgalaxy.games.fireworks.utils.agentbuilder.AgentFactory;
import com.fossgalaxy.games.fireworks.utils.agentbuilder.AgentFinder;
import com.fossgalaxy.games.fireworks.utils.agentbuilder.ConstructorFactory;
import com.fossgalaxy.games.fireworks.utils.agentbuilder.MethodFactory;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.logging.Logger;

public class BotsList {
    private static final java.util.logging.Logger LOG = Logger.getLogger(BotsList.class.getName());

    private final SortedSet<String> _supportedBots = new TreeSet<>();
    private final AgentFinder _agentFinder = new AgentFinder();
    private final Map<String, AgentFactory> knownFactories = new HashMap<>();

    public void init() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        LOG.info("scanning for Bots in classpath");
        LOG.info(ClasspathHelper.forJavaClassPath().toString());

        Class<AgentFinder> clazz = AgentFinder.class;
        Method scanMethod = clazz.getDeclaredMethod("scanForAgents");
        scanMethod.setAccessible(true);
        scanMethod.invoke(_agentFinder);
        // TODO figure out why this doesn't work inside of Docker and it only shows a small set

       // scanForAgents();
        _supportedBots.addAll(filterForNoArgAgents(_agentFinder.getFactories().values()));
    }

    private void scanForAgents() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(
                        new MethodAnnotationsScanner(),
                        new SubTypesScanner(),
                        new TypeAnnotationsScanner())
                .setExpandSuperTypes(true));

        //find all subtypes of the agent class
        scanForConstructors(reflections);

        // find all annotated static methods
        scanForStaticMethods(reflections);
    }

    private void scanForConstructors(Reflections reflections) {
        Set<Class<? extends Agent>> agentClazzes = reflections.getSubTypesOf(Agent.class);
        LOG.info("All subtypes of Agent: " + agentClazzes);
        for (Class<? extends Agent> agentClazz : agentClazzes) {
        }
    }

    private void scanForStaticMethods(Reflections reflections) {
        Set<Method> methods = reflections.getMethodsAnnotatedWith(AgentBuilderStatic.class);
        LOG.info("All methods of AgentBuilderStatic: " + methods);

        for (Method method : methods) {

        }
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
                } else {
                    LOG.info(String.format("Dropping %s converters=%s", factory.name(), Arrays.toString(converters)));
                }
            } else if (factory instanceof MethodFactory) {
                Function<String, ?>[] converters = (Function<String, ?>[]) methodConverters.get(factory);
                if (converters == null || converters.length == 0 ) {
                    validNames.add(factory.name());
                } else {
                    LOG.info(String.format("Dropping %s converters=%s", factory.name(), Arrays.toString(converters)));
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
