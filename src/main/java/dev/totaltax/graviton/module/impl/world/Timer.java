package dev.totaltax.graviton.module.impl.world;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.graviton.event.EventTarget;
import dev.totaltax.graviton.module.Category;
import dev.totaltax.graviton.module.Module;

public class Timer extends Module {
    public Timer() {
        super("Timer", "Changes the clients TPS", InputConstants.KEY_B, Category.WORLD);
    }

    @EventTarget
    public void onUpdate() {
        // TODO: Fix this at some point
    }
}
