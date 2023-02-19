package immersivecomputers.entities;

import immersivecomputers.computer.Cable;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCable extends TileEntity {
    private Cable cable;

    public TileEntityCable() {

    }

    public void notifyNew(Cable cable) {
        this.cable.setNext(cable);
        cable.setPrevious(this.cable);
    }

    public void notifyBroken(Cable cable) {
        if (cable == this.cable.getPrevious()) {
            this.cable.setPrevious(null);
        } else {
            this.cable.setNext(null);
        }
    }

    public void breakCable() {
        this.cable.breakCable();
    }

}
