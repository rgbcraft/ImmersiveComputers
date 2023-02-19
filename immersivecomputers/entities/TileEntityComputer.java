package immersivecomputers.entities;

import immersivecomputers.computer.Cpu;
import net.minecraft.tileentity.TileEntity;

public class TileEntityComputer extends TileEntity {
    public Cpu cpu;

    public TileEntityComputer() {
        cpu = new Cpu();
    }

    public void boot() {
        if (cpu.halted) {
            this.cpu.boot();
        }
    }

}
