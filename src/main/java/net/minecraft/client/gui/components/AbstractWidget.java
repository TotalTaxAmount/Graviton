package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.BelowOrAboveWidgetTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.MenuTooltipPositioner;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractWidget extends GuiComponent implements Renderable, GuiEventListener, LayoutElement, NarratableEntry {
   public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
   public static final ResourceLocation ACCESSIBILITY_TEXTURE = new ResourceLocation("textures/gui/accessibility.png");
   private static final double PERIOD_PER_SCROLLED_PIXEL = 0.5D;
   private static final double MIN_SCROLL_PERIOD = 3.0D;
   protected int width;
   protected int height;
   private int x;
   private int y;
   private Component message;
   protected boolean isHovered;
   public boolean active = true;
   public boolean visible = true;
   protected float alpha = 1.0F;
   private int tabOrderGroup;
   private boolean focused;
   @Nullable
   private Tooltip tooltip;
   private int tooltipMsDelay;
   private long hoverOrFocusedStartTime;
   private boolean wasHoveredOrFocused;

   public AbstractWidget(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Component p_93633_) {
      this.x = p_93629_;
      this.y = p_93630_;
      this.width = p_93631_;
      this.height = p_93632_;
      this.message = p_93633_;
   }

   public int getHeight() {
      return this.height;
   }

   public void render(PoseStack p_93657_, int p_93658_, int p_93659_, float p_93660_) {
      if (this.visible) {
         this.isHovered = p_93658_ >= this.getX() && p_93659_ >= this.getY() && p_93658_ < this.getX() + this.width && p_93659_ < this.getY() + this.height;
         this.renderWidget(p_93657_, p_93658_, p_93659_, p_93660_);
         this.updateTooltip();
      }
   }

   private void updateTooltip() {
      if (this.tooltip != null) {
         boolean flag = this.isHovered || this.isFocused() && Minecraft.getInstance().getLastInputType().isKeyboard();
         if (flag != this.wasHoveredOrFocused) {
            if (flag) {
               this.hoverOrFocusedStartTime = Util.getMillis();
            }

            this.wasHoveredOrFocused = flag;
         }

         if (flag && Util.getMillis() - this.hoverOrFocusedStartTime > (long)this.tooltipMsDelay) {
            Screen screen = Minecraft.getInstance().screen;
            if (screen != null) {
               screen.setTooltipForNextRenderPass(this.tooltip, this.createTooltipPositioner(), this.isFocused());
            }
         }

      }
   }

   protected ClientTooltipPositioner createTooltipPositioner() {
      return (ClientTooltipPositioner)(!this.isHovered && this.isFocused() && Minecraft.getInstance().getLastInputType().isKeyboard() ? new BelowOrAboveWidgetTooltipPositioner(this) : new MenuTooltipPositioner(this));
   }

   public void setTooltip(@Nullable Tooltip p_259796_) {
      this.tooltip = p_259796_;
   }

   public void setTooltipDelay(int p_259732_) {
      this.tooltipMsDelay = p_259732_;
   }

   protected MutableComponent createNarrationMessage() {
      return wrapDefaultNarrationMessage(this.getMessage());
   }

   public static MutableComponent wrapDefaultNarrationMessage(Component p_168800_) {
      return Component.translatable("gui.narrate.button", p_168800_);
   }

   public abstract void renderWidget(PoseStack p_268228_, int p_268034_, int p_268009_, float p_268085_);

   protected static void renderScrollingString(PoseStack p_275352_, Font p_275395_, Component p_275742_, int p_275307_, int p_275548_, int p_275592_, int p_275385_, int p_275291_) {
      int i = p_275395_.width(p_275742_);
      int j = (p_275548_ + p_275385_ - 9) / 2 + 1;
      int k = p_275592_ - p_275307_;
      if (i > k) {
         int l = i - k;
         double d0 = (double)Util.getMillis() / 1000.0D;
         double d1 = Math.max((double)l * 0.5D, 3.0D);
         double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / 2.0D + 0.5D;
         double d3 = Mth.lerp(d2, 0.0D, (double)l);
         enableScissor(p_275307_, p_275548_, p_275592_, p_275385_);
         drawString(p_275352_, p_275395_, p_275742_, p_275307_ - (int)d3, j, p_275291_);
         disableScissor();
      } else {
         drawCenteredString(p_275352_, p_275395_, p_275742_, (p_275307_ + p_275592_) / 2, j, p_275291_);
      }

   }

   protected void renderScrollingString(PoseStack p_275527_, Font p_275333_, int p_275661_, int p_275656_) {
      int i = this.getX() + p_275661_;
      int j = this.getX() + this.getWidth() - p_275661_;
      renderScrollingString(p_275527_, p_275333_, this.getMessage(), i, this.getY(), j, this.getY() + this.getHeight(), p_275656_);
   }

   public void renderTexture(PoseStack p_268327_, ResourceLocation p_268031_, int p_268218_, int p_267959_, int p_268261_, int p_267978_, int p_267937_, int p_268330_, int p_268160_, int p_267985_, int p_268321_) {
      RenderSystem.setShaderTexture(0, p_268031_);
      int i = p_267978_;
      if (!this.isActive()) {
         i = p_267978_ + p_267937_ * 2;
      } else if (this.isHoveredOrFocused()) {
         i = p_267978_ + p_267937_;
      }

      RenderSystem.enableDepthTest();
      blit(p_268327_, p_268218_, p_267959_, (float)p_268261_, (float)i, p_268330_, p_268160_, p_267985_, p_268321_);
   }

   public void onClick(double p_93634_, double p_93635_) {
   }

   public void onRelease(double p_93669_, double p_93670_) {
   }

   protected void onDrag(double p_93636_, double p_93637_, double p_93638_, double p_93639_) {
   }

   public boolean mouseClicked(double p_93641_, double p_93642_, int p_93643_) {
      if (this.active && this.visible) {
         if (this.isValidClickButton(p_93643_)) {
            boolean flag = this.clicked(p_93641_, p_93642_);
            if (flag) {
               this.playDownSound(Minecraft.getInstance().getSoundManager());
               this.onClick(p_93641_, p_93642_);
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(double p_93684_, double p_93685_, int p_93686_) {
      if (this.isValidClickButton(p_93686_)) {
         this.onRelease(p_93684_, p_93685_);
         return true;
      } else {
         return false;
      }
   }

   protected boolean isValidClickButton(int p_93652_) {
      return p_93652_ == 0;
   }

   public boolean mouseDragged(double p_93645_, double p_93646_, int p_93647_, double p_93648_, double p_93649_) {
      if (this.isValidClickButton(p_93647_)) {
         this.onDrag(p_93645_, p_93646_, p_93648_, p_93649_);
         return true;
      } else {
         return false;
      }
   }

   protected boolean clicked(double p_93681_, double p_93682_) {
      return this.active && this.visible && p_93681_ >= (double)this.getX() && p_93682_ >= (double)this.getY() && p_93681_ < (double)(this.getX() + this.width) && p_93682_ < (double)(this.getY() + this.height);
   }

   @Nullable
   public ComponentPath nextFocusPath(FocusNavigationEvent p_265640_) {
      if (this.active && this.visible) {
         return !this.isFocused() ? ComponentPath.leaf(this) : null;
      } else {
         return null;
      }
   }

   public boolean isMouseOver(double p_93672_, double p_93673_) {
      return this.active && this.visible && p_93672_ >= (double)this.getX() && p_93673_ >= (double)this.getY() && p_93672_ < (double)(this.getX() + this.width) && p_93673_ < (double)(this.getY() + this.height);
   }

   public void playDownSound(SoundManager p_93665_) {
      p_93665_.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int p_93675_) {
      this.width = p_93675_;
   }

   public void setAlpha(float p_93651_) {
      this.alpha = p_93651_;
   }

   public void setMessage(Component p_93667_) {
      this.message = p_93667_;
   }

   public Component getMessage() {
      return this.message;
   }

   public boolean isFocused() {
      return this.focused;
   }

   public boolean isHovered() {
      return this.isHovered;
   }

   public boolean isHoveredOrFocused() {
      return this.isHovered() || this.isFocused();
   }

   public boolean isActive() {
      return this.visible && this.active;
   }

   public void setFocused(boolean p_93693_) {
      this.focused = p_93693_;
   }

   public NarratableEntry.NarrationPriority narrationPriority() {
      if (this.isFocused()) {
         return NarratableEntry.NarrationPriority.FOCUSED;
      } else {
         return this.isHovered ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
      }
   }

   public final void updateNarration(NarrationElementOutput p_259921_) {
      this.updateWidgetNarration(p_259921_);
      if (this.tooltip != null) {
         this.tooltip.updateNarration(p_259921_);
      }

   }

   protected abstract void updateWidgetNarration(NarrationElementOutput p_259858_);

   protected void defaultButtonNarrationText(NarrationElementOutput p_168803_) {
      p_168803_.add(NarratedElementType.TITLE, this.createNarrationMessage());
      if (this.active) {
         if (this.isFocused()) {
            p_168803_.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.focused"));
         } else {
            p_168803_.add(NarratedElementType.USAGE, Component.translatable("narration.button.usage.hovered"));
         }
      }

   }

   public int getX() {
      return this.x;
   }

   public void setX(int p_254495_) {
      this.x = p_254495_;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int p_253718_) {
      this.y = p_253718_;
   }

   public void visitWidgets(Consumer<AbstractWidget> p_265566_) {
      p_265566_.accept(this);
   }

   public ScreenRectangle getRectangle() {
      return LayoutElement.super.getRectangle();
   }

   public int getTabOrderGroup() {
      return this.tabOrderGroup;
   }

   public void setTabOrderGroup(int p_268123_) {
      this.tabOrderGroup = p_268123_;
   }
}