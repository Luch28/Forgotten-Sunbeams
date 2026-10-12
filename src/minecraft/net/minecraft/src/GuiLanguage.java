package net.minecraft.src;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.awt.Desktop;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.Minecraft;

public class GuiLanguage extends GuiScreen {
    protected GuiScreen parentScreen;
    private GuiLanguageSlot languageList;
    private List<String> langFiles = new ArrayList<String>();
    private int selectedIndex = -1;
    private File langDir;
    
    public GuiLanguage(GuiScreen guiscreen, GameSettings gamesettings) {
        parentScreen = guiscreen;
    }

    public GuiLanguage(GuiScreen parent) {
        this.parentScreen = parent;
        this.langDir = new File(Minecraft.getMinecraftDir(), "lang");
        initLangDirectory();
        findLanguageFiles();
    }

    private void initLangDirectory() {
        try {
            if (!langDir.exists()) {
                langDir.mkdirs();
            }

            URL jarUrl = GuiLanguage.class.getProtectionDomain().getCodeSource().getLocation();
            File jarFile = new File(URLDecoder.decode(jarUrl.getFile(), "UTF-8"));

            if (jarFile.isFile() && jarFile.getName().endsWith(".jar")) {
                ZipFile zip = new ZipFile(jarFile);
                Enumeration<? extends ZipEntry> entries = zip.entries();
                
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String name = entry.getName();
                    
                    if ((name.startsWith("lang/") || name.startsWith("/lang/")) && !entry.isDirectory()) {
                        if (name.endsWith(".properties") || name.endsWith(".lang")) {
                            File destFile = new File(langDir, name.substring(name.lastIndexOf('/') + 1));
                            
                            if (!destFile.exists()) {
                                InputStream is = zip.getInputStream(entry);
                                FileOutputStream fos = new FileOutputStream(destFile);
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = is.read(buffer)) != -1) {
                                    fos.write(buffer, 0, bytesRead);
                                }
                                fos.close();
                                is.close();
                            }
                        }
                    }
                }
                zip.close();
            }

            File defaultLangFile = new File(langDir, "en_US.lang");
            if (!defaultLangFile.exists() && !new File(langDir, "en_US.properties").exists()) {
                PrintWriter writer = new PrintWriter(new FileWriter(defaultLangFile));
                writer.println("# Place your translations here");
                writer.println("gui.done=Done");
                writer.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findLanguageFiles() {
        try {
            if (langDir.exists() && langDir.isDirectory()) {
                for (File file : langDir.listFiles()) {
                    String fileName = file.getName();
                    // ПРОВЕРКА: Показываем только файлы, содержащие "_" и имеющие расширение .lang/.properties
                    if (fileName.contains("_") && 
                       (fileName.endsWith(".properties") || fileName.endsWith(".lang"))) {
                        langFiles.add(fileName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (langFiles.isEmpty()) {
            langFiles.add("en_US.lang");
        }
        Collections.sort(langFiles);
    }

    public void initGui() {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        this.languageList = new GuiLanguageSlot(this);
        controlList.add(new GuiButton(101, width / 2 - 184, height / 72 + 12, 90, 20, stringtranslate.translateKey("options.video")));
        controlList.add(new GuiButton(100, width / 2 - 92, height / 72 + 12, 90, 20, stringtranslate.translateKey("options.controls")));
        controlList.add(new GuiButton(300, width / 2 + 2, height / 72 + 12, 90, 20, "Language"));
        controlList.add(new GuiButton(0, width / 2 + 94, height / 72 + 12, 90, 20, stringtranslate.translateKey("menu.options")));
        this.controlList.add(new GuiButton(200, this.width / 2 - 0, this.height - 38, 100, 20, stringtranslate.translateKey("gui.done")));
        this.controlList.add(new GuiButton(7, this.width / 2 - 105, this.height - 38, 100, 20, "Open lang"));
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 101) {
                mc.gameSettings.saveOptions();
                mc.displayGuiScreen(new GuiVideoSettings(this, this.mc.gameSettings));
            }
            else if (guibutton.id == 100) {
                mc.gameSettings.saveOptions();
                mc.displayGuiScreen(new GuiControls(this, this.mc.gameSettings));
            }
            else if (guibutton.id == 200) {
            	mc.gameSettings.saveOptions();
                mc.displayGuiScreen(new GuiMainMenu());
            }
            else if (guibutton.id == 7) {
                try {
                    Desktop.getDesktop().open(this.langDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(guibutton.id == 0) {
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
            }
        }
    }

    public void selectLanguage(int index) {
        this.selectedIndex = index;
        String selectedFile = langFiles.get(index);
        try {
            StringTranslate stringtranslate = StringTranslate.getInstance();
            java.lang.reflect.Field field;
            try {
                field = StringTranslate.class.getDeclaredField("translateTable");
            } catch (NoSuchFieldException e) {
                field = StringTranslate.class.getDeclaredField("b");
            }
            field.setAccessible(true);
            Properties translateTable = (Properties) field.get(stringtranslate);
            
            // ВАЖНО: Мы НЕ очищаем таблицу (clear), чтобы сохранить ванильные тексты игры!
            // translateTable.clear(); 

            File fileToLoad = new File(langDir, selectedFile);
            if (fileToLoad.exists()) {
                InputStream is = new FileInputStream(fileToLoad);
                translateTable.load(new InputStreamReader(is, Charset.forName("UTF-8")));
                is.close();
            }
            
            // Перерисовываем интерфейс, чтобы кнопки обновились на выбранном языке
            this.initGui();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawScreen(int i, int j, float f) {
        this.languageList.drawScreen(i, j, f);
        drawGradientRect(0, 0, width, 38, 0xCC101010, 0xCC101010);
        drawGradientRect(0, height - 48, width, height, 0xCC101010, 0xCC101010);
        this.drawCenteredString(this.fontRenderer, "Select Language", this.width / 2, 20, 16777215);
        super.drawScreen(i, j, f);
    }

    class GuiLanguageSlot extends GuiSlot {
        final GuiLanguage parent;

        public GuiLanguageSlot(GuiLanguage parent) {
            super(parent.mc, parent.width, parent.height, 32, parent.height - 48, 24);
            this.parent = parent;
        }

        protected int getSize() {
            return parent.langFiles.size();
        }

        protected void elementClicked(int i, boolean flag) {
            parent.selectLanguage(i);
        }

        protected boolean isSelected(int i) {
            return i == parent.selectedIndex;
        }

        protected void drawBackground() {
            parent.drawDefaultBackground();
        }

        protected int getContentHeight() {
            return this.getSize() * 24;
        }

        protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator) {
            String name = parent.langFiles.get(i).replace(".properties", "").replace(".lang", "");
            parent.drawCenteredString(parent.fontRenderer, name, parent.width / 2, k + 2, 16777215);
        }
        private GameSettings options;
    }
}