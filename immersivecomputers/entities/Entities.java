package immersivecomputers.entities;

import cpw.mods.fml.common.registry.GameRegistry;

public class Entities {
    public static void init() {
        GameRegistry.registerTileEntity(TileEntityComputer.class, "tileEntityComputer");
        GameRegistry.registerTileEntity(TileEntityConsole.class, "tileEntityConsole");
    }
}
