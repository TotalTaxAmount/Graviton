package dev.totaltax.graviton.command;

import dev.totaltax.graviton.event.impl.EventSendChat;
import org.reflections.Reflections;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandManager {
    private List<Command> commands = new CopyOnWriteArrayList<>();

    public void init() {
        new Reflections("dev.totaltax.graviton.command.impl").getSubTypesOf(Command.class).forEach(m -> {
            try {
                commands.add(m.newInstance());
            } catch (InstantiationException | IllegalAccessException ignore) {}
        });
    }

    public void handleChat(EventSendChat event) {}

    public List<Command> getModules() {
        return commands;
    }
}
