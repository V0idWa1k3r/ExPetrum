package v0id.exp.world.chunk;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.Constants;
import v0id.api.core.util.nbt.NBTChain;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.world.chunk.IExPChunk;

import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
public class ExPChunk implements IExPChunk
{
    public static final ExPChunk EMPTY = new ExPChunk();
    public final Chunk owner;
    public EnumOre dormantOre;
    public float dormantOreAmount;
    public Map<BlockPos, Float> temperatureData = Maps.newHashMap();

    public boolean dormantOre_IsDirty;
    public boolean dormantOreAmount_IsDirty;
    public boolean temperatureData_IsDirty;
    public boolean isDirty;

    private ExPChunk()
    {
        this.owner = null;
    }

    public ExPChunk(Chunk c)
    {
        this.owner = c;
    }

    @Override
    public Chunk getOwner()
    {
        return this.owner;
    }

    @Override
    public EnumOre getDormantOre()
    {
        return this.dormantOre;
    }

    @Override
    public void setDormantOre(EnumOre ore)
    {
        this.dormantOre_IsDirty |= ore != this.dormantOre;
        this.isDirty |= this.dormantOre_IsDirty;
        this.dormantOre = ore;
    }

    @Override
    public float getDormantOreAmount()
    {
        return this.dormantOreAmount;
    }

    @Override
    public void setDormantOreAmount(float f)
    {
        this.dormantOreAmount_IsDirty |= this.dormantOreAmount != f;
        this.isDirty |= this.dormantOreAmount_IsDirty;
        this.dormantOreAmount = f;
    }

    @Override
    public boolean isDirty()
    {
        return this.isDirty;
    }

    @Override
    public void setDirty()
    {
        this.setDirty(true);
    }

    public void setDirty(boolean b)
    {
        this.isDirty = this.dormantOre_IsDirty = this.dormantOreAmount_IsDirty = this.temperatureData_IsDirty = b;
    }

    @Override
    public void onTick()
    {

    }

    @Override
    public Map<BlockPos, Float> getAllTemperatureModifiers()
    {
        return this.temperatureData;
    }

    @Override
    public float getTemperatureModifierAt(BlockPos pos)
    {
        return this.temperatureData.get(pos);
    }

    @Override
    public void setTemperatureModifierAt(BlockPos pos, float f)
    {
        this.temperatureData_IsDirty |= f != this.getTemperatureModifierAt(pos);
        this.isDirty |= this.temperatureData_IsDirty;
        this.temperatureData.put(pos, f);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        this.setDirty(true);
        return this.serializePartialNBT();
    }

    public NBTTagCompound serializePartialNBT()
    {
        NBTTagCompound ret = new NBTTagCompound();
        if (this.dormantOre_IsDirty)
        {
            ret.setByte("oreID", (byte) this.dormantOre.ordinal());
            this.dormantOre_IsDirty = false;
        }

        if (this.dormantOreAmount_IsDirty)
        {
            ret.setFloat("oreAmount", this.dormantOreAmount);
            this.dormantOreAmount_IsDirty = false;
        }

        if (this.temperatureData_IsDirty)
        {
            NBTTagList list = new NBTTagList();
            this.temperatureData.forEach((BlockPos pos, Float f) -> list.appendTag(NBTChain.startChain().withLong("key", pos.toLong()).withFloat("value", f).endChain()));
            ret.setTag("temperatureData", list);
            this.temperatureData_IsDirty = false;
        }

        this.isDirty = false;
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("oreID", Constants.NBT.TAG_BYTE))
        {
            this.dormantOre = EnumOre.values()[nbt.getByte("oreID")];
        }

        if (nbt.hasKey("oreAmount", Constants.NBT.TAG_FLOAT))
        {
            this.dormantOreAmount = nbt.getFloat("oreAmount");
        }

        if (nbt.hasKey("temperatureData", Constants.NBT.TAG_LIST))
        {
            this.temperatureData.clear();
            StreamSupport.stream(nbt.getTagList("temperatureData", Constants.NBT.TAG_COMPOUND).spliterator(), false).map(tag -> (NBTTagCompound)tag).forEach(tag -> this.temperatureData.put(BlockPos.fromLong(tag.getLong("key")), tag.getFloat("value")));
        }
    }
}
