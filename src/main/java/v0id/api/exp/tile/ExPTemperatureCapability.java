package v0id.api.exp.tile;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ExPTemperatureCapability
{
    @CapabilityInject(ITemperatureHandler.class)
    public static final Capability<ITemperatureHandler> cap = null;
    public static final ResourceLocation KEY = new ResourceLocation("exp", "temperaturehandler");

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ITemperatureHandler.class, new Capability.IStorage<ITemperatureHandler>()
        {
            @Override
            public NBTBase writeNBT(Capability<ITemperatureHandler> capability, ITemperatureHandler instance, EnumFacing side)
            {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<ITemperatureHandler> capability, ITemperatureHandler instance, EnumFacing side, NBTBase nbt)
            {
                assert nbt instanceof NBTTagCompound : "Temperature data can only be loaded from NBTTagCompound!";
                instance.deserializeNBT((NBTTagCompound) nbt);
            }

        }, () -> (ITemperatureHandler)Class.forName("v0id.exp.util.temperature.TemperatureHandler").getDeclaredMethod("createDefault").invoke(null));
    }
}
