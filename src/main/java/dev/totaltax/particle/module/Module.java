package dev.totaltax.particle.module;

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
}
