package naudio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import naudio.blocks.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {

    private final String name;
    public static CreativeTabs tabN = new CreativeTab(CreativeTabs.getNextID(), "N");

    public CreativeTab(int par1, String par2Str) {
        super(par1, par2Str);
        this.name = par2Str;
    }

    @Override
    public String toString() {
        return super.getTabLabel();
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        if (this.name.equals(tabN.getTabLabel())) {
            return new ItemStack(Blocks.nAudioBlock);
        }
        return null;
    }


    @Override
    public String getTranslatedTabLabel() {
        return this.name;
    }

}
