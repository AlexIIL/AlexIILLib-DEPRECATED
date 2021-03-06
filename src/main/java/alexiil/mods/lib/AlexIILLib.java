package alexiil.mods.lib;

import java.io.File;
import java.text.DecimalFormat;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import alexiil.mods.lib.coremod.ChatTextTime;
import alexiil.mods.lib.coremod.RoamingIPAddress;
import alexiil.mods.lib.coremod.VanillaMethods;

@Mod(modid = Lib.Mod.ID, version = "0.1", guiFactory = "alexiil.mods.lib.gui.ConfigGuiFactory", useMetadata = true)
public class AlexIILLib extends AlexIILMod {
    public static final DecimalFormat df = new DecimalFormat();

    @Instance(Lib.Mod.ID)
    public static AlexIILLib instance;

    public static Property betterPotions, timeText, roamingIP, connectExternally;

    static {
        loadConfigs();
    }

    public static void loadConfigs() {
        Configuration cfg = new Configuration(new File("./config/" + Lib.Mod.ID + ".cfg"));
        cfg.load();
        betterPotions = cfg.get("general", "betterPotions", false);
        timeText = cfg.get("general", "textTime", false);
        roamingIP = cfg.get("general", "roamingIP", false);
        connectExternally = cfg.get("general", "connectExternally", true);
    }

    @Override
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        log.info("This is AlexIIL Lib, version " + meta.version);

        betterPotions = cfg.getProp("betterPotions", false);
        timeText = cfg.getProp("textTime", false);
        roamingIP = cfg.getProp("roamingIP", false);

        cfg.saveAll();

        event.getModConfigurationDirectory().mkdirs();

        VanillaMethods.init();
        ChatTextTime.init();
        RoamingIPAddress.init();
    }

    @Override
    public String getCommitHash() {
        return Lib.Mod.COMMIT_HASH;
    }

    @Override
    public int getBuildType() {
        return Lib.Mod.buildType();
    }

    @Override
    public String getUser() {
        return "AlexIIL";
    }

    @Override
    public String getRepo() {
        return "AlexIILLib";
    }
}
