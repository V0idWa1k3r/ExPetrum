package v0id.exp.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import v0id.api.core.VoidApi;
import v0id.api.core.network.VoidNetwork;
import v0id.api.core.settings.VCSettings;
import v0id.api.core.util.nbt.NBTChain;
import v0id.api.core.util.nbt.NBTList;
import v0id.api.exp.block.ILeaves;
import v0id.api.exp.block.ILog;
import v0id.api.exp.data.ExPPackets;

public class EntityFallingTree extends Entity
{
	public volatile ConcurrentHashMap<BlockPos, IBlockState> data = new ConcurrentHashMap();
	public volatile boolean isDataConstructed = false;
	public boolean isDataSent = false;
	public static final Gson gson = new Gson();
	public BlockPos brokenAt;
	
	// As the wood getting recursion can get very intensive with huge trees
	// I need as much space on stack as possible
	// Every local is stored on the stack, obviously
	// Thus I remove the most costly ones from the function and move them to fields
	// But then it is not thread safe!
	// Solution: use ThreadLocal
	private static ThreadLocal<BlockPos> p = new ThreadLocal();
	private static ThreadLocal<IBlockState> stateAt = new ThreadLocal();
	
	public boolean isFalling;
	public float fallAngle;
	public float fallProgress;
	
	public static DataParameter<Float> FALL_ANGLE = EntityDataManager.createKey(EntityFallingTree.class, DataSerializers.FLOAT);
	public static DataParameter<Float> FALL_PROGRESS = EntityDataManager.createKey(EntityFallingTree.class, DataSerializers.FLOAT);
	
	public EntityFallingTree(World worldIn, BlockPos pos, EntityPlayer destroyer)
	{
		this(worldIn);
		if (!worldIn.isRemote)
		{
			Thread worker = new Thread(() -> EntityFallingTree.constructData(this, worldIn, pos));
			worker.setDaemon(true);
			worker.setPriority(Thread.MIN_PRIORITY);
			worker.start();
		}
		
		this.brokenAt = pos;
		this.noClip = true;
		this.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = pos.getX();
        this.prevPosY = pos.getY();
        this.prevPosZ = pos.getZ();
        this.isFalling = destroyer != null;
        this.determineFallAngle(destroyer);
	}
	
	public EntityFallingTree(World worldIn)
	{
		super(worldIn);
		this.preventEntitySpawning = true;
	}
	
	public void determineFallAngle(EntityPlayer p)
	{
		if (p == null)
		{
			return;
		}
		
		float rYaw = p.rotationYaw + 180;
		while (rYaw < 0)
		{
			rYaw += 360;
		}
		
		this.fallAngle = rYaw % 360;
	}
	
	public static void constructData(EntityFallingTree of, World w, BlockPos initialPos)
	{
		synchronized(of)
		{
			ConcurrentHashMap<BlockPos, IBlockState> data = new ConcurrentHashMap();
			HashSet<BlockPos> scannedData = Sets.newHashSet();
			data.put(initialPos, w.getBlockState(initialPos));
			scannedData.add(initialPos);
			tryScanForWood(initialPos.up(), w, scannedData, w.getBlockState(initialPos), data);
			scannedData.clear();
			of.data = data;
			of.isDataConstructed = true;
		}
	}
	
	public static void tryScanForWood(BlockPos at, World w, HashSet<BlockPos> scannedPos, IBlockState initialBlock, Map<BlockPos, IBlockState> bak)
	{
		if (scannedPos.contains(at))
		{
			return;
		}
		
		IBlockState state = w.getBlockState(at);
		if (state.getBlock() instanceof ILog || state.getBlock() instanceof ILeaves)
		{
			if (state.getBlock() instanceof ILog)
			{
				if (((ILog)state.getBlock()).isSameWoodType(w, state, at, initialBlock))
				{
					bak.put(at, state);
					scannedPos.add(at);
					for (int dx = -1; dx <= 1; ++dx)
					{
						for (int dy = -1; dy <= 1; ++dy)
						{
							for (int dz = -1; dz <= 1; ++dz)
							{
								if (dx == 0 && dy == 0 && dz == 0)
								{
									continue;
								}
								
								p.set(at.add(dx, dy, dz));
								if (scannedPos.contains(p.get()))
								{
									continue;
								}
								
								stateAt.set(w.getBlockState(p.get()));
								if (!(stateAt.get().getBlock() instanceof ILog || stateAt.get().getBlock() instanceof ILeaves))
								{
									continue;
								}
								
								if (stateAt.get().getBlock() instanceof ILog && !((ILog)stateAt.get().getBlock()).isSameWoodType(w, stateAt.get(), p.get(), initialBlock))
								{
									continue;
								}
								
								if (stateAt.get().getBlock() instanceof ILeaves && !((ILeaves)stateAt.get().getBlock()).isSameWoodType(w, stateAt.get(), p.get(), initialBlock))
								{
									continue;
								}
								
								tryScanForWood(p.get(), w, scannedPos, initialBlock, bak);
							}
						}
					}
				}
			}
			else
			{
				if (((ILeaves)state.getBlock()).isSameWoodType(w, state, at, initialBlock))
				{
					bak.put(at, state);
					scannedPos.add(at);
					for (int dx = -1; dx <= 1; ++dx)
					{
						for (int dy = -1; dy <= 1; ++dy)
						{
							for (int dz = -1; dz <= 1; ++dz)
							{
								if (dx == 0 && dy == 0 && dz == 0)
								{
									continue;
								}
								
								p.set(at.add(dx, dy, dz));
								if (scannedPos.contains(p.get()))
								{
									continue;
								}
								
								stateAt.set(w.getBlockState(p.get()));
								if (!(stateAt.get().getBlock() instanceof ILog || stateAt.get().getBlock() instanceof ILeaves))
								{
									continue;
								}
								
								if (stateAt.get().getBlock() instanceof ILog && !((ILog)stateAt.get().getBlock()).isSameWoodType(w, stateAt.get(), p.get(), initialBlock))
								{
									continue;
								}
								
								if (stateAt.get().getBlock() instanceof ILeaves && !((ILeaves)stateAt.get().getBlock()).isSameWoodType(w, stateAt.get(), p.get(), initialBlock))
								{
									continue;
								}
								
								tryScanForWood(p.get(), w, scannedPos, initialBlock, bak);
							}
						}
					}
				}
			}
		}
		else
		{
			return;
		}
	}

	@Override
	protected void entityInit()
	{
		this.getDataManager().register(FALL_ANGLE, 0F);
		this.getDataManager().register(FALL_PROGRESS, 0F);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("data", NBT.TAG_LIST))
		{
			deserializeDataFromNBT(compound);
		}
		
		this.isDataConstructed = compound.getBoolean("isDataConstructed");
		this.isDataSent = compound.getBoolean("isDataSent");
		NBTTagCompound pTag = compound.getCompoundTag("brokenAt");
		this.brokenAt = new BlockPos(pTag.getInteger("X"), pTag.getInteger("Y"), pTag.getInteger("Z"));
		this.fallAngle = compound.getFloat("fallAngle");
		this.fallProgress = compound.getFloat("fallProgress");
	}

	public void deserializeDataFromNBT(NBTTagCompound compound)
	{
		this.data.clear();
		for (NBTTagCompound tag : NBTList.<NBTTagCompound>of(compound.getTagList("data", NBT.TAG_COMPOUND)))
		{
			NBTTagCompound pTag = tag.getCompoundTag("pos");
			BlockPos pos = new BlockPos(pTag.getInteger("X"), pTag.getInteger("Y"), pTag.getInteger("Z"));
			IBlockState state = NBTUtil.readBlockState(tag);
			this.data.put(pos, state);
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		serializeDataToNBT(compound);
		compound.setBoolean("isDataConstructed", this.isDataConstructed);
		compound.setBoolean("isDataSent", this.isDataSent);
		compound.setTag("brokenAt", NBTUtil.createPosTag(this.brokenAt));
		compound.setFloat("fallAngle", this.fallAngle);
		compound.setFloat("fallProgress", this.fallProgress);
	}

	public void serializeDataToNBT(NBTTagCompound compound)
	{
		NBTTagList lst = new NBTTagList();
		for (Entry<BlockPos, IBlockState> dataEntry : this.data.entrySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			NBTUtil.writeBlockState(tag, dataEntry.getValue());
			tag.setTag("pos", NBTUtil.createPosTag(dataEntry.getKey()));
			lst.appendTag(tag);
		}
		
		compound.setTag("data", lst);
	}

	@Override
	public void onUpdate()
	{
		if (((VCSettings)(VoidApi.config.dataHolder)).recoveryMode)
		{
			return;
		}
		
		if (!this.world.isRemote)
		{
			this.getDataManager().set(FALL_ANGLE, this.fallAngle);
			this.getDataManager().set(FALL_PROGRESS, this.fallProgress);
		}
		
		super.onUpdate();
		
		if (this.world.isRemote)
		{
			this.fallAngle = this.getDataManager().get(FALL_ANGLE);
			this.fallProgress = this.getDataManager().get(FALL_PROGRESS);
		}
		
		if (this.ticksExisted >= 2)
		{
			if (this.fallProgress == 0)
			{
				this.fallProgress = 1;
			}
			
			this.fallProgress *= 1.1;
			List<BlockPos> toDelete = Lists.newArrayList();
			boolean doFall = false;
			if (this.fallProgress >= 180)
			{
				doFall = true;
			}
			
			for (Entry<BlockPos, IBlockState> entry : this.data.entrySet())
			{
				Vec3d blockVecBase = new Vec3d(entry.getKey()).subtract(this.posX - 0.5D, this.posY, this.posZ - 0.5D);
				Vec3d blockVecWithPitch = blockVecBase.rotatePitch((float) Math.toRadians(this.fallProgress));
				double ca = Math.cos(Math.toRadians(this.fallAngle));
				double sa = Math.sin(Math.toRadians(this.fallAngle));
				Vec3d blockVecWithYaw = new Vec3d(ca*blockVecWithPitch.xCoord - sa*blockVecWithPitch.zCoord, blockVecWithPitch.yCoord, sa*blockVecWithPitch.xCoord + ca*blockVecWithPitch.zCoord);
				BlockPos worldBP = new BlockPos(new Vec3d(this.posX, this.posY + 1, this.posZ).add(blockVecWithYaw));
				if (worldBP.equals(this.brokenAt))
				{
					continue;
				}
				
				BlockPos below = worldBP.down();
				if (!this.world.isRemote)
				{
					List<EntityLivingBase> mobs = this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(below, worldBP.add(1, 1, 1)));
					if (!mobs.isEmpty())
					{
						mobs.forEach(e -> { e.attackEntityFrom(DamageSource.FALLING_BLOCK, 20); e.knockBack(this, 2, 1, 1); });
					}
				}
				
				IBlockState belowState = this.world.getBlockState(below);
				if (!this.world.isAirBlock(below))
				{
					if (entry.getValue().getBlock() instanceof ILeaves)
					{
						toDelete.add(entry.getKey());
						continue;
					}
					else
					{
						if (this.world.getBlockState(below).isOpaqueCube())
						{
							if (entry.getValue().getBlock() instanceof ILog)
							{
								doFall = true;
								break;
							}
						}
						else
						{
							// TODO block break effects
							if (!this.world.isRemote)
							{
								this.world.setBlockToAir(below);
							}
							
							continue;
						}
					}
				}
			}
			
			for (BlockPos pos : toDelete)
			{
				this.data.remove(toDelete);
			}
			
			if (doFall && !this.world.isRemote)
			{
				this.setDead();
				for (Entry<BlockPos, IBlockState> entry : this.data.entrySet())
				{
					
					
					Vec3d blockVecBase = new Vec3d(entry.getKey()).subtract(this.posX - 0.5D, this.posY, this.posZ - 0.5D);
					Vec3d blockVecWithPitch = blockVecBase.rotatePitch((float) Math.toRadians(this.fallProgress));
					//Vec3d blockVecWithYaw = blockVecWithPitch.rotateYaw((float) Math.toRadians(this.fallAngle));
					double ca = Math.cos(Math.toRadians(this.fallAngle));
					double sa = Math.sin(Math.toRadians(this.fallAngle));
					Vec3d blockVecWithYaw = new Vec3d(ca*blockVecWithPitch.xCoord - sa*blockVecWithPitch.zCoord, blockVecWithPitch.yCoord, sa*blockVecWithPitch.xCoord + ca*blockVecWithPitch.zCoord);
					if (entry.getValue().getBlock() instanceof ILog)
					{
						((ILog)entry.getValue().getBlock()).dropLogItem(this.world, entry.getValue(), new Vec3d(this.posX, this.posY + 0.5, this.posZ).add(blockVecWithYaw));
					}
					//this.world.setBlockState(worldBP, entry.getValue());
				}
			}
		}
		
		if (!this.world.isRemote && this.isDataConstructed && !this.isDataSent && this.ticksExisted >= 2)
		{
			this.isDataSent = true;
			NBTTagCompound tag = new NBTTagCompound();
			this.serializeDataToNBT(tag);
			VoidNetwork.sendDataToAll(ExPPackets.FALLING_TREE, NBTChain.startChain().withTag("dataTag", tag).withString("uuid", this.getPersistentID().toString()).endChain());
			for (BlockPos pos : this.data.keySet())
			{
				this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
			}
		}
	}
	
	@Override
	public BlockPos getPosition()
	{
		return new BlockPos(this.posX - 0.5D, this.posY + 0.5, this.posZ - 0.5D);
	}

	
	@Override
	protected void doBlockCollisions()
	{
		
	}

	@Override
	public void fall(float distance, float damageMultiplier)
	{
		
	}

	@Override
	public void applyEntityCollision(Entity entityIn)
	{
		
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public boolean canBePushed()
	{
		return false;
	}

	@Override
	public boolean isEntityInvulnerable(DamageSource source)
	{
		return true;
	}
}
