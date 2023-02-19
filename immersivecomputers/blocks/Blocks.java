package immersivecomputers.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import railcraft.common.api.core.items.ItemRegistry;
import net.minecraft.item.ItemStack;

public class Blocks {
    public static Block blockComputer, blockConsole, blockCable, blockSwitch;

    public static void init() {
        blockComputer = new BlockComputer(1300, Material.iron);
        blockConsole = new BlockConsole(1301, Material.iron);
        blockCable = new BlockConsole(1302, Material.iron);
        blockSwitch = new BlockConsole(1303, Material.iron);
    }

    public static void registerBlocks() throws ClassNotFoundException {
        GameRegistry.registerBlock(blockComputer, "blockComputer");
        GameRegistry.registerBlock(blockConsole, "blockConsole");
        GameRegistry.registerBlock(blockCable, "blockCable");
        GameRegistry.registerBlock(blockSwitch, "blockSwitch");
        LanguageRegistry.addName(blockCable, "Bus Cable");
        LanguageRegistry.addName(blockSwitch, "Bus Switch");
        LanguageRegistry.addName(blockComputer, "Computer");
        LanguageRegistry.addName(blockConsole, "Console");
    }
}
