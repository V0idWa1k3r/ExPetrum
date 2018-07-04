package v0id.exp.entity;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;
import v0id.api.exp.player.EnumFoodGroup;
import v0id.exp.item.ItemFood;

import java.util.Map;

public class ExPAITempt extends EntityAITempt
{
    private final EnumFoodGroup[] temptGroup;

    public ExPAITempt(EntityCreature temptedEntityIn, double speedIn, boolean scaredByPlayerMovementIn, EnumFoodGroup... temptItemIn)
    {
        super(temptedEntityIn, speedIn, scaredByPlayerMovementIn, Sets.newHashSet());
        this.temptGroup = temptItemIn;
    }

    @Override
    protected boolean isTempting(ItemStack stack)
    {
        if (stack.getItem() instanceof ItemFood)
        {
            ItemFood food = (ItemFood) stack.getItem();
            for (Map.Entry<EnumFoodGroup, Float> entry : food.getEntry(stack).getNutrientData().entrySet())
            {
                if (ArrayUtils.contains(this.temptGroup, entry.getKey()))
                {
                    return true;
                }
            }
        }

        return false;
    }
}