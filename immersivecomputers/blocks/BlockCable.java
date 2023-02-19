package immersivecomputers.blocks;

import immersivecomputers.CreativeTab;
import immersivecomputers.entities.TileEntityCable;
import immersivecomputers.entities.TileEntityConsole;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCable extends BlockContainer {
    protected BlockCable(int par1, Material par2Material) {
        super(par1, par2Material);

        setCreativeTab(CreativeTab.tabImmersiveComputers);
        setBlockName("blockCable");
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntityCable();
    }
}
