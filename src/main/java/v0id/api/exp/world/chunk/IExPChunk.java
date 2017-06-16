package v0id.api.exp.world.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;
import v0id.api.exp.block.EnumOre;
import v0id.api.exp.world.IExPWorld;

import java.util.Map;

/**
 * Created by V0idWa1k3r on 16-Jun-17.
 */
public interface IExPChunk extends INBTSerializable<NBTTagCompound>
{
    Chunk getOwner();

    EnumOre getDormantOre();

    void setDormantOre(EnumOre ore);

    float getDormantOreAmount();

    void setDormantOreAmount(float amt);

    boolean isDirty();

    void setDirty();

    void onTick();

    Map<BlockPos, Float> getAllTemperatureModifiers();

    float getTemperatureModifierAt(BlockPos pos);

    void setTemperatureModifierAt(BlockPos pos, float f);

    public static IExPChunk of(Chunk c)
    {
        return IExPWorld.of(c.getWorld()).getChunkDataAt(c.getPos());
    }
}
