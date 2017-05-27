package v0id.exp.handler;

import java.lang.reflect.Field;
import java.util.Optional;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
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
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import v0id.api.core.logging.LogLevel;
import v0id.api.core.util.MC;
import v0id.api.exp.block.IWater;
import v0id.api.exp.data.ExPMisc;
import v0id.api.exp.data.ExPPotions;
import v0id.api.exp.player.ExPPlayerCapability;
import v0id.api.exp.player.IExPPlayer;
import v0id.api.exp.world.ExPWorldCapability;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.player.ExPPlayer;
import v0id.exp.player.PlayerManager;
import v0id.exp.player.inventory.ManagedSlot;
import v0id.exp.player.inventory.PlayerInventoryHelper;
import v0id.exp.util.WeatherUtils;
import v0id.exp.world.ExPWorld;
import v0id.exp.world.gen.WorldTypeExP;

public class ExPHandlerServer
{
	@SubscribeEvent
	public void onLivingJump(LivingJumpEvent event)
	{
		if (event.getEntityLiving() != null)
		{
			if (event.getEntityLiving().isPotionActive(ExPPotions.stunned))
			{
				event.getEntityLiving().motionY -= 10;
			}
		}
	}
	
	@SubscribeEvent
	public void onInteractLeftClickBlock(PlayerInteractEvent.LeftClickBlock event)
	{
		if (event.getEntityPlayer() != null)
		{
			if (event.getEntityPlayer().isPotionActive(ExPPotions.stunned))
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event)
	{
		if (event.getEntityPlayer() != null)
		{
			if (event.getEntityPlayer().isPotionActive(ExPPotions.stunned))
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onInteractRightClickItem(PlayerInteractEvent.RightClickItem event)
	{
		if (event.getEntityPlayer() != null)
		{
			if (event.getEntityPlayer().isPotionActive(ExPPotions.stunned))
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onInteractEntitySpecific(PlayerInteractEvent.EntityInteractSpecific event)
	{
		if (event.getEntityPlayer() != null)
		{
			if (event.getEntityPlayer().isPotionActive(ExPPotions.stunned))
			{
				event.setCanceled(true);
			}
		}	
	}
	
	@SubscribeEvent
	public void onInteractEntity(PlayerInteractEvent.EntityInteract event)
	{
		if (event.getEntityPlayer() != null)
		{
			if (event.getEntityPlayer().isPotionActive(ExPPotions.stunned))
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onBreakSpeed(PlayerEvent.BreakSpeed event)
	{
		if (event.getEntityPlayer() != null)
		{
			if (event.getEntityPlayer().isPotionActive(ExPPotions.stunned))
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event)
	{
		if (event.getSource() != null && event.getSource().getSourceOfDamage() != null)
		{
			if (event.getSource().getSourceOfDamage() instanceof EntityLivingBase && ((EntityLivingBase)event.getSource().getSourceOfDamage()).isPotionActive(ExPPotions.stunned))
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onTryPickupItem(EntityItemPickupEvent event)
	{
		if (event.getItem() != null && !event.getItem().getEntityItem().isEmpty() && !event.getItem().isDead && !event.getEntityPlayer().world.isRemote)
		{
			int i = PlayerInventoryHelper.findFirstAvailableSlotFor(event.getItem().getEntityItem(), Optional.empty(), event.getEntityPlayer());
			if (i == -1)
			{
				event.setCanceled(true);
			}
			else
			{
				if (i < Short.MAX_VALUE)
				{
					event.getEntityPlayer().inventory.setInventorySlotContents(i, event.getItem().getEntityItem().copy());
					net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerItemPickupEvent(event.getEntityPlayer(), event.getItem());
					event.getEntityPlayer().onItemPickup(event.getItem(), event.getItem().getEntityItem().getCount());
					event.getEntityPlayer().addStat(StatList.getObjectsPickedUpStats(event.getItem().getEntityItem().getItem()), event.getItem().getEntityItem().getCount());
					event.getItem().setDead();
					event.setCanceled(true);
				}
				else
				{
					int slotID = i - Short.MAX_VALUE;
					ItemStack toIncrement = event.getEntityPlayer().inventory.getStackInSlot(slotID);
					Optional<Slot> s = Optional.ofNullable(event.getEntityPlayer().openContainer != null ? event.getEntityPlayer().openContainer.getSlot(slotID) : null);
					int max = Math.min(toIncrement.getMaxStackSize(), s.isPresent() ? s.get().getItemStackLimit(toIncrement) : event.getEntityPlayer().inventory.getInventoryStackLimit());
					int current = toIncrement.getCount();
					final int added = Math.min(event.getItem().getEntityItem().getCount(), max - current);
					net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerItemPickupEvent(event.getEntityPlayer(), event.getItem());
					event.getEntityPlayer().addStat(StatList.getObjectsPickedUpStats(event.getItem().getEntityItem().getItem()), added);
					event.getItem().getEntityItem().shrink(added);
					toIncrement.grow(added);
					event.getEntityPlayer().world.playSound(null, event.getEntityPlayer().getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (event.getEntityPlayer().world.rand.nextFloat() - event.getEntityPlayer().world.rand.nextFloat()) * 1.4F + 2.0F);
					if (toIncrement.isEmpty())
					{
						event.getItem().setDead();
					}
					
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onContainerOpened(PlayerContainerEvent.Open event)
	{
		try
		{
			Container c = event.getContainer();
			for (int i = 0; i < c.inventorySlots.size(); ++i)
			{
				Slot s = c.inventorySlots.get(i);
				if (s.getClass().equals(Slot.class) && s.inventory instanceof InventoryPlayer && !(s instanceof ManagedSlot) && s.getSlotIndex() >= 9 && s.getSlotIndex() < 36)
				{
					ManagedSlot wrapper = new ManagedSlot(s);
					c.inventorySlots.remove(i);
					c.inventorySlots.add(i, wrapper);
				}
			}
		}
		catch (Exception ex)
		{
			ExPMisc.modLogger.log(LogLevel.Error, "ExPetrum was unable to initialize it's inventory system! This is most likely caused by another mod!", ex);
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.getEntity() instanceof EntityPlayer)
		{
			try
			{
				EntityPlayer player = (EntityPlayer) event.getEntity();
				ContainerPlayer playerContainer = (ContainerPlayer) player.inventoryContainer;
				Optional<ContainerPlayer> inventoryContainer = Optional.empty();
				if (player.inventoryContainer instanceof ContainerPlayer)
				{
					inventoryContainer = Optional.of((ContainerPlayer)player.inventoryContainer);
				}
				
				for (int i = 9; i < 36; ++i)
				{
					Slot s = playerContainer.getSlot(i);
					ManagedSlot wrapper = new ManagedSlot(s);
					playerContainer.inventorySlots.remove(i);
					playerContainer.inventorySlots.add(i, wrapper);
					if (inventoryContainer.isPresent())
					{
						s = inventoryContainer.get().getSlot(i);
						if (!(s instanceof ManagedSlot))
						{
							wrapper = new ManagedSlot(s);
							inventoryContainer.get().inventorySlots.remove(i);
							inventoryContainer.get().inventorySlots.add(i, wrapper);
						}
					}
				}
			}
			catch (Exception ex)
			{
				ExPMisc.modLogger.log(LogLevel.Error, "ExPetrum was unable to initialize it's inventory system! This is most likely caused by another mod!", ex);
			}
		}
	}
	
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
