package v0id.api.exp.world;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ExPWorldCapability
{
	@CapabilityInject(IExPWorld.class)
	public static Capability<IExPWorld> worldCap = null;
	public static final ResourceLocation KEY = new ResourceLocation("exp", "worlddata");
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IExPWorld.class, new IStorage<IExPWorld>()
		{
			@Override
			public NBTBase writeNBT(Capability<IExPWorld> capability, IExPWorld instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IExPWorld> capability, IExPWorld instance, EnumFacing side, NBTBase nbt)
			{
				assert nbt instanceof NBTTagCompound : "World data can only be loaded from NBTTagCompound!";
				instance.deserializeNBT((NBTTagCompound) nbt);
			}
			
		}, () -> (IExPWorld)Class.forName("v0id.exp.world.ExPWorld").getDeclaredMethod("createDefault").invoke(null));
	}
}
