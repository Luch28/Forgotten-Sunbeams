// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, EnumOptions, GuiSmallButton, 
//            GameSettings, GuiSlider, GuiButton, ScaledResolution

public class GuiVideoSettings extends GuiScreen
{

    public GuiVideoSettings(GuiScreen guiscreen, GameSettings gamesettings)
    {
        field_22107_a = "Video Settings";
        field_22110_h = guiscreen;
        guiGameSettings = gamesettings;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        field_22107_a = stringtranslate.translateKey("options.videoTitle");
        int i = 0;
        EnumOptions aenumoptions[] = field_22108_k;
        int j = aenumoptions.length;
        for(int k = 0; k < j; k++)
        {
            EnumOptions enumoptions = aenumoptions[k];
            if(!enumoptions.getEnumFloat())
            {
                controlList.add(new GuiSmallButton(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, guiGameSettings.getKeyBinding(enumoptions)));
            } else
            {
                controlList.add(new GuiSlider(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, guiGameSettings.getKeyBinding(enumoptions), guiGameSettings.getOptionFloatValue(enumoptions)));
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
            guiGameSettings.setOptionValue(((GuiSmallButton)guibutton).returnEnumOptions(), 1);
            guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
        }
        else if (guibutton.id == 101) {
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(new GuiVideoSettings(this, this.mc.gameSettings));
        }
        else if (guibutton.id == 100) {
            mc.gameSettings.saveOptions();
            mc.displayGuiScreen(new GuiControls(this, this.mc.gameSettings));
        }
        else if (guibutton.id == 200) {
        	mc.gameSettings.saveOptions();
        	mc.displayGuiScreen(null);
        }
        else if (guibutton.id == 300)
        {
            mc.displayGuiScreen(new GuiLanguage(this));
        }
        
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        setWorldAndResolution(mc, i, j);
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawGradientRect(0, 0, width, 38, 0xCC101010, 0xCC101010);
        drawGradientRect(0, height - 48, width, height, 0xCC101010, 0xCC101010);
        drawCenteredString(fontRenderer, null, width / 2, 20, 0xffffff);
        super.drawScreen(i, j, f);
    }

    private GuiScreen field_22110_h;
    protected String field_22107_a;
    private GameSettings guiGameSettings;
    private static EnumOptions field_22108_k[];

    static 
    {
        field_22108_k = (new EnumOptions[] {
            EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AMBIENT_OCCLUSION, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL
        });
    }
}
