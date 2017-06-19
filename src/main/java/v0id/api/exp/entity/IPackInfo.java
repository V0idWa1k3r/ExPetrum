package v0id.api.exp.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 * Pack info for entities. Use {@link v0id.exp.util.EntityPackManager EntityPackManager} to work with the data directly(yes, that is not in the API package). <br>
 * You can iterate over this to get all currently loaded entities that belong to the pack.
 * @author V0idWa1k3r
 */
public interface IPackInfo extends INBTSerializable<NBTTagCompound>, Iterable<EntityLivingBase>
{
    /**
     * Gets the total amount of entities in this pack(including unloaded ones)
     * @return - The amount of entities in this pack
     */
    default int getAmount()
    {
        return this.getEntities().size();
    }

    /**
     * Gets a set of UUIDs that correspond to the entities this info holds. This data is persistent.
     * @return - a set of UUID of entities this info contains.
     */
    Set<UUID> getEntities();

    /**
     * Checks if this data contains no more UUID entries and is safe to delete.
     * @return - a boolean value indicating whether this data contains no more UUID entries and is safe to delete.
     */
    default boolean isEmpty()
    {
        return this.getAmount() == 0;
    }

    /**
     * Adds an animal to the pack. You should not call this manually as this is called from {@link v0id.api.exp.entity.IAnimal#setPack(IPackInfo) IAnimal's setPack method}
     * @param animal : the animal added to the pack.
     */
    void addEntity(@Nonnull IAnimal animal);

    /**
     * Removes an animal from the pack(common cause - pack change, death). You should not call this manually as this is called from {@link v0id.api.exp.entity.IAnimal#setPack(IPackInfo) IAnimal's setPack method}
     * @param animal : the animal removed from the pack.
     */
    void removeEntity(@Nonnull IAnimal animal);

    /**
     * Gets the leader of this pack as an entity if it is loaded.
     * @return - The leader of this pack as an entity if it is loaded.
     */
    Optional<EntityLivingBase> getPackLeaderAsEntity();

    /**
     * Gets the UUID of the pack's leader.
     * @return - The UUID of the pack's leader.
     */
    UUID getPackLeader();

    /**
     * Gets the ID of this pack.
     * @return - The ID of this pack.
     */
    UUID getID();

    /**
     * Gets the total pack reputation towards a specified player. <br>
     * This can be negative.
     * The player must not be null.
     * @param player : the player to get the reputation of.
     * @return - a value indicating pack's reputation towards the player(usually within the [-100 - 100] bounds)
     */
    float getReputation(@Nonnull EntityPlayer player);

    /**
     * Sets the total pack reputation towards a specified player. <br>
     * The player must not be null.
     * @param player : the player to set the reputation of.
     * @param newValue - a new reputation value. Should be kept within the [-100 - 100] bound but can exceed it.
     */
    void setReputation(@Nonnull EntityPlayer player, float newValue);
}
