package immersivecomputers.entities;

import immersivecomputers.computer.io.Terminal;
import net.minecraft.tileentity.TileEntity;

public class TileEntityConsole extends TileEntity {
    public Terminal terminal;

    public TileEntityConsole() {
        terminal = new Terminal();
    }

}
