package dev.totaltax.graviton.module;

import dev.totaltax.graviton.Graviton;
import dev.totaltax.graviton.event.EventManager;
import net.minecraft.client.Minecraft;

public class Module {
    protected Minecraft mc = Minecraft.getInstance();

    private String name;
    private String displayName;
    private int key;
    private Category category;
    private boolean enabled;

    public Module() {
        super();
    }

    public Module(String name, int key, Category category) {
        this.name = name;
        this.displayName = name;
        this.key = key;
        this.category = category;
        this.enabled = false;
    }

    public void onEnable() {
        Graviton.getInstance().getEventManager().register(this);
    }

    public void onDisable() {
        EventManager.unregister(this);
    }

    public void onToggle() {
        // TODO: Add sounds
    }

    public void setEnabled(boolean e) {
        this.enabled = e;
        if (this.enabled)
            onEnable();
        else
            onDisable();
    }

    public void toggle() {
        this.enabled = !this.enabled;
        onToggle();
        if (this.enabled)
            onEnable();
        else
            onDisable();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getKey() {
        return key;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }
}
