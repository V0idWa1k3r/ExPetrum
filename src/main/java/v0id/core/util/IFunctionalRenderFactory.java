package v0id.core.util;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
@FunctionalInterface
public interface IFunctionalRenderFactory<T extends Entity> extends IRenderFactory<T>
{
	@Override
	Render<? super T> createRenderFor(RenderManager manager);
	
	static <T extends Entity>void registerEntityRenderingHandler(Class<T> clazz, IFunctionalRenderFactory<T> factory)
	{
		RenderingRegistry.registerEntityRenderingHandler(clazz, factory);
	}
}
