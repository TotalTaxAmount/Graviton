package dev.totaltax.particle.module.impl.movement;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.particle.event.EventTarget;
import dev.totaltax.particle.event.impl.EventUpdate;
import dev.totaltax.particle.module.Category;
import dev.totaltax.particle.module.Module;

public class Fly extends Module {
    public Fly() {
        super("Fly", InputConstants.KEY_G, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.player.getAbilities().flying = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.getAbilities().flying = false;
    }
}
