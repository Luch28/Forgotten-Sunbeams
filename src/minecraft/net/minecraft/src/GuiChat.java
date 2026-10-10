package net.minecraft.client.gui;

import net.minecraft.src.forgetten_sunbeams.text.ITextField;
import net.minecraft.src.forgetten_sunbeams.text.TextFieldEditor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GuiChat extends GuiScreen implements ITextField {
   protected String message = "";
   private int currentIndex = 0;
   private String originalMessage;
   private int updateCounter = 0;
   private final TextFieldEditor editor = new TextFieldEditor(this);

   public GuiChat() {
      this.editor.setCursor(10000);
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.ingameGUI.chatScroll = 0;
   }

   public void updateScreen() {
      ++this.updateCounter;
      int dWheel = Mouse.getDWheel();
      if (dWheel != 0) {
         GuiIngame var10000 = this.mc.ingameGUI;
         var10000.chatScroll += dWheel / 100;
      }

   }

   public void keyTyped(char c, int key, int mouseX, int mouseY) {
      if (key == 200 && this.mc.thePlayer.messages.size() > 0) {
         if (this.currentIndex == 0) {
            this.originalMessage = this.getText();
         }

         if (this.currentIndex < this.mc.thePlayer.messages.size()) {
            ++this.currentIndex;
            this.setText(this.getMessage(this.currentIndex));
            this.editor.setCursor(10000);
         }

      } else {
         if (key == 208 && this.currentIndex > 0) {
            if (this.currentIndex - 1 == 0) {
               --this.currentIndex;
               this.setText(this.originalMessage);
               this.editor.setCursor(10000);
            } else {
               --this.currentIndex;
               this.setText(this.getMessage(this.currentIndex));
               this.editor.setCursor(10000);
            }
         }

         if (key == 1 && !(this instanceof GuiSleepMP)) {
            this.mc.displayGuiScreen((GuiScreen)null);
         } else if (key == 28) {
            String s = this.message.trim();
            if (s.length() > 0) {
               String s1 = this.message.trim();
               if (!this.mc.lineIsCommand(s1)) {
                  this.mc.thePlayer.sendChatMessage(s1);
               }
            }

            this.mc.displayGuiScreen((GuiScreen)null);
         } else {
            this.editor.handleInput(key, c);
         }
      }
   }

   public void drawScreen(int x, int y, float renderPartialTicks) {
      this.drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
      boolean drawCursor = this.updateCounter / 6 % 2 == 0;
      int cursor = this.editor.getCursor();
      this.drawString(this.fontRenderer, this.message, 4, this.height - 12, 14737632);
      if (drawCursor) {
         int width = this.fontRenderer.getStringWidth(this.message);
         if (cursor < this.message.length()) {
            width = this.fontRenderer.getStringWidth(this.message.substring(0, cursor));
         }

         this.drawString(this.fontRenderer, "_", 4 + width, this.height - 12, 14737632);
      }

      super.drawScreen(x, y, renderPartialTicks);
   }

   public void mouseClicked(int x, int y, int button) {
      if (button == 0) {
         if (this.mc.ingameGUI.field_933_a == null) {
            super.mouseClicked(x, y, button);
         } else {
            if (this.message.length() > 0 && !this.message.endsWith(" ")) {
               this.message = this.message + " ";
            }

            this.message = this.message + this.mc.ingameGUI.field_933_a;
            int byte0 = 256;
            if (this.message.length() > byte0) {
               this.message = this.message.substring(0, byte0);
            }

         }
      }
   }

   public void setText(String text) {
      this.message = text;
   }

   public String getText() {
      return this.message;
   }

   public int maxLength() {
      return 256;
   }

   private String getMessage(int currentIndex) {
      return (String)this.mc.thePlayer.messages.get(this.mc.thePlayer.messages.size() - currentIndex);
   }
}