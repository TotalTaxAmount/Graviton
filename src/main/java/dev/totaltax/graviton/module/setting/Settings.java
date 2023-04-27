package dev.totaltax.graviton.module.setting;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Settings {
    private final List<Setting> settings = new ArrayList<>();

    public Setting add(Setting setting) {
        settings.add(setting);
        return setting;
    }

    public Setting getByName(String name) {
        for (Setting s : this.settings) {
            if (s.getName().equals(name)) return s;
        }
        return null;
    }

    public Setting getByIndex(int index) {
        return this.settings.get(index);
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "settings=" + settings +
                '}';
    }
}
