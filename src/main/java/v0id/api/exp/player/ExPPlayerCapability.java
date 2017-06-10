package v0id.api.exp.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ExPPlayerCapability
{
	@CapabilityInject(IExPPlayer.class)
	public static Capability<IExPPlayer> playerCap = null;
	public static final ResourceLocation KEY = new ResourceLocation("exp", "playerdata");
	
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IExPPlayer.class, new IStorage<IExPPlayer>()
		{
			@Override
			public NBTBase writeNBT(Capability<IExPPlayer> capability, IExPPlayer instance, EnumFacing side)
			{
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<IExPPlayer> capability, IExPPlayer instance, EnumFacing side, NBTBase nbt)
			{
				assert nbt instanceof NBTTagCompound : "Player data can only be loaded from NBTTagCompound!";
				instance.deserializeNBT((NBTTagCompound) nbt);
			}
			
		}, () -> (IExPPlayer)Class.forName("v0id.exp.player.ExPPlayer").getDeclaredMethod("createDefault").invoke(null));
	}
}
