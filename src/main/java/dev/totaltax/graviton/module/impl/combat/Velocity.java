package dev.totaltax.graviton.module.impl.combat;

import com.mojang.blaze3d.platform.InputConstants;
import dev.totaltax.graviton.event.EventTarget;
import dev.totaltax.graviton.event.impl.EventReceivePacket;
import dev.totaltax.graviton.module.Category;
import dev.totaltax.graviton.module.Module;
import dev.totaltax.graviton.module.setting.Setting;
import dev.totaltax.graviton.module.setting.impl.NumberSetting;
import dev.totaltax.graviton.util.ChatUtil;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;

public class Velocity extends Module {

    private NumberSetting vertical = (NumberSetting) this.settingGroup.add(NumberSetting.Builder.newInstance()
            .setDefault(0)
            .setMax(100)
            .setMin(0)
            .setIncrement(1)
            .setName("Vertical")
            .setDescription("Kb")
            .build());

    public Velocity() {
        super("Velocity", "Prevents knock back", InputConstants.KEY_V, Category.COMBAT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ChatUtil.sendDebug(this.settingGroup.toString());
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event){
        if (event.getPacket() instanceof ClientboundSetEntityMotionPacket packet) {
            assert mc.player != null;
            if (packet.id == mc.player.getId()){
                //event.setCancelled(true);
                packet.xa = 0;
                packet.ya = 0;
                packet.za = 0;
            }
        }
    }
}
