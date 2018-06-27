package v0id.core.proxy;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import v0id.core.VCLoggers;
import v0id.core.logging.LogLevel;
import v0id.core.util.IItemColorProvider;
import v0id.core.util.itemmodel.AutoModelProvider;
import v0id.core.util.itemmodel.NoAutoModel;
import v0id.core.util.java.IInstanceProvider;
import v0id.core.util.java.Instance;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

@SuppressWarnings("unused")
public class VoidClient implements IVoidProxy
{
	public Map<String, FontRenderer> getFontRenderers()
	{
		return fontRenderers;
	}

	private final Map<String, FontRenderer> fontRenderers = Maps.newHashMap();
	
	@Override
	public String getClientPlayerName()
	{
		return Minecraft.getMinecraft().player.getName();
	}
	
	@Override
	public World getClientWorld()
	{
		return Minecraft.getMinecraft().world;
	}
	
	@Override
	public IThreadListener getClientListener()
	{
		return Minecraft.getMinecraft();
	}
	
	@Override
	public Object getFontRenderer(String s)
	{
		return this.fontRenderers.getOrDefault(s, Minecraft.getMinecraft().fontRenderer);
	}
	
	@Override
	public void registerAllItemModels(Class<?> c)
	{
		for (Field f : c.getDeclaredFields())
		{
			if (Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()))
			{
				if (Item.class.isAssignableFrom(f.getType()))
				{
					if (!f.isAnnotationPresent(NoAutoModel.class))
					{
						try
						{
							Item i = (Item) f.get(null);
							if (i != null)
                            {
                                if (f.isAnnotationPresent(AutoModelProvider.class))
                                {
                                    f.getAnnotation(AutoModelProvider.class).provider().newInstance().provideAll(i);
                                }
                                else
                                {
                                    //noinspection ConstantConditions
                                    ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
                                }
                            }
						}
						catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
				else
				{
					if (Block.class.isAssignableFrom(f.getType()))
					{
						if (!f.isAnnotationPresent(NoAutoModel.class))
						{
							try
							{
								Item i = Item.getItemFromBlock((Block) f.get(null));
                                if (f.isAnnotationPresent(AutoModelProvider.class))
                                {
                                    f.getAnnotation(AutoModelProvider.class).provider().newInstance().provideAll(i);
                                }
                                else
                                {
                                    //noinspection ConstantConditions
                                    ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
                                }
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public void registerItemColor(IItemColorProvider provider, Item...items)
	{
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(provider::getColor, items);
	}
	
	@Override
	public String format(String s, Object...objects)
	{
		return I18n.format(s, objects);
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public Object provideClientOnlyInstance(String classname)
	{
		try
		{
			Class c = Class.forName(classname);
			if (IInstanceProvider.class.isAssignableFrom(c))
			{
				for (Field f : c.getDeclaredFields())
				{
					if (Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && f.isAnnotationPresent(Instance.class))
					{
						if (f.get(null) == null)
						{
							f.set(null, c.newInstance());
						}
						
						return f.get(null);
					}
				}
				
				return c.newInstance();
			}
			else
			{
				return c.newInstance();
			}
		}
		catch(Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Could not obtain an instance of class %s using default constructor ()!", ex, classname);
			return null;
		}
	}
	
	@Override
	public void subscribeClient(String classname, EventBus bus)
	{
		try
		{
			bus.register(Class.forName(classname).newInstance());
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Could not subscribe %s to %s on client-only side!!", ex, classname, bus.toString());
		}
	}
	
	@Override
	public void executeOnClient(String classname, String methodname, Object instance, Object... params)
	{
		try
		{
			if (params == null)
			{
				params = new Object[0];
			}
			
			Class c = Class.forName(classname);
			Class[] paramsClasses = new Class[params.length / 2];
			Object[] paramsObjects = new Object[params.length / 2];
			for (int i = 0; i < params.length; ++i)
			{
				if (i < params.length / 2)
				{
					paramsClasses[i] = (Class) params[i];
				}
				else
				{
					paramsObjects[i - params.length / 2] = params[i];
				}
			}
			
			@SuppressWarnings("unchecked") Method m = c.getDeclaredMethod(methodname, paramsClasses);
			m.invoke(instance, paramsObjects);
		}
		catch (Exception ex)
		{
			VCLoggers.loggerErrors.log(LogLevel.Error, "Could not execute %s.%s on client!", ex, classname, methodname);
		}
	}
	
	@Override
	public int getClientGrassColor(IBlockAccess w, BlockPos pos)
	{
		return BiomeColorHelper.getGrassColorAtPos(w, pos);
	}
	
	@Override
	public float getRenderPartialTicks()
	{
		return Minecraft.getMinecraft().getRenderPartialTicks();
	}
}
