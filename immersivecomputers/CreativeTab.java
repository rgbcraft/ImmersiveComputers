package immersivecomputers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import immersivecomputers.blocks.Blocks;

public class CreativeTab extends CreativeTabs {

    public String name;
    public static CreativeTabs tabImmersiveComputers = new CreativeTab(CreativeTabs.getNextID(), "ImmersiveComputers");

    public CreativeTab(int par1, String par2Str) {
        super(par1, par2Str);

        this.name = par2Str;
    }

    @Override
    public String toString() {
        return super.getTabLabel();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getIconItemStack() {
        return new ItemStack(Blocks.blockComputer);
    }

    @Override
    public String getTranslatedTabLabel() {
        return this.name;
    }
}
