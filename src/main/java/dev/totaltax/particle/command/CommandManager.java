package dev.totaltax.particle.command;

import org.reflections.Reflections;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandManager {
    private List<Command> commands = new CopyOnWriteArrayList<>();

    public void init() {
        new Reflections("dev.totaltax.particle.command.impl").getSubTypesOf(Command.class).forEach(m -> {
            try {
                commands.add(m.newInstance());
            } catch (InstantiationException | IllegalAccessException ignore) {}
        });
    }

    public List<Command> getModules() {
        return commands;
    }
}
