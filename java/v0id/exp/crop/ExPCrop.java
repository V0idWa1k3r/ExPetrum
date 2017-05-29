package v0id.exp.crop;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

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
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import v0id.api.core.VCStatics;
import v0id.api.core.network.PacketType;
import v0id.api.core.network.VoidNetwork;
import v0id.api.core.util.DimBlockPos;
import v0id.api.exp.block.property.ExPBlockProperties;
import v0id.api.exp.metal.EnumToolClass;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.api.exp.tile.crop.EnumCropBug;
import v0id.api.exp.tile.crop.EnumCropHarvestAction;
import v0id.api.exp.tile.crop.EnumPlantNutrient;
import v0id.api.exp.tile.crop.IExPCrop;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.EnumSeason;
import v0id.api.exp.world.TemperatureRange;

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
		NBTTagCompound sent = new NBTTagCompound();
		sent.setTag("tileData", this.getContainer().serializeNBT());
		sent.setTag("blockPosData", new DimBlockPos(this.getContainer().getPos(), this.getContainer().getWorld().provider.getDimension()).serializeNBT());
		VoidNetwork.sendDataToAllAround(PacketType.TileData, sent, new TargetPoint(this.getContainer().getWorld().provider.getDimension(), this.getContainer().getPos().getX(), this.getContainer().getPos().getY(), this.getContainer().getPos().getZ(), 96));
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
		return this.stats.type == EnumCrop.DEAD ? true : this.stats.wild;
	}

	@Override
	public NonNullList<ItemStack> getMatureDrops()
	{
		return CropLogic.createDrops(this, VCStatics.rng);
	}

	@Override
	public NonNullList<ItemStack> getSeedDrops()
	{
		return NonNullList.withSize(1, CropLogic.createSeeds(this, VCStatics.rng));
	}

	@Override
	public Pair<EnumActionResult, NonNullList<ItemStack>> onHarvest(EntityPlayer harvester, World harvestedIn, BlockPos harvestedAt, IBlockState selfBlockReference, EnumHand playerHarvestHand,ItemStack playerHarvestItem, boolean isHarvestingWithRMB)
	{
		harvestedIn = Optional.ofNullable(harvestedIn).orElse(this.getContainer().getWorld());
		if (this.isDead() || harvestedIn.isRemote)
		{
			return Pair.of(EnumActionResult.PASS, NonNullList.withSize(0, ItemStack.EMPTY));
		}
		
		harvestedAt = Optional.ofNullable(harvestedAt).orElse(this.getContainer().getPos());
		selfBlockReference = Optional.ofNullable(selfBlockReference).orElse(harvestedIn.getBlockState(harvestedAt));
		Set<String> toolClasses = playerHarvestItem.getItem().getToolClasses(playerHarvestItem);
		List<ItemStack> dropsBase = Lists.newArrayList();
		
		if (this.getGrowth() <= 1)
		{
			if (this.getType() == EnumCrop.PEPPER)
			{
				int stage = this.getGrowthIndex();
				if (toolClasses.contains(EnumToolClass.KNIFE.getName()))
				{
					dropsBase.addAll(this.getSeedDrops());
					if (isHarvestingWithRMB)
					{
						this.setGrowth(0.4F);
						harvestedIn.setBlockState(harvestedAt, selfBlockReference.withProperty(ExPBlockProperties.CROP_GROWTH_STAGE, 4));
						NBTTagCompound sent = new NBTTagCompound();
						sent.setTag("tileData", this.getContainer().serializeNBT());
						sent.setTag("blockPosData", new DimBlockPos(this.getContainer().getPos(), this.getContainer().getWorld().provider.getDimension()).serializeNBT());
						VoidNetwork.sendDataToAllAround(PacketType.TileData, sent, new TargetPoint(this.getContainer().getWorld().provider.getDimension(), this.getContainer().getPos().getX(), this.getContainer().getPos().getY(), this.getContainer().getPos().getZ(), 96));
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
						if (isHarvestingWithRMB)
						{
							this.setGrowth(0.4F);
							harvestedIn.setBlockState(harvestedAt, selfBlockReference.withProperty(ExPBlockProperties.CROP_GROWTH_STAGE, 4));
							NBTTagCompound sent = new NBTTagCompound();
							sent.setTag("tileData", this.getContainer().serializeNBT());
							sent.setTag("blockPosData", new DimBlockPos(this.getContainer().getPos(), this.getContainer().getWorld().provider.getDimension()).serializeNBT());
							VoidNetwork.sendDataToAllAround(PacketType.TileData, sent, new TargetPoint(this.getContainer().getWorld().provider.getDimension(), this.getContainer().getPos().getX(), this.getContainer().getPos().getY(), this.getContainer().getPos().getZ(), 96));
						}
					}
				}
			}
			else
			{
				if (this.getGrowth() == 1)
				{
					if (toolClasses.contains(EnumToolClass.KNIFE.getName()))
					{
						dropsBase.addAll(this.getSeedDrops());
						this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference);
						if (harvester != null)
						{
							playerHarvestItem.damageItem(1, harvester);
						}
					}
					else
					{
						if (this.getType().isGrain())
						{
							if (toolClasses.contains(EnumToolClass.SCYTHE.getName()))
							{
								dropsBase.addAll(this.getMatureDrops());
								this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference);
							}
							else
							{
								if (isHarvestingWithRMB)
								{
									if (harvestedIn.rand.nextDouble() < 0.1D)
									{
										dropsBase.addAll(this.getMatureDrops());
										this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference);
									}
									else
									{
										harvestedIn.playSound(null, harvestedAt, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1);
									}
								}
								else
								{
									dropsBase.addAll(this.getMatureDrops());
									this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference);
								}
							}
						}
						else
						{
							if (isHarvestingWithRMB)
							{
								if (harvestedIn.rand.nextDouble() < 0.3D)
								{
									dropsBase.addAll(this.getMatureDrops());
									this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference);
								}
								else
								{
									harvestedIn.playSound(null, harvestedAt, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1, 1);
								}
							}
							else
							{
								dropsBase.addAll(this.getMatureDrops());
								this.handleHarvest(harvestedIn, harvestedAt, selfBlockReference);
							}
						}
					}
				}
			}
		}
		else
		{
			return Pair.of(EnumActionResult.FAIL, NonNullList.withSize(0, ItemStack.EMPTY));
		}
		
		NonNullList<ItemStack> ret = NonNullList.withSize(dropsBase.size(), ItemStack.EMPTY);
		for (int i = 0; i < dropsBase.size(); ++i)
		{
			ret.set(i, dropsBase.get(i));
		}
		
		return Pair.of(EnumActionResult.SUCCESS, ret);
	}
	
	public void handleHarvest(World w, BlockPos pos, IBlockState state)
	{
		if (this.getHarvestAction() == EnumCropHarvestAction.DESTROY)
		{
			w.setBlockToAir(pos);
		}
		else
		{
			if (this.getHarvestAction() == EnumCropHarvestAction.SPECIAL)
			{
				return;
			}
			
			int setbackBy = this.getHarvestAction().ordinal();
			this.setGrowth((float)(this.getType().getData().growthStages - setbackBy) / this.getType().getData().growthStages);
			w.setBlockState(pos, state.withProperty(ExPBlockProperties.CROP_GROWTH_STAGE, this.getGrowthIndex()));
			NBTTagCompound sent = new NBTTagCompound();
			sent.setTag("tileData", this.getContainer().serializeNBT());
			sent.setTag("blockPosData", new DimBlockPos(this.getContainer().getPos(), this.getContainer().getWorld().provider.getDimension()).serializeNBT());
			VoidNetwork.sendDataToAllAround(PacketType.TileData, sent, new TargetPoint(this.getContainer().getWorld().provider.getDimension(), this.getContainer().getPos().getX(), this.getContainer().getPos().getY(), this.getContainer().getPos().getZ(), 96));
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
		return (int) (this.stats.type == EnumCrop.DEAD ? 0 : (int)(this.stats.growth * (this.stats.type.getData().growthStages - 1)));
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
		return this.isDead() ? false : this.stats.type.getData().harvestSeason.contains(season);
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
		if (this.isDead())
		{
			return;
		}
		
		this.setHealth(this.getHealth() - damage);
		if (this.isDead())
		{
			this.stats.type = EnumCrop.DEAD;
		}
	}
}
