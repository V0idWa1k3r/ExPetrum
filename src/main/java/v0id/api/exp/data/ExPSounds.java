package v0id.api.exp.data;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import v0id.core.markers.StaticStorage;

@StaticStorage
@GameRegistry.ObjectHolder(ExPRegistryNames.modid)
public class ExPSounds
{
    @GameRegistry.ObjectHolder(ExPRegistryNames.soundNewAge)
    public static final SoundEvent newAge = null;
}
