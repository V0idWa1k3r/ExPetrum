package v0id.api.exp.gravity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Apply to your Block class
 * @author V0idWa1k3r
 *
 */
public interface IGravitySusceptible
{
	default boolean canFall(World w, IBlockState state, BlockPos pos, BlockPos trigger)
	{
		return !GravityHelper.isSupported(w, pos);
	}
	
	@SuppressWarnings("SameReturnValue")
    default boolean fall(World w, IBlockState state, BlockPos pos, BlockPos trigger)
	{
		return false;
	}
	
	default void onFall(World w, BlockPos fallenAt)
	{
		
	}
	
	@SuppressWarnings("SameReturnValue")
    default boolean destroysNonOpaqueBlocksOnImpact()
	{
		return true;
	}
	
	@SuppressWarnings("SameReturnValue")
    default boolean breaksOnFall()
	{
		return false;
	}
	
	default void onCollided(Entity collidedWith, EntityFallingBlock self)
	{
		collidedWith.attackEntityFrom(DamageSource.FALLING_BLOCK, this.getFallDamage(collidedWith, self));
	}
	
	int getFallDamage(Entity collidedWith, EntityFallingBlock self);
}
