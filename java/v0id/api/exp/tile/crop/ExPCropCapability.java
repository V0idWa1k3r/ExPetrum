package v0id.api.exp.tile.crop;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ExPCropCapability
{
	@CapabilityInject(IExPCrop.class)
	public static Capability<IExPCrop> cropCap = null;
	public static final ResourceLocation KEY = new ResourceLocation("exp", "cropdata");
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IExPCrop.class, new IStorage<IExPCrop>()
		{
			@Override
			public NBTBase writeNBT(Capability<IExPCrop> capability, IExPCrop instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IExPCrop> capability, IExPCrop instance, EnumFacing side, NBTBase nbt)
			{
				assert nbt instanceof NBTTagCompound : "Crop data can only be loaded from NBTTagCompound!";
				instance.deserializeNBT((NBTTagCompound) nbt);
			}
			
		}, () -> (IExPCrop)Class.forName("v0id.exp.crop.ExPCrop").getDeclaredMethod("createDefault").invoke(null));
	}
}
