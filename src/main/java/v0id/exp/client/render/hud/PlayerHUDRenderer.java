package v0id.exp.client.render.hud;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import v0id.api.exp.data.ExPTextures;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.util.Helpers;

public class PlayerHUDRenderer
{
	
	public static final float hotbarUEnd = 0.75F;
	public static final float hotbarVEnd = 0.1328125F;
	public static final float selectionUEnd = 0.140625F;
	public static final float selectionVStart = hotbarVEnd;
	public static final float selectionVEnd = selectionVStart + 0.140625F;
	public static final float offhandUStart = selectionUEnd;
	public static final float offhandUEnd = offhandUStart + 0.140625F;
	public static final float healthVStart = 0.9375F;
	public static final float healthVEnd = 1F;
	public static final float healthUStart = 0;
	public static final float healthUEnd = 0.0625F;
	public static final float hungerVStart = 0.9375F;
	public static final float hungerVEnd = 1F;
	public static final float hungerUStart = 0.0625F;
	public static final float hungerUEnd = 0.125F;
	public static final float thirstVStart = 0.9375F;
	public static final float thirstVEnd = 1F;
	public static final float thirstUStart = 0.125F;
	public static final float thirstUEnd = 0.1875F;
	public static final float tempVStart = 0.9375F;
	public static final float tempVEnd = 1F;
	public static final float tempUStart = 0.1875F;
	public static final float tempUEnd = 0.25F;
	public static final float stateVStart = 0.9375F;
	public static final float stateVEnd = 1F;
	public static final float stateUStart = 0.25F;
	public static final float stateUEnd = 0.3125F;
	public static final float wSeasonVStart = 0.875F;
	public static final float wSeasonVEnd = 0.9375F;
	public static final float wSeasonUStart = 0;
	public static final float wSeasonUEnd = 0.0625F;
	public static final float wWindVStart = 0.875F;
	public static final float wWindVEnd = 0.9375F;
	public static final float wWindUStart = 0.0625F;
	public static final float wWindUEnd = 0.125F;
	public static final float wTVStart = 0.875F;
	public static final float wTVEnd = 0.9375F;
	public static final float wTUStart = 0.125F;
	public static final float wTUEnd = 0.1875F;
	public static final float wHVStart = 0.875F;
	public static final float wHVEnd = 0.9375F;
	public static final float wHUStart = 0.1875F;
	public static final float wHUEnd = 0.25F;
	public static final float heatDeathTemp = 41.5F;
	public static final float freezeDeathTemp = 31F;
	
	public static void render(float partialTicks)
	{
		Minecraft.getMinecraft().mcProfiler.startSection("expetrumHud");
		Minecraft.getMinecraft().mcProfiler.startSection("staticBackground");
		EntityPlayer p = Minecraft.getMinecraft().player;
		IExPPlayer player = IExPPlayer.of(p);
		World w = Minecraft.getMinecraft().world;
		IExPWorld world = IExPWorld.of(w);
		Minecraft.getMinecraft().renderEngine.bindTexture(ExPTextures.PLAYER_HUD[player.getProgressionStage().ordinal()]);
		VertexBuffer vb = Tessellator.getInstance().getBuffer();
		ScaledResolution sRes = new ScaledResolution(Minecraft.getMinecraft());
		EnumHandSide hand = p.getPrimaryHand().opposite();
		float cX = sRes.getScaledWidth() / 2;
		float xY = sRes.getScaledHeight() / 2;
		
		GlStateManager.enableAlpha();
		GlStateManager.color(1, 1, 1, 1F);
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.pos(cX - 96, sRes.getScaledHeight(), 0).tex(0, hotbarVEnd).endVertex();
		vb.pos(cX + 96, sRes.getScaledHeight(), 0).tex(hotbarUEnd, hotbarVEnd).endVertex();
		vb.pos(cX + 96, sRes.getScaledHeight() - 34, 0).tex(hotbarUEnd, 0).endVertex();
		vb.pos(cX - 96, sRes.getScaledHeight() - 34, 0).tex(0, 0).endVertex();
		int selectedIndex = p.inventory.currentItem;
		float sX = cX - 79 + 20 * selectedIndex;
		vb.pos(sX - 18, sRes.getScaledHeight(), 0).tex(0, selectionVEnd).endVertex();
		vb.pos(sX + 18, sRes.getScaledHeight(), 0).tex(selectionUEnd, selectionVEnd).endVertex();
		vb.pos(sX + 18, sRes.getScaledHeight() - 36, 0).tex(selectionUEnd, selectionVStart).endVertex();
		vb.pos(sX - 18, sRes.getScaledHeight() - 36, 0).tex(0, selectionVStart).endVertex();
		sX = cX - (hand == EnumHandSide.LEFT ? 120 : -120);
		vb.pos(sX - 18, sRes.getScaledHeight(), 0).tex(offhandUStart, selectionVEnd).endVertex();
		vb.pos(sX + 18, sRes.getScaledHeight(), 0).tex(offhandUEnd, selectionVEnd).endVertex();
		vb.pos(sX + 18, sRes.getScaledHeight() - 36, 0).tex(offhandUEnd, selectionVStart).endVertex();
		vb.pos(sX - 18, sRes.getScaledHeight() - 36, 0).tex(offhandUStart, selectionVStart).endVertex();
		
		vb.pos(0, sRes.getScaledHeight() - 84, 0).tex(healthUStart, healthVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 84, 0).tex(healthUEnd, healthVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 100, 0).tex(healthUEnd, healthVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 100, 0).tex(healthUStart, healthVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 68, 0).tex(hungerUStart, hungerVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 68, 0).tex(hungerUEnd, hungerVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 84, 0).tex(hungerUEnd, hungerVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 84, 0).tex(hungerUStart, hungerVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 52, 0).tex(thirstUStart, thirstVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 52, 0).tex(thirstUEnd, thirstVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 68, 0).tex(thirstUEnd, thirstVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 68, 0).tex(thirstUStart, thirstVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 36, 0).tex(tempUStart, tempVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 36, 0).tex(tempUEnd, tempVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 52, 0).tex(tempUEnd, tempVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 52, 0).tex(tempUStart, tempVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 20, 0).tex(stateUStart, stateVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 20, 0).tex(stateUEnd, stateVEnd).endVertex();
		vb.pos(16, sRes.getScaledHeight() - 36, 0).tex(stateUEnd, stateVStart).endVertex();
		vb.pos(0, sRes.getScaledHeight() - 36, 0).tex(stateUStart, stateVStart).endVertex();
		
		vb.pos(sRes.getScaledWidth() - 16, sRes.getScaledHeight() - 68, 0).tex(wSeasonUStart, wSeasonVEnd).endVertex();
		vb.pos(sRes.getScaledWidth(), sRes.getScaledHeight() - 68, 0).tex(wSeasonUEnd, wSeasonVEnd).endVertex();
		vb.pos(sRes.getScaledWidth(), sRes.getScaledHeight() - 84, 0).tex(wSeasonUEnd, wSeasonVStart).endVertex();
		vb.pos(sRes.getScaledWidth() - 16, sRes.getScaledHeight() - 84, 0).tex(wSeasonUStart, wSeasonVStart).endVertex();
		vb.pos(sRes.getScaledWidth() - 16, sRes.getScaledHeight() - 52, 0).tex(wWindUStart, wWindVEnd).endVertex();
		vb.pos(sRes.getScaledWidth(), sRes.getScaledHeight() - 52, 0).tex(wWindUEnd, wWindVEnd).endVertex();
		vb.pos(sRes.getScaledWidth(), sRes.getScaledHeight() - 68, 0).tex(wWindUEnd, wWindVStart).endVertex();
		vb.pos(sRes.getScaledWidth() - 16, sRes.getScaledHeight() - 68, 0).tex(wWindUStart, wWindVStart).endVertex();
		vb.pos(sRes.getScaledWidth() - 16, sRes.getScaledHeight() - 36, 0).tex(wTUStart, wTVEnd).endVertex();
		vb.pos(sRes.getScaledWidth(), sRes.getScaledHeight() - 36, 0).tex(wTUEnd, wTVEnd).endVertex();
		vb.pos(sRes.getScaledWidth(), sRes.getScaledHeight() - 52, 0).tex(wTUEnd, wTVStart).endVertex();
		vb.pos(sRes.getScaledWidth() - 16, sRes.getScaledHeight() - 52, 0).tex(wTUStart, wTVStart).endVertex();
		vb.pos(sRes.getScaledWidth() - 16, sRes.getScaledHeight() - 20, 0).tex(wHUStart, wHVEnd).endVertex();
		vb.pos(sRes.getScaledWidth(), sRes.getScaledHeight() - 20, 0).tex(wHUEnd, wHVEnd).endVertex();
		vb.pos(sRes.getScaledWidth(), sRes.getScaledHeight() - 36, 0).tex(wHUEnd, wHVStart).endVertex();
		vb.pos(sRes.getScaledWidth() - 16, sRes.getScaledHeight() - 36, 0).tex(wHUStart, wHVStart).endVertex();
		Tessellator.getInstance().draw();
		GlStateManager.disableAlpha();
		Minecraft.getMinecraft().mcProfiler.endSection();
		Minecraft.getMinecraft().mcProfiler.startSection("hotbar");
		renderHotbar(partialTicks, sRes);
		Minecraft.getMinecraft().mcProfiler.endSection();
		Minecraft.getMinecraft().mcProfiler.startSection("text");
		boolean unicode = Minecraft.getMinecraft().fontRenderer.getUnicodeFlag();
		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(true);
		Minecraft.getMinecraft().mcProfiler.startSection("player");
		float hpPercentage = player.getCurrentHealth() / player.getMaxHealth(true);
		String hpText = I18n.format(String.format("exp.hud.hp_%d", (int)MathHelper.clamp(Math.floor(hpPercentage * 6), 0, 5)));
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(hpText, 16, sRes.getScaledHeight() - 98, 0xffffff);
		float hungerPercentage = player.getCalories() / 2000;
		String hungerText = I18n.format(String.format("exp.hud.hunger_%d", (int)MathHelper.clamp(Math.floor(hungerPercentage * 6), 0, 6)));
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(hungerText, 16, sRes.getScaledHeight() - 82, 0xffffff);
		float thirstPercentage = player.getThirst() / player.getMaxThirst(true);
		String thirstText = I18n.format(String.format("exp.hud.thirst_%d", (int)MathHelper.clamp(Math.floor(thirstPercentage * 6), 0, 5)));
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(thirstText, 16, sRes.getScaledHeight() - 64, 0xffffff);
		float temp = player.getCurrentTemperature();
		
		final float tempIndexer = heatDeathTemp - freezeDeathTemp;
		float playerTempIndex = temp - freezeDeathTemp;
		float playerTempDeltaPercentage = playerTempIndex / tempIndexer;
		String tempText = I18n.format(String.format("exp.hud.temp_%d", (int)MathHelper.clamp(Math.floor(playerTempDeltaPercentage * 10), -1, 10)));
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(tempText, 16, sRes.getScaledHeight() - 48, 0xffffff);
		byte feelsIndex = 0;
		String feelsText = I18n.format(String.format("exp.hud.mood_%d", feelsIndex));
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(feelsText, 16, sRes.getScaledHeight() - 32, 0xffffff);
		Minecraft.getMinecraft().mcProfiler.endSection();
		Minecraft.getMinecraft().mcProfiler.startSection("world");
		String seasonText = I18n.format("exp.hud.season." + world.getCurrentSeason().name().toLowerCase());
		int seasonTextWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(seasonText);
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(seasonText, sRes.getScaledWidth() - 16 - seasonTextWidth, sRes.getScaledHeight() - 82, 0xffffff);
		float windMS = world.getWindStrength();
		float windKH = windMS * 18 / 5;
		int windStrengthIndex = Helpers.getWindStrengthIndex(windKH);
		Vec3d wdir = world.getWindDirection();
		EnumFacing windDirection = EnumFacing.getFacingFromVector((float)wdir.x, (float)wdir.y, (float)wdir.z);
		String windText = windDirection.name().substring(0, 1) + " " + I18n.format(String.format("exp.hud.wind.strength_%d", windStrengthIndex));
		int windTextWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(windText);
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(windText, sRes.getScaledWidth() - 16 - windTextWidth, sRes.getScaledHeight() - 64, 0xffffff);
		int wtempIndex = Helpers.getTemperatureIndex(Helpers.getTemperatureAt(w, p.getPosition()));
		String wtempText = I18n.format(String.format("exp.hud.wtemp_%d", wtempIndex));
		int wtempTextWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(wtempText);
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(wtempText, sRes.getScaledWidth() - 16 - wtempTextWidth, sRes.getScaledHeight() - 48, 0xffffff);
		float moisture = Helpers.getMoistureAt(w, p.getPosition());
		int mIndex = (int) MathHelper.clamp(Math.floor(moisture * 6), 0, 6);
		String moistureText = I18n.format(String.format("exp.hud.humi_%d", w.isRaining() ? -1 : mIndex));
		int moistureTextWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(moistureText);
		Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(moistureText, sRes.getScaledWidth() - 16 - moistureTextWidth, sRes.getScaledHeight() - 32, 0xffffff);
		Minecraft.getMinecraft().mcProfiler.endSection();
		Minecraft.getMinecraft().fontRenderer.setUnicodeFlag(unicode);
		Minecraft.getMinecraft().mcProfiler.endSection();
		Minecraft.getMinecraft().mcProfiler.endSection();
	}
	
	private static void renderHotbar(float partialTicks, ScaledResolution sRes)
	{
		if (Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityPlayer)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            EntityPlayer entityplayer = (EntityPlayer)Minecraft.getMinecraft().getRenderViewEntity();
            ItemStack itemstack = entityplayer.getHeldItemOffhand();
            EnumHandSide enumhandside = entityplayer.getPrimaryHand().opposite();
            int i = sRes.getScaledWidth() / 2;
            float f = 0;
            int j = 182;
            int k = 91;
            
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();

            for (int l = 0; l < 9; ++l)
            {
                int i1 = i - 90 + l * 20 + 3;
                int j1 = sRes.getScaledHeight() - 16 - 9;
                renderHotbarItem(i1, j1, partialTicks, entityplayer, (ItemStack)entityplayer.inventory.mainInventory.get(l));
            }

            if (!itemstack.isEmpty())
            {
                int l1 = sRes.getScaledHeight() - 16 - 3;

                if (enumhandside == EnumHandSide.LEFT)
                {
                    renderHotbarItem(i - 102 - 26, l1 - 8, partialTicks, entityplayer, itemstack);
                }
                else
                {
                    renderHotbarItem(i + 102 + 10, l1 - 8, partialTicks, entityplayer, itemstack);
                }
            }

            if (Minecraft.getMinecraft().gameSettings.attackIndicator == 2)
            {
                float f1 = Minecraft.getMinecraft().player.getCooledAttackStrength(0.0F);

                if (f1 < 1.0F)
                {
                    int i2 = sRes.getScaledHeight() - 20;
                    int j2 = i + 91 + 6;

                    if (enumhandside == EnumHandSide.RIGHT)
                    {
                        j2 = i - 91 - 22;
                    }

                    Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
                    int k1 = (int)(f1 * 19.0F);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    //this.drawTexturedModalRect(j2, i2, 0, 94, 18, 18);
                    //this.drawTexturedModalRect(j2, i2 + 18 - k1, 18, 112 - k1, 18, k1);
                }
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
	}
	
	protected static void renderHotbarItem(int p_184044_1_, int p_184044_2_, float p_184044_3_, EntityPlayer player, ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            float f = (float)stack.getAnimationsToGo() - p_184044_3_;

            if (f > 0.0F)
            {
                GlStateManager.pushMatrix();
                float f1 = 1.0F + f / 5.0F;
                GlStateManager.translate((float)(p_184044_1_ + 8), (float)(p_184044_2_ + 12), 0.0F);
                GlStateManager.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float)(-(p_184044_1_ + 8)), (float)(-(p_184044_2_ + 12)), 0.0F);
            }

            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(player, stack, p_184044_1_, p_184044_2_);

            if (f > 0.0F)
            {
                GlStateManager.popMatrix();
            }

            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRenderer, stack, p_184044_1_, p_184044_2_);
        }
    }
}
