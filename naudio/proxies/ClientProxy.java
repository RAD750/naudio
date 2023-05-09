package naudio.proxies;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import naudio.audio.NAudioManager;
import naudio.solid.rendertables.RenderTableNAudioBlock;
import naudio.tileentities.NAudioTE;

public class ClientProxy
        extends CommonProxy {
    @Override
    public void initSounds() {
        NAudioManager.init();
    }

    @Override
    public void initRenderers() {
    }

    @Override
    public void initKeys() {
    }

    @Override
    public void initItems() {
    }

    @Override
    public void loadKeys() {
    }

    @Override
    public void loadItems() {
    }
    @Init
    public void init(FMLInitializationEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(NAudioTE.class, new RenderTableNAudioBlock());
    }
}
