package v0id.exp.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import v0id.api.exp.data.ExPPackets;
import v0id.api.exp.recipe.RecipesAnvil;
import v0id.core.network.IPacketHandler;
import v0id.core.network.VoidNetwork;
import v0id.exp.tile.TileAnvil;

public class PacketHandlerAnvilRecipe implements IPacketHandler
{
	@Override
	public void handleData(NBTTagCompound data)
	{
		World w = DimensionManager.getWorld(data.getInteger("world"));
        BlockPos pos = new BlockPos(data.getInteger("posX"), data.getInteger("posY"), data.getInteger("posZ"));
        w.getMinecraftServer().addScheduledTask(() ->
        {
            TileEntity tile = w.getTileEntity(pos);
            if (tile instanceof TileAnvil)
            {
                ((TileAnvil) tile).prepareIngot(RecipesAnvil.allRecipes.get(data.getInteger("recipe")));
            }
        });
	}

	@Override
	public NBTTagCompound handleRequest(String requester)
	{
		return null;
	}

	public static void sendPacket(TileAnvil tile, RecipesAnvil.IAnvilRecipe recipe)
	{
	    NBTTagCompound tag = new NBTTagCompound();
	    tag.setInteger("world", tile.getWorld().provider.getDimension());
	    tag.setInteger("posX", tile.getPos().getX());
	    tag.setInteger("posY", tile.getPos().getY());
	    tag.setInteger("posZ", tile.getPos().getZ());
	    tag.setInteger("recipe", recipe.getID());
		VoidNetwork.sendDataToServer(ExPPackets.ANVIL_RECIPE, tag);
	}
	
	public static void sendRequestPacket()
	{
	}
}
