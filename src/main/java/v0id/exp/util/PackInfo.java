package v0id.exp.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import v0id.api.exp.entity.IAnimal;
import v0id.api.exp.entity.IPackInfo;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.stream.StreamSupport;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 */
public class PackInfo implements IPackInfo
{
    private final Set<UUID> entitiesSet = Sets.newHashSet();
    private UUID leaderUUID;
    private UUID packID;
    private final Map<UUID, Float> playerReps = Maps.newHashMap();

    public PackInfo()
    {

    }

    public PackInfo(UUID id)
    {
        packID = id;
    }

    @Override
    public Set<UUID> getEntities()
    {
        return entitiesSet;
    }

    @Override
    public void addEntity(@Nonnull IAnimal animal)
    {
        if (!entitiesSet.contains(animal.getOwner().getPersistentID()))
        {
            entitiesSet.add(animal.getOwner().getPersistentID());
        }
    }

    @Override
    public void removeEntity(@Nonnull IAnimal animal)
    {
        entitiesSet.remove(animal.getOwner().getPersistentID());
    }

    WeakReference<EntityLivingBase> refPackLeader;

    @Override
    public Optional<EntityLivingBase> getPackLeaderAsEntity()
    {
        if (this.leaderUUID == null)
        {
            return Optional.empty();
        }

        if (refPackLeader == null || refPackLeader.get() == null)
        {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            for (WorldServer world : server.worlds)
            {
                Entity e = world.getEntityFromUuid(this.leaderUUID);
                if (e != null && e instanceof EntityLivingBase)
                {
                    this.refPackLeader = new WeakReference<>((EntityLivingBase) e);
                    break;
                }
            }
        }

        return Optional.ofNullable(refPackLeader.get());
    }

    @Override
    public UUID getPackLeader()
    {
        return this.leaderUUID;
    }

    @Override
    public UUID getID()
    {
        if (packID == null)
        {
            packID = UUID.randomUUID();
        }

        return packID;
    }

    @Override
    public float getReputation(@Nonnull EntityPlayer player)
    {
        return playerReps.getOrDefault(player.getPersistentID(), 0F);
    }

    @Override
    public void setReputation(@Nonnull EntityPlayer player, float newValue)
    {
        playerReps.put(player.getPersistentID(), newValue);
    }

    @Override
    public Iterator<EntityLivingBase> iterator()
    {
        return new Iterator<EntityLivingBase>()
        {
            final Iterator<UUID> iter = PackInfo.this.entitiesSet.iterator();
            final WorldServer world = (WorldServer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
            @Override
            public boolean hasNext()
            {
                return iter.hasNext();
            }

            @Override
            public EntityLivingBase next()
            {
                UUID next = iter.next();
                Entity e = world.getEntityFromUuid(next);
                return e != null && e instanceof EntityLivingBase ? (EntityLivingBase) e : null;
            }
        };
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setLong("idLeast", this.packID.getLeastSignificantBits());
        ret.setLong("idMost", this.packID.getMostSignificantBits());
        ret.setLong("leaderLeast", this.leaderUUID.getLeastSignificantBits());
        ret.setLong("leaderMost", this.leaderUUID.getMostSignificantBits());
        NBTTagList listEnts = new NBTTagList();
        this.entitiesSet.forEach(uuid ->
        {
            listEnts.appendTag(new NBTTagLong(uuid.getMostSignificantBits()));
            listEnts.appendTag(new NBTTagLong(uuid.getLeastSignificantBits()));
        });

        ret.setTag("entities", listEnts);
        NBTTagList listPlayerReps = new NBTTagList();
        this.playerReps.forEach((UUID uuid, Float rep) ->
        {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setLong("keyLeast", uuid.getLeastSignificantBits());
            tagCompound.setLong("keyMost", uuid.getMostSignificantBits());
            tagCompound.setFloat("value", rep);
            listPlayerReps.appendTag(tagCompound);
        });

        ret.setTag("playerReps", listPlayerReps);
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.packID = new UUID(nbt.getLong("idMost"), nbt.getLong("idLeast"));
        this.leaderUUID = new UUID(nbt.getLong("leaderMost"), nbt.getLong("leaderLeast"));
        this.entitiesSet.clear();
        Iterator<NBTBase> iter = nbt.getTagList("entities", Constants.NBT.TAG_LONG).iterator();
        while (iter.hasNext())
        {
            this.entitiesSet.add(new UUID(((NBTTagLong)iter.next()).getLong(), ((NBTTagLong)iter.next()).getLong()));
        }

        this.playerReps.clear();
        StreamSupport.stream(nbt.getTagList("playerReps", Constants.NBT.TAG_COMPOUND).spliterator(), false).map(n -> (NBTTagCompound)n).forEach(tag -> this.playerReps.put(new UUID(tag.getLong("keyMost"), tag.getLong("keyLeast")), tag.getFloat("value")));
    }

    public static IPackInfo provideInfoFor(IAnimal requester, UUID packID)
    {
        return EntityPackManager.getLoadOrCreatePackInfo(packID, PackInfo.class, PackInfo::new);
    }
}
