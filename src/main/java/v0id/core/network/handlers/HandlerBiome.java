package v0id.core.network.handlers;

import org.apache.commons.lang3.SerializationUtils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import v0id.core.VoidApi;
import v0id.core.network.IPacketHandler;
import v0id.core.util.ChunkUtils;
import v0id.core.util.DimBlockPos;
import v0id.core.util.MC;

public class HandlerBiome implements IPacketHandler {

	@Override
	public void handleData(NBTTagCompound data)
	{
		Biome b = Biome.getBiome(data.getInteger("biomeID"));
		DimBlockPos dbp = SerializationUtils.deserialize(data.getByteArray("blockPosData"));
		if (MC.getSide() == Side.CLIENT)
		{
			VoidApi.proxy.getClientListener().addScheduledTask(() -> {
					if (VoidApi.proxy.getClientWorld().isBlockLoaded(dbp.pos))
						ChunkUtils.setBiomeAt(b, dbp.pos, VoidApi.proxy.getClientWorld());}
			);
		}
		else
		{
			WorldServer ws = DimensionManager.getWorld(dbp.dim);
			ws.addScheduledTask(() -> {
					if (ws.isBlockLoaded(dbp.pos))
						ChunkUtils.setBiomeAt(b, dbp.pos, ws);}
			);
		}
	}

	@Override
	public NBTTagCompound handleRequest(String s) 
	{
		return null;
	}

}
