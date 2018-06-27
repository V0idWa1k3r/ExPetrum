package v0id.exp.item;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import v0id.api.exp.block.EnumBerry;
import v0id.api.exp.block.EnumShrubType;
import v0id.api.exp.block.EnumTreeType;
import v0id.api.exp.data.ExPCreativeTabs;
import v0id.api.exp.data.ExPOreDict;
import v0id.api.exp.data.ExPRegistryNames;
import v0id.api.exp.data.IOreDictEntry;
import v0id.api.exp.inventory.IWeightProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class ItemStick extends Item implements IInitializableItem, IWeightProvider, IOreDictEntry
{
	public ItemStick()
	{
		super();
		this.initItem();
	}

	@Override
	public float provideWeight(ItemStack item)
	{
		return 0.01F;
	}

	@Override
	public Pair<Byte, Byte> provideVolume(ItemStack item)
	{
		return IWeightProvider.DEFAULT_VOLUME;
	}

	@Override
	public void initItem()
	{
		this.setRegistryName(ExPRegistryNames.asLocation(ExPRegistryNames.itemStick));
		this.setUnlocalizedName(this.getRegistryName().toString().replace(':', '.'));
		this.setCreativeTab(ExPCreativeTabs.tabPlantlife);
		this.setHasSubtypes(true);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (tab != this.getCreativeTab())
		{
			return;
		}

		Stream.of(EnumTreeType.values()).forEach(c -> subItems.add(new ItemStack(this, 1, c.ordinal())));
		Stream.of(EnumShrubType.values()).forEach(c -> subItems.add(new ItemStack(this, 1, EnumTreeType.values().length + c.ordinal())));
		Stream.of(EnumBerry.values()).forEach(c -> subItems.add(new ItemStack(this, 1, EnumTreeType.values().length + EnumShrubType.values().length + c.ordinal())));
	}

	@Override
	public void registerOreDictNames()
	{
		Stream.of(ExPOreDict.itemStick).forEach(s -> { 
			OreDictionary.registerOre(s, new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE)); 
			AtomicInteger i = new AtomicInteger(0);
			Stream.of(ExPOreDict.stickNames).forEach(ss -> OreDictionary.registerOre(s + Character.toUpperCase(ss.charAt(0)) + ss.substring(1), new ItemStack(this, 1, i.getAndIncrement())));
		});
	}

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ArrayList<HitData> lst = rayTraceEntities(worldIn, playerIn, playerIn.getPositionVector(), playerIn.getLookVec().normalize(), 128, 1);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private static final Predicate<? super Entity> entityPredicate = Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity entity) { return entity != null && entity.canBeCollidedWith() && entity instanceof EntityLivingBase; }
    });

    public static ArrayList<HitData> rayTraceEntities(World world, EntityPlayer shooter, Vec3d startPos, Vec3d dirVecNormal, int length, int maxHitCount)
    {
        Vec3d startVec = new Vec3d(startPos.x, startPos.y + shooter.getEyeHeight(), startPos.z);
        Vec3d endVec   = startVec.add(dirVecNormal.scale(length));

        RayTraceResult result = world.rayTraceBlocks(startVec, endVec, false, true, false);

        Vec3d aabbVec1 = startVec.add(new Vec3d(0, -1, 0));
        Vec3d aabbVec2 = endVec.add(new Vec3d(0, 1, 0));

        AxisAlignedBB bb = new AxisAlignedBB(aabbVec1, result != null ? result.hitVec : aabbVec2);
        List<Entity> entities = world.getEntitiesInAABBexcluding(shooter, bb, entityPredicate);

        ArrayList<HitData> entitiesInVector = new ArrayList<>();
        Vec3d endVecEntityCheck = result != null ? result.hitVec : endVec;

        for (Entity entity : entities)
        {
            EntityLivingBase victim = (EntityLivingBase)entity;
            if (!victim.noClip && victim.getEntityBoundingBox() != null)
            {
                RayTraceResult intercept = victim.getEntityBoundingBox().calculateIntercept(startVec, endVecEntityCheck);
                if (intercept != null)
                {
                    intercept.hitInfo = true;
                    entitiesInVector.add(new HitData(victim, intercept, shooter));
                    if (entitiesInVector.size() >= maxHitCount) { break; }
                }
            }
        }

        return entitiesInVector;
    }
}
