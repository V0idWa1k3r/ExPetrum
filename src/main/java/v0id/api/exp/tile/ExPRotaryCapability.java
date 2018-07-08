package v0id.api.exp.tile;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ExPRotaryCapability
{
    @CapabilityInject(IRotaryHandler.class)
    public static final Capability<IRotaryHandler> cap = null;
    public static final ResourceLocation KEY = new ResourceLocation("exp", "rotaryhandler");

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IRotaryHandler.class, new Capability.IStorage<IRotaryHandler>()
        {
            @Override
            public NBTBase writeNBT(Capability<IRotaryHandler> capability, IRotaryHandler instance, EnumFacing side)
            {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IRotaryHandler> capability, IRotaryHandler instance, EnumFacing side, NBTBase nbt)
            {
                assert nbt instanceof NBTTagCompound : "Rotary data can only be loaded from NBTTagCompound!";
                instance.deserializeNBT((NBTTagCompound) nbt);
            }

        }, () -> (IRotaryHandler)Class.forName("v0id.exp.util.RotaryHandler").getDeclaredMethod("createDefault").invoke(null));
    }
}
