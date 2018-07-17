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
	static
	{
		ExPFluids.saltWater = new Fluid(ExPRegistryNames.fluidSaltWater, new ResourceLocation("exp", "blocks/fluid/water_still"), new ResourceLocation("exp", "blocks/fluid/water_flow")).setColor(0x9900acff);
		FluidRegistry.WATER.setColor(0x99005aff);
		ExPFluids.freshWater = new Fluid(ExPRegistryNames.fluidFreshWater, new ResourceLocation("exp", "blocks/fluid/water_still"), new ResourceLocation("exp", "blocks/fluid/water_flow")).setColor(0x99005aff);

		ExPFluids.lava = new Fluid(ExPRegistryNames.fluidLava, new ResourceLocation("exp", "blocks/fluid/lava_still"), new ResourceLocation("exp", "blocks/fluid/lava_flow"))
				.setLuminosity(15).setDensity(3000).setViscosity(6000).setTemperature(1300).setUnlocalizedName(Blocks.LAVA.getUnlocalizedName()).setColor(0xffffffff);

		ExPFluids.oil = new Fluid(ExPRegistryNames.fluidOil, new ResourceLocation("exp", "blocks/fluid/generic"), new ResourceLocation("exp", "blocks/fluid/generic"))
				.setDensity(900).setViscosity(8000).setColor(0xff000000);

		ExPFluids.clay = new Fluid(ExPRegistryNames.fluidClay, new ResourceLocation("exp", "blocks/fluid/clay"), new ResourceLocation("exp", "blocks/fluid/clay"))
            .setDensity(1746).setViscosity(20000).setColor(0xff8b6d4a);

		ExPFluids.milk = new Fluid(ExPRegistryNames.fluidMilk, new ResourceLocation("exp", "blocks/fluid/generic"), new ResourceLocation("exp", "blocks/fluid/generic"))
				.setDensity(1000).setViscosity(3000).setColor(0x66ffffff);

		ExPFluids.tannin = new Fluid(ExPRegistryNames.fluidTannin, new ResourceLocation("exp", "blocks/fluid/generic"), new ResourceLocation("exp", "blocks/fluid/generic"))
				.setDensity(1000).setViscosity(1000).setColor(0x6634250b);

		ExPFluids.oliveOil = new Fluid(ExPRegistryNames.fluidOliveOil, new ResourceLocation("exp", "blocks/fluid/generic"), new ResourceLocation("exp", "blocks/fluid/generic"))
				.setDensity(1000).setViscosity(1000).setColor(0x66dbe200);

		ExPFluids.walnutOil = new Fluid(ExPRegistryNames.fluidWalnutOil, new ResourceLocation("exp", "blocks/fluid/generic"), new ResourceLocation("exp", "blocks/fluid/generic"))
				.setDensity(1000).setViscosity(1000).setColor(0x66dbe200);

        ExPFluids.juice = new Fluid(ExPRegistryNames.fluidJuice, new ResourceLocation("exp", "blocks/fluid/generic"), new ResourceLocation("exp", "blocks/fluid/generic"))
                .setDensity(1000).setViscosity(1000).setColor(0x66ab3db8);

		ExPFluids.brine = new Fluid(ExPRegistryNames.fluidBrine, new ResourceLocation("exp", "blocks/fluid/generic"), new ResourceLocation("exp", "blocks/fluid/generic"))
				.setDensity(1000).setViscosity(1000).setColor(0x66308d8d);

	}
	
	public ExPFluidRegistry()
	{
		super();
	}

	@Override
	public void preInit(FMLPreInitializationEvent evt)
	{
		super.preInit(evt);
		FluidRegistry.registerFluid(ExPFluids.saltWater);
		FluidRegistry.registerFluid(ExPFluids.freshWater);
		FluidRegistry.registerFluid(ExPFluids.lava);
		FluidRegistry.registerFluid(ExPFluids.oil);
		FluidRegistry.registerFluid(ExPFluids.clay);
		FluidRegistry.registerFluid(ExPFluids.milk);
		FluidRegistry.registerFluid(ExPFluids.tannin);
		FluidRegistry.registerFluid(ExPFluids.oliveOil);
		FluidRegistry.registerFluid(ExPFluids.walnutOil);
		FluidRegistry.registerFluid(ExPFluids.juice);
		FluidRegistry.registerFluid(ExPFluids.brine);
	}
}
