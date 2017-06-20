package v0id.api.exp.entity;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

import java.io.IOException;

/**
 * Created by V0idWa1k3r on 19-Jun-17.
 */
public class DataSerializerLong implements DataSerializer<Long>
{
    public static final DataSerializer<Long> LONG = new DataSerializerLong();
    static
    {
        DataSerializers.registerSerializer(LONG);
    }

    @Override
    public void write(PacketBuffer buf, Long value)
    {
        buf.writeLong(value);
    }

    @Override
    public Long read(PacketBuffer buf) throws IOException
    {
        return buf.readLong();
    }

    @Override
    public DataParameter<Long> createKey(int id)
    {
        return new DataParameter<>(id, this);
    }

    @Override
    public Long copyValue(Long value)
    {
        return value;
    }
}
