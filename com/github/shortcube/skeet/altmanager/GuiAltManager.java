package com.github.shortcube.skeet.altmanager;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public final class GuiAltManager extends GuiScreen
{
  private GuiPasswordField password;
  private final GuiScreen previousScreen;
  private AltLoginThread thread;
  private GuiTextField username;

  public GuiAltManager(GuiScreen previousScreen)
  {
    this.previousScreen = previousScreen;
  }

  protected void actionPerformed(GuiButton button) {
    switch (button.id) {
    case 1:
      this.mc.displayGuiScreen(this.previousScreen);
      break;
    case 0:
      this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
      this.thread.start();
    }
  }

  public void drawScreen(int x, int y, float z)
  {
    this.drawDefaultBackground();

    drawCenteredString(Minecraft.fontRendererObj, "Account Login", width / 2 - 10, 20, -1);
    drawCenteredString(Minecraft.fontRendererObj, this.thread == null ? "§7Waiting..." : this.thread.getStatus(), width / 2 - 9, 29, -1);

    this.username.drawTextBox();
    this.password.drawTextBox();
    if (this.username.getText().isEmpty()) {
      drawString(Minecraft.fontRendererObj, "E-Mail", width / 2 - 96, 66, -7829368);
    }
    if (this.password.getText().isEmpty()) {
      drawString(Minecraft.fontRendererObj, "Password", width / 2 - 96, 106, -7829368);
    }
    super.drawScreen(x, y, z);
  }

  public void initGui() {
    int var3 = height / 4 + 24;
    this.buttonList.add(new GuiButton(0, width / 2 - 100, var3 + 42 + 20, "Login"));
    this.buttonList.add(new GuiButton(1, width / 2 - 100, var3 + 42 + 20 + 24, "Back"));
    this.username = new GuiTextField(var3, Minecraft.fontRendererObj, width / 2 - 100, 60, 200, 20);
    this.password = new GuiPasswordField(Minecraft.fontRendererObj, width / 2 - 100, 100, 200, 20);
    this.username.setFocused(true);
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
      if ((!this.username.isFocused()) && (!this.password.isFocused())) {
        this.username.setFocused(true);
      } else {
        this.username.setFocused(this.password.isFocused());
        this.password.setFocused(!this.username.isFocused());
      }
    }
    if (character == '\r') {
      actionPerformed((GuiButton)this.buttonList.get(0));
    }
    this.username.textboxKeyTyped(character, key);
    this.password.textboxKeyTyped(character, key);
  }

  protected void mouseClicked(int x, int y, int button) {
    try {
      super.mouseClicked(x, y, button);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.username.mouseClicked(x, y, button);
    this.password.mouseClicked(x, y, button);
  }

  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  public void updateScreen() {
    this.username.updateCursorCounter();
    this.password.updateCursorCounter();
  }
}