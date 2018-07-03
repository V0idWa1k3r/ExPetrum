package v0id.api.exp.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.security.InvalidParameterException;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 * The generic stats interface for the animal. The implementation differs per animal class.
 * @author V0idWa1k3r
 */
public interface IAnimalStats extends INBTSerializable<NBTTagCompound>
{
    /**
     * Creates a 'mix' of these stats with some other stats. Usually averages all traits and adds random numbers here and there. <br>
     * A common use example is breeding.
     * @param other : the other stats. The implementation must be of the same type as this!
     * @return - the 'mixed' stats between two stats.
     */
    IAnimalStats mix(@Nonnull IAnimalStats other) throws InvalidParameterException;
}
