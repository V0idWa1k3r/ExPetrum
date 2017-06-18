package v0id.api.exp.tile.crop;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ExPFarmlandCapability
{
	@CapabilityInject(IExPCrop.class)
	public static final Capability<IFarmland> farmlandCap = null;
	public static final ResourceLocation KEY = new ResourceLocation("exp", "farmlanddata");
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IFarmland.class, new IStorage<IFarmland>()
		{
			@Override
			public NBTBase writeNBT(Capability<IFarmland> capability, IFarmland instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IFarmland> capability, IFarmland instance, EnumFacing side, NBTBase nbt)
			{
				assert nbt instanceof NBTTagCompound : "Farmland data can only be loaded from NBTTagCompound!";
				instance.deserializeNBT((NBTTagCompound) nbt);
			}
			
		}, () -> (IFarmland)Class.forName("v0id.exp.crop.ExPFarmland").getDeclaredMethod("createDefault").invoke(null));
	}
}
