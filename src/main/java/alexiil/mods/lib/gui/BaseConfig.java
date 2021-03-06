package alexiil.mods.lib.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import alexiil.mods.lib.AlexIILMod;
import alexiil.mods.lib.LangUtils;
import alexiil.mods.lib.git.GitHubUser;

public abstract class BaseConfig extends GuiScreen {
    private GitHubUserScrollingList contributors;
    private CommitScrollingList commits;
    public final AlexIILMod mod;
    private GuiButton helpClose;
    private boolean help = false;
    private int xPosHelp = 0;
    private List<List<String>> helpText;
    /** Used to determine the button positions across the top */
    protected int totalLength;

    public BaseConfig(GuiScreen screen, AlexIILMod mod) {
        fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
        this.mod = mod;
        setupGui();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        setupGui();
    }

    protected void setupGui() {
        int width = 0;
        for (GitHubUser usr : mod.getContributors())
            width = Math.max(width, fontRendererObj.getStringWidth(usr.login));

        contributors = new GitHubUserScrollingList(this, width + 40, this.height, 40, this.height - 40, 10);
        for (GitHubUser c : mod.getContributors())
            contributors.userList.add(c);

        commits = new CommitScrollingList(this, this.width - width - 80, this.height, 40, this.height - 40, width + 60);

        int index = 0;
        int maxXPos = Math.min(this.width - xPosHelp, 400);

        helpText = new ArrayList<List<String>>();
        while (true) {
            String preTranslation = "alexiillib.gui.config.help." + index;
            String text = LangUtils.format(preTranslation);
            if (preTranslation.equals(text))
                break;
            String[] strings1 = text.split("\n");
            for (int i = 0; i < strings1.length; i++) {
                String s = strings1[i];
                String nextLine = "";
                while (fontRendererObj.getStringWidth(s) > maxXPos && s != null) {
                    if (s.length() <= 10)
                        break;
                    nextLine = s.substring(s.length() - 1) + nextLine;
                    s = s.substring(0, s.length() - 1);
                }
                if (nextLine.length() > 0) {
                    strings1 = Arrays.copyOf(strings1, strings1.length + 1);
                    strings1[i] = s;
                    strings1[i + 1] = nextLine;
                }
            }
            String[] strings = strings1;
            helpText.add(Arrays.asList(strings));
            index++;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        if (mod.connectExternally.getBoolean()) {
            commits.drawScreen(mouseX, mouseY, partialTicks);
            contributors.drawScreen(mouseX, mouseY, partialTicks);
            drawString(fontRendererObj, LangUtils.format("alexiillib.gui.contributors"), 8, 30, 0xFFFFFF);
            String text = LangUtils.format("alexiillib.gui.commits");
            drawString(fontRendererObj, text, this.width - fontRendererObj.getStringWidth(text) - 10, 30, 0xFFFFFF);
        }
        else {
            String text = LangUtils.format("alexiillib.gui.connectExternallyDisabled");
            int textWidth = fontRendererObj.getStringWidth(text);
            drawHoveringText(Collections.singletonList(text), (this.width - textWidth) / 2, this.height / 2);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (help) {
            int yHeight = 0;
            for (List<String> ss : helpText)
                yHeight += fontRendererObj.FONT_HEIGHT * (ss.size() + 1);
            drawGradientRect(xPosHelp, 40, xPosHelp + 440, yHeight + 80, 0xFF000000, 0xFF000000);
            int yPos = 60;
            for (List<String> strings : helpText) {
                drawHoveringText(strings, xPosHelp, yPos);
                yPos += (strings.size() + 2) * fontRendererObj.FONT_HEIGHT;
            }
        }
    }

    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        String text = LangUtils.format("alexiillib.config.button");
        int length = fontRendererObj.getStringWidth(text) + 20;
        totalLength = 10;
        buttonList.add(new GuiButton(0, totalLength, 1, length, 20, text));
        totalLength += length;

        text = LangUtils.format("alexiillib.config.help");
        length = fontRendererObj.getStringWidth(text) + 20;
        buttonList.add(new GuiButton(1, totalLength, 1, length, 20, text));
        xPosHelp = totalLength;
        totalLength += length;

        text = LangUtils.format("alexiillib.config.closeHelp");
        length = fontRendererObj.getStringWidth(text) + 20;
        helpClose = new GuiButton(2, totalLength, 1, length, 20, text);
        helpClose.visible = false;
        buttonList.add(helpClose);
        totalLength += length;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(new ActualConfig(this, mod));
        }
        if (button.id == 1) {
            help = true;
            helpClose.visible = true;
        }
        if (button.id == 2 && helpClose.visible) {
            help = false;
            helpClose.visible = false;
        }
    }
}
