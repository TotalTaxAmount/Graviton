package dev.totaltax.particle.module;

import org.reflections.Reflections;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ModuleManager {

    private List<Module> modules = new CopyOnWriteArrayList<>();

    public void init() {
        new Reflections("dev.totaltax.particle.module.impl").getSubTypesOf(Module.class).forEach(m -> {
            try {
                modules.add(m.newInstance());
            } catch (InstantiationException | IllegalAccessException ignore) {}
        });
    }

    public List<Module> getModules() {
        return modules;
    }
}
