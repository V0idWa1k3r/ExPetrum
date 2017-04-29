package v0id.exp.registry;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import v0id.api.exp.data.ExPFluids;
import v0id.api.exp.data.ExPRegistryNames;

public class ExPFluidRegistry extends AbstractRegistry
{
	public ExPFluidRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		ExPFluids.saltWater = new Fluid(ExPRegistryNames.fluidSaltWater, new ResourceLocation("exp", "blocks/fluid/water_still"), new ResourceLocation("exp", "blocks/fluid/water_flow")){
			
			@Override
			public int getColor()
		    {
		        return 0x9900acff;
		    }
		};
		
		ExPFluids.freshWater = new Fluid(ExPRegistryNames.fluidFreshWater, new ResourceLocation("exp", "blocks/fluid/water_still"), new ResourceLocation("exp", "blocks/fluid/water_flow")){
			
			@Override
			public int getColor()
		    {
		        return 0x99005aff;
		    }
		};
		
		ExPFluids.lava = new Fluid(ExPRegistryNames.fluidLava, new ResourceLocation("exp", "blocks/fluid/lava_still"), new ResourceLocation("exp", "blocks/fluid/lava_flow"))
				.setLuminosity(15).setDensity(3000).setViscosity(6000).setTemperature(1300).setUnlocalizedName(Blocks.LAVA.getUnlocalizedName());
		
		FluidRegistry.registerFluid(ExPFluids.saltWater);
		FluidRegistry.registerFluid(ExPFluids.freshWater);
		FluidRegistry.registerFluid(ExPFluids.lava);
	}
}
