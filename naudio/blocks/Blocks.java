package naudio.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;

public class Blocks {
    public static Block nAudioBlock;

    public static void initBlocks() {
        nAudioBlock = new NAudioBlock(3299);
    }

    public static void registerBlocks() {
        GameRegistry.registerBlock(nAudioBlock, "nAudioBlock");
    }

    public static void registerLanguages() {
        LanguageRegistry.addName(nAudioBlock, "N Audio System");
    }
}
