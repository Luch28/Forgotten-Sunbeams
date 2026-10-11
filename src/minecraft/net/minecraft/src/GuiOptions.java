// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, EnumOptions, GuiSmallButton, 
//            GameSettings, GuiSlider, GuiButton, GuiVideoSettings, 
//            GuiControls

public class GuiOptions extends GuiScreen
{

    public GuiOptions(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Options";
        parentScreen = guiscreen;
        options = gamesettings;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        screenTitle = stringtranslate.translateKey("options.title");
        int i = 0;
        EnumOptions aenumoptions[] = field_22135_k;
        int j = aenumoptions.length;
        for(int k = 0; k < j; k++)
        {
            EnumOptions enumoptions = aenumoptions[k];
            if(!enumoptions.getEnumFloat())
            {
                controlList.add(new GuiSmallButton(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, options.getKeyBinding(enumoptions)));
            } else
            {
                controlList.add(new GuiSlider(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, options.getKeyBinding(enumoptions), options.getOptionFloatValue(enumoptions)));
            }
            i++;
        }

        controlList.add(new GuiButton(101, width / 2 - 184, height / 72 + 12, 90, 20, stringtranslate.translateKey("options.video")));
        controlList.add(new GuiButton(100, width / 2 - 92, height / 72 + 12, 90, 20, stringtranslate.translateKey("options.controls")));
        controlList.add(new GuiButton(300, width / 2 + 2, height / 72 + 12, 90, 20, "Language"));
        controlList.add(new GuiButton(0, width / 2 + 94, height / 72 + 12, 90, 20, stringtranslate.translateKey("menu.options")));
        controlList.add(new GuiButton(200, width / 2 - 100, height - 38, stringtranslate.translateKey("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if(!guibutton.enabled)
        {
            return;
        }
        if(guibutton.id < 100 && (guibutton instanceof GuiSmallButton))
        {
            options.setOptionValue(((GuiSmallButton)guibutton).returnEnumOptions(), 1);
            guibutton.displayString = options.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        }
        if (guibutton.id == 101) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiVideoSettings(this, this.mc.gameSettings));
        }
        else if (guibutton.id == 100) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(new GuiControls(this, this.mc.gameSettings));
        }
        else if(guibutton.id == 200)
        {
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(null);
        }
        else if (guibutton.id == 300)
        {
            mc.displayGuiScreen(new GuiLanguage(this));
        }
    }
    
    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawGradientRect(0, 0, width, 38, 0xCC101010, 0xCC101010);
        drawGradientRect(0, height - 48, width, height, 0xCC101010, 0xCC101010);
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
        super.drawScreen(i, j, f);
    }

    private GuiScreen parentScreen;
    protected String screenTitle;
    private GameSettings options;
    private static EnumOptions field_22135_k[];

    static 
    {
        field_22135_k = (new EnumOptions[] {
            EnumOptions.MUSIC, EnumOptions.SOUND, EnumOptions.INVERT_MOUSE, EnumOptions.SENSITIVITY, EnumOptions.DIFFICULTY
        });
    }
}
