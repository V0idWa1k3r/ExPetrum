package v0id.core.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@FunctionalInterface
public interface IFunctionalItemColor extends IItemColor
{
	@Override
	int colorMultiplier(@Nonnull ItemStack stack, int tintIndex);
	
	static void registerItemColorHandler(IFunctionalItemColor color, Item... itemsIn)
	{
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, itemsIn);
	}

	static void registerItemColorHandler(IFunctionalItemColor color, Block... itemsIn)
	{
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, itemsIn);
	}
}
