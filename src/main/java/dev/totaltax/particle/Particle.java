package dev.totaltax.particle;

import dev.totaltax.particle.event.EventManager;
import dev.totaltax.particle.event.EventTarget;
import dev.totaltax.particle.event.impl.EventKey;
import dev.totaltax.particle.module.ModuleManager;
import io.netty.util.internal.logging.InternalLogger;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Particle {
    private String name = "Particle";
    private String version = "0.0.1-dev";
    private Logger logger = LogManager.getLogger("Particle");
    private static Particle instance = new Particle();
    private ModuleManager moduleManager;

    // Import stuff...
    private EventManager eventManager;


    public void start() {
        logger.info("Initializing Particle...");
        eventManager = new EventManager();
        moduleManager = new ModuleManager();

        moduleManager.init();

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

    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().forEach(m -> {
            if (m.getKey() == event.getKey() && event.getDirection() == EventKey.Type.DOWN) {
                m.toggle();
            }
        });
    }

    public Logger getLogger() {
        return logger;
    }
}
