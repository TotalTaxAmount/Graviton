package dev.totaltax.particle;

import dev.totaltax.particle.command.CommandManager;
import dev.totaltax.particle.event.EventManager;
import dev.totaltax.particle.event.EventTarget;
import dev.totaltax.particle.event.impl.EventKey;
import dev.totaltax.particle.module.ModuleManager;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Particle {
    private String name = "Particle";
    private String version = "0.0.1-dev";
    private Logger logger = LogManager.getLogger("Particle");
    private static Particle instance = new Particle();

    // Import stuff...
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private EventManager eventManager;


    public void start() {
        logger.info("Initializing Particle...");
        Minecraft.getInstance().getWindow().setTitle(name + " >> " + version);
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        moduleManager.init();
        commandManager.init();

        logger.info("Done!");
        eventManager.register(this);
    }

    public void stop() {
        logger.info("Shutting down!");
        EventManager.unregister(this);
    }
    public static Particle getInstance() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public Logger getLogger() {
        return logger;
    }

    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().forEach(m -> {
            if (m.getKey() == event.getKey() && event.getDirection() == EventKey.Type.DOWN) {
                m.toggle();
            }
        });
    }
}
