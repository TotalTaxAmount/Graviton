package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {
   private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
   private final A innerModel;
   private final A outerModel;
   private final TextureAtlas armorTrimAtlas;

   public HumanoidArmorLayer(RenderLayerParent<T, M> p_267286_, A p_267110_, A p_267150_, ModelManager p_267238_) {
      super(p_267286_);
      this.innerModel = p_267110_;
      this.outerModel = p_267150_;
      this.armorTrimAtlas = p_267238_.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
   }

   public void render(PoseStack p_117096_, MultiBufferSource p_117097_, int p_117098_, T p_117099_, float p_117100_, float p_117101_, float p_117102_, float p_117103_, float p_117104_, float p_117105_) {
      this.renderArmorPiece(p_117096_, p_117097_, p_117099_, EquipmentSlot.CHEST, p_117098_, this.getArmorModel(EquipmentSlot.CHEST));
      this.renderArmorPiece(p_117096_, p_117097_, p_117099_, EquipmentSlot.LEGS, p_117098_, this.getArmorModel(EquipmentSlot.LEGS));
      this.renderArmorPiece(p_117096_, p_117097_, p_117099_, EquipmentSlot.FEET, p_117098_, this.getArmorModel(EquipmentSlot.FEET));
      this.renderArmorPiece(p_117096_, p_117097_, p_117099_, EquipmentSlot.HEAD, p_117098_, this.getArmorModel(EquipmentSlot.HEAD));
   }

   private void renderArmorPiece(PoseStack p_117119_, MultiBufferSource p_117120_, T p_117121_, EquipmentSlot p_117122_, int p_117123_, A p_117124_) {
      ItemStack itemstack = p_117121_.getItemBySlot(p_117122_);
      Item $$9 = itemstack.getItem();
      if ($$9 instanceof ArmorItem armoritem) {
         if (armoritem.getEquipmentSlot() == p_117122_) {
            this.getParentModel().copyPropertiesTo(p_117124_);
            this.setPartVisibility(p_117124_, p_117122_);
            boolean flag1 = this.usesInnerModel(p_117122_);
            boolean flag = itemstack.hasFoil();
            if (armoritem instanceof DyeableArmorItem) {
               int i = ((DyeableArmorItem)armoritem).getColor(itemstack);
               float f = (float)(i >> 16 & 255) / 255.0F;
               float f1 = (float)(i >> 8 & 255) / 255.0F;
               float f2 = (float)(i & 255) / 255.0F;
               this.renderModel(p_117119_, p_117120_, p_117123_, armoritem, flag, p_117124_, flag1, f, f1, f2, (String)null);
               this.renderModel(p_117119_, p_117120_, p_117123_, armoritem, flag, p_117124_, flag1, 1.0F, 1.0F, 1.0F, "overlay");
            } else {
               this.renderModel(p_117119_, p_117120_, p_117123_, armoritem, flag, p_117124_, flag1, 1.0F, 1.0F, 1.0F, (String)null);
            }

            if (p_117121_.level.enabledFeatures().contains(FeatureFlags.UPDATE_1_20)) {
               ArmorTrim.getTrim(p_117121_.level.registryAccess(), itemstack).ifPresent((p_267897_) -> {
                  this.renderTrim(armoritem.getMaterial(), p_117119_, p_117120_, p_117123_, p_267897_, flag, p_117124_, flag1, 1.0F, 1.0F, 1.0F);
               });
            }

         }
      }
   }

   protected void setPartVisibility(A p_117126_, EquipmentSlot p_117127_) {
      p_117126_.setAllVisible(false);
      switch (p_117127_) {
         case HEAD:
            p_117126_.head.visible = true;
            p_117126_.hat.visible = true;
            break;
         case CHEST:
            p_117126_.body.visible = true;
            p_117126_.rightArm.visible = true;
            p_117126_.leftArm.visible = true;
            break;
         case LEGS:
            p_117126_.body.visible = true;
            p_117126_.rightLeg.visible = true;
            p_117126_.leftLeg.visible = true;
            break;
         case FEET:
            p_117126_.rightLeg.visible = true;
            p_117126_.leftLeg.visible = true;
      }

   }

   private void renderModel(PoseStack p_117107_, MultiBufferSource p_117108_, int p_117109_, ArmorItem p_117110_, boolean p_117111_, A p_117112_, boolean p_117113_, float p_117114_, float p_117115_, float p_117116_, @Nullable String p_117117_) {
      VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(p_117108_, RenderType.armorCutoutNoCull(this.getArmorLocation(p_117110_, p_117113_, p_117117_)), false, p_117111_);
      p_117112_.renderToBuffer(p_117107_, vertexconsumer, p_117109_, OverlayTexture.NO_OVERLAY, p_117114_, p_117115_, p_117116_, 1.0F);
   }

   private void renderTrim(ArmorMaterial p_267946_, PoseStack p_268019_, MultiBufferSource p_268023_, int p_268190_, ArmorTrim p_267984_, boolean p_267965_, A p_267949_, boolean p_268259_, float p_268337_, float p_268095_, float p_268305_) {
      TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(p_268259_ ? p_267984_.innerTexture(p_267946_) : p_267984_.outerTexture(p_267946_));
      VertexConsumer vertexconsumer = textureatlassprite.wrap(ItemRenderer.getFoilBufferDirect(p_268023_, Sheets.armorTrimsSheet(), true, p_267965_));
      p_267949_.renderToBuffer(p_268019_, vertexconsumer, p_268190_, OverlayTexture.NO_OVERLAY, p_268337_, p_268095_, p_268305_, 1.0F);
   }

   private A getArmorModel(EquipmentSlot p_117079_) {
      return (A)(this.usesInnerModel(p_117079_) ? this.innerModel : this.outerModel);
   }

   private boolean usesInnerModel(EquipmentSlot p_117129_) {
      return p_117129_ == EquipmentSlot.LEGS;
   }

   private ResourceLocation getArmorLocation(ArmorItem p_117081_, boolean p_117082_, @Nullable String p_117083_) {
      String s = "textures/models/armor/" + p_117081_.getMaterial().getName() + "_layer_" + (p_117082_ ? 2 : 1) + (p_117083_ == null ? "" : "_" + p_117083_) + ".png";
      return ARMOR_LOCATION_CACHE.computeIfAbsent(s, ResourceLocation::new);
   }
}