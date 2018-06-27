package v0id.exp.crop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import org.apache.commons.lang3.tuple.Pair;
import v0id.core.network.PacketType;
import v0id.core.network.VoidNetwork;
import v0id.core.util.DimBlockPos;
import v0id.api.exp.event.crop.CropEvent;
import v0id.api.exp.event.crop.CropEvent.Harvest.Action;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.tile.crop.*;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.TemperatureRange;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ExPCrop implements IExPCrop
{
	public CropStats stats = new CropStats();
	public TileEntity holder;
	public boolean isSick;
	public Calendar timeKeeper = new Calendar();
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("stats", this.stats.serializeNBT());
		tag.setBoolean("eaten", this.isSick);
		tag.setTag("calendar", this.timeKeeper.serializeNBT());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.stats = new CropStats();
		this.stats.deserializeNBT(nbt.getCompoundTag("stats"));
		this.isSick = nbt.getBoolean("eaten");
		this.timeKeeper.deserializeNBT((NBTTagLong) nbt.getTag("calendar"));
	}

	@Override
	public TileEntity getContainer()
	{
		return this.holder;
	}

	@Override
	public void onTick(){}

	@Override
	public void onWorldTick()
	{
		CropLogic.onWorldUpdate(this);
		this.sendUpdatePacket();
	}

	@Override
	public TemperatureRange getSurvivalTemperature()
	{
		return this.stats.type == EnumCrop.DEAD ? new TemperatureRange() : this.stats.growthRanges[0];
	}

	@Override
	public TemperatureRange getOptimalTemperature()
	{
		return this.stats.type == EnumCrop.DEAD ? new TemperatureRange() : this.stats.growthRanges[1];
	}

	@Override
	public TemperatureRange getIdealTemperature()
	{
		return this.stats.type == EnumCrop.DEAD ? new TemperatureRange() : this.stats.growthRanges[2];
	}

	@Override
	public Pair<Float, Float> getMoistureRange()
	{
		return this.stats.type == EnumCrop.DEAD ? Pair.of(0F, 0F) : this.stats.humidityGrowthRange;
	}

	@Override
	public int getGeneration()
	{
		return this.stats.type == EnumCrop.DEAD ? 0 : this.stats.generation;
	}

	@Override
	public boolean isWild()
	{
		return this.stats.type == EnumCrop.DEAD || this.stats.wild;
	}

	@Override
	public NonNullList<ItemStack> getMatureDrops()
	{
		return CropLogic.createDrops(this);
	}

	@Override
	public NonNullList<ItemStack> getSeedDrops()
	{
		return NonNullList.withSize(1, CropLogic.createSeeds(this));
	}

	@Override
	public Pair<EnumActionResult, NonNullList<ItemStack>> onHarvest(EntityPlayer harvester, World harvestedIn, BlockPos harvestedAt, IBlockState selfBlockReference, EnumHand playerHarvestHand,ItemStack playerHarvestItem, boolean isHarvestingWithRMB)
	{
		harvestedIn = Optional.ofNullable(harvestedIn).orElse(this.getContainer().getWorld());
		if (this.isDead() || harvestedIn.isRemote || MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.Pre(this, harvestedIn, harvestedAt, isHarvestingWithRMB)))
		{
			return Pair.of(EnumActionResult.PASS, NonNullList.withSize(0, ItemStack.EMPTY));
		}
		
		harvestedAt = Optional.ofNullable(harvestedAt).orElse(this.getContainer().getPos());
		selfBlockReference = Optional.ofNullable(selfBlockReference).orElse(harvestedIn.getBlockState(harvestedAt));
		CropEvent.Harvest.GardeningSpadeCheck checkEvent = new CropEvent.Harvest.GardeningSpadeCheck(this, harvestedIn, harvestedAt, playerHarvestItem, isHarvestingWithRMB);
		MinecraftForge.EVENT_BUS.post(checkEvent);
		boolean isUsingSpade = checkEvent.getResult() == Result.DEFAULT ? playerHarvestItem.getItem().getToolClasses(playerHarvestItem).contains(EnumToolClass.GARDENING_SPADE.getName()) : checkEvent.getResult() == Result.ALLOW;
		List<ItemStack> dropsBase = Lists.newArrayList();
		if (this.getGrowth() <= 1)
		{
			if (this.getType() == EnumCrop.PEPPER)
			{
				int stage = this.getGrowthIndex();
				if (isUsingSpade && stage >= this.getType().getData().growthStages - 3)
				{
					dropsBase.addAll(this.getSeedDrops());
					MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.PopulateDropList(this, harvestedIn, harvestedAt, isHarvestingWithRMB, dropsBase, Action.HARVEST_SEEDS));
					if (isHarvestingWithRMB)
					{
						CropEvent.Harvest.PepperSetGrowth evt = new CropEvent.Harvest.PepperSetGrowth(this, harvestedIn, harvestedAt, isHarvestingWithRMB, 0.4F, Action.HARVEST_SEEDS);
						if (!MinecraftForge.EVENT_BUS.post(evt))
						{
							this.setGrowth(evt.growth);
							this.sendUpdatePacket();
						}
					}
					
					if (harvester != null)
					{
						playerHarvestItem.damageItem(1, harvester);
					}
				}
				else
				{
					if (stage >= this.getType().getData().growthStages - 3)
					{
						dropsBase.addAll(this.getMatureDrops());
						MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.PopulateDropList(this, harvestedIn, harvestedAt, isHarvestingWithRMB, dropsBase, Action.HARVEST_DROPS));
						if (isHarvestingWithRMB)
						{
							CropEvent.Harvest.PepperSetGrowth evt = new CropEvent.Harvest.PepperSetGrowth(this, harvestedIn, harvestedAt, isHarvestingWithRMB, 0.4F, Action.HARVEST_DROPS);
							if (!MinecraftForge.EVENT_BUS.post(evt))
							{
								this.setGrowth(evt.growth);
								this.sendUpdatePacket();
							}
						}
					}
					else
					{
						MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.Post(this, harvestedIn, harvestedAt, isHarvestingWithRMB));
						return Pair.of(EnumActionResult.FAIL, NonNullList.withSize(0, ItemStack.EMPTY));
					}
				}
			}
			else
			{
				if (this.getGrowth() == 1)
				{
					if (isUsingSpade)
					{
						dropsBase.addAll(this.getSeedDrops());
						MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.PopulateDropList(this, harvestedIn, harvestedAt, isHarvestingWithRMB, dropsBase, Action.HARVEST_SEEDS));
						this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference, Action.HARVEST_SEEDS, isHarvestingWithRMB);
						if (harvester != null)
						{
							playerHarvestItem.damageItem(1, harvester);
						}
					}
					else
					{
						if (this.getType().isGrain())
						{
							if (playerHarvestItem.getItem().getToolClasses(playerHarvestItem).contains(EnumToolClass.SCYTHE.getName()))
							{
								dropsBase.addAll(this.getMatureDrops());
								MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.PopulateDropList(this, harvestedIn, harvestedAt, isHarvestingWithRMB, dropsBase, Action.HARVEST_DROPS));
								this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference, Action.HARVEST_DROPS, isHarvestingWithRMB);
							}
							else
							{
								if (isHarvestingWithRMB)
								{
									if (this.tryHarvest(0.1, harvestedIn, harvestedAt, harvestedIn.rand))
									{
										dropsBase.addAll(this.getMatureDrops());
										MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.PopulateDropList(this, harvestedIn, harvestedAt, isHarvestingWithRMB, dropsBase, Action.HARVEST_DROPS));
										this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference, Action.HARVEST_DROPS, isHarvestingWithRMB);
									}
									else
									{
										harvestedIn.playSound(null, harvestedAt, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1);
									}
								}
								else
								{
									dropsBase.addAll(this.getMatureDrops());
									MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.PopulateDropList(this, harvestedIn, harvestedAt, isHarvestingWithRMB, dropsBase, Action.HARVEST_DROPS));
									this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference, Action.HARVEST_DROPS, isHarvestingWithRMB);
								}
							}
						}
						else
						{
							if (isHarvestingWithRMB)
							{
								if (this.tryHarvest(0.3, harvestedIn, harvestedAt, harvestedIn.rand))
								{
									dropsBase.addAll(this.getMatureDrops());
									MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.PopulateDropList(this, harvestedIn, harvestedAt, isHarvestingWithRMB, dropsBase, Action.HARVEST_DROPS));
									this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference, Action.HARVEST_DROPS, isHarvestingWithRMB);
								}
								else
								{
									harvestedIn.playSound(null, harvestedAt, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1);
								}
							}
							else
							{
								dropsBase.addAll(this.getMatureDrops());
								MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.PopulateDropList(this, harvestedIn, harvestedAt, isHarvestingWithRMB, dropsBase, Action.HARVEST_DROPS));
								this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference, Action.HARVEST_DROPS, isHarvestingWithRMB);
							}
						}
					}
				}
			}
		}
		else
		{
			MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.Post(this, harvestedIn, harvestedAt, isHarvestingWithRMB));
			return Pair.of(EnumActionResult.FAIL, NonNullList.withSize(0, ItemStack.EMPTY));
		}
		
		NonNullList<ItemStack> ret = NonNullList.withSize(dropsBase.size(), ItemStack.EMPTY);
		for (int i = 0; i < dropsBase.size(); ++i)
		{
			ret.set(i, dropsBase.get(i));
		}
		
		MinecraftForge.EVENT_BUS.post(new CropEvent.Harvest.Post(this, harvestedIn, harvestedAt, isHarvestingWithRMB));
		return Pair.of(EnumActionResult.SUCCESS, ret);
	}
	
	public boolean tryHarvest(double baseChance, World w, BlockPos pos, Random rand)
	{
		CropEvent.Harvest.HarvestAttempt event = new CropEvent.Harvest.HarvestAttempt(this, w, pos, baseChance);
		switch (event.getResult())
		{
			case DENY:
			{
				return false;
			}
			
			case ALLOW:
			{
				return true;
			}
			
			default:
			{
				return rand.nextDouble() <= baseChance;
			}
		}
	}
	
	public void sendUpdatePacket()
	{
		NBTTagCompound sent = new NBTTagCompound();
		sent.setTag("tileData", this.getContainer().serializeNBT());
		sent.setTag("blockPosData", new DimBlockPos(this.getContainer().getPos(), this.getContainer().getWorld().provider.getDimension()).serializeNBT());
		VoidNetwork.sendDataToAllAround(PacketType.TileData, sent, new TargetPoint(this.getContainer().getWorld().provider.getDimension(), this.getContainer().getPos().getX(), this.getContainer().getPos().getY(), this.getContainer().getPos().getZ(), 96));
	}
	
	public void handleHarvest(World w, BlockPos pos, IBlockState state, Action a, boolean b)
	{
		EnumCropHarvestAction todo = this.getHarvestAction();
		CropEvent.Harvest.OnHarvest evt = new CropEvent.Harvest.OnHarvest(this, w, pos, b, a, todo);
		if (MinecraftForge.EVENT_BUS.post(evt))
		{
			return;
		}
		
		todo = evt.actionAttempted;
		if (todo == EnumCropHarvestAction.DESTROY)
		{
			w.setBlockToAir(pos);
		}
		else
		{
			if (todo == EnumCropHarvestAction.SPECIAL)
			{
				return;
			}
			
			int setbackBy = this.getHarvestAction().ordinal();
			this.setGrowth((float)(this.getType().getData().growthStages - setbackBy) / this.getType().getData().growthStages);
			this.sendUpdatePacket();
		}
	}

	@Override
	public void handleTransfer(Calendar uprootedAt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canGrowOverride(boolean baseCanGrow)
	{
		return baseCanGrow;
	}

	@Override
	public float getHealth()
	{
		return this.stats.type == EnumCrop.DEAD ? 0 : this.stats.health;
	}

	@Override
	public void setHealth(float newValue)
	{
		if (this.stats.type == EnumCrop.DEAD)
		{
			return;
		}
		
		this.stats.health = newValue;
	}

	@Override
	public EnumMap<EnumPlantNutrient, Float> getNutrientConsumption()
	{
		return this.stats.type == EnumCrop.DEAD ? Maps.newEnumMap(EnumPlantNutrient.class) : this.stats.nutrientConsumption;
	}

	@Override
	public void setNutrientConsumption(EnumPlantNutrient nutrient, float newValue)
	{
		if (this.stats.type == EnumCrop.DEAD)
		{
			return;
		}
		
		this.stats.nutrientConsumption.put(nutrient, newValue);
	}

	@Override
	public float getWaterConsumption()
	{
		return this.stats.type == EnumCrop.DEAD ? 0 : this.stats.waterConsumption;
	}

	@Override
	public void setWaterConsumption(float newValue)
	{
		if (this.stats.type == EnumCrop.DEAD)
		{
			return;
		}
		
		this.stats.waterConsumption = newValue;
	}

	@Override
	public EnumCrop getType()
	{
		return this.stats.type;
	}

	@Override
	public int getGrowthIndex()
	{
		return this.stats.type == EnumCrop.DEAD ? 0 : (int)(this.stats.growth * (this.stats.type.getData().growthStages - 1));
	}

	@Override
	public float getGrowth()
	{
		return this.stats.type == EnumCrop.DEAD ? 0 : this.stats.growth;
	}

	@Override
	public void setGrowth(float newValue)
	{
		if (this.stats.type == EnumCrop.DEAD)
		{
			return;
		}
		
		this.stats.growth = newValue;
	}

	@Override
	public Optional<EnumCropBug> getBug()
	{
		return this.isDead() ? Optional.empty() : Optional.of(this.stats.type.getData().bug);
	}

	@Override
	public boolean isBeingEaten()
	{
		return this.isSick;
	}

	@Override
	public void setBeingEaten(boolean b)
	{
		this.isSick = b;
	}

	@Override
	public EnumCropHarvestAction getHarvestAction()
	{
		return this.isDead() ? null : this.stats.type.getData().harvestAction;
	}

	@Override
	public boolean isHarvestSeason(EnumSeason season)
	{
		return !this.isDead() && this.stats.type.getData().harvestSeason.contains(season);
	}
	
	public void initDefaultsForStats(EnumCrop crop)
	{
		this.stats.createDefaults(crop);
	}
	
	public void createStatsFromItem(ItemStack item)
	{
		this.stats.createFromItemNBT(item.getSubCompound("exp.cropdata"));
	}

	public static ExPCrop createDefault()
	{
		return new ExPCrop();
	}
	
	public static ExPCrop createWithTile(TileEntity tile)
	{
		ExPCrop ret = createDefault();
		ret.holder = tile;
		return ret;
	}

	@Override
	public void causeDamage(float damage)
	{
		CropEvent.Damage dmgEvent = new CropEvent.Damage(this, this.getContainer().getWorld(), this.getContainer().getPos(), damage);
		if (this.isDead() || MinecraftForge.EVENT_BUS.post(dmgEvent))
		{
			return;
		}
		
		damage = dmgEvent.amount;
		this.setHealth(this.getHealth() - damage);
		if (this.isDead())
		{
			this.stats.type = EnumCrop.DEAD;
		}
	}
}
