package dev.totaltax.particle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Particle {
    private String name = "Particle";
    private String version = "0.0.1-dev";
    private Logger logger = LogManager.getLogger("Particle");
    private static Particle instance = new Particle();

    public void start() {
        logger.info("Initializing Particle...");
    }

    public void stop() {
        logger.info("Shutting down!");
    }

    public static Particle getInstance() {
        return instance;
    }
}
