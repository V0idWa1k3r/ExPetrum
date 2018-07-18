package v0id.exp.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.data.*;
import v0id.api.exp.inventory.IWeightProvider;
import v0id.api.exp.tile.crop.*;
import v0id.api.exp.world.Calendar;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.crop.CropStats;
import v0id.exp.crop.ExPCrop;
import v0id.exp.net.ExPNetwork;
import v0id.exp.tile.TileCrop;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ItemSeeds extends Item implements IOreDictEntry, IWeightProvider
{
	public ItemSeeds()
	{
		super();
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemSeeds));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setHasSubtypes(true);
	}
	
	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.itemSeeds).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			Stream.of(ExPOreDict.cropNames).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndIncrement())));
		});
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (tab != this.getCreativeTab())
		{
			return;
		}

		for (int i = 0; i < EnumCrop.values().length - 1; ++i)
		{
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return 0.05F;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return IWeightProvider.DEFAULT_VOLUME;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new CapabilityExPSeeds(stack);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return EnumActionResult.FAIL;
		}

		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile != null && tile.hasCapability(ExPFarmlandCapability.farmlandCap, EnumFacing.UP))
		{
			if (worldIn.isAirBlock(pos.up()))
			{
				// FIXME fix packet pipeline render issues!
				try
				{
					ItemStack held = player.getHeldItem(hand);
					CapabilityExPSeeds seedsCap = (CapabilityExPSeeds) held.getCapability(ExPSeedsCapability.seedsCap, null);
					CropStats stats = new CropStats(seedsCap.getOrCreateStats());
					worldIn.setBlockState(pos.up(), ExPBlocks.crop.getDefaultState());
					TileCrop cropTile = (TileCrop) worldIn.getTileEntity(pos.up());
					ExPCrop cropCap = (ExPCrop) IExPCrop.of(cropTile);
					cropCap.stats = stats;
					cropCap.timeKeeper = new Calendar(IExPWorld.of(worldIn).today().getTime());
                    ExPNetwork.sendTileData(cropTile, true);
					held.shrink(1);
					return EnumActionResult.SUCCESS;
				}
				catch (Exception ex)
				{
					ExPMisc.modLogger.error("Unable to place seeds at " + pos.toString() + "!", ex);
				}
			}
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	public static class CapabilityExPSeeds implements IExPSeed, ICapabilityProvider
	{
		public final ItemStack container;
		public CropStats data;
		
		public CapabilityExPSeeds()
		{
			this(new ItemStack(ExPItems.seeds));
		}
		
		public CapabilityExPSeeds(ItemStack is)
		{
			this.container = is;
			this.data = this.getOrCreateStats();
		}
		
		@Override
		public NBTTagCompound serializeNBT()
		{
			return this.container.getOrCreateSubCompound("exp.seedData");
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			this.container.getTagCompound().setTag("exp.seedData", nbt);
		}

		@Override
		public ItemStack getContainer()
		{
			return this.container;
		}

		@Override
		public EnumCrop getCrop()
		{
			return this.data.type;
		}

		@Override
		public void setCrop(EnumCrop crop)
		{
			this.data.type = crop;
			this.container.getTagCompound().setTag("exp.seedData", this.data.createItemNBT(null));
		}

		@Override
		public int getGeneration()
		{
			return this.data.generation;
		}

		@Override
		public void setGeneration(int newVal)
		{
			this.data.generation = newVal;
			this.container.getTagCompound().setTag("exp.seedData", this.data.createItemNBT(null));
		}

		@Override
		public NBTTagCompound getExtendedData()
		{
			return this.seedTag();
		}

		@Override
		public void setExtendedData(NBTTagCompound tag)
		{
			this.container.getTagCompound().setTag("exp.seedData", tag);
			this.data = this.getOrCreateStats();
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == ExPSeedsCapability.seedsCap;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			return capability == ExPSeedsCapability.seedsCap ? ExPSeedsCapability.seedsCap.cast(this) : null;
		}
		
		public NBTTagCompound seedTag()
		{
			return this.container.getOrCreateSubCompound("exp.seedData");
		}
		
		public CropStats getOrCreateStats()
		{
			CropStats ret = new CropStats();
			if (this.seedTag().hasNoTags() && this.container.getMetadata() <= EnumCrop.values().length - 1)
			{
				ret.type = EnumCrop.values()[this.container.getMetadata() + 1];
				if (ret.type.getData() != null)
                {
                    ret.createDefaults(ret.type);
                }

				ret.wild = false;
			}
			else
			{
				ret.createFromItemNBT(this.seedTag());
			}

			this.container.getTagCompound().setTag("exp.seedData", ret.createItemNBT(null));
			return ret;
		}
	}
}
