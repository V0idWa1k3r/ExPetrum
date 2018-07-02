package v0id.api.exp.metal;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.function.Consumer;

public class AnvilMinigame
{
    public static final Card BEND = new Card("exp.bend.name", "exp.bend.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            progressIncrementer.accept(10);
            this.damageHammer(hammer, player, 1);
            this.changeIntegrity(ingot, -5);
            this.changeLostIntegrity(ingot, 2);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card UPSET = new Card("exp.upset.name", "exp.upset.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            progressIncrementer.accept(5);
            this.damageHammer(hammer, player, 2);
            this.changeIntegrity(ingot, -5);
            this.changeLostIntegrity(ingot, 2);
            this.changeHeat(ingot, 1);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card PUNCH = new Card("exp.punch.name", "exp.punch.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            progressIncrementer.accept(5);
            this.damageHammer(hammer, player, 1);
            this.changeIntegrity(ingot, -15);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card SHARPEN = new Card("exp.sharpen.name", "exp.sharpen.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            progressIncrementer.accept(5);
            this.damageHammer(hammer, player, 1);
            this.changeIntegrity(ingot, -10);
            this.changeLostIntegrity(ingot, 3);
            this.changeHeat(ingot, 1);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card CLEAR = new Card("exp.clear.name", "exp.clear.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            this.damageHammer(hammer, player, 1);
            this.changeIntegrity(ingot, 5);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card HIT = new Card("exp.hit.name", "exp.hit.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            progressIncrementer.accept(10);
            this.damageHammer(hammer, player, 1);
            this.changeIntegrity(ingot, -5);
            this.changeHeat(ingot, 1);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card SWAG = new Card("exp.swag.name", "exp.swag.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            this.damageHammer(hammer, player, 5);
            progressIncrementer.accept(5);
            this.changeIntegrity(ingot, 5);
            this.changeLostIntegrity(ingot, 5);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card SWIPE = new Card("exp.swipe.name", "exp.swipe.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            this.damageHammer(hammer, player, 3);
            this.changeIntegrity(ingot, -20);
            this.changeLostIntegrity(ingot, 10);
            this.changeHeat(ingot, 1);
            progressIncrementer.accept(20);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card MEND = new Card("exp.mend.name", "exp.mend.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            this.damageHammer(hammer, player, 1);
            this.changeIntegrity(ingot, 10);
            this.changeHeat(ingot, 1);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }

        @Override
        public int getWeight(int heat)
        {
            return 5;
        }
    };

    public static final Card EXTRACT = new Card("exp.extract.name", "exp.extract.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            this.damageHammer(hammer, player, 1);
            this.changeIntegrity(ingot, -10);
            this.changeHeat(ingot, -1);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card COMBINE = new Card("exp.combine.name", "exp.combine.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            this.damageHammer(hammer, player, 3);
            this.changeIntegrity(ingot, -10);
            this.changeLostIntegrity(ingot, 5);
            progressIncrementer.accept(20);
            this.changeHeat(ingot, 1);
            world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 2.0F);
        }
    };

    public static final Card MELT = new Card("exp.melt.name", "exp.melt.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            this.changeHeat(ingot, 2);
            this.changeIntegrity(ingot, 20);
            world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    };

    public static final Card OVERHEAT = new Card("exp.overheat.name", "exp.overheat.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            player.attackEntityFrom(DamageSource.ON_FIRE, 2);
        }

        @Override
        public int getWeight(int heat)
        {
            return super.getWeight(heat) * heat;
        }
    };

    public static final Card OVERHEAT2 = new Card("exp.overheat.name", "exp.overheat2.desc")
    {
        @Override
        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
            super.doEffects(ingot, hammer, player, world, pos, progressIncrementer);
            this.changeIntegrity(ingot, -10);
        }

        @Override
        public int getWeight(int heat)
        {
            return super.getWeight(heat) * heat;
        }
    };

    public static void ensureAllCardsAreRegistered()
    {
        if (Card.allCards.size() == 0)
        {
            throw new IllegalStateException("Impossible exception - ExPetrum registered no cards!");
        }
    }

    public static class Card
    {
        public static final List<Card> allCards = Lists.newArrayList();

        public String name;
        public String desc;
        public int id;

        public Card(String s, String s1)
        {
            this.name = s;
            this.desc = s1;
            this.id = allCards.size();
            allCards.add(this);
        }

        public String getName()
        {
            return name;
        }

        public String getDesc()
        {
            return desc;
        }

        public void doEffects(ItemStack ingot, ItemStack hammer, EntityPlayerMP player, WorldServer world, BlockPos pos, Consumer<Integer> progressIncrementer)
        {
        }

        private void ensureTagExists(ItemStack ingot)
        {
            if (!ingot.hasTagCompound())
            {
                ingot.setTagCompound(new NBTTagCompound());
            }

            if (!ingot.getTagCompound().hasKey("exp:smithing"))
            {
                ingot.getTagCompound().setTag("exp:smithing", new NBTTagCompound());
            }
        }

        public void damageHammer(ItemStack hammer, EntityPlayerMP playerMP, int by)
        {
            hammer.damageItem(by, playerMP);
        }

        public void changeIntegrity(ItemStack ingot, int by)
        {
            this.ensureTagExists(ingot);
            NBTTagCompound tag = ingot.getTagCompound().getCompoundTag("exp:smithing");
            tag.setInteger("integrity", tag.getInteger("integrity") + by);
            if (tag.getInteger("integrity") <= 0)
            {
                ingot.shrink(1);
            }
        }

        public void changeLostIntegrity(ItemStack ingot, int by)
        {
            this.ensureTagExists(ingot);
            NBTTagCompound tag = ingot.getTagCompound().getCompoundTag("exp:smithing");
            tag.setInteger("integrityLost", tag.getInteger("integrityLost") + by);
        }

        public void changeHeat(ItemStack ingot, int by)
        {
            this.ensureTagExists(ingot);
            NBTTagCompound tag = ingot.getTagCompound().getCompoundTag("exp:smithing");
            tag.setInteger("heat", tag.getInteger("heat") + by);
        }

        public int getWeight(int heat)
        {
            return 10;
        }
    }
}
