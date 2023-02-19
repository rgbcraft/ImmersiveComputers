package immersivecomputers.blocks;

import immersivecomputers.CreativeTab;
import immersivecomputers.entities.TileEntityComputer;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockComputer extends BlockContainer {
    public BlockComputer(int id, Material material) {
        super(id, material);

        setCreativeTab(CreativeTab.tabImmersiveComputers);
        setBlockName("blockComputer");
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntityComputer();
    }

    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        if (!par1World.isRemote) {
            ((TileEntityComputer) par1World.getBlockTileEntity(par2, par3, par4)).boot();
        }

        return true;
    }

    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
        if (!par1World.isRemote) {
            ((TileEntityComputer) par1World.getBlockTileEntity(par2, par3, par4)).cpu.halted = true;
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
}
