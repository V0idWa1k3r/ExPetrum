package v0id.exp.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import v0id.api.exp.entity.DataSerializerLong;
import v0id.api.exp.entity.ExPAnimalCapability;
import v0id.api.exp.entity.IAnimal;
import v0id.api.exp.entity.IAnimalProvider;

import javax.annotation.Nullable;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 */
public abstract class EntityAnimal extends EntityCreature implements IAnimals, IAnimalProvider
{
    public static final DataParameter<Boolean> PARAM_GENDER = EntityDataManager.createKey(EntityAnimal.class, DataSerializers.BOOLEAN);
    public static final DataParameter<Long> PARAM_AGE = EntityDataManager.createKey(EntityAnimal.class, DataSerializerLong.LONG);
    public static final DataParameter<Integer> PARAM_PREGNANCY = EntityDataManager.createKey(EntityAnimal.class, DataSerializers.VARINT);
    public static final DataParameter<Boolean> PARAM_DOMESTICATED = EntityDataManager.createKey(EntityAnimal.class, DataSerializers.BOOLEAN);
    public IAnimal animalCapability = new ExPAnimal(this);

    public EntityAnimal(World worldIn)
    {
        super(worldIn);
    }

    @Override
    public DataParameter<Boolean> getGenderParam()
    {
        return PARAM_GENDER;
    }

    @Override
    public DataParameter<Long> getAgeParam()
    {
        return PARAM_AGE;
    }

    @Override
    public DataParameter<Integer> getPregnancyParam()
    {
        return PARAM_PREGNANCY;
    }

    @Override
    public DataParameter<Boolean> getDomesticatedParam()
    {
        return PARAM_DOMESTICATED;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPAnimalCapability.animalCap || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        return capability == ExPAnimalCapability.animalCap ? ExPAnimalCapability.animalCap.cast(animalCapability) : super.getCapability(capability, facing);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setTag("animalData", this.animalCapability.serializeNBT());
        compound.setLong("age", this.getDataManager().get(PARAM_AGE));
        compound.setBoolean("gender", this.getDataManager().get(PARAM_GENDER));
        compound.setInteger("pregnancy", this.getDataManager().get(PARAM_PREGNANCY));
        compound.setBoolean("domesticated", this.getDataManager().get(PARAM_DOMESTICATED));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.animalCapability.deserializeNBT(compound.getCompoundTag("animalData"));
        this.getDataManager().set(PARAM_AGE, compound.getLong("age"));
        this.getDataManager().set(PARAM_GENDER, compound.getBoolean("gender"));
        this.getDataManager().set(PARAM_PREGNANCY, compound.getInteger("pregnancy"));
        this.getDataManager().set(PARAM_DOMESTICATED, compound.getBoolean("domesticated"));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.getDataManager().register(PARAM_GENDER, false);
        this.getDataManager().register(PARAM_AGE, 0L);
        this.getDataManager().register(PARAM_DOMESTICATED, false);
        this.getDataManager().register(PARAM_PREGNANCY, 0);
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        if (!this.world.isRemote)
        {
            this.animalCapability.onTick();
        }
    }

    @Nullable
    public abstract ResourceLocation getMainTexture(float partialTicks);

    public abstract ResourceLocation getColorableFeaturesTexture(float partialTicks);

    public abstract ResourceLocation getGenderSpecificTextures(float partialTicks);

    public abstract float[] getFeatureColors(float partialTicks);
}
