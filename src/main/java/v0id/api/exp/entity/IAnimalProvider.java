package v0id.api.exp.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import v0id.api.exp.player.FoodGroup;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 * An interface that ExP animals inherit. The default IAnimal implementation makes use of it.
 * @author V0idWa1k3r
 */
public interface IAnimalProvider
{
    float getMaxFamiliarity();

    FoodGroup[] getFavouriteFood();

    DataParameter<Boolean> getGenderParam();

    DataParameter<Long> getAgeParam();

    DataParameter<Integer> getPregnancyParam();

    DataParameter<Boolean> getDomesticatedParam();

    long getRandomPregnancyTicks();

    IAnimalStats getOrCreateStats();

    void setStats(IAnimalStats newStats) throws IllegalArgumentException;

    void processInteraction(EntityPlayer interactor);
}