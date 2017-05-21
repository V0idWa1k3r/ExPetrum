package v0id.api.exp.tile.crop;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ExPSeedsCapability
{
	@CapabilityInject(IExPSeed.class)
	public static Capability<IExPSeed> seedsCap = null;
	public static final ResourceLocation KEY = new ResourceLocation("exp", "seeddata");
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IExPSeed.class, new IStorage<IExPSeed>()
		{
			@Override
			public NBTBase writeNBT(Capability<IExPSeed> capability, IExPSeed instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IExPSeed> capability, IExPSeed instance, EnumFacing side, NBTBase nbt)
			{
				assert nbt instanceof NBTTagCompound : "Seed data can only be loaded from NBTTagCompound!";
				instance.deserializeNBT((NBTTagCompound) nbt);
			}
			
		}, () -> (IExPSeed)Class.forName("v0id.exp.item.ItemSeeds.CapabilityExPSeeds").newInstance());
	}
}
