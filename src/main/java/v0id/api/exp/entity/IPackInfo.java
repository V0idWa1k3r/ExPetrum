package v0id.api.exp.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 */
public interface IPackInfo extends INBTSerializable<NBTTagCompound>, Iterable<EntityLivingBase>
{
    int getAmount();

    Set<UUID> getEntities();

    default boolean isEmpty()
    {
        return this.getAmount() == 0;
    }

    void addEntity(IAnimal animal);

    void removeEntity(IAnimal animal);

    Optional<EntityLivingBase> getPackLeaderAsEntity();

    UUID getPackLeader();
}
