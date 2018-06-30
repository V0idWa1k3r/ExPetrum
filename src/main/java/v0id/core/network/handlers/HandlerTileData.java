package v0id.core.network.handlers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import v0id.core.VoidApi;
import v0id.core.network.IPacketHandler;
import v0id.core.util.DimBlockPos;
import v0id.core.util.MC;

import java.util.Optional;

public class HandlerTileData implements IPacketHandler {

	@Override
	public void handleData(NBTTagCompound data) 
	{
		DimBlockPos dbp = new DimBlockPos();
		dbp.deserializeNBT(data.getCompoundTag("blockPosData"));
		NBTTagCompound tileData = data.getCompoundTag("tileData");
		if (MC.getSide() == Side.CLIENT)
		{
			VoidApi.proxy.getClientListener().addScheduledTask(() -> {
					if (VoidApi.proxy.getClientWorld().isBlockLoaded(dbp.pos) && VoidApi.proxy.getClientWorld().getTileEntity(dbp.pos) != null)
                    {
                        Optional.ofNullable(VoidApi.proxy.getClientWorld().getTileEntity(dbp.pos)).ifPresent(t -> t.deserializeNBT(tileData));
                    }

					VoidApi.proxy.getClientWorld().markBlockRangeForRenderUpdate(dbp.pos, dbp.pos);
			});
		}
		else
		{
			// Server deserializing a tile from client data?..
			WorldServer ws = DimensionManager.getWorld(dbp.dim);
			ws.addScheduledTask(() -> {
					if (ws.isBlockLoaded(dbp.pos) && ws.getTileEntity(dbp.pos) != null)
                    {
                        Optional.ofNullable(ws.getTileEntity(dbp.pos)).ifPresent(t -> t.deserializeNBT(tileData));
                    }
			});
		}
	}

	@Override
	public NBTTagCompound handleRequest(String s) 
	{
		return null;
	}

}
