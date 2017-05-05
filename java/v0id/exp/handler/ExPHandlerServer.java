package v0id.exp.handler;

import java.lang.reflect.Field;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.ChunkProviderOverworld;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import v0id.api.core.logging.LogLevel;
import v0id.api.core.util.MC;
import v0id.api.exp.block.IWater;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.player.ExPPlayerCapability;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.world.ExPWorldCapability;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.player.ExPPlayer;
import v0id.exp.player.PlayerManager;
import v0id.exp.util.WeatherUtils;
import v0id.exp.world.ExPWorld;
import v0id.exp.world.gen.WorldTypeExP;

public class ExPHandlerServer
{
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent.RightClickBlock event)
	{
		if (!event.getWorld().isRemote)
		{
			EntityPlayer player = event.getEntityPlayer();
			IExPPlayer data = IExPPlayer.of(player);
			if (data.getThirst() < data.getMaxThirst(true) - 200)
			{
				RayTraceResult rtr = event.getWorld().rayTraceBlocks(player.getPositionEyes(1), player.getPositionEyes(1).add(player.getLook(1).scale(3)), true, false, false);
				if (rtr != null && rtr.typeOfHit == Type.BLOCK)
				{
					BlockPos pos = rtr.getBlockPos();
					IBlockState hit = event.getWorld().getBlockState(pos);
					if (hit.getBlock() instanceof IWater && !((IWater)hit.getBlock()).isSalt(event.getWorld(), pos))
					{
						int level = hit.getValue(BlockFluidBase.LEVEL);
						if (level > 0)
						{
							event.getWorld().setBlockState(pos, hit.withProperty(BlockFluidBase.LEVEL, level - 1));
						}
						else
						{
							event.getWorld().setBlockToAir(pos);
						}
						
						data.setThirst(data.getThirst() + 200, true);
						event.getWorld().playSound(null, pos, SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 1, 1);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onItemUseFinish(LivingEntityUseItemEvent.Tick event)
	{
		if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote)
		{
			int useCurrent = event.getDuration();
			if (useCurrent == 1)
			{
				IExPPlayer data = IExPPlayer.of((EntityPlayer) event.getEntity());
				if (data != null)
				{
					PlayerManager.handlePlayerEatenFood((EntityPlayer) event.getEntity(), data, event.getItem());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlaceBlock(BlockEvent.BreakEvent event)
	{
		if (event.getPlayer() != null && !event.getWorld().isRemote)
		{
			IExPPlayer data = IExPPlayer.of(event.getPlayer());
			if (data != null)
			{
				PlayerManager.handlePlayerPlaceBlock(event.getPlayer(), data);
			}
		}
	}
	
	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event)
	{
		if (event.getPlayer() != null && !event.getWorld().isRemote)
		{
			IExPPlayer data = IExPPlayer.of(event.getPlayer());
			if (data != null)
			{
				PlayerManager.handlePlayerBrokeBlock(event.getPlayer(), data);
			}
		}
	}
	
	@SubscribeEvent
	public void onDamageTaken(LivingHurtEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().world.isRemote)
		{
			if (event.getSource() != null && !event.getSource().canHarmInCreative())
			{
				PlayerManager.takeDamage((EntityPlayer) event.getEntityLiving(), event.getSource(), event.getAmount());
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent evt)
	{
		if (evt.world.hasCapability(ExPWorldCapability.worldCap, null) && evt.phase == Phase.END && evt.world.provider.getDimension() == 0)
		{
			IExPWorld.of(evt.world).onTick();
			if (evt.world.isRaining() && evt.world instanceof WorldServer)
			{
				WeatherUtils.handleServerTick((WorldServer) evt.world);
			}
		}
	}
	
	@SubscribeEvent
	public void onCapabilitiesWorld(AttachCapabilitiesEvent<World> evt)
	{
		if (!(evt.getObject().getWorldType() instanceof WorldTypeExP))
		{
			ExPMisc.modLogger.log(LogLevel.Debug, "The world initialized without ExPetrum world type! Have you forgot to set it in advanced world settings?");
		}
		
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
