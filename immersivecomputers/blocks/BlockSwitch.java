package immersivecomputers.blocks;

import immersivecomputers.CreativeTab;
import immersivecomputers.entities.TileEntitySwitch;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSwitch extends BlockContainer {
    protected BlockSwitch(int par1, Material par2Material) {
        super(par1, par2Material);

        setCreativeTab(CreativeTab.tabImmersiveComputers);
        setBlockName("blockSwitch");
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntitySwitch();
    }
}
