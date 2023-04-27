package dev.totaltax.graviton.command;

import dev.totaltax.graviton.event.impl.EventSendChat;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommandManager {
    private List<Command> commands = new CopyOnWriteArrayList<>();

    public void init() {
        new Reflections("dev.totaltax.graviton.command.impl").getSubTypesOf(Command.class).forEach(c -> {
            try {
                commands.add(c.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignore) {}
        });
    }

    public void handleChat(EventSendChat event) {
        String prefix = ".";
    }


    public List<Command> getModules() {
        return commands;
    }
}
