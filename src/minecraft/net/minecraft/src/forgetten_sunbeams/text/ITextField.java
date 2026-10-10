package net.minecraft.src.forgetten_sunbeams.text;

import net.minecraft.src.forgetten_sunbeams.util.ChatAllowedCharacters;

public interface ITextField {
   void setText(String var1);

   String getText();

   int maxLength();

   default boolean isCharacterAllowed(char c) {
      return ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(c) >= 0 || Character.isLetterOrDigit(c);
   }
}