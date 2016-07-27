package com.github.shortcube.skeet.gui;

import java.awt.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map.Entry;

import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.opengl.Display;

import com.github.shortcube.skeet.Skeet;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldType;

public class GuiIngame extends net.minecraft.client.gui.GuiIngame {
	
	private static final Minecraft MINECRAFT = Minecraft.getMinecraft();

	public GuiIngame(Minecraft mcIn) {
		super(mcIn);
	}
	
	public void renderGameOverlay(float partialTicks) {
		int position = 0;
		super.renderGameOverlay(partialTicks);
		if(MINECRAFT.gameSettings.fps == true)
			renderFPS(position++);
		if(MINECRAFT.gameSettings.position == true) {
			renderCords(position++, position++, position++);
			renderPitch(position++);
		}
		if(MINECRAFT.gameSettings.clock == true) {
			Date date = new Date();
			renderClock(date, position++);
			renderDate(date, position++);
		}
		if(MINECRAFT.getCurrentServerData() != null) {
			if(MINECRAFT.gameSettings.server == true) {
				renderIP(position++);
				renderPing(position++);
			}
		}
		if(MINECRAFT.gameSettings.team == true) {
			if(MINECRAFT.thePlayer.getTeam() != null) 
				renderTeam(position++);
		}
		if(MINECRAFT.gameSettings.memory == true) 
			renderMem(position++);
		
		renderHUD(new ScaledResolution(MINECRAFT), partialTicks);
		renderCurrentItemInHand(new ScaledResolution(MINECRAFT), partialTicks);
	}

	private void renderMem(int count) {
		ArrayList<String> infos = getDebugInfoRight();
		drawString(MINECRAFT.fontRendererObj, "§7[§6Memory§7] §f" + infos.get(1), 2, count * 10 + 2, 0xFFFFFF);
	}

	private void renderTeam(int count) {
		drawString(MINECRAFT.fontRendererObj, "§7[§6Team§7] §f" + MINECRAFT.thePlayer.getTeam().getChatFormat().getValueByName(MINECRAFT.thePlayer.getTeam().getChatFormat().getFriendlyName()) + String.valueOf(MINECRAFT.thePlayer.getTeam().getRegisteredName()), 2, count * 10 + 2, 0xFFFFFF);
	}

	private void renderPitch(int count) {
		drawString(MINECRAFT.fontRendererObj, "§7[§6F§7] §f" + String.valueOf(Math.round(MINECRAFT.thePlayer.rotationPitch)), 2, count * 10 + 2, 0xFFFFFF);
	}

	private void renderPing(int count) {
		drawString(MINECRAFT.fontRendererObj, "§7[§6Ping§7] §f" + String.valueOf(MINECRAFT.getCurrentServerData().pingToServer), 2, count * 10 + 2, 0xFFFFFF);
	}

	private void renderIP(int count) {
		drawString(MINECRAFT.fontRendererObj, "§7[§6IP§7] §f" + String.valueOf(MINECRAFT.getCurrentServerData().serverIP), 2, count * 10 + 2, 0xFFFFFF);
	}

	private void renderFPS(int count) {
		drawString(MINECRAFT.fontRendererObj, "§7[§6FPS§7] §f" + String.valueOf(MINECRAFT.getDebugFPS()), 2, count * 10 + 2, 0xFFFFFF);
	}
	
	private void renderCords(int count, int count2, int count3) {
		drawString(MINECRAFT.fontRendererObj, "§7[§6X§7] §f" + String.valueOf(Math.round(MINECRAFT.thePlayer.posX)), 2, count * 10 + 2, 0xFFFFFF);
		drawString(MINECRAFT.fontRendererObj, "§7[§6Y§7] §f" + String.valueOf(Math.round(MINECRAFT.thePlayer.posY)), 2, count2 * 10 + 2, 0xFFFFFF);
		drawString(MINECRAFT.fontRendererObj, "§7[§6Z§7] §f" + String.valueOf(Math.round(MINECRAFT.thePlayer.posZ)), 2, count3 * 10 + 2, 0xFFFFFF);
	}
	
	DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yy");
	
	private void renderClock(Date date, int count) {
		drawString(MINECRAFT.fontRendererObj, "§7[§6Clock§7] §f" + String.valueOf(dateFormat.format(date)), 2, count * 10 + 2, 0xFFFFFF);
	}
	
	private void renderDate(Date date, int count) {
		drawString(MINECRAFT.fontRendererObj, "§7[§6Date§7] §f" + String.valueOf(dateFormat1.format(date)), 2, count * 10 + 2, 0xFFFFFF);
	}
	
	private void renderCurrentItemInHand(ScaledResolution sr, float partialTicks ) {
		int i = sr.getScaledWidth() / 2;
		int l1 = sr.getScaledHeight() - 16 - 3;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.enableGUIStandardItemLighting();
		func_184044_a(i - 91 - 26, l1, partialTicks, MINECRAFT.thePlayer, MINECRAFT.thePlayer.getHeldItemMainhand() );
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}
	
	private void renderHUD(ScaledResolution sr, float partialTicks) {
		renderHelmet(sr, partialTicks);
		renderChestPlate(sr, partialTicks);
		renderLeggins(sr, partialTicks);
		renderShoes(sr, partialTicks);
	}

	private void renderShoes(ScaledResolution sr, float partialTicks) {
		int i = sr.getScaledWidth() / 2;
		int l1 = sr.getScaledHeight() - 16 - 3;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.enableGUIStandardItemLighting();
		func_184044_a(i + 91 + 125, l1 - 185, partialTicks, MINECRAFT.thePlayer, MINECRAFT.thePlayer.getItemStackFromSlot(EntityEquipmentSlot.FEET) );
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}

	private void renderLeggins(ScaledResolution sr, float partialTicks) {
		int i = sr.getScaledWidth() / 2;
		int l1 = sr.getScaledHeight() - 16 - 3;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.enableGUIStandardItemLighting();
		func_184044_a(i + 91 + 125, l1 - 200, partialTicks, MINECRAFT.thePlayer, MINECRAFT.thePlayer.getItemStackFromSlot(EntityEquipmentSlot.LEGS) );
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}

	private void renderChestPlate(ScaledResolution sr, float partialTicks) {
		int i = sr.getScaledWidth() / 2;
		int l1 = sr.getScaledHeight() - 16 - 3;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.enableGUIStandardItemLighting();
		func_184044_a(i + 91 + 125, l1 - 215, partialTicks, MINECRAFT.thePlayer, MINECRAFT.thePlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST) );
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}

	private void renderHelmet(ScaledResolution sr, float partialTicks) {
		int i = sr.getScaledWidth() / 2;
		int l1 = sr.getScaledHeight() - 16 - 3;
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderHelper.enableGUIStandardItemLighting();
		func_184044_a(i + 91 + 125, l1 - 230, partialTicks, MINECRAFT.thePlayer, MINECRAFT.thePlayer.getItemStackFromSlot(EntityEquipmentSlot.HEAD) );
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}
	
	protected <T extends Comparable<T>> ArrayList<String> getDebugInfoRight()
    {
        long i = Runtime.getRuntime().maxMemory();
        long j = Runtime.getRuntime().totalMemory();
        long k = Runtime.getRuntime().freeMemory();
        long l = j - k;
        ArrayList<String> list = Lists.newArrayList(new String[] {String.format("Java: %s %dbit", new Object[]{System.getProperty("java.version"), Integer.valueOf(MINECRAFT.isJava64bit() ? 64 : 32)}), String.format("% 2d%% %03d/%03dMB", new Object[]{Long.valueOf(l * 100L / i), Long.valueOf(bytesToMb(l)), Long.valueOf(bytesToMb(i))}), String.format("Allocated: % 2d%% %03dMB", new Object[]{Long.valueOf(j * 100L / i), Long.valueOf(bytesToMb(j))}), "", String.format("CPU: %s", new Object[]{OpenGlHelper.getCpu()}), "", String.format("Display: %dx%d (%s)", new Object[]{Integer.valueOf(Display.getWidth()), Integer.valueOf(Display.getHeight()), GlStateManager.glGetString(7936)}), GlStateManager.glGetString(7937), GlStateManager.glGetString(7938)});

        if (this.isReducedDebug())
        {
            return list;
        }
        else
        {
            if (MINECRAFT.objectMouseOver != null && MINECRAFT.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && MINECRAFT.objectMouseOver.getBlockPos() != null)
            {
                BlockPos blockpos = MINECRAFT.objectMouseOver.getBlockPos();
                IBlockState iblockstate = MINECRAFT.theWorld.getBlockState(blockpos);

                if (MINECRAFT.theWorld.getWorldType() != WorldType.DEBUG_WORLD)
                {
                    iblockstate = iblockstate.getActualState(MINECRAFT.theWorld, blockpos);
                }

                list.add("");
                list.add(String.valueOf(Block.blockRegistry.getNameForObject(iblockstate.getBlock())));

                for (Entry < IProperty<?>, Comparable<? >> entry : iblockstate.getProperties().entrySet())
                {
                    IProperty<T> iproperty = (IProperty)entry.getKey();
                    T t = (T)entry.getValue();
                    String s = iproperty.getName(t);

                    if (t == Boolean.TRUE)
                    {
                        s = TextFormatting.GREEN + s;
                    }
                    else if (t == Boolean.FALSE)
                    {
                        s = TextFormatting.RED + s;
                    }

                    list.add(iproperty.getName() + ": " + s);
                }
            }

            return list;
        }
    }
	
	private static long bytesToMb(long bytes)
    {
        return bytes / 1024L / 1024L;
    }
	
	private boolean isReducedDebug()
    {
        return MINECRAFT.thePlayer.hasReducedDebug() || MINECRAFT.gameSettings.reducedDebugInfo;
    }

}
