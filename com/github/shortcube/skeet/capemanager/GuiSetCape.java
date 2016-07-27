package com.github.shortcube.skeet.capemanager;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.github.shortcube.skeet.Skeet;
import com.github.shortcube.skeet.altmanager.AltLoginThread;
import com.github.shortcube.skeet.altmanager.GuiPasswordField;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public final class GuiSetCape extends GuiScreen
{
  private final GuiScreen previousScreen;
  private AltLoginThread thread;
  private GuiTextField username;

  public GuiSetCape(GuiScreen previousScreen)
  {
    this.previousScreen = previousScreen;
  }

  protected void actionPerformed(GuiButton button) {
    switch (button.id) {
    case 1:
      this.mc.displayGuiScreen(this.previousScreen);
      break;
    case 0:
    	if(Minecraft.getMinecraft().thePlayer != null)Skeet.getInstance().updatePlayer(username.getText());
    }
  }

  public void drawScreen(int x, int y, float z)
  {
    this.drawDefaultBackground();

    drawCenteredString(Minecraft.fontRendererObj, "Set Cape", width / 2 - 10, 20, -1);

    this.username.drawTextBox();
    if (this.username.getText().isEmpty()) {
      drawString(Minecraft.fontRendererObj, "URL", width / 2 - 96, 66, -7829368);
    }
    super.drawScreen(x, y, z);
  }

  public void initGui() {
    int var3 = height / 4 + 24;
    this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 42 + 20, "Set"));
    this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 42 + 20 + 24, "Back"));
    this.username = new GuiTextField(var3, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
    this.username.setFocused(true);
    this.username.setMaxStringLength(1000000000);
    Keyboard.enableRepeatEvents(true);
  }

  protected void keyTyped(char character, int key)
  {
	  
    try
    {
      super.keyTyped(character, key);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (character == '\t') {
      if ((!this.username.isFocused())) {
        this.username.setFocused(true);
      } else {
        this.username.setFocused(true);
      }
    }
    if (character == '\r') {
      actionPerformed((GuiButton)this.buttonList.get(0));
    }
    this.username.textboxKeyTyped(character, key);
  }

  protected void mouseClicked(int x, int y, int button) {
    try {
      super.mouseClicked(x, y, button);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.username.mouseClicked(x, y, button);
  }

  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  public void updateScreen() {
    this.username.updateCursorCounter();
  }
}