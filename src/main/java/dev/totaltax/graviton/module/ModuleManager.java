package dev.totaltax.graviton.module;

import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {

    private final List<Module> modules = new CopyOnWriteArrayList<>();

    public void init() {
        new Reflections("dev.totaltax.graviton.module.impl").getSubTypesOf(Module.class).forEach(m -> {
            try {
                modules.add(m.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignore) {}
        });
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModuleByName(String name) {
        return modules.stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
