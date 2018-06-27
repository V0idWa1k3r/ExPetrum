package v0id.exp.entity;

import com.google.common.collect.Maps;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import v0id.api.core.util.nbt.NBTChain;
import v0id.api.exp.entity.*;
import v0id.api.exp.player.FoodGroup;
import v0id.api.exp.world.Calendar;
import v0id.exp.util.PackInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 */
public class ExPAnimal implements IAnimal
{
    private EntityLivingBase owner;
    private Calendar calendar = new Calendar();
    private final Map<UUID, Float> familiarityLevels = Maps.newHashMap();
    private final Map<UUID, Boolean> interactionMemory = Maps.newHashMap();
    private BlockPos home;
    private UUID packID;
    private IAnimalStats[] offspringStats;
    private float hunger;
    private float thirst;
    private boolean isSick;
    private EnumGender effectiveGender;
    private long effectiveAge;
    private int effectivePregnancyTimer;
    private boolean effectiveIsDomesticated;

    public ExPAnimal()
    {

    }

    public ExPAnimal(EntityLivingBase owner)
    {
        assert owner instanceof IAnimalProvider : "Can't use default IAnimal implementation on non-IAnimalProvider entity!";
        this.owner = owner;
    }

    public IAnimalProvider getAsProvider()
    {
        return (IAnimalProvider) owner;
    }

    @Override
    public EntityLivingBase getOwner()
    {
        return this.owner;
    }

    @Override
    public void onTick()
    {
        if (!this.getOwner().world.isRemote)
        {
            if (this.effectiveAge != this.getOwner().getDataManager().get(this.getAsProvider().getAgeParam()))
            {
                this.getOwner().getDataManager().set(this.getAsProvider().getAgeParam(), this.effectiveAge);
            }

            if ((this.effectiveGender == EnumGender.FEMALE) != this.getOwner().getDataManager().get(this.getAsProvider().getGenderParam()))
            {
                this.getOwner().getDataManager().set(this.getAsProvider().getGenderParam(), this.effectiveGender == EnumGender.FEMALE);
            }

            if (this.effectivePregnancyTimer != this.getOwner().getDataManager().get(this.getAsProvider().getPregnancyParam()))
            {
                this.getOwner().getDataManager().set(this.getAsProvider().getPregnancyParam(), this.effectivePregnancyTimer);
            }

            if (this.effectiveIsDomesticated != this.getOwner().getDataManager().get(this.getAsProvider().getDomesticatedParam()))
            {
                this.getOwner().getDataManager().set(this.getAsProvider().getDomesticatedParam(), this.effectiveIsDomesticated);
            }

            this.setAge(this.getAge() + 1);
        }
    }

    @Override
    public EnumGender getGender()
    {
        return this.owner.getDataManager().get(this.getAsProvider().getGenderParam()) ? EnumGender.FEMALE : EnumGender.MALE;
    }

    @Override
    public void setGender(EnumGender newGender)
    {
        this.effectiveGender = newGender;
    }

    @Override
    public long getAge()
    {
        return this.owner.getDataManager().get(this.getAsProvider().getAgeParam());
    }

    @Override
    public void setAge(long newAge)
    {
        this.effectiveAge = newAge;
    }

    @Override
    public float getDailyDeathChance()
    {
        return 0;
    }

    @Override
    public Calendar getLastTickTime()
    {
        return this.calendar;
    }

    @Override
    public void setLastTickTime(long lastTickTime)
    {
        this.calendar.setTime(lastTickTime);
    }

    @Override
    public boolean isDirty()
    {
        return false;
    }

    @Override
    public float getFamiliarity(@Nonnull EntityPlayer of)
    {
        return this.familiarityLevels.getOrDefault(of.getPersistentID(), 0F);
    }

    @Override
    public float getMaxFamiliarity()
    {
        return this.getAsProvider().getMaxFamiliarity();
    }

    @Override
    public void setFamiliarity(EntityPlayer of, float newFamiliarity)
    {
        newFamiliarity = MathHelper.clamp(newFamiliarity, -100F, this.getMaxFamiliarity());
        this.familiarityLevels.put(of.getPersistentID(), newFamiliarity);
    }

    @Override
    public BlockPos getHome()
    {
        return this.home;
    }

    @Override
    public void setHome(BlockPos newPos)
    {
        this.home = newPos;
    }

    @Nullable
    @Override
    public IPackInfo getPack()
    {
        if (this.isDomesticated())
        {
            return null;
        }

        return packID != null ? PackInfo.provideInfoFor(this, this.packID) : null;
    }

    @Override
    public void setPack(@Nullable IPackInfo pack)
    {
        if (this.isDomesticated())
        {
            return;
        }

        Optional.ofNullable(this.getPack()).ifPresent(p -> p.removeEntity(this));
        Optional.ofNullable(pack).ifPresent(p -> p.addEntity(this));
        this.packID = pack == null ? null : pack.getID();
    }

    @Override
    public FoodGroup[] getFavouriteFood()
    {
        return this.getAsProvider().getFavouriteFood();
    }

    @Override
    public boolean canBreed(EntityLivingBase other)
    {
        if (other != null && this.getOwner().getClass().isAssignableFrom(other.getClass()))
        {
            IAnimal animal = IAnimal.of(other);
            if (animal != null)
            {
                return animal.getGender().isOpposite(this.getGender()) && !this.isPregnant() && !animal.isPregnant();
            }
        }

        return false;
    }

    @Override
    public void breed(EntityLivingBase other)
    {
        // TODO breeding!
    }

    @Override
    public boolean isPregnant()
    {
        return this.getPregnancyTicksLeft() > 0;
    }

    @Nullable
    @Override
    public IAnimalStats[] getOffspringStats()
    {
        return this.offspringStats;
    }

    @Override
    public void setPregnant(EntityLivingBase partner)
    {
        // TODO breeding!
    }

    @Override
    public IAnimalStats getStats()
    {
        return this.getAsProvider().getOrCreateStats();
    }

    @Override
    public void setStats(IAnimalStats stats) throws IllegalArgumentException
    {
        this.getAsProvider().setStats(stats);
    }

    @Override
    public int getPregnancyTicksLeft()
    {
        return this.getOwner().getDataManager().get(this.getAsProvider().getPregnancyParam());
    }

    @Override
    public void giveBirth()
    {
        // TODO breeding!
    }

    @Override
    public float getFood()
    {
        return this.hunger;
    }

    @Override
    public float getThirst()
    {
        return this.thirst;
    }

    @Override
    public void setFood(float newValue)
    {
        this.hunger = MathHelper.clamp(newValue, 0, 100);
    }

    @Override
    public void setThirst(float newValue)
    {
        this.thirst = MathHelper.clamp(newValue, 0, 100);
    }

    @Override
    public boolean wasInteractedWith(@Nonnull EntityPlayer player)
    {
        return this.interactionMemory.getOrDefault(player.getPersistentID(), false);
    }

    @Override
    public void interact(EntityPlayer player)
    {
        this.getAsProvider().processInteraction(player);
        this.interactionMemory.put(player.getPersistentID(), true);
    }

    @Override
    public boolean isSick()
    {
        return this.isSick;
    }

    @Override
    public void setSick(boolean isSick)
    {
        this.isSick = isSick;
    }

    @Override
    public boolean isDomesticated()
    {
        return this.getOwner().getDataManager().get(this.getAsProvider().getDomesticatedParam());
    }

    @Override
    public void onNewDayStarted()
    {
        // TODO handle new day
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setLong("calendar", this.calendar.getTime());
        if (this.packID != null)
        {
            ret.setLong("packMost", this.packID.getMostSignificantBits());
            ret.setLong("packLeast", this.packID.getLeastSignificantBits());
        }

        if (this.home != null)
        {
            ret.setLong("pos", this.home.toLong());
        }

        ret.setFloat("hunger", this.hunger);
        ret.setFloat("thirst", this.thirst);
        ret.setBoolean("isSick", this.isSick);
        if (this.offspringStats != null)
        {
            NBTTagList tagList = new NBTTagList();
            Arrays.stream(this.offspringStats).forEach(stat -> tagList.appendTag(stat.serializeNBT()));
            ret.setTag("offspring", tagList);
        }

        NBTTagList familiarityList = new NBTTagList();
        this.familiarityLevels.forEach((UUID uuid, Float f) -> familiarityList.appendTag(NBTChain.startChain().withLong("keyMost", uuid.getMostSignificantBits()).withLong("keyLeast", uuid.getLeastSignificantBits()).withFloat("value", f).endChain()));
        ret.setTag("familiarity", familiarityList);
        NBTTagList interactionList = new NBTTagList();
        this.interactionMemory.forEach((UUID uuid, Boolean b) -> interactionList.appendTag(NBTChain.startChain().withLong("keyMost", uuid.getMostSignificantBits()).withLong("keyLeast", uuid.getLeastSignificantBits()).withBool("value", b).endChain()));
        ret.setTag("interaction", interactionList);
        ret.setBoolean("gender", this.effectiveGender == EnumGender.FEMALE);
        ret.setLong("age", this.effectiveAge);
        ret.setInteger("pregnancy", this.effectivePregnancyTimer);
        ret.setBoolean("domesticated", this.effectiveIsDomesticated);
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.calendar.setTime(nbt.getLong("calendar"));
        if (nbt.hasKey("packMost", Constants.NBT.TAG_LONG))
        {
            this.packID = new UUID(nbt.getLong("packMost"), nbt.getLong("packLeast"));
        }

        if (nbt.hasKey("pos"))
        {
            this.home = BlockPos.fromLong(nbt.getLong("pos"));
        }

        if (nbt.hasKey("offspring", Constants.NBT.TAG_LIST))
        {
            NBTTagList offspringList = nbt.getTagList("offspring", Constants.NBT.TAG_COMPOUND);
            this.offspringStats = new IAnimalStats[offspringList.tagCount()];
            for (int i = 0; i < offspringList.tagCount(); i++)
            {
                NBTTagCompound tag = offspringList.getCompoundTagAt(i);
                try
                {
                    this.offspringStats[i] = (IAnimalStats) Class.forName(tag.getString("class")).newInstance();
                    this.offspringStats[i].deserializeNBT(tag);
                }
                catch (Exception ignored)
                {

                }
            }
        }

        this.hunger = nbt.getFloat("hunger");
        this.thirst = nbt.getFloat("thirst");
        this.isSick = nbt.getBoolean("isSick");
        StreamSupport.stream(nbt.getTagList("familiarity", Constants.NBT.TAG_COMPOUND).spliterator(), false).map(n -> (NBTTagCompound)n).forEach(tag -> this.familiarityLevels.put(new UUID(tag.getLong("keyMost"), tag.getLong("keyLeast")), tag.getFloat("value")));
        StreamSupport.stream(nbt.getTagList("interaction", Constants.NBT.TAG_COMPOUND).spliterator(), false).map(n -> (NBTTagCompound)n).forEach(tag -> this.interactionMemory.put(new UUID(tag.getLong("keyMost"), tag.getLong("keyLeast")), tag.getBoolean("value")));
        this.effectiveGender = nbt.getBoolean("gender") ? EnumGender.FEMALE : EnumGender.MALE;
        this.effectiveAge = nbt.getLong("age");
        this.effectivePregnancyTimer = nbt.getInteger("pregnancy");
        this.effectiveIsDomesticated = nbt.getBoolean("domesticated");
    }

    public static ExPAnimal createDefault()
    {
        return new ExPAnimal();
    }
}
