package net.minecraft.src.forgetten_sunbeams.text;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import org.lwjgl.input.Keyboard;

public class TextFieldEditor {
   private final ITextField textField;
   private int cursor;

   public static String substring(String s, int start, int end) {
      if (start < 0) {
         start = 0;
      }

      if (end > s.length()) {
         end = s.length();
      }

      return start > end ? "" : s.substring(start, end);
   }

   public static String substring(String s, int start) {
      return substring(s, start, s.length());
   }

   public TextFieldEditor(ITextField textField) {
      this.textField = textField;
      this.cursor = textField.getText().length();
   }

   public int getCursor() {
      if (this.cursor < 0) {
         this.setCursor(0);
      }

      if (this.cursor > this.textField.getText().length()) {
         this.setCursor(this.textField.getText().length());
      }

      return this.cursor;
   }

   public String getText() {
      return this.textField.getText();
   }

   public void setText(String s) {
      if (s.length() > this.textField.maxLength()) {
         s = substring(s, 0, this.textField.maxLength());
      }

      this.textField.setText(s);
   }

   public boolean handleInput(int key, char c) {
      if (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) {
         if (key == 46) {
            this.stringToClipboard(this.getText());
            return false;
         }

         if (key == 47) {
            String pasteString = this.clipboardToString();
            if (pasteString != null) {
               this.addStringAtCursor(pasteString);
            }

            return true;
         }

         if (key == 203) {
            this.setCursor(this.findNextAnchor(-1));
            return false;
         }

         if (key == 205) {
            this.setCursor(this.findNextAnchor(1));
            return false;
         }

         if (key == 14) {
            int next = this.findNextAnchor(-1);
            int d = this.cursor - next;
            this.deleteCharsBeforeCursor(d);
            return true;
         }

         if (key == 211) {
            int next = this.findNextAnchor(1);
            int d = next - this.cursor;
            this.deleteCharsAfterCursor(d);
            return true;
         }
      }

      if (key == 199) {
         this.setCursor(0);
         return false;
      } else if (key == 207) {
         this.setCursor(this.getText().length());
         return false;
      } else if (key == 203) {
         this.moveCursor(-1);
         return false;
      } else if (key == 205) {
         this.moveCursor(1);
         return false;
      } else if (key == 14) {
         this.deleteCharsBeforeCursor(1);
         return true;
      } else if (key == 211) {
         this.deleteCharsAfterCursor(1);
         return true;
      } else {
         this.addCharAtCursor(c);
         return true;
      }
   }

   public void moveCursor(int i) {
      this.setCursor(this.cursor + i);
   }

   public void setCursor(int i) {
      if (i <= 0) {
         i = 0;
      }

      if (i > this.getText().length()) {
         i = this.getText().length();
      }

      this.cursor = i;
   }

   public void addCharAtCursor(char c) {
      String currentText = this.getText();
      int remaining = this.textField.maxLength() - currentText.length();
      if (remaining != 0) {
         if (this.textField.isCharacterAllowed(c)) {
            if (this.cursor > currentText.length()) {
               this.setCursor(currentText.length());
            }

            if (this.cursor < 0) {
               this.setCursor(0);
            }

            if (currentText.length() == 0) {
               this.setText(String.valueOf(c));
            } else if (this.cursor == currentText.length()) {
               this.setText(currentText + c);
            } else {
               String before = substring(currentText, 0, this.cursor);
               String after = substring(currentText, this.cursor);
               this.setText(before + c + after);
            }

            ++this.cursor;
         }
      }
   }

   public void addStringAtCursor(String s) {
      for(char c : s.toCharArray()) {
         this.addCharAtCursor(c);
      }

   }

   public void deleteCharsBeforeCursor(int i) {
      if (this.cursor < 0) {
         this.cursor = 0;
      }

      String before;
      if (i > this.cursor) {
         before = "";
      } else {
         before = substring(this.getText(), 0, this.cursor - i);
      }

      String after = substring(this.getText(), this.cursor);
      this.setText(before + after);
      if (!after.isEmpty()) {
         this.setCursor(this.getCursor() - 1);
      }

   }

   public void deleteCharsAfterCursor(int i) {
      if (this.cursor != this.getText().length()) {
         String after;
         if (this.cursor + i > this.getText().length()) {
            after = "";
         } else {
            after = substring(this.getText(), this.cursor + i);
         }

         String before = substring(this.getText(), 0, this.cursor);
         this.setText(before + after);
      }
   }

   public int findNextAnchor(int direction) {
      if (direction == 0) {
         return this.cursor;
      } else {
         if (direction < 0) {
            direction = -1;
         }

         if (direction > 0) {
            direction = 1;
         }

         String text = this.getText();
         boolean isWhitespace;
         if (direction < 0) {
            int i = this.cursor - 1;
            if (this.cursor == 0) {
               return this.cursor;
            }

            if (this.cursor >= text.length()) {
               i = text.length() - 1;
            }

            char c = text.charAt(i);
            isWhitespace = Character.isWhitespace(c);
         } else {
            if (this.cursor >= text.length()) {
               return this.cursor;
            }

            if (this.cursor == text.length() - 1) {
               return text.length();
            }

            char c = text.charAt(this.cursor);
            isWhitespace = Character.isWhitespace(c);
         }

         int i;
         if (direction < 0) {
            int j = this.cursor - 1;
            if (this.cursor >= text.length()) {
               j = text.length() - 1;
            }

            for(i = j; i > 0; --i) {
               char c = text.charAt(i);
               if (isWhitespace && !Character.isWhitespace(c) || !isWhitespace && Character.isWhitespace(c)) {
                  ++i;
                  break;
               }
            }
         } else {
            for(i = this.cursor; i < text.length(); ++i) {
               char c = text.charAt(i);
               if (isWhitespace && !Character.isWhitespace(c) || !isWhitespace && Character.isWhitespace(c)) {
                  break;
               }
            }
         }

         return i;
      }
   }

   private void stringToClipboard(String string) {
      try {
         StringSelection selection = new StringSelection(string);
         Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         clipboard.setContents(selection, selection);
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private String clipboardToString() {
      try {
         Transferable trans = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object)null);
         if (trans != null && trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String)trans.getTransferData(DataFlavor.stringFlavor);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }
}