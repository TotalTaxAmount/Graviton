package dev.totaltax.graviton.module.impl.movement;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.graviton.event.EventTarget;
import dev.totaltax.graviton.event.impl.EventUpdate;
import dev.totaltax.graviton.module.Category;
import dev.totaltax.graviton.module.Module;

public class Fly extends Module {
    public Fly() {
        super("Fly", "Allows you to fly", InputConstants.KEY_G, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().flyingSpeed = 0.1F;
        mc.player.getAbilities().mayfly = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.getAbilities().flying = false;
        mc.player.getAbilities().mayfly = false;
    }
}
