package v0id.api.exp.entity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import v0id.api.exp.tile.crop.IExPCrop;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 */
public class ExPAnimalCapability
{
    @CapabilityInject(IExPCrop.class)
    public static final Capability<IAnimal> animalCap = null;
    public static final ResourceLocation KEY = new ResourceLocation("exp", "animaldata");

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IAnimal.class, new Capability.IStorage<IAnimal>()
        {
            @Override
            public NBTBase writeNBT(Capability<IAnimal> capability, IAnimal instance, EnumFacing side)
            {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<IAnimal> capability, IAnimal instance, EnumFacing side, NBTBase nbt)
            {
                assert nbt instanceof NBTTagCompound : "Animal data can only be loaded from NBTTagCompound!";
                instance.deserializeNBT((NBTTagCompound) nbt);
            }

        }, () -> (IAnimal)Class.forName("v0id.exp.entity.ExPAnimal").getDeclaredMethod("createDefault").invoke(null));
    }
}
