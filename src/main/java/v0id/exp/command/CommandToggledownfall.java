package v0id.exp.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import v0id.api.exp.world.IExPWorld;
import v0id.exp.world.ExPWorld;

public class CommandToggledownfall extends CommandBase
{
    @Override
    public String getName()
    {
        return "toggledownfall";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "commands.downfall.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        ExPWorld world = (ExPWorld) IExPWorld.of(server.getWorld(0));
        if (world.rainTicksRemaining <= 0)
        {
            world.rainTicksRemaining = (int) (1000 + world.getWorld().rand.nextDouble() * 20000);
        }
        else
        {
            world.rainTicksRemaining = 0;
            world.setOverhaulHumidity(0);
            world.accumulatedHumidity = 0F;
            world.accumulatedHumidity_isDirty = true;
        }

        notifyCommandListener(sender, this, "commands.downfall.success");
    }
}
