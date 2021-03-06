package v0id.exp.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import v0id.api.exp.player.EnumPlayerProgression;
import v0id.api.exp.tile.ISyncableTile;
import v0id.exp.ExPetrum;
import v0id.exp.entity.EntityFallingTree;
import v0id.exp.entity.EntityProjectile;
import v0id.exp.player.ExPPlayer;
import v0id.exp.tile.TileAnvil;
import v0id.exp.tile.TileMechanicalPotteryStation;
import v0id.exp.tile.TilePotteryStation;
import v0id.exp.world.ExPWorld;

public class ExPNetwork
{
    public static final SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("exp");

    public static void init()
    {
        WRAPPER.registerMessage(MessageAnvilRecipe.Handler.class, MessageAnvilRecipe.class, 0, Side.SERVER);
        WRAPPER.registerMessage(MessageCardClick.Handler.class, MessageCardClick.class, 1, Side.SERVER);
        WRAPPER.registerMessage(MessageCraftPottery.Handler.class, MessageCraftPottery.class, 2, Side.SERVER);
        WRAPPER.registerMessage(MessageFallingTree.Handler.class, MessageFallingTree.class, 3, Side.CLIENT);
        WRAPPER.registerMessage(MessageNewAge.Handler.class, MessageNewAge.class, 4, Side.CLIENT);
        WRAPPER.registerMessage(MessagePlayerData.Handler.class, MessagePlayerData.class, 5, Side.CLIENT);
        WRAPPER.registerMessage(MessageSettings.Handler.class, MessageSettings.class, 6, Side.CLIENT);
        WRAPPER.registerMessage(MessageSpecialAttack.Handler.class, MessageSpecialAttack.class, 7, Side.CLIENT);
        WRAPPER.registerMessage(MessageSpecialAttack.Handler.class, MessageSpecialAttack.class, 8, Side.SERVER);
        WRAPPER.registerMessage(MessageWeld.Handler.class, MessageWeld.class, 9, Side.SERVER);
        WRAPPER.registerMessage(MessageWorldData.Handler.class, MessageWorldData.class, 10, Side.CLIENT);
        WRAPPER.registerMessage(MessageTileData.Handler.class, MessageTileData.class, 11, Side.CLIENT);
        WRAPPER.registerMessage(MessageSelectPottery.Handler.class, MessageSelectPottery.class, 12, Side.SERVER);
        WRAPPER.registerMessage(MessageProjectileCollide.Handler.class, MessageProjectileCollide.class, 13, Side.CLIENT);
    }

    public static void sendAnvilRecipe(TileAnvil anvil, int recipe)
    {
        WRAPPER.sendToServer(new MessageAnvilRecipe(anvil.getPos(), anvil.getWorld().provider.getDimension(), recipe));
    }

    public static void sendAnvilCard(TileAnvil anvil, int card)
    {
        WRAPPER.sendToServer(new MessageCardClick(anvil.getPos(), anvil.getWorld().provider.getDimension(), card));
    }

    public static void sendCraftPottery(TilePotteryStation tile, int recipe)
    {
        WRAPPER.sendToServer(new MessageCraftPottery(tile.getPos(), tile.getWorld().provider.getDimension(), recipe));
    }

    public static void sendNewAge(EntityPlayer to, EnumPlayerProgression progression)
    {
        WRAPPER.sendTo(new MessageNewAge(progression), (EntityPlayerMP) to);
    }

    public static void sendPlayerData(ExPPlayer player)
    {
        WRAPPER.sendTo(new MessagePlayerData(player.serializePartialNBT()), (EntityPlayerMP) player.owner);
    }

    public static void sendSettings(NBTTagCompound settings, EntityPlayerMP to)
    {
        WRAPPER.sendTo(new MessageSettings(settings), to);
    }

    public static void sendSpecialAttackClick(NBTTagCompound tag)
    {
        WRAPPER.sendToServer(new MessageSpecialAttack(tag));
    }

    public static void sendSpecialAttackSync(NBTTagCompound tag, EntityPlayerMP to)
    {
        WRAPPER.sendTo(new MessageSpecialAttack(tag), to);
    }

    public static void sendWeld(TileAnvil anvil)
    {
        WRAPPER.sendToServer(new MessageWeld(anvil.getPos(), anvil.getWorld().provider.getDimension()));
    }

    public static void sendWorldData(ExPWorld world)
    {
        WRAPPER.sendToAll(new MessageWorldData(world.serializePartialNBT()));
    }

    public static void sendFallingTree(EntityFallingTree tree, NetworkRegistry.TargetPoint point)
    {
        NBTTagCompound tag = new NBTTagCompound();
        tree.serializeDataToNBT(tag);
        WRAPPER.sendToAllAround(new MessageFallingTree(tree, tag), point);
    }

    public static void sendTileData(ISyncableTile tile, boolean doRefresh)
    {
        WRAPPER.sendToAllAround(new MessageTileData(tile, doRefresh), new NetworkRegistry.TargetPoint(((TileEntity)tile).getWorld().provider.getDimension(), ((TileEntity)tile).getPos().getX(), ((TileEntity)tile).getPos().getY(), ((TileEntity)tile).getPos().getZ(), ExPetrum.proxy.getViewDistance() << 4));
    }

    public static void sendSelectPottery(TileMechanicalPotteryStation tile, int recipe)
    {
        WRAPPER.sendToServer(new MessageSelectPottery(tile.getPos(), tile.getWorld().provider.getDimension(), recipe));
    }

    public static void sendProjectileCollide(EntityProjectile projectile)
    {
        WRAPPER.sendToAllAround(new MessageProjectileCollide(projectile.getEntityId(), projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw, projectile.rotationPitch), new NetworkRegistry.TargetPoint(projectile.dimension, projectile.posX, projectile.posY, projectile.posZ, 32));
    }
}
