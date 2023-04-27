package dev.totaltax.graviton.module.impl.movement;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.graviton.event.EventTarget;
import dev.totaltax.graviton.event.impl.EventUpdate;
import dev.totaltax.graviton.module.Category;
import dev.totaltax.graviton.module.Module;


public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Automatically sprint", InputConstants.KEY_X, Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        //TODO: Implement settings!!
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
