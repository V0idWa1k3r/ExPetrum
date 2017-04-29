package v0id.exp.handler;

import java.lang.reflect.Field;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderOverworld;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import v0id.api.core.util.MC;
import v0id.api.exp.player.ExPPlayerCapability;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.world.ExPWorldCapability;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.player.ExPPlayer;
import v0id.exp.world.ExPWorld;
import v0id.exp.world.gen.WorldTypeExP;

public class ExPHandlerServer
{
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent evt)
	{
		if (evt.world.hasCapability(ExPWorldCapability.worldCap, null) && evt.phase == Phase.END)
		{
			IExPWorld.of(evt.world).onTick();
		}
	}
	
	@SubscribeEvent
	public void onCapabilitiesWorld(AttachCapabilitiesEvent<World> evt)
	{
		handleWorldSettings(evt.getObject());
		evt.addCapability(ExPWorldCapability.KEY, this.createCapabilityProviderOfIExPWorld(evt.getObject()));
	}
	
	public void handleWorldSettings(World w)
	{
		try
		{
			if (w.getChunkProvider() instanceof ChunkProviderServer && w.getWorldType() instanceof WorldTypeExP)
			{
				ChunkProviderServer cps = (ChunkProviderServer) w.getChunkProvider();
				if (cps.chunkGenerator instanceof ChunkProviderOverworld)
				{
					ChunkProviderOverworld cpo = (ChunkProviderOverworld) cps.chunkGenerator;
					for (Field f : ChunkProviderOverworld.class.getDeclaredFields())
					{
						if (f.getType() == ChunkProviderSettings.class)
						{
							f.setAccessible(true);
							ChunkProviderSettings.Factory factory = new ChunkProviderSettings.Factory();
							factory.seaLevel = 127;
							factory.baseSize = 17.25f;
							f.set(cpo, factory.build());
							break;
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	

	@SubscribeEvent
	public void playerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.player.hasCapability(ExPPlayerCapability.playerCap, null) && event.phase == Phase.END)
		{
			IExPPlayer.of(event.player).onTick();
		}
	}
	
	@SubscribeEvent
	public void playerClone(PlayerEvent.Clone event)
	{
		if (!event.isWasDeath() || MC.getSide() == Side.CLIENT)
		{
			return;
		}
		
		IExPPlayer.of(event.getEntityPlayer()).deserializeNBT(IExPPlayer.of(event.getOriginal()).serializeNBT());
		((ExPPlayer)IExPPlayer.of(event.getEntityPlayer())).setAllDirty(true);
		IExPPlayer.of(event.getEntityPlayer()).sendNBT();
	}
	
	@SubscribeEvent
	public void attachCapabiltiesEvent(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof EntityPlayer)
		{
			event.addCapability(ExPPlayerCapability.KEY, createCapabilityProviderOfIExPPlayer((EntityPlayer) event.getObject()));
		}
	}

	public ICapabilitySerializable<NBTTagCompound> createCapabilityProviderOfIExPPlayer(EntityPlayer player)
	{
		return new ICapabilitySerializable<NBTTagCompound>(){
			final ExPPlayer defaultData = ExPPlayer.createDefaultWithOwner(player);
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing)
			{
				return capability.equals(ExPPlayerCapability.playerCap);
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing)
			{
				return capability.equals(ExPPlayerCapability.playerCap) ? ExPPlayerCapability.playerCap.cast(this.defaultData) : null;
			}

			@Override
			public NBTTagCompound serializeNBT()
			{
				return (NBTTagCompound) ExPPlayerCapability.playerCap.getStorage().writeNBT(ExPPlayerCapability.playerCap, this.defaultData, null);
			}

			@Override
			public void deserializeNBT(NBTTagCompound nbt)
			{
				ExPPlayerCapability.playerCap.getStorage().readNBT(ExPPlayerCapability.playerCap, this.defaultData, null, nbt);
			}
		};
	}
	
	public ICapabilitySerializable<NBTTagCompound> createCapabilityProviderOfIExPWorld(World w)
	{
		return new ICapabilitySerializable<NBTTagCompound>(){
			final ExPWorld defaultData = ExPWorld.createWithOwner(w);
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing)
			{
				return capability.equals(ExPWorldCapability.worldCap);
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing)
			{
				return capability.equals(ExPWorldCapability.worldCap) ? ExPWorldCapability.worldCap.cast(this.defaultData) : null;
			}

			@Override
			public NBTTagCompound serializeNBT()
			{
				return (NBTTagCompound) ExPWorldCapability.worldCap.getStorage().writeNBT(ExPWorldCapability.worldCap, this.defaultData, null);
			}

			@Override
			public void deserializeNBT(NBTTagCompound nbt)
			{
				ExPWorldCapability.worldCap.getStorage().readNBT(ExPWorldCapability.worldCap, this.defaultData, null, nbt);
			}
		};
	}
}
