package v0id.exp.net;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import v0id.api.exp.data.ExPPackets;
import v0id.api.exp.recipe.RecipesPottery;
import v0id.core.network.IPacketHandler;
import v0id.core.network.VoidNetwork;
import v0id.exp.tile.TilePotteryStation;
import v0id.exp.util.OreDictManager;

import java.util.Arrays;

public class PacketHandlerCraftPottery implements IPacketHandler
{
	@Override
	public void handleData(NBTTagCompound data)
	{
		World w = DimensionManager.getWorld(data.getInteger("world"));
        BlockPos pos = new BlockPos(data.getInteger("posX"), data.getInteger("posY"), data.getInteger("posZ"));
        TileEntity tile = w.getTileEntity(pos);
        if (tile != null && tile instanceof TilePotteryStation)
        {
            TilePotteryStation potteryStation = (TilePotteryStation) tile;
            ItemStack is = potteryStation.inventory.getStackInSlot(0);
            if (!is.isEmpty() && Arrays.stream(OreDictManager.getOreNames(is)).anyMatch(s -> s.equalsIgnoreCase("clay")) && is.getCount() == 8)
            {
                potteryStation.inventory.setStackInSlot(0, RecipesPottery.allRecipes.get(data.getInteger("recipe")).getItem().copy());
            }
        }
	}

	@Override
	public NBTTagCompound handleRequest(String requester)
	{
		return null;
	}

	public static void sendSyncPacket(int recipe, TilePotteryStation tile)
	{
	    NBTTagCompound tag = new NBTTagCompound();
	    tag.setInteger("world", tile.getWorld().provider.getDimension());
	    tag.setInteger("posX", tile.getPos().getX());
	    tag.setInteger("posY", tile.getPos().getY());
	    tag.setInteger("posZ", tile.getPos().getZ());
	    tag.setInteger("recipe", recipe);
		VoidNetwork.sendDataToServer(ExPPackets.CRAFT_POTTERY, tag);
	}
	
	public static void sendRequestPacket()
	{
	}
}
