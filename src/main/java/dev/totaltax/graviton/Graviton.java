package dev.totaltax.graviton;

import dev.totaltax.graviton.command.CommandManager;
import dev.totaltax.graviton.event.EventManager;
import dev.totaltax.graviton.event.EventTarget;
import dev.totaltax.graviton.event.impl.EventKey;
import dev.totaltax.graviton.event.impl.EventUpdate;
import dev.totaltax.graviton.file.FileManager;
import dev.totaltax.graviton.module.ModuleManager;
import dev.totaltax.graviton.ui.Ultralight;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Graviton {
    private String name = "Graviton";
    private String version = "0.0.1-dev";
    private Logger logger = LogManager.getLogger("Particle");
    private static Graviton instance = new Graviton();

    // Import stuff...
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private EventManager eventManager;
    private FileManager fileManager;


    public void start() {
        logger.info("Initializing Graviton...");
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        fileManager = new FileManager();

        moduleManager.init();
        commandManager.init();
        fileManager.init();
        Ultralight.init();
        // TODO: This is gonna be a lot of work...

        logger.info("Done!");
        eventManager.register(this);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Minecraft.getInstance().getWindow().setTitle(name + " >> " + version);
    }
    public void stop() {
        logger.info("Shutting down!");
        EventManager.unregister(this);
    }

    public static Graviton getInstance() {
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
