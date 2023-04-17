package dev.totaltax.particle.module.impl.movement;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.particle.Particle;
import dev.totaltax.particle.event.EventTarget;
import dev.totaltax.particle.event.impl.EventUpdate;
import dev.totaltax.particle.module.Category;
import dev.totaltax.particle.module.Module;


public class Sprint extends Module {
    public Sprint() {
        super("Sprint", InputConstants.KEY_X, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        Particle.getInstance().getLogger().debug("What the fuck?");
        // TODO: Add setting so its not just true
        if ((!mc.player.horizontalCollision && mc.player.input.forwardImpulse > 0) || true) {
            mc.player.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setSprinting(false);
    }
}
