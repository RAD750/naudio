package naudio;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import naudio.blocks.Blocks;
import naudio.config.ConfigHandler;
import naudio.handler.NCraftingHandler;
import naudio.network.PacketHandler;
import naudio.proxies.CommonProxy;
import naudio.tileentities.TileEntities;

@Mod(modid = "NAudio_Enn3DevPlayer", name = "NAudio Mod", version = "1.0")
@NetworkMod(channels = {"naudio"}, clientSideRequired = true, serverSideRequired = true, packetHandler = PacketHandler.class)
public class NAudio {
    @Mod.Instance(value = "NAudio_Enn3DevPlayer")
    public static NAudio instance;
    @SidedProxy(clientSide = "naudio.proxies.ClientProxy", serverSide = "naudio.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        Blocks.initBlocks();
        Blocks.registerBlocks();
        proxy.initSounds();
        proxy.initRenderers();
        proxy.initKeys();
        proxy.initItems();
        TileEntities.registerTileEntities();
    }
    
    @Init
    public void init (FMLInitializationEvent event) {
        proxy.init(event);
        Blocks.registerLanguages();
        NCraftingHandler.addCraftings();
    }

    @Mod.Init
    public void load(FMLInitializationEvent event) {
        proxy.loadKeys();
        proxy.loadItems();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event) {
    }
}
