package tweaker.tfcraft;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import minetweaker.MineTweakerAPI;
import tweaker.tfcraft.handlers.Anvil;
import tweaker.tfcraft.handlers.Barrel;
import tweaker.tfcraft.handlers.Heat;
import tweaker.tfcraft.handlers.Kiln;
import tweaker.tfcraft.handlers.Loom;
import tweaker.tfcraft.handlers.Quern;

@Mod(modid="terraFirmaTweaker", name = "TerraFirmaTweaker", version = "1.0.0")
public class TerraFirmaCraft {

	@EventHandler
	public void init(FMLInitializationEvent e){
		MineTweakerAPI.registerClass(Anvil.class);
		MineTweakerAPI.registerClass(Barrel.class);
		MineTweakerAPI.registerClass(Heat.class);
		MineTweakerAPI.registerClass(Kiln.class);
		MineTweakerAPI.registerClass(Loom.class);
		MineTweakerAPI.registerClass(Quern.class);
		
	}
}
