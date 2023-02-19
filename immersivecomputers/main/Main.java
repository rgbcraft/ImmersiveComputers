package immersivecomputers.main;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import immersivecomputers.computer.thread.CpuExecutor;
import net.minecraft.src.BaseMod;
import immersivecomputers.blocks.Blocks;
import immersivecomputers.entities.Entities;

@Mod(name = "ImmersiveComputers", version = "1.0", modid = "immersivecomputers")
public class Main extends BaseMod {
    public static final String MODID = "immersivecomputers";
    public static final CpuExecutor EXECUTOR = new CpuExecutor();

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void load() {}

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
        Blocks.init();
        Entities.init();
    }

    @Mod.Init
    public void init(FMLInitializationEvent event) {
        EXECUTOR.init();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event) {
        try {
            Blocks.registerBlocks();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Mod.ServerStopping
    public void serverStopping(FMLServerStoppingEvent event) {
        EXECUTOR.shutdown();
    }
}
