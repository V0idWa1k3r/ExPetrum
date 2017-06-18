package v0id.exp.client.registry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import v0id.api.exp.data.ExPBlockProperties;
import v0id.api.exp.tile.crop.EnumCrop;
import v0id.exp.block.plant.BlockGenericShrubbery;

/**
 * Created by V0idWa1k3r on 18-Jun-17.
 */
public class ExPStateMappers
{
    public static class StateMapperCrop extends StateMapperBase
    {
        final Block b;

        public StateMapperCrop(Block b)
        {
            super();
            this.b = b;
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            EnumCrop cropType = state.getValue(ExPBlockProperties.CROP_TYPE);
            return new ModelResourceLocation(new ResourceLocation(this.b.getRegistryName().getResourceDomain(), String.format("crops/%s", cropType.getName())), cropType.getData() == null ? "normal" : String.format("stage=%d", Math.min(state.getValue(ExPBlockProperties.CROP_GROWTH_STAGE), cropType.getData().growthStages - 1)));
        }
    }

    public static class StateMapperFluid extends StateMapperBase
    {
        final Block b;

        public StateMapperFluid(Block b)
        {
            super();
            this.b = b;
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            return new ModelResourceLocation(this.b.getRegistryName(), "fluid");
        }
    }

    public static class StateMapperCattail extends StateMapperBase
    {
        public StateMapperCattail()
        {
            super();
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            return new ModelResourceLocation("exp:cattail");
        }
    }

    public static class StateMapperGenericShrubbery extends StateMapperBase
    {
        private final Block b;

        public StateMapperGenericShrubbery(Block b)
        {
            super();
            this.b = b;
        }

        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state)
        {
            switch (state.getValue(ExPBlockProperties.SHRUBBERY_TYPE))
            {
                case TROPICAL:
                {
                    return new ModelResourceLocation("exp:tropical_shrubbery", String.format("color=%s,leaf=%d", state.getValue(BlockGenericShrubbery.BLOOM_COLOR).getName(), state.getValue(BlockGenericShrubbery.TROPIC_PLANT_LEAF)));
                }

                case FLOWER:
                {
                    return new ModelResourceLocation("exp:flower_shrubbery", String.format("color=%s", state.getValue(BlockGenericShrubbery.BLOOM_COLOR).getName()));
                }

                case SMALL_SHRUB:
                {
                    return new ModelResourceLocation("exp:small_shrubbery", String.format("color=%s", state.getValue(BlockGenericShrubbery.BLOOM_COLOR).getName()));
                }

                case MUSHROOM:
                {
                    return new ModelResourceLocation("exp:mushroom_shrubbery", String.format("color=%s", state.getValue(BlockGenericShrubbery.BLOOM_COLOR).getName()));
                }

                default:
                {
                    return new ModelResourceLocation(b.getRegistryName(), "normal");
                }
            }
        }
    }
}
